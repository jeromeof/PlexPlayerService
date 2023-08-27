package com.pragmaticaudio.restserver.server;

import android.content.Context;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import com.pragmaticaudio.restserver.Cache;
import com.pragmaticaudio.restserver.annotations.ExceptionHandler;
import com.pragmaticaudio.restserver.annotations.RestController;
import com.pragmaticaudio.restserver.annotations.RestServer;
import com.pragmaticaudio.restserver.annotations.methods.DELETE;
import com.pragmaticaudio.restserver.annotations.methods.GET;
import com.pragmaticaudio.restserver.annotations.methods.POST;
import com.pragmaticaudio.restserver.annotations.methods.PUT;
import com.pragmaticaudio.restserver.server.authentication.BaseAuthentication;
import com.pragmaticaudio.restserver.server.converter.BaseConverter;
import com.pragmaticaudio.restserver.server.dictionary.HeaderType;
import com.pragmaticaudio.restserver.server.dictionary.ResponseStatus;
import com.pragmaticaudio.restserver.server.exceptions.NoAnnotationException;
import com.pragmaticaudio.restserver.server.exceptions.NotFoundException;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;
import com.pragmaticaudio.restserver.utils.ReflectionUtils;

/**
 * 100% BASED on https://github.com/skornei/restserver
 *  As that is Apache 2.0 licence I have renamed the packages and added some fixes
 *  and changes needed for Android.
 */
public abstract class BaseRestServer {

    /**
     * Http сервер
     */
    private HttpServer httpServer;

    /**
     * Контроллеры обработчики запросов
     */
    private Map<String, Object> controllers = new HashMap<>();

    /**
     * Конвертер объектов
     */
    private BaseConverter converter;

    private BaseAuthentication authentication;


    public BaseRestServer() {
        RestServer restServer = getClass().getAnnotation(RestServer.class);
        if (restServer != null) {
            if (!restServer.converter().equals(void.class) &&
                    BaseConverter.class.isAssignableFrom(restServer.converter())) {
                try {
                    this.converter = (BaseConverter) restServer.converter().newInstance();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            if (!restServer.authentication().equals(void.class) &&
                    BaseAuthentication.class.isAssignableFrom(restServer.authentication())) {
                try {
                    this.authentication = (BaseAuthentication) restServer.authentication().newInstance();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            for (Class<?> cls : restServer.controllers()) {
                if (cls.isAnnotationPresent(RestController.class)) {
                    RestController restController = cls.getAnnotation(RestController.class);

                    // Create an instance and store it
                    Object controller = null;
                    try {
                        controller = cls.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    controllers.put(restController.value(), controller);
                }
            }
        } else {
            throw new NoAnnotationException(getClass().getSimpleName(), RestServer.class.getSimpleName());
        }
    }

    /**
     * Allow to be overriden but default to the annotation as per original implementation
     * @return
     */
    public int getPort() {
        RestServer restServer = getClass().getAnnotation(RestServer.class);
        return restServer.port();
    }

    /**
     * Запускаем сервер
     * @throws IOException
     */
    public void start() throws IOException {
        // Move this here as we only want to listen when we start the server
        httpServer = new HttpServer(getPort());

        httpServer.start();
    }

    /**
     * Останавливаем сервер
     */
    public void stop() {
        httpServer.stop();
    }

    /**
     * Получить контроллер для этого адреса
     * @param uri адрес
     * @return контроллер
     */
    private Object getController(String uri) {
        if (controllers.containsKey(uri))
            return controllers.get(uri);

        return null;
    }

    protected Map<String, Object> getControllers() {
        return controllers;
    }

    /**
     * Http server
     */
    private class HttpServer extends NanoHTTPD {

        public HttpServer(int port) {
            super(port);
        }

        /**
         * Получаем данные от клиента
         *
         * @param session сессия
         * @return ответ
         */
        @Override
        public Response serve(IHTTPSession session) {
            //Request information
            RequestInfo requestInfo = new RequestInfo(session.getRemoteIpAddress(),
                    session.getHeaders(),
                    session.getParameters());

            //Reply Information
            ResponseInfo responseInfo = new ResponseInfo();

            //Get the controller
            Object controller = getController(session.getUri());

            //Found the controller
            if (controller != null) {
                Class cls = controller.getClass();
                try {
                    //Read body
                    if (session.getHeaders().containsKey(HeaderType.CONTENT_LENGTH)) {
                        Integer contentLength = Integer.valueOf(session.getHeaders().get(HeaderType.CONTENT_LENGTH));
                        if (contentLength > 0) {
                            byte[] buffer = new byte[contentLength];
                            session.getInputStream().read(buffer, 0, contentLength);
                            requestInfo.setBody(buffer);
                        }
                    }

                    //Get the method
                    ReflectionUtils.MethodInfo methodInfo = null;
                    if (session.getMethod() == Method.GET)
                        methodInfo = ReflectionUtils.getDeclaredMethodInfo(controller, cls, GET.class);
                    else if (session.getMethod() == Method.POST)
                        methodInfo = ReflectionUtils.getDeclaredMethodInfo(controller, cls, POST.class);
                    else if (session.getMethod() == Method.PUT)
                        methodInfo = ReflectionUtils.getDeclaredMethodInfo(controller, cls, PUT.class);
                    else if (session.getMethod() == Method.DELETE)
                        methodInfo = ReflectionUtils.getDeclaredMethodInfo(controller, cls, DELETE.class);

                    //If the method is found
                    if (methodInfo != null) {
                        //RequiresAuthentication
                        if (authentication != null &&
                                methodInfo.isRequiresAuthentication() &&
                                !authentication.authentication(requestInfo)) {
                            //Answer 401
                            return newFixedLengthResponse(ResponseStatus.UNAUTHORIZED,
                                    NanoHTTPD.MIME_PLAINTEXT,
                                    ResponseStatus.UNAUTHORIZED.getDescription());
                        }

                        //Accept
                        String accept = methodInfo.getAccept();
                        if (accept != null) {
                            if (session.getHeaders().containsKey(HeaderType.CONTENT_TYPE)) {
                                String contentType = session.getHeaders().get(HeaderType.CONTENT_TYPE);
                                if (!accept.equals(contentType)) {
                                    //Answer 415
                                    return newFixedLengthResponse(ResponseStatus.UNSUPPORTED_MEDIA_TYPE,
                                            NanoHTTPD.MIME_PLAINTEXT,
                                            ResponseStatus.UNSUPPORTED_MEDIA_TYPE.getDescription());
                                }
                            }
                        }

                        //Get the response type
                        String produces = methodInfo.getProduces();
                        if (produces != null)
                            responseInfo.setType(produces);

                        //If we are waiting for an object
                        Object paramObject = null;
                        if (converter != null) {
                            Class paramClass = methodInfo.getParamClass();
                            if (paramClass != null && requestInfo.isBodyAvailable()) {
                                paramObject = converter.writeValue(requestInfo.getBody(), paramClass);
                            }
                        }
                        Context androidContext = null;
                        try {
                            androidContext = Cache.getContext();
                        } catch (
                                Exception e) {// Ignore this as might just be a standalone run of the code
                        }

                        //If we do not return anything
                        if (methodInfo.isVoidResult()) {
                            methodInfo.invoke(androidContext,

                                    requestInfo,
                                    responseInfo,
                                    this,
                                    paramObject);
                        } else {
                            //Return the answer
                            Object result = methodInfo.invoke(androidContext,
                                    requestInfo,
                                    responseInfo,
                                    paramObject);

                            if (converter != null)
                                responseInfo.setBody(converter.writeValueAsBytes(result));
                        }

                        //Sending response
                        Response response = newFixedLengthResponse(responseInfo.getStatus(),
                                responseInfo.getType(),
                                responseInfo.getBodyInputStream(),
                                responseInfo.getBodyLength());

                        Map<String, String> headers = responseInfo.getHeaders();
                        for (String header : headers.keySet()) {
                            response.addHeader(header, headers.get(header));
                        }

                        return response;
                    }
                } catch (Throwable throwable) {
                    // Simple hack to allow custom not found exceptions when we are called incorrectly
                    if (((InvocationTargetException) throwable).getTargetException() instanceof NotFoundException) {
                        return newFixedLengthResponse(ResponseStatus.NOT_FOUND,
                                NanoHTTPD.MIME_PLAINTEXT,
                                ResponseStatus.NOT_FOUND.getDescription());
                    }
                    //Return error 500 in case it is not otherwise configured in ExceptionHandler
                    responseInfo.setStatus(ResponseStatus.INTERNAL_SERVER_ERROR);

                    //Get the method
                    ReflectionUtils.MethodInfo methodInfo = ReflectionUtils.getDeclaredMethodInfo(controller, cls, ExceptionHandler.class);
                    if (methodInfo != null) {
                        //Get the response type
                        String produces = methodInfo.getProduces();
                        if (produces != null)
                            responseInfo.setType(produces);

                        try {
                            methodInfo.invoke(throwable, responseInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //Sending response
                    return newFixedLengthResponse(responseInfo.getStatus(),
                            responseInfo.getType(),
                            responseInfo.getBodyInputStream(),
                            responseInfo.getBodyLength());
                }
            }

            //Answer 404
            return newFixedLengthResponse(ResponseStatus.NOT_FOUND,
                    NanoHTTPD.MIME_PLAINTEXT,
                    ResponseStatus.NOT_FOUND.getDescription());
        }
    }
}

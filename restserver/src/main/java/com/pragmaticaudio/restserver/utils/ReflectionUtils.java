package com.pragmaticaudio.restserver.utils;

import android.content.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.pragmaticaudio.restserver.annotations.Accept;
import com.pragmaticaudio.restserver.annotations.Produces;
import com.pragmaticaudio.restserver.annotations.RequiresAuthentication;
import com.pragmaticaudio.restserver.server.exceptions.NotFoundException;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;
import com.pragmaticaudio.restserver.server.protocol.ResponseInfo;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new RuntimeException();
    }

    /**
     * Information about the method
     */
    public static class MethodInfo {

        private Object object;
        private Method method;

        public MethodInfo(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public String getProduces() {
            Annotation annotation = method.getAnnotation(Produces.class);
            if (annotation != null)
                return ((Produces) annotation).value();

            return null;
        }

        public String getAccept() {
            Annotation annotation = method.getAnnotation(Accept.class);
            if (annotation != null)
                return ((Accept) annotation).value();

            return null;
        }

        public Class getParamClass() {
            for (Class cls : method.getParameterTypes()) {
                if (!Context.class.equals(cls) &&
                        !ResponseInfo.class.equals(cls) &&
                        !RequestInfo.class.equals(cls))
                    return cls;
            }

            return null;
        }

        public boolean isVoidResult() {
            return method.getReturnType().equals(Void.TYPE);
        }

        public boolean isRequiresAuthentication() {
            Annotation annotation = method.getAnnotation(RequiresAuthentication.class);
            if (annotation != null)
                return true;

            return false;
        }

        public Object invoke(Object... params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NotFoundException {
            List<Object> newParams = new ArrayList<>();

            // Sort in the order of the specified parameters
            for (Class cls : method.getParameterTypes()) {
                for (Object param : params) {
                    if (param != null && cls.isInstance(param)) {
                        newParams.add(param);
                        break;
                    }
                }
            }

            return method.invoke(object, newParams.toArray());
        }
    }

    /**
     * Getting the method with the required annotation
     * @param object объект
     * @param type класс
     * @param annotationClass аннотация
     * @return метод
     */
    public static MethodInfo getDeclaredMethodInfo(Object object, Class<?> type, Class annotationClass) {
        Method[] methods = type.getMethods();

        for (Method method : methods) {
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation != null)
                return new MethodInfo(object, method);
        }

        Class<?> parentType = type.getSuperclass();
        if (parentType != null) {
            return getDeclaredMethodInfo(object, parentType, annotationClass);
        }

        return null;
    }
}

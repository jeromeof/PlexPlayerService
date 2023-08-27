package com.pragmaticaudio.restserver.server.authentication;

import com.pragmaticaudio.restserver.server.protocol.RequestInfo;

public interface BaseAuthentication {

    /**
     * Аутентификация
     * @param requestInfo параметры запроса
     * @return прошла аутентификация или нет
     */
    boolean authentication(RequestInfo requestInfo);
}

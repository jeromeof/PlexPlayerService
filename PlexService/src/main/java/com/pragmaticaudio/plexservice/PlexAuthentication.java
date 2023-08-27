package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.restserver.server.authentication.BaseAuthentication;
import com.pragmaticaudio.restserver.server.protocol.RequestInfo;

public class PlexAuthentication implements BaseAuthentication {
    @Override
    public boolean authentication(RequestInfo requestInfo) {
        return true;
    }
}
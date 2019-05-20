package ru.urfu.Server;

import java.util.UUID;

public class AuthorizationReply {
    private AuthorizationStatus authorizationStatus;
    private UUID gameToken;

    public AuthorizationStatus getAuthorizationStatus() {
        return authorizationStatus;
    }

    public UUID getGameToken() {
        return gameToken;
    }

    private AuthorizationReply() {
    }

    public AuthorizationReply(AuthorizationStatus authorizationStatus, UUID gameToken) {
        this.authorizationStatus = authorizationStatus;
        this.gameToken = gameToken;
    }
}

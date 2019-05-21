package ru.urfu.Client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import ru.urfu.Server.AuthorizationReply;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;

public class AuthorizationSessionHandler extends StompSessionHandlerAdapter {
    private Logger logger = LogManager.getLogger(AuthorizationSessionHandler.class);
    private LogInForm logInForm;

    public AuthorizationSessionHandler(LogInForm logInForm) {
        this.logInForm = logInForm;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return AuthorizationReply.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        logger.info("Got reply from server");
        try {
            logInForm.processAuthorizationReply((AuthorizationReply) payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/user/" + logInForm.getUserName() + "/auth_reply", this);
        logger.info("Subscribed to /user/" + logInForm.getUserName() + "/auth_reply");
        try {
            logInForm.sendAuthorizeRequest();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
        logger.info(new String(payload));
    }
}

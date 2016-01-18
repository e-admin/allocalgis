package com.localgis.webservices.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;

import com.localgis.webservices.geomarketing.ln.UserValidationLN;

public class PWCBHandler implements CallbackHandler {

    public void handle(Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {

        for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
            UserValidationLN userValidationLN = new UserValidationLN();
    		try{
    			userValidationLN.getActiveAndValidatedUserId(pwcb.getIdentifer(), pwcb.getPassword());
    		}catch (Exception e){
                throw new UnsupportedCallbackException(callbacks[i], "fallo de autenticación");
            }
        }
    }
}
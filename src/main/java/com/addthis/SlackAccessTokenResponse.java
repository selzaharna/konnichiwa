package com.addthis;

import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.token.OAuthToken;

/**
 * Created by sami on 3/26/15.
 */
public class SlackAccessTokenResponse extends OAuthAccessTokenResponse {

    @Override
    public String getAccessToken() {
        return getParam("access_token");
    }

    @Override
    public Long getExpiresIn() {
        return null;
    }

    @Override
    public String getRefreshToken() {
        return null;
    }

    @Override
    public String getScope() {
        return getParam("scope");
    }

    @Override
    public OAuthToken getOAuthToken() {
        return null;
    }

    @Override
    protected void setBody(String body) throws OAuthProblemException {

    }

    @Override
    protected void setContentType(String contentType) {

    }

    @Override
    protected void setResponseCode(int responseCode) {

    }
}

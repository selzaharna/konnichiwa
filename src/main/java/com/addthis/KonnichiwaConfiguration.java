package com.addthis;

import io.dropwizard.Configuration;

/**
 * Created by sami on 3/26/15.
 */
public class KonnichiwaConfiguration extends Configuration {

    private String hello;

    private String slackClientId;

    private String slackRedirect;

    private String slackSecret;

    private String slackTokenLocation;

    private String slackAuthorization;

    private String postAuthRedirect;

    private String failureRedirect;

    public String getSlackClientId() {
        return slackClientId;
    }

    public void setSlackClientId(String slackClientId) {
        this.slackClientId = slackClientId;
    }

    public String getSlackRedirect() {
        return slackRedirect;
    }

    public void setSlackRedirect(String slackRedirect) {
        this.slackRedirect = slackRedirect;
    }

    public String getSlackSecret() {
        return slackSecret;
    }

    public void setSlackSecret(String slackSecret) {
        this.slackSecret = slackSecret;
    }

    public String getSlackTokenLocation() {
        return slackTokenLocation;
    }

    public void setSlackTokenLocation(String slackTokenLocation) {
        this.slackTokenLocation = slackTokenLocation;
    }

    public String getSlackAuthorization() {
        return slackAuthorization;
    }

    public void setSlackAuthorization(String slackAuthorization) {
        this.slackAuthorization = slackAuthorization;
    }

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public String getPostAuthRedirect() {
        return postAuthRedirect;
    }

    public void setPostAuthRedirect(String postAuthRedirect) {
        this.postAuthRedirect = postAuthRedirect;
    }

    public String getFailureRedirect() {
        return failureRedirect;
    }

    public void setFailureRedirect(String failureRedirect) {
        this.failureRedirect = failureRedirect;
    }
}

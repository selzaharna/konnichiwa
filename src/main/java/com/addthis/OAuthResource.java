package com.addthis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.eclipse.jetty.util.URIUtil;
import sun.net.www.http.HttpClient;

import javax.servlet.http.*;
import javax.servlet.http.Cookie;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Created by sami on 3/26/15.
 */

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class OAuthResource {

    final private KonnichiwaConfiguration config;

    public OAuthResource(KonnichiwaConfiguration config){
        this.config = config;
    }

    @GET
    @Path("/authenticate")
    public void helloWorld(@Context HttpServletRequest request, @Context HttpServletResponse response) {

        try {

            OAuthClientRequest oauthRequest = OAuthClientRequest
                    .authorizationLocation(config.getSlackAuthorization())
                    .setClientId(config.getSlackClientId())
                    .setRedirectURI(config.getSlackRedirect())
                    .buildQueryMessage();

            response.sendRedirect(oauthRequest.getLocationUri());

        } catch(OAuthSystemException e){

        } catch(IOException e) {

        }
    }


    @GET
    @Path("/response")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleResponse(@Context HttpServletRequest request){
        Response response = null;

        try {
            OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            String code = oar.getCode();

            OAuthClientRequest authRequest = OAuthClientRequest
                    .tokenLocation(config.getSlackTokenLocation())
                    .setClientId(config.getSlackClientId())
                    .setClientSecret(config.getSlackSecret())
                    .setRedirectURI(config.getSlackRedirect())
                    .setCode(code)
                    .buildQueryMessage();


            //create OAuth client that uses custom http client under the hood
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            OAuthJSONAccessTokenResponse oAuthResponse =
                    oAuthClient.accessToken(authRequest,OAuthJSONAccessTokenResponse.class);

            String token = oAuthResponse.getAccessToken();

            URI uri = new URI(config.getPostAuthRedirect()+"?access_token="+token);


            response = javax.ws.rs.core.Response.
                    seeOther(uri).
                    build();

            return response;

        } catch(Exception e) {
            e.printStackTrace();
            try {
                URI uri = new URI(config.getFailureRedirect());

                response = javax.ws.rs.core.Response.
                        seeOther(uri).
                        build();
            }catch (Exception exc){}
        } finally{

        }
        return response;

    }
}

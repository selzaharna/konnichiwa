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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

//    final private HttpClient client;

    public OAuthResource(){}

    @GET
    @Path("/authenticate")
    public void helloWorld(@Context HttpServletRequest request, @Context HttpServletResponse response) {

        try {

            OAuthClientRequest oauthRequest = OAuthClientRequest
                    .authorizationLocation("https://slack.com/oauth/authorize")
                    .setClientId("2194787930.4206060083")
                    .setRedirectURI("http://www-dev.addthis.com:8080/konnichiwa/auth/response")
                    .buildQueryMessage();

            response.sendRedirect(oauthRequest.getLocationUri());

        } catch(OAuthSystemException e){

        } catch(IOException e) {

        }
    }

    @GET
    @Path("/complete")
    public String complete(@Context HttpServletRequest request){
        return "Im here";
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
                    .tokenLocation("https://slack.com/api/oauth.access")
                    .setClientId("2194787930.4206060083")
                    .setClientSecret("d89a56f09f24377695b2feaf636915b2")
                    .setRedirectURI("http://www-dev.addthis.com:8080/konnichiwa/auth/response")
                    .setCode(code)
                    .buildQueryMessage();


            //create OAuth client that uses custom http client under the hood
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            OAuthJSONAccessTokenResponse oAuthResponse =
                    oAuthClient.accessToken(authRequest,OAuthJSONAccessTokenResponse.class);

            String token = oAuthResponse.getAccessToken();

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode payload = mapper.createObjectNode();
            ObjectNode data = mapper.createObjectNode();
            data.put("access_token", token);
            payload.set("content", data);
            response =  Response.ok(data).build();

        } catch(Exception e) {
            e.printStackTrace();
            response =  Response.serverError().build();
        } finally{

        }
        return response;

    }
}

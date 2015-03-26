import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

/**
 * Created by sami on 3/26/15.
 */

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class OAuthResource {

    public OAuthResource(){}

    @GET
    public String helloWorld(@QueryParam("name") Optional<String> name) {
        return "Hello World";
    }
}

package exception;

import javax.json.Json;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResponseExceptionProvider implements ExceptionMapper<WebApplicationException>{
	 @Override
	    public Response toResponse(WebApplicationException exception) {
	        int code = exception.getResponse().getStatus();
	        return Response.status(code)
	                .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
	                .build();
	    }
}

package org.wso2.carbon.apimgt.rest.api.analytics;


import io.swagger.annotations.ApiParam;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.apimgt.rest.api.analytics.dto.ApplicationCountListDTO;
import org.wso2.carbon.apimgt.rest.api.analytics.factories.ApplicationApiServiceFactory;
import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.Request;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Component(
    name = "org.wso2.carbon.apimgt.rest.api.analytics.ApplicationApi",
    service = Microservice.class,
    immediate = true
)
@Path("/api/am/analytics/v1.[\\d]+/application")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@ApplicationPath("/application")
@io.swagger.annotations.Api(description = "the application API")
public class ApplicationApi implements Microservice  {
   private final ApplicationApiService delegate = ApplicationApiServiceFactory.getApplicationApi();

    @GET
    @Path("/count-over-time")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve application created over time details ", notes = "Get application count over time details from summarized data. ", response = ApplicationCountListDTO.class, authorizations = {
        @io.swagger.annotations.Authorization(value = "OAuth2Security", scopes = {
            @io.swagger.annotations.AuthorizationScope(scope = "apim:application_graph", description = "View Graphs Releated to applications")
        })
    }, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK. Requested applications count over time information is returned ", response = ApplicationCountListDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable. The requested media type is not supported ", response = ApplicationCountListDTO.class) })
    public Response applicationCountOverTimeGet(@ApiParam(value = "Defines the starting timestamp of the interval ",required=true) @QueryParam("startTime") String startTime
,@ApiParam(value = "Defines the ending timestamp of the interval ",required=true) @QueryParam("endTime") String endTime
, @Context Request request)
    throws NotFoundException {
        return delegate.applicationCountOverTimeGet(startTime,endTime, request);
    }
}

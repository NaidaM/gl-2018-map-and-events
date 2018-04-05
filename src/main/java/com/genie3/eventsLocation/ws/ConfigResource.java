package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.elastic.DB;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/config")

public class ConfigResource {

    @POST
    @Path("/index")
    public Response create() {
        DB.createAllIndex();
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/index")
    public Response destroy() {
        DB.delateAllIndex();
        return Response.status(Response.Status.OK).build();
    }

}

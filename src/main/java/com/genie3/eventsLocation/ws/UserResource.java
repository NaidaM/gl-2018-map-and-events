package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")

public class UserResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public User get(@PathParam("pseudo") String pseudo) {

        return Dao.getUserDao().getUser(pseudo);

        /*
        *   return Response.status(Response.Status.OK).entity(user).build();
        *    use this to return result with specific Http status and the method return must be
        *    javax.ws.rs.core.Response
        */
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User create(User user) {
        return Dao.getUserDao().createUser(user);
       // return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public User update(@PathParam("pseudo") String pseudo,User user) {
        return Dao.getUserDao().updateUser(user);
       // return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public Response delete(@PathParam("pseudo") String pseudo) {
        if(Dao.getUserDao().deleteUser(pseudo)){
            return Response.status(Response.Status.OK).build();
        }else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }
}

package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users")

public class UserResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public User get(@PathParam("pseudo") String pseudo) {

        return Dao.getUserDao().get(pseudo);

        /*
        *   return Response.status(Response.Status.OK).entity(user).build();
        *    use this to return result with specific Http status and the method return must be
        *    javax.ws.rs.core.Response
        */
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public User update(@PathParam("pseudo") String pseudo,User user) {
        return Dao.getUserDao().update(user);
       // return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public Response delete(@PathParam("pseudo") String pseudo) {
        if(Dao.getUserDao().delete(pseudo)){
            return Response.status(Response.Status.OK).build();
        }else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps")
    public List<EventMap> getMaps(@PathParam("pseudo") String pseudo) {

        User u = Dao.getUserDao().get(pseudo);
        return Dao.getMapDao().read(null,"user_id = "+u.getId(),null,20);

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps")
    public EventMap createMap(@PathParam("pseudo") String pseudo,EventMap map) {

        User u = Dao.getUserDao().get(pseudo);
        map.setUser(u);
        return Dao.getMapDao().create(map);

    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps/{map_id}")
    public EventMap updateMap(@PathParam("pseudo") String pseudo,
                              @PathParam("map_id") int mapIp,EventMap map) {
        map.setId(mapIp);
        return Dao.getMapDao().update(map);
    }


    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps/{map_id}")
    public Response delete(@PathParam("pseudo") String pseudo,
                           @PathParam("map_id") int mapIp) {

        if(Dao.getMapDao().delete(mapIp)){
            return Response.status(Response.Status.OK).build();
        }else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }


}

package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.elastic.Database;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.Error;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.Place;
import com.genie3.eventsLocation.models.User;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/users")

public class UserResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    @RolesAllowed({"user"})
    public Response get(@PathParam("pseudo") String pseudo) {

        try {

             User u =  Dao.getUserDao().getWithPseudo(pseudo);
            return Response.status(Response.Status.OK).entity(u).build();
        }catch (DaoException.NotFoundException ex){
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }

        /*
        *   return Response.status(Response.Status.OK).entity(user).build();
        *    use this to return result with specific Http status and the method return must be
        *    javax.ws.rs.core.Res0ponse
        */
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public Response update(@PathParam("pseudo") String pseudo,User user) {

        try{
            User u =  Dao.getUserDao().update(user);
            return Response.status(Response.Status.OK).entity(u).build();

        }catch (DaoException.DaoInternalError ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

       // return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed({"user"})
    public Response delete(@PathParam("id") String id) {

        try {
           Dao.getUserDao().delete(id);
            return Response.status(Response.Status.OK).build();

        }catch (Exception ex){
            Error error = new Error(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps")
    @RolesAllowed({"user"})
    public Response getMaps(@PathParam("pseudo") String pseudo) {


            List<EventMap> maps =  Dao.getMapDao().
                    readForUser(pseudo,null,"",null,20);

            return Response.status(Response.Status.OK).entity(maps).build();




    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps")
    @RolesAllowed({"user"})
    public Response createMap(@PathParam("pseudo") String pseudo,@Valid EventMap map) {

        try {

            User u =  Dao.getUserDao().getWithPseudo(pseudo);
            map.setUser(u);

            EventMap eventMap =  Dao.getMapDao().create(map);

            return Response.status(Response.Status.CREATED).entity(eventMap).build();
        }catch (DaoException.NotFoundException ex){
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }catch (DaoException.DaoInternalError ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }




    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps/{map_id}")
    @RolesAllowed({"user"})
    public Response updateMap(@PathParam("pseudo") String pseudo,
                              @PathParam("map_id") int mapIp,@Valid EventMap map) {

        try {
            map.setId(mapIp);
            EventMap map1 = Dao.getMapDao().update(map);
            return Response.status(Response.Status.OK).entity(map1).build();
        }catch (DaoException.DaoInternalError ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }


    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps/{map_id}")
    @RolesAllowed({"user"})
    public Response delete(@PathParam("pseudo") String pseudo,
                           @PathParam("map_id") int mapIp) {

        try {
            if(Dao.getMapDao().delete(""+mapIp)){
                return Response.status(Response.Status.OK).build();
            }else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }catch (DaoException.DaoInternalError ex){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }


    }

   

}

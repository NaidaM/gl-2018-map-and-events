package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.dao.DaoFactory;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.entities.Error;
import com.genie3.eventsLocation.entities.EventMap;
import com.genie3.eventsLocation.entities.User;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed({"user"})
    public Response get(@PathParam("pseudo") String pseudo) {

        try {

            User u = DaoFactory.getUserDao().getWithPseudo(pseudo);
            u.setMaps(null);
            return Response.status(Response.Status.OK).entity(u).build();

        } catch (DaoException.NotFoundException ex) {

            return Response.status(Response.Status.NOT_FOUND).entity(new Error(ex.getMessage())).build();
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    public Response update(@PathParam("pseudo") String pseudo,
                           @NotNull(message = "Post body must not empty")
                           @Valid User user) {

        try {
            User u = DaoFactory.getUserDao().update(user, "user");
            return Response.status(Response.Status.OK).entity(u).build();

        } catch (DaoException.DaoInternalError ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(ex.getMessage()))
                    .build();
        }

        // return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}")
    @RolesAllowed({"user"})
    public Response delete(@PathParam("pseudo") String pseudo) {

        try {
            User u = DaoFactory.getUserDao().getWithPseudo(pseudo);
            DaoFactory.getUserDao().delete(u.getId(), "user");
            return Response.status(Response.Status.OK).build();

        } catch (Exception ex) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(ex.getMessage()))
                    .build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps")
    @RolesAllowed({"user"})
    public Response getMaps(@PathParam("pseudo") String pseudo) {


        List<EventMap> maps;
        try {
            User u = DaoFactory.getUserDao().getWithPseudo(pseudo);
            maps = DaoFactory.getMapDao().readUserMap(u.getId());
            u.setMaps(maps);
            return Response.status(Response.Status.OK).entity(u).build();

        } catch (DaoException.NotFoundException ex) {

            Error error = new Error(ex.getMessage());

            return Response.status(Response.Status.NOT_FOUND).entity(error).build();

        } catch (DaoInternalError e) {

            Error error = new Error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }


    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps/friends")
    @RolesAllowed({"user"})
    public Response getFriendMaps(@PathParam("pseudo") String pseudo) {


        List<EventMap> maps;
        try {

            maps = DaoFactory.getMapDao().getFriendMap(pseudo);

            return Response.status(Response.Status.OK).entity(maps).build();

        } catch (DaoException.NotFoundException ex) {

            Error error = new Error(ex.getMessage());

            return Response.status(Response.Status.NOT_FOUND).entity(error).build();

        } catch (DaoInternalError e) {

            Error error = new Error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }


    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps")
    @RolesAllowed({"user"})
    public Response createMap(@NotNull(message = "Post body must not empty")
                              @Valid EventMap map,
                              @PathParam("pseudo") String pseudo) {


        try {

            User u = DaoFactory.getUserDao().getWithPseudo(pseudo);
            map.setUser(u);
            //map.toArray(map.getTaglist());

            EventMap eventMap = DaoFactory.getMapDao().create(map, "map");

            // Just for not display it
            eventMap.setUser(null);
            return Response.status(Response.Status.CREATED).entity(eventMap).build();
        } catch (DaoException.NotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        } catch (DaoException.DaoInternalError ex) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Error(ex.getMessage())).build();
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps/{map_id}")
    @RolesAllowed({"user"})
    public Response updateMap(@PathParam("pseudo") String pseudo,
                              @PathParam("map_id") String mapIp,
                              @NotNull(message = "Post body must not empty")
                              @Valid EventMap map) {

        try {

            map.setId(mapIp);
            EventMap map1 = DaoFactory.getMapDao().update(map, "map");
            return Response.status(Response.Status.OK).entity(map1).build();

        } catch (DaoException.DaoInternalError ex) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Error(ex.getMessage())).build();
        }

    }


    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{pseudo}/maps/{map_id}")
    @RolesAllowed({"user"})
    public Response delete(@PathParam("pseudo") String pseudo,
                           @PathParam("map_id") String mapIp) {

        try {

            if (DaoFactory.getMapDao().delete(mapIp, "map")) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

        } catch (DaoException.DaoInternalError ex) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(ex.getMessage()))
                    .build();
        }

    }
}

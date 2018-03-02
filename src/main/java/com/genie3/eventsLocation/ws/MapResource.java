package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.Error;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.Place;
import com.genie3.eventsLocation.models.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/maps")

public class MapResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response get(@PathParam("pseudo") int mapId) {

        try {
            EventMap map =  Dao.getMapDao().get(mapId);
            return Response.status(Response.Status.CREATED).entity(map).build();
        }catch (DaoException.NotFoundException ex){

            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }


    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventMap> getAll() {

        // returns all public maps
        return Dao.getMapDao().read(null,null,null,null);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/places")
    public List<Place> getPlaces(@PathParam("pseudo") int mapId) {

        return null;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{map_id}/places")
    public Response createPlace(@PathParam("map_id") int mapId,Place place) {
        place.getMap().setId(mapId);
        try {
            Place p = Dao.getPlaceDao().create(place);
            return Response.status(Response.Status.CREATED).entity(p).build();
        }catch (DaoException.DaoInternalError ex){

            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        }


    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{map_id}/places/{place_id}")
    public Place updatePlace(@PathParam("map_id") int mapId,
                                @PathParam("place_id") int placeId, Place place) {
        place.setId(placeId);
        place.getMap().setId(mapId);

        return Dao.getPlaceDao().update(place);
    }


    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{map_id}/places/{place_id}")
    public Response delete(@PathParam("map_id") int mapId,
                           @PathParam("place_id") int placeId) {

        if(Dao.getPlaceDao().delete(placeId)){
            return Response.status(Response.Status.OK).build();
        }else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }


}

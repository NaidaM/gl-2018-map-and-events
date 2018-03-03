package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.Place;

import javax.validation.Valid;
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
            ArrayList<Place> places = Dao.getPlaceDao().getPlaceForMap(mapId);
            map.setPlaces(places);
            return Response.status(Response.Status.OK).entity(map).build();
        }catch (DaoException.NotFoundException ex){

            return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
        }


    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventMap> getAll() {
        
        return Dao.getMapDao().read(null,null,null,null);

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/places")
    public List<Place> getPlaces(@PathParam("pseudo") int mapId) {

        return Dao.getPlaceDao().getPlaceForMap(mapId);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{map_id}/places")
    public Response createPlace(@PathParam("map_id") int mapId,@Valid Place place) {
        //place.getMap().setId(mapId);
        try {
            Place p = Dao.getPlaceDao().create(place);
            return Response.status(Response.Status.CREATED).entity(p).build();
        }catch (DaoException.DaoInternalError ex){

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }


    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{map_id}/places/{place_id}")
    public Response updatePlace(@PathParam("map_id") int mapId,
                                @PathParam("place_id") int placeId,@Valid Place place) {
        place.setId(placeId);

        try {
            Place p =  Dao.getPlaceDao().update(place);
            return Response.status(Response.Status.OK).entity(p).build();
        }catch (DaoException.DaoInternalError ex){

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }


    }


    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{map_id}/places/{place_id}")
    public Response delete(@PathParam("map_id") int mapId,
                           @PathParam("place_id") int placeId) {


        try {
            if(Dao.getPlaceDao().delete(placeId)){
                return Response.status(Response.Status.OK).build();
            }else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        }catch (DaoException.DaoInternalError ex){

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }


    }


}

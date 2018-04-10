package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.exception.DaoException.NotFoundException;
import com.genie3.eventsLocation.models.Error;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.Place;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
	public Response get(@PathParam("id") String  mapId) {

		try {
			EventMap map =  Dao.getMapDao().get(mapId,"map");
			ArrayList<Place> places = Dao.getPlaceDao().getPlaceForMap(mapId);
			map.setPlaces(places);
			return Response.status(Response.Status.OK).entity(map).build();
		}
		catch (DaoException.NotFoundException ex){

			return Response.status(Response.Status.NOT_FOUND)
					.entity(new Error(ex.getMessage()))
					.build();
		}
		catch (DaoException.DaoInternalError ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Error(ex.getMessage()))
					.build();
		}


	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Response getAll() {

		try {
			List<EventMap> events = Dao.getMapDao().getPublicMap();

			return Response.status(Response.Status.OK).entity(events).build();

		}catch (DaoException.NotFoundException ex){

			return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
		}
		catch (DaoException.DaoInternalError ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Error(ex.getMessage()))
					.build();
		}


	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}/places")
	public Response getPlaces(@PathParam("id") String mapId) {

		try {
			List<Place> p = Dao.getPlaceDao().getPlaceForMap(mapId);
            return Response.status(Response.Status.OK).entity(p).build();
		} catch (DaoInternalError e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new Error(e.getMessage()))
                    .build();
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search")
	public Response getMaps(@QueryParam("tag") final List<String> tags) {
		String [] taglist = new String[tags.size()];
		tags.toArray(taglist);
		try {
			List< EventMap> maps = DB.getMapWithTag(taglist);
			return Response.status(Response.Status.OK).entity(maps).build();
		}catch (DaoException.NotFoundException ex){
			return Response.status(Response.Status.NOT_FOUND).entity(new Error(ex.getMessage())).build();
		}catch (DaoException.DaoInternalError ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Error(ex.getMessage())).build();
		}

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchmap")
	public Response getSearchMaps(@QueryParam("tag") List<String> tags) {
		String [] taglist = new String[tags.size()];
		tags.toArray(taglist);
		
		List<EventMap> maps;
		try {
			maps = DB.searchMapTags(taglist);
			return Response.status(Response.Status.OK).entity(maps).build();
		} catch (NotFoundException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(new Error(e.getMessage())).build();
		}
		
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{map_id}/places")
	@PermitAll
	public Response createPlace(@PathParam("map_id") String  mapId,
								@NotNull(message = "Post body must not empty")
								@Valid Place place) {

		try {
			 EventMap map = Dao.getMapDao().get(mapId,"map");
			 place.setMap(map);
			Place p = Dao.getPlaceDao().create(place,"place");
			p.setMap(null);
			return Response.status(Response.Status.CREATED).entity(p).build();
		}catch (DaoException.NotFoundException ex){
			return Response.status(Response.Status.NOT_FOUND).entity(new Error(ex.getMessage())).build();
		}
		catch (DaoException.DaoInternalError ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Error(ex.getMessage())).build();
		}


	}


	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{map_id}/places/{place_id}")
	@PermitAll
	public Response updatePlace(@PathParam("map_id") String mapId,
			@PathParam("place_id") String placeId,
			@NotNull(message = "Post body must not empty")
			@Valid Place place) {

	    place.setId(placeId);


		try {
			Place p =  Dao.getPlaceDao().update(place,"place");
			return Response.status(Response.Status.OK).entity(p).build();
		}catch (DaoException.DaoInternalError ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Error(ex.getMessage())).build();
		}


	}


	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{map_id}/places/{place_id}")
	public Response delete(@PathParam("map_id") String mapId,
			@PathParam("place_id") String placeId) {


		try {
			if(Dao.getPlaceDao().delete(placeId,"place")){

				return Response.status(Response.Status.OK).build();
			}else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		}catch (DaoException.DaoInternalError ex){

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new Error(ex.getMessage())).build();
		}


	}


}

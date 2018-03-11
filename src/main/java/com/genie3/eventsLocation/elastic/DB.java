package com.genie3.eventsLocation.elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
//import java.util.concurrent.ExecutionException;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;

import org.elasticsearch.action.delete.DeleteResponse;

import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.Place;
import com.genie3.eventsLocation.models.User;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.search.MultiMatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.mindrot.jbcrypt.BCrypt;
import org.elasticsearch.rest.RestStatus;


public final class DB {


	static String host = "127.0.0.1";
	static int port = 9300;
	static String userIndex = "user";
	static String mapIndex = "map";
	static String placeIndex = "place";

	private static TransportClient client;

	private static TransportClient getClient() {
		if(client == null){
			try {
				client = new PreBuiltTransportClient(Settings.EMPTY)
						.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
			}catch (UnknownHostException ex){
				ex.printStackTrace();
			}
		}

		return client;
	}

	@SuppressWarnings({"unchecked"})

	public static <T> T create (T t,String table) throws DaoException.DaoInternalError {
		TransportClient client = getClient();

		try {
			XContentBuilder builder = jsonBuilder()
					.startObject();

			if (t instanceof User){
				User user = (User) t;
				builder = createUser(user,builder);
				IndexResponse resp = client.prepareIndex(userIndex, table)
						.setSource(builder)
						.get();
				user.setId(resp.getId());
				return (T) user;
			}
			if (t instanceof Place) {
					Place place = (Place)t;
					builder = createPlace(place,builder);
					IndexResponse resp = client.prepareIndex(placeIndex, table)
							.setSource(builder)
							.get();
					place.setId(resp.getId());
					return (T) place;
			}
			if (t instanceof EventMap) {
				EventMap map = (EventMap)t;
				builder = createMap(map,builder);
				IndexResponse resp = client.prepareIndex(mapIndex, table)
						.setSource(builder)
						.get();
				 map.setId(resp.getId());
				 return (T) map;
			}
						
				else {
				
				throw new  DaoException.DaoInternalError("Sorry , this Object not yet implemented");
			}

		}catch (Exception ex){
			throw new  DaoException.DaoInternalError(ex.getMessage());
		}
	}


	public static boolean delete(String id,String table) throws DaoException.DaoInternalError{

		TransportClient cl = getClient();
		try {

			DeleteResponse del = cl.prepareDelete(table,table,id).get();
			if (del.status().equals(RestStatus.OK)){
				return true;
			}else {
				throw new DaoException.DaoInternalError(del.status().toString());
			}
		}catch (Exception ex){
			throw new DaoException.DaoInternalError(ex.getMessage());
		}

	}

	@SuppressWarnings({"unchecked"})

	public static <T> T update (T t,String table) throws DaoException.DaoInternalError {
		TransportClient client = getClient();
		try {
			UpdateRequest updateRequest = new UpdateRequest();
			updateRequest.index(table);
			updateRequest.type(table);

			XContentBuilder builder = jsonBuilder()
					.startObject();

			if (t instanceof User){
				User user = (User) t;
				updateRequest.id(user.getId());

				builder.field("pseudo", user.getPseudo())
				.field("email", user.getEmail())
				.field("token", user.getToken())
				.endObject();
				updateRequest.doc(builder);
				client.update(updateRequest).get();
				return (T) user;
			}
			if(t instanceof Place) {
				Place p =(Place)t;
				updateRequest.id(p.getId());
				builder=updatePlace(p,builder);
				updateRequest.doc(builder);
				client.update(updateRequest).get();
				return (T) p;
			}
			if(t instanceof EventMap) {
				EventMap map=(EventMap)t;
				updateRequest.id(map.getId());
				builder = updateMap(map,builder);
				updateRequest.doc(builder);
				client.update(updateRequest).get();
				return (T) map;
			}
			else {
				throw new  DaoException.DaoInternalError("Sorry , this Object not yet implemented");
			}

		}catch (Exception ex){
			throw new  DaoException.DaoInternalError(ex.getMessage());
		}
	}

	public static XContentBuilder updatePlace(Place place,XContentBuilder builder)  throws IOException {
		builder = jsonBuilder()
			    .startObject()
			        .field("name", place.getName())
			        .field("description", place.getDescription())
			        .field("latitude", place.getLatitude())
			        .field("longitude", place.getLongitude())
			        .field("category", place.getcategory())
			    .endObject();
		
		return builder;
	}

	public static XContentBuilder updateMap(EventMap map,XContentBuilder builder) throws IOException {
		

					builder 
			        .field("name", map.getName())
			        .field("description", map.getDescription())
			    .endObject();
		
		return builder;	}

	
	public static XContentBuilder createPlace(Place place,XContentBuilder builder)  throws IOException {
		
		
					builder
			        .field("name", place.getName())
			        .field("latitude", place.getLatitude())
			        .field("longitude", place.getLongitude())
			        .field("description", place.getDescription())
			        .field("category", place.getcategory())
			        .field("mapId",place.getMap().getId())
			    .endObject();
		
		return builder;
	}
	
	public static XContentBuilder createMap(EventMap map,XContentBuilder builder) throws IOException {
		

		 			builder 
			        .field("name", map.getName())
			        .field("description", map.getDescription())
			        .field("userId",map.getUser().getId())
			    .endObject();
		
		return builder;	}
	public static XContentBuilder createUser(User user,XContentBuilder builder) throws IOException {

		builder.field("pseudo", user.getPseudo())
		.field("email", user.getEmail())
		.field("password", HashPwd(user.getPassword()))
		.field("token", user.getToken())
		.field("role", user.getRole())
		.endObject();
		
		return builder;	}
	

	public static User getUserWithPseudo(String name) throws DaoException.NotFoundException{
		TransportClient cl = getClient();
		try {
			TermQueryBuilder qb = new TermQueryBuilder("pseudo", name);
			SearchResponse res = cl.prepareSearch(userIndex)
					.setTypes("user")
					.setQuery(qb)
					.get();
			SearchHit[] searchHit = res.getHits().getHits();
			if (searchHit.length == 0)
				throw new  DaoException.NotFoundException("Unknown user with pseudo : "+name);
			Map<String, Object> map = searchHit[0].getSourceAsMap();
			User user = new User();

			user.setId(searchHit[0].getId());
			user.setEmail((String) map.get("email"));
			user.setPseudo(name);
			user.setToken((String) map.get("token"));
			user.setRole((String) map.get("role"));
			user.setPassword((String) map.get("password"));
			return user;
		}catch (Exception ex){
			throw new DaoException.NotFoundException(ex.getMessage());
		}

	}


	@SuppressWarnings({"unchecked"})
	public static <T> T get(String id,String table) throws  DaoException.NotFoundException {
		TransportClient cl = getClient();

		GetResponse res= cl.prepareGet(table,table,id).get();

		if(!res.isExists())
            throw new  DaoException.NotFoundException("No data found for id : "+id);
		if(table.equals("user")){
			Map<String, Object> map = res.getSourceAsMap();
			User user = new User();
			user.setId(id);
			user.setEmail((String) map.get("email"));
			user.setPseudo((String)map.get("pseudo"));
			user.setToken((String)map.get("token"));
			user.setRole((String) map.get("role"));
			user.setPassword((String) map.get("password"));
			return (T) user;
		}

		else if(table.equals("map")){
			Map<String, Object> map = res.getSourceAsMap();
			EventMap eventMap = new EventMap();

			eventMap.setId(id);
			eventMap.setName((String) map.get("name"));
			eventMap.setDescription((String)map.get("description"));

			User u = DB.get((String)map.get("userId"),"user");
			u.setMaps(null);
			eventMap.setUser(u);


			return (T) eventMap;
		}



		else {

			throw new  DaoException.NotFoundException("Sorry , this Object not yet implemented");
		}

		

	}

	public static ArrayList<Place> getPlaces(String mapId) throws DaoInternalError  {
		TransportClient cl = getClient();
		ArrayList<Place> places= new ArrayList<Place>();
		try {


			SearchResponse res = cl.prepareSearch(placeIndex)
					.setTypes(placeIndex)
					.setQuery(QueryBuilders.matchQuery("mapId",mapId)).get();
			/*TermQueryBuilder qb= new TermQueryBuilder("mapId", mapId);
			SearchResponse res= cl.prepareSearch(placeIndex)
					.setTypes(placeIndex)
					.setQuery(qb)
					.get();*/
			SearchHit[] searchHit= res.getHits().getHits();

			if(searchHit.length !=0 ) {
				
				for(int i=0; i<searchHit.length; i++) {

					Map<String, Object> map = searchHit[i].getSourceAsMap();
					Place place= new Place();

					place.setId(searchHit[i].getId());
					place.setName((String)map.get("name"));
					place.setDescription((String)map.get("description"));
					place.setLatitude((String)map.get("latitude"));
					place.setLongitude((String)map.get("longitude"));
					place.setcategory((String)map.get("category"));
					
					places.add(place);
				}
			}
			return places;
		}catch (Exception ex){
			throw new DaoException.DaoInternalError(ex.getMessage());
		}

	}
	
	public static ArrayList<EventMap> getUserMap(String userId) throws DaoInternalError  {
		TransportClient cl = getClient();

		//TermQueryBuilder qb= new TermQueryBuilder("userId", userId);


		try {
			SearchResponse res = cl.prepareSearch(mapIndex)
					.setTypes(mapIndex)
					.setQuery(QueryBuilders.matchQuery("userId",userId)).get();

			/*SearchResponse res= cl.prepareSearch(mapIndex)
					.setTypes(mapIndex)
					.setQuery(qb)
					.get();*/

			SearchHit[] searchHit= res.getHits().getHits();

			ArrayList<EventMap> eventMaps= new ArrayList<EventMap>();
			System.out.println("hits " + res.getHits().totalHits);
			System.out.println("Id " + userId);
			System.out.println("Length " + searchHit.length);
			if(searchHit.length !=0 ) {
				
				for(int i=0; i<searchHit.length; i++) {

					Map<String, Object> map = searchHit[i].getSourceAsMap();
					EventMap eventMap= new EventMap();

					eventMap.setId(searchHit[i].getId());
					eventMap.setName((String)map.get("name"));
					eventMap.setDescription((String)map.get("description"));
					eventMap.setPlaces(null);
					eventMaps.add(eventMap);
				}
			}
			return eventMaps;


		}catch (Exception ex){
			throw new DaoException.DaoInternalError(ex.getMessage());
		}


	}

	public static boolean ExistPseudo(String Pseudo)  {
		TransportClient cl = getClient();
		//TermQueryBuilder qb= new TermQueryBuilder("pseudo", Pseudo);
		try {

			SearchResponse res = cl.prepareSearch(userIndex)
					.setTypes(userIndex)
					.setQuery(QueryBuilders.matchQuery("pseudo",Pseudo)).get();

			/*SearchResponse res= cl.prepareSearch(userIndex)
					.setTypes(userIndex)
					.setQuery(qb)
					.get();*/
			SearchHit[] searchHit= res.getHits().getHits();
			return  (searchHit.length == 1);
		}catch (Exception ex){
			return false;
		}


	}

	static String HashPwd(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(15));
	}


}

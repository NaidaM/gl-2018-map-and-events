package com.genie3.eventsLocation.elastic;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.models.*;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

//import java.util.concurrent.ExecutionException;


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

				if(client.connectedNodes().isEmpty()){
					System.out.println("NULL");
				}


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
				builder.field("created_at",projectDateTime())
                    .field("updated_at",projectDateTime());

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

			BulkByScrollResponse response =
					DeleteByQueryAction.INSTANCE.newRequestBuilder(cl)
					.source("_all")
					.filter(QueryBuilders.matchQuery("mapId", id))
					.get();

			BulkByScrollResponse response2 =
					DeleteByQueryAction.INSTANCE.newRequestBuilder(cl)
					.source("_all")
					.filter(QueryBuilders.matchQuery("user.id", id))
					.get();

			response.getDeleted();
			response2.getDeleted();


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
			builder.field("updated_at",projectDateTime());

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

	public static void updateUserPassword(User u,String password){
		TransportClient client = getClient();
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(userIndex);
		updateRequest.type(userIndex);
		try {
			XContentBuilder builder = jsonBuilder()
					.startObject();
			updateRequest.id(u.getId());
			builder.field("password", HashPwd(password));
			builder.endObject();
			updateRequest.doc(builder);
			client.update(updateRequest).get();
		}catch (Exception ex){
			System.out.println(ex.getMessage());
		}

	}

	public static XContentBuilder updatePlace(Place place,XContentBuilder builder)  throws IOException {

		builder = jsonBuilder()

			    .startObject()
			        .field("name", place.getName())
			        .field("description", place.getDescription())
			        .field("latitude", place.getLatitude())
			        .field("longitude", place.getLongitude())
			    .endObject();

		return builder;
	}

	public static XContentBuilder updateMap(EventMap map,XContentBuilder builder) throws IOException {

		List<String> tags = new ArrayList<String>();
		for (int i = 0;i<map.getTags().size();i++){
			tags.add(map.getTags().get(i).getName());
		}

		List<String> friends = new ArrayList<String>();
		for (int i = 0;i<map.getFriends().size();i++){
			friends.add(map.getFriends().get(i).getPseudo());
		}


		builder
		.field("name", map.getName())
		.field("description", map.getDescription())
		.field("isPrivate", Boolean.valueOf(map.getIsPrivate()))
		.field("tags", tags)
		.field("friends", friends)
		.field("updated_at", projectDateTime())

		.endObject();

		return builder;	}


	public static XContentBuilder createPlace(Place place,XContentBuilder builder)  throws IOException {

		
					builder
			        .field("name", place.getName())
			        .field("latitude", place.getLatitude())
			        .field("longitude", place.getLongitude())
			        .field("description", place.getDescription())
			        .field("mapId",place.getMap().getId())
			    .endObject();

		return builder;
	}

	public static XContentBuilder createMap(EventMap map,XContentBuilder builder) throws IOException {

		     HashMap<String,String> user = new HashMap<String, String>();
		     List<String> tags = new ArrayList<String>();
		     for (int i = 0;i<map.getTags().size();i++){
		         tags.add(map.getTags().get(i).getName());
             }

		     List<String> friends = new ArrayList<String>();
		     for (int i = 0;i<map.getFriends().size();i++){
				 friends.add(map.getFriends().get(i).getPseudo());
             }

		     user.put("id",map.getUser().getId());
		     user.put("pseudo",map.getUser().getPseudo());

		 					builder
			        .field("name", map.getName())
			        .field("description", map.getDescription())
			        .field("isPrivate", Boolean.valueOf(map.getIsPrivate()))
			        .field("user",user)
					.field("tags",tags)
					.field("friends",friends)

			    .endObject();

		return builder;	}

	public static XContentBuilder createUser(User user,XContentBuilder builder) throws IOException {

		builder.field("pseudo", user.getPseudo())
		.field("email", user.getEmail())
		.field("password", HashPwd(user.getPassword()))
		.field("token", user.getToken())
		.field("role", user.getRole())
		.endObject();

		return builder;
	}


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

		try {
			GetResponse res = cl.prepareGet(table, table, id).get();

			if (!res.isExists())
				throw new DaoException.NotFoundException("No data found for id : " + id);
			if (table.equals("user")) {
				Map<String, Object> map = res.getSourceAsMap();
				User user = new User();
				user.setId(id);
				user.setEmail((String) map.get("email"));
				user.setPseudo((String) map.get("pseudo"));
				user.setToken((String) map.get("token"));
				user.setRole((String) map.get("role"));
				user.setPassword((String) map.get("password"));
				return (T) user;
			} else if (table.equals("map")) {
				Map<String, Object> map = res.getSourceAsMap();
				EventMap eventMap = new EventMap();

				eventMap.setId(id);
				eventMap.setName((String) map.get("name"));
				eventMap.setDescription((String) map.get("description"));
				eventMap.setVisibility((String.valueOf(map.get("isPrivate"))));

				User u = new User();
				Map<String, String> mapuser = (Map<String, String>) map.get("user");
				u.setId(mapuser.get("id"));
				u.setPseudo((mapuser.get("pseudo")));
				u.setMaps(null);
				eventMap.setUser(u);


				return (T) eventMap;
			}
			else {

				throw new  DaoException.NotFoundException("Sorry , this Object not yet implemented");
			}
		}catch (IndexNotFoundException ex){
			throw new  DaoException.NotFoundException("No data found");
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
					place.setDescription((String)map.get("description"));
					place.setLatitude((String)map.get("latitude"));
					place.setLongitude((String)map.get("longitude"));

					places.add(place);
				}
			}
			return places;
		}catch (Exception ex){
			throw new DaoException.DaoInternalError(ex.getMessage());
		}

	}



	@SuppressWarnings({"unchecked"})

	public static ArrayList<EventMap> getUserMap(String userId) throws DaoInternalError,DaoException.NotFoundException {
		TransportClient cl = getClient();

		//TermQueryBuilder qb= new TermQueryBuilder("userId", userId);


		try {
			SearchResponse res = cl.prepareSearch(mapIndex)
					.setTypes(mapIndex)
					.setQuery(QueryBuilders.matchQuery("user.id",userId))
                    .addSort("updated_at" , SortOrder.DESC).get();


			SearchHit[] searchHit= res.getHits().getHits();

			ArrayList<EventMap> eventMaps= new ArrayList<EventMap>();
			if(searchHit.length !=0 ) {

				for(int i=0; i<searchHit.length; i++) {

					Map<String, Object> map = searchHit[i].getSourceAsMap();
					EventMap eventMap= new EventMap();

					eventMap.setId(searchHit[i].getId());
					eventMap.setName((String)map.get("name"));
					eventMap.setDescription((String)map.get("description"));
					eventMap.setPlaces(null);
					eventMap.setVisibility((String.valueOf(map.get("isPrivate"))));
					ArrayList<String> tags =  (ArrayList<String>) map.get("tags");
					ArrayList<String> friends =  (ArrayList<String>) map.get("friends");
					ArrayList<Tag> tagsList = new ArrayList<Tag>();
					ArrayList<Friend> friendList = new ArrayList<Friend>();
					for (int t = 0; t<tags.size();t++){
						Tag tag = new Tag(tags.get(t));
						tagsList.add(tag);
					}
					for (int j = 0; j<friends.size();j++){
						Friend friend = new Friend(friends.get(j));
						friendList.add(friend);
					}
					eventMap.setTags(tagsList);
					eventMap.setFriends(friendList);
					eventMaps.add(eventMap);
				}
			}
			return eventMaps;


		}catch (IndexNotFoundException ex){
			throw new  DaoException.NotFoundException("No map data found for user: "+ userId);
		}
		catch (Exception ex){
			throw new DaoException.DaoInternalError(ex.getMessage());
		}


	}


	@SuppressWarnings({"unchecked"})
	public static ArrayList<EventMap> getPublicMap() throws DaoInternalError,DaoException.NotFoundException {
		TransportClient cl = getClient();

		try {
			SearchResponse res = cl.prepareSearch(mapIndex)
					.setTypes(mapIndex)
					.setQuery(QueryBuilders.matchQuery("isPrivate",false)).get();

			SearchHit[] searchHit= res.getHits().getHits();

			ArrayList<EventMap> eventMaps= new ArrayList<EventMap>();
			if(searchHit.length !=0 ) {

				for(int i=0; i<searchHit.length; i++) {

					Map<String, Object> map = searchHit[i].getSourceAsMap();
					EventMap eventMap= new EventMap();

					eventMap.setId(searchHit[i].getId());
					eventMap.setName((String)map.get("name"));
					eventMap.setDescription((String)map.get("description"));
					eventMap.setPlaces(null);
					eventMaps.add(eventMap);

					ArrayList<Tag> tags =  (ArrayList<Tag>) map.get("tags");
					ArrayList<Friend> friends =  (ArrayList<Friend>) map.get("friends");

					eventMap.setTags(tags);
					eventMap.setFriends(friends);

					User u = new User();
					Map<String, String> mapuser = (Map<String, String> ) map.get("user");
					u.setId(mapuser.get("id"));
					u.setPseudo((mapuser.get("pseudo")));
					u.setMaps(null);
					eventMap.setUser(u);
				}
			}
			return eventMaps;


		}catch (IndexNotFoundException ex){

			throw new  DaoException.NotFoundException("No data found");
		}
		catch (Exception ex){
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
	
	//search a map using the query qb
	public static SearchHit[] MapSearch(QueryBuilder qb) {
		TransportClient cl = getClient();
		SearchResponse res= cl.prepareSearch(mapIndex)
				.setTypes(mapIndex)
				.setQuery(qb)
				.get();

		return res.getHits().getHits();
	}

	//return map with places with tags search
	public SearchHit[] searchPublicMapTags(String[] search) {
		TransportClient cl= getClient();
		
		BoolQueryBuilder qb= QueryBuilders.boolQuery()
				.minimumShouldMatch(1);
		for(String s : search) {
			qb.should(QueryBuilders.termQuery("tags", s));
		}
		
		SearchResponse res= cl.prepareSearch(placeIndex)
				.setTypes(placeIndex)
				.setQuery(qb)
				.get();
		
		SearchHit[] tab = res.getHits().getHits();
		String[] tabid= new String[tab.length];

		for(int i=0; i<tab.length; i++) {
			tabid[i]= tab[i].getId();
		}

		return MapSearch(queryMapID(tabid, false));
	}

	// return query for map with id id, if priv is true search private and public map else only public map
	public QueryBuilder queryMapID(String[] id, boolean priv) {
		BoolQueryBuilder qb= QueryBuilders.boolQuery();

		if(!priv)
			qb.mustNot(QueryBuilders.termQuery("isPrivate", false));
		qb.should(QueryBuilders.idsQuery(mapIndex).addIds(id))
		.minimumShouldMatch(1);
		
		return qb;
	}

	//search a map using the query qb
	public static SearchHit[] mapSearch(QueryBuilder qb) {
		TransportClient cl = getClient();
		SearchResponse res= cl.prepareSearch(mapIndex)
				.setTypes(mapIndex)
				.setQuery(qb)
				.get();

		return res.getHits().getHits();
	}

	// search map by using it tags
	public static SearchHit[] searchMapTags(String[] tags) {
		TransportClient cl= getClient();
		BoolQueryBuilder qb= QueryBuilders.boolQuery()
				.minimumShouldMatch(1);
		for(String s : tags) {
			qb.should(QueryBuilders.termQuery("tags", s));
		}
		SearchResponse res = cl.prepareSearch(mapIndex)
				.setTypes(mapIndex)
				.setQuery(qb)
				.get();
		return res.getHits().getHits();
	}


	public static String projectDateTime(){
        LocalDateTime date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }


   public static void createAllIndex(){
       IndicesAdminClient indicesAdminClient = getClient().admin().indices();
       try {
           indicesAdminClient.prepareCreate("user").get();
           indicesAdminClient.prepareCreate("place").get();
           indicesAdminClient.prepareCreate("map").get();
       }catch (Exception e){
           System.out.println("Error when creating index");
       }

   }

   public static void delateAllIndex(){
       IndicesAdminClient indicesAdminClient = getClient().admin().indices();
       try {
           indicesAdminClient.prepareDelete("user").get();
           indicesAdminClient.prepareDelete("place").get();
           indicesAdminClient.prepareDelete("map").get();
       }catch (Exception e){
           System.out.println("Error when creating index");
       }

   }



	@SuppressWarnings({"unchecked"})

	public static ArrayList<EventMap> getFriendMap(String pseudo) throws DaoInternalError,DaoException.NotFoundException {
		TransportClient cl = getClient();

		try {
			SearchResponse res = cl.prepareSearch(mapIndex)
					.setTypes(mapIndex)
					.setQuery(QueryBuilders.matchQuery("friends",pseudo))
					.addSort("updated_at" , SortOrder.DESC).get();

			SearchHit[] searchHit= res.getHits().getHits();

			ArrayList<EventMap> eventMaps= new ArrayList<EventMap>();

			if(searchHit.length !=0 ) {

				for(int i=0; i<searchHit.length; i++) {

					Map<String, Object> map = searchHit[i].getSourceAsMap();
					EventMap eventMap= new EventMap();

					eventMap.setId(searchHit[i].getId());
					eventMap.setName((String)map.get("name"));
					eventMap.setDescription((String)map.get("description"));
					eventMap.setPlaces(null);
					User u = new User();

					Map<String, String> mapuser = (Map<String, String> ) map.get("user");
					u.setId(mapuser.get("id"));
					u.setPseudo((mapuser.get("pseudo")));
					eventMap.setUser(u);
					eventMap.setVisibility((String.valueOf(map.get("isPrivate"))));
					ArrayList<String> tags =  (ArrayList<String>) map.get("tags");
					ArrayList<String> friends =  (ArrayList<String>) map.get("friends");
					ArrayList<Tag> tagsList = new ArrayList<Tag>();
					ArrayList<Friend> friendList = new ArrayList<Friend>();
					for (int t = 0; t<tags.size();t++){
						Tag tag = new Tag(tags.get(t));
						tagsList.add(tag);
					}
					for (int j = 0; j<friends.size();j++){
						Friend friend = new Friend(friends.get(j));
						friendList.add(friend);
					}
					eventMap.setTags(tagsList);
					eventMap.setFriends(friendList);
					eventMaps.add(eventMap);
				}
			}
			return eventMaps;


		}catch (IndexNotFoundException ex){
			throw new  DaoException.NotFoundException("No map data found for user: "+ pseudo);
		}
		catch (Exception ex){
			throw new DaoException.DaoInternalError(ex.getMessage());
		}


	}



}

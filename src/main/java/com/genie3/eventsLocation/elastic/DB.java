package com.genie3.eventsLocation.elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.genie3.eventsLocation.exception.DaoException;
import org.elasticsearch.action.delete.DeleteResponse;

import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.Place;
import com.genie3.eventsLocation.models.User;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.mindrot.jbcrypt.BCrypt;
import org.elasticsearch.rest.RestStatus;


public final class DB {


	 private static String host = "127.0.0.1";
	 private static int port = 9300;
	 private static String index = "events_location";
	
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
				IndexResponse resp = client.prepareIndex(index, table)
						.setSource(builder)
						.get();
				user.setId(resp.getId());
				return (T) user;
			}
			if (t instanceof Place) {
					Place place = (Place)t;
					builder = createPlace(place,builder);
					IndexResponse resp = client.prepareIndex(index, table)
							.setSource(builder)
							.get();
					place.setId(resp.getId());
					return (T) place;
			}
			if (t instanceof EventMap) {
				EventMap map = (EventMap)t;
				builder = createMap(map,builder);
				IndexResponse resp = client.prepareIndex(index, table)
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
			DeleteResponse del = cl.prepareDelete(index,table,id).get();
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
			updateRequest.index(index);
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
			else {
				throw new  DaoException.DaoInternalError("Sorry , this Object not yet implemented");
			}

		}catch (Exception ex){
			throw new  DaoException.DaoInternalError(ex.getMessage());
		}
	}



	
	public static XContentBuilder createPlace(Place place,XContentBuilder builder)  throws IOException {
		
		
		builder = jsonBuilder()
			    .startObject()
			        .field("name", place.getName())
			        .field("latitude", place.getLatitude())
			        .field("longitude", place.getLongitude())
			        .field("description", place.getDescription())
			        .field("category", place.getcategory())
			        .field("mapId",place.getplaceId())
			    .endObject();
		
		return builder;
	}
	
	public static XContentBuilder createMap(EventMap map,XContentBuilder builder) throws IOException {
		

		 builder = jsonBuilder()
			    .startObject()
			        .field("name", map.getName())
			        .field("description", map.getDescription())
			        .field("userId",map.getUser())
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
	

	public static User getUserWithPseudo(String name) throws DaoException.DaoInternalError{
		TransportClient cl = getClient();
		try {
			TermQueryBuilder qb = new TermQueryBuilder("pseudo", name);
			SearchResponse res = cl.prepareSearch(index)
					.setTypes("user")
					.setQuery(qb)
					.get();
			SearchHit[] searchHit = res.getHits().getHits();
			if (searchHit.length == 0)
				return null;
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
			throw new DaoException.DaoInternalError(ex.getMessage());
		}

	}


	@SuppressWarnings({"unchecked"})
	public static <T> T get(String id,String table) throws  DaoException.NotFoundException {
		TransportClient cl = getClient();

		GetResponse res= cl.prepareGet(index,table,id).get();

		if(!res.isExists())
			return null;
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
		}else {

			throw new  DaoException.NotFoundException("Sorry , this Object not yet implemented");
		}

		

	}


	
	public static boolean ExistPseudo(String Pseudo)  {
		TransportClient cl = getClient();
		TermQueryBuilder qb= new TermQueryBuilder("pseudo", Pseudo);
		try {
			SearchResponse res= cl.prepareSearch(index)
					.setTypes("user")
					.setQuery(qb)
					.get();
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

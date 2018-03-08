package com.genie3.eventsLocation.elastic;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import com.genie3.eventsLocation.models.User;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.rest.RestStatus;
import com.genie3.eventsLocation.models.User;


public class Database {
	/*private final TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
	        .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
	*/
	
	private static TransportClient client;
	
	private static TransportClient getClient() throws UnknownHostException {
		if(client == null)
			client = new PreBuiltTransportClient(Settings.EMPTY)
	        .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

		return client;
	}
	
	public static void createUser(User user)
			throws  IOException {
		TransportClient cl= getClient();
		XContentBuilder builder = jsonBuilder()
			    .startObject()
			        .field("pseudo", user.getPseudo())
			        .field("email", user.getEmail())
			        .field("password", user.getPassword())
			        .field("token", user.getToken())
			        .field("role", user.getRole())
			    .endObject();
		
		cl.prepareIndex("project", "user")
				.setSource(builder)
				.get();
		
	}
	
	public static void createPlace(String name,
			double latitude,
			double longitude,
			String description,
			String category) throws IOException {
		
		TransportClient cl= getClient();
		XContentBuilder builder = jsonBuilder()
			    .startObject()
			        .field("name", name)
			        .field("latitude", latitude)
			        .field("longitude", longitude)
			        .field("description", description)
			        .field("category", category)
			        .field("mapId")
			    .endObject();
		
		cl.prepareIndex("project", "place")
				.setSource(builder)
				.get();
		
	}
	
	public static void createMap(String nom,
			String description) {
		
		
	}
	public static User getUser(String name) throws UnknownHostException{
		TransportClient cl = getClient();
		TermQueryBuilder qb= new TermQueryBuilder("pseudo", name);
		SearchResponse res= cl.prepareSearch("project")
				.setTypes("user")
				.setQuery(qb)
				.get();
		SearchHit[] searchHit= res.getHits().getHits();
		if(searchHit.length == 0)
			return null;
		Map<String, Object> map =searchHit[0].getSourceAsMap();
		User user = new User();
		user.setId(searchHit[0].docId());
		user.setEmail((String) map.get("email"));
		user.setPseudo(name);
		user.setToken((String)map.get("token"));
		return user;

	}
	public static boolean delateUser(String id)throws UnknownHostException {
	
		TransportClient cl = getClient();
		TermQueryBuilder qb= new TermQueryBuilder("id", id);
		DeleteResponse del = cl.prepareDelete("project","user", id).get();
		return del.status().equals(RestStatus.OK);
			
		
	}
	
	
}

package com.genie3.eventsLocation.dao;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import com.genie3.eventsLocation.elastic.Database;
import com.genie3.eventsLocation.models.User;

public class UserDao  {

	public void create(User user) throws UnknownHostException, IOException {
		Database.createUser(user);
	}
	

}

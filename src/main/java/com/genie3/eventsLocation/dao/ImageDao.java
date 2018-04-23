package com.genie3.eventsLocation.dao;

import javax.ws.rs.core.Response;
import java.io.InputStream;

public interface ImageDao {

    Response  upload(InputStream fileInputStream,String filename,String Idplace);
	Response download(String fileName);
	boolean deletePhoto(String photo);
}
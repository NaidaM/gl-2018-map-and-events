package com.genie3.eventsLocation.dao;

import java.io.InputStream;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface ImageInterface {

    public Response  upload(InputStream fileInputStream,String filename);
	public Response download(String fileName);
	public ArrayList<String> Images();
	
}
package com.genie3.eventsLocation.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;


public class ImageImpl implements ImageInterface {
    

	 private static String UPLOAD_PATH = "/home/yannis/EventLoc";
	
	public Response  upload(InputStream fileInputStream,FormDataContentDisposition fileMetaData) {
		try
        {
            int read = 0;
            byte[] bytes = new byte[1024];
 
            OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + fileMetaData.getFileName()));
            while ((read = fileInputStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            System.out.println("--- name: " + fileMetaData.getFileName());
            out.flush();
            out.close();
        } catch (IOException e)
        {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }
        return Response.ok(fileMetaData.getFileName() + " uploaded successfully !!").build();
    
	}

	public Response download(String fileName) {     
			    String fileLocation = UPLOAD_PATH+ fileName;		
			    Response response = null;
			    NumberFormat myFormat = NumberFormat.getInstance();
			    myFormat.setGroupingUsed(true);
			    File file = new File(UPLOAD_PATH + fileName);
			    if (file.exists()) {
			      ResponseBuilder builder = Response.ok(file);
			      builder.header("Content-Disposition", "attachment; filename=" + file.getName());
			      response = builder.build();
		
			    } else {		
			      response = Response.status(404).
			      entity("FILE NOT FOUND: " + fileLocation).
			      type("text/plain").build();}
			    
        return response;
}
    public ArrayList<String> Images() {
    	// TODO Auto-generated method stub
		return null;
	}
}
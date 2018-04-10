package com.genie3.eventsLocation.dao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.mindrot.jbcrypt.BCrypt;

import com.genie3.eventsLocation.elastic.DB;



public class ImageImpl implements ImageInterface {
    

	 private static String UPLOAD_PATH = "Upload/";
	 public static final int nombre = 1000000;
	
	public Response  upload(InputStream fileInputStream,String filename,String Idplace) {
		try{
            int read = 0;
            byte[] bytes = new byte[1024];
            Random rd = new Random();
            String path = UPLOAD_PATH +HashPhoto(""+rd.nextInt(nombre))+".jpg";
            OutputStream out = new FileOutputStream(new File(path));   
            ByteArrayOutputStream bao = new ByteArrayOutputStream();           
            while ((read = fileInputStream.read(bytes)) != -1) {
                bao.write(bytes, 0, read);
            }
            byte[] data = bao.toByteArray();
            InputStream in  = new ByteArrayInputStream(data);
			BufferedImage image = ImageIO.read(in);
			Iterator<ImageWriter> writers=  ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) writers.next();            
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);
            writer.write(null, new IIOImage(image, null, null), param);            
            System.out.println("--- name: " + filename);
            
            out.flush();
            out.close();
            DB.addPhoto(path, Idplace);
        } catch (IOException e)
        {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }catch (Exception e)
        {
            throw new WebApplicationException("file is not image. Please try again !!");
        }
        return Response.ok(filename + " uploaded successfully !!").build();
           
	}

	static String HashPhoto(String photo) {
		return BCrypt.hashpw(photo, BCrypt.gensalt(15));
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
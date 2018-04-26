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

import com.genie3.eventsLocation.elastic.DB;



public class ImageDaoImpl implements ImageDao {
    

	 private static String UPLOAD_PATH = "Upload/";
	 private static final int nombre = 1000000;
	
	public Response  upload(InputStream fileInputStream,String filename,String Idplace) {
		try{
            int read = 0;
            int b=0;
            byte[] bytes = new byte[1024];
            Random rd = new Random();
            String path = (DB.projectDateTime()+rd.nextInt(nombre)).replaceAll("\\.", "");
            path= path.replaceAll(":", "")+".jpg";
            OutputStream out = new FileOutputStream(new File(UPLOAD_PATH +path));
            ByteArrayOutputStream bao = new ByteArrayOutputStream();           
            while ((read = fileInputStream.read(bytes)) != -1) {
                bao.write(bytes, 0, read);
                b++;
            }
            b*=1024;
            byte[] data = bao.toByteArray();
            InputStream in  = new ByteArrayInputStream(data);
			BufferedImage image = ImageIO.read(in);
			Iterator<ImageWriter> writers=  ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) writers.next();            
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            if(b<1048576) {
            	 writer.write(null, new IIOImage(image, null, null), null);
                 System.out.println("--- name: " + filename);
                 out.flush();
                 out.close();
            }else {
            	param.setCompressionQuality(0.70f);
            	writer.write(null, new IIOImage(image, null, null), param);
            	System.out.println("--- name: " + filename);            
            	out.flush();
            	out.close();}
            DB.addPhoto(path, Idplace);
        } catch (IOException e)
        {
        	e.printStackTrace();
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }catch (Exception e)
        {
            throw new WebApplicationException("file is not image. Please try again !!");
        }
        return Response.ok(filename + " uploaded successfully !!").build();
           
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

	public boolean deletePhoto(String photo) {	
		String path = UPLOAD_PATH+ photo;
		NumberFormat myFormat = NumberFormat.getInstance();
		myFormat.setGroupingUsed(true);
		try {
			if(DB.deletePhoto(photo)) {
				File file = new File(path);
				if(file.delete()) {
					return true;
				}else
					return false;
			}else
				return false;
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.dao.Dao;

import java.io.InputStream;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/upload")
public class UploadTEST {
    @POST
    @Path("/image")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadImage(  @FormDataParam("file") List<FormDataBodyPart> bodyParts
                                  ) throws Exception
    {

			for (int i = 0; i< bodyParts.size();i++){
				BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
				String fileName = bodyParts.get(i).getContentDisposition().getFileName();




				Dao.getImageDAO().upload(bodyPartEntity.getInputStream(), fileName);
			}



		return Response.status(Response.Status.OK).entity("Ok").build();
    }


	@GET
	@Path("/download")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFilebyQuery(@QueryParam("filename") String fileName) {
		    return Dao.getImageDAO().download(fileName);
		  }
	
	@GET
	@Path("/download/{filename}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFilebyPath(@PathParam("filename")  String fileName) {
		    return Dao.getImageDAO().download(fileName);
		  }

}
	



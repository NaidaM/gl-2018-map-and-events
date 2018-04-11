package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.dao.DaoFactory;
import com.genie3.eventsLocation.entities.Error;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Path("/upload")
public class UploadResource {
    
	@POST
    @Path("/image/{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadImage(@PathParam("id") String placeId,
                                @FormDataParam("file") List<FormDataBodyPart> bodyParts
    )  {

        for (int i = 0; i < bodyParts.size(); i++) {
            BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
            String fileName = bodyParts.get(i).getContentDisposition().getFileName();
            DaoFactory.getImageDAO().upload(bodyPartEntity.getInputStream(), fileName,placeId);
        }

        return Response.status(Response.Status.OK).entity("Ok").build();
    }


	
    @GET
    @Path("/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFilebyQuery(@QueryParam("filename") String fileName) {
        return DaoFactory.getImageDAO().download(fileName);
    }

    @GET
    @Path("/download/{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFilebyPath(@PathParam("filename") String fileName) {
        return DaoFactory.getImageDAO().download(fileName);
        
        }
        

    @GET
    @Path("/date")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response currentDate() {
        LocalDateTime date = LocalDateTime.now();
        String text = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        //LocalDate parsedDate = LocalDate.parse(text, formatter);
        return Response.status(Response.Status.OK).entity(text).build();

    }

}
	



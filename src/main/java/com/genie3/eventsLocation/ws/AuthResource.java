package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.constraints.AttemptAuthUser;
import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.filters.AuthentificationFilter;
import com.genie3.eventsLocation.filters.TokenSecurity;
import com.genie3.eventsLocation.models.Error;
import com.genie3.eventsLocation.models.User;
import org.jose4j.lang.JoseException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

@Path("/auth")

public class AuthResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response get(@AttemptAuthUser User user) {

        try {

            User user1 = Dao.getUserDao().getWithPseudo(user.getPseudo());

            Dao.getUserDao().authenticate(user.getPassword(), user1.getPassword());
            // Generate token
            String token = TokenSecurity.generateJwtToken(String.valueOf(user1.getId()));

            user1.setToken(token);

            //insert into bd

               // DB.setToken(user1.getId(),token);
                DB.update(user1,"user");



            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("access_token", token );


          //  HashMap<String,String> token = Dao.getUserDao().getToken(user.getPseudo());


            return Response.status(Response.Status.OK).entity(map).build();

        }catch (DaoException.NotFoundException ex){

            Error error = new Error(ex.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();

        }catch (Exception ex){
            Error error = new Error(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }

        /*
        *   return Response.status(Response.Status.OK).entity(user).build();
        *    use this to return result with specific Http status and the method return must be
        *    javax.ws.rs.core.Response
        */
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response create(@Valid @ValidPassword User user)  {

        try {
            user.setRole("user");
            if(!DB.ExistPseudo(user.getPseudo())){
                User user1 = Dao.getUserDao().create(user,"user");
                return Response.status(Response.Status.CREATED).entity(user1).build();
            }else {
                Error error = new Error("This pseudo already exist");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }

        }catch (DaoException.DaoInternalError ex){

            Error error = new Error(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }catch (Exception ex){
            Error error = new Error("This pseudo already exist");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/check")
    public boolean ExistPseudo(@QueryParam("pseudo") String Pseudo) {

        return DB.ExistPseudo(Pseudo);
    }

}

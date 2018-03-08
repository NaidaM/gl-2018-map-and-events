package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.constraints.AttemptAuthUser;
import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.dao.Dao;
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
import java.util.HashMap;

@Path("/auth")

public class AuthResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response get(@AttemptAuthUser User user) {

        try {
            Dao.getUserDao().authenticate(user.getPseudo(), user.getPassword());
            User user1 = Dao.getUserDao().get(user.getPseudo());

            // Generate token
            String token = TokenSecurity.generateJwtToken(String.valueOf(user1.getId()));

            user1.setToken(token);
            user1.setRole("user");
            //insert into bd

            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put(AuthentificationFilter.AUTHORIZATION_PROPERTY, token );


          //  HashMap<String,String> token = Dao.getUserDao().getToken(user.getPseudo());


            return Response.status(Response.Status.OK).entity(map).build();
        }catch (DaoException.NotFoundException ex){
            Error error = new Error(ex.getMessage());

            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }catch (JoseException ex){
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
            User user1 = Dao.getUserDao().create(user);
            return Response.status(Response.Status.CREATED).entity(user1).build();
        }catch (DaoException.DaoInternalError ex){

            Error error = new Error(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }

    }

}

package com.genie3.eventsLocation.ws;

import com.genie3.eventsLocation.constraints.AttemptAuthUser;
import com.genie3.eventsLocation.constraints.ValidPassword;
import com.genie3.eventsLocation.dao.DaoFactory;
import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.filters.TokenSecurity;
import com.genie3.eventsLocation.entities.Error;
import com.genie3.eventsLocation.entities.User;
import com.genie3.eventsLocation.entities.UserPasswordUpdate;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path("/auth")

public class AuthResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response get(@NotNull(message = "Post body must not empty") @AttemptAuthUser User user) {

        try {

            User user1 = DaoFactory.getUserDao().getWithPseudo(user.getPseudo());

            DaoFactory.getUserDao().authenticate(user.getPassword(), user1.getPassword());
            // Generate token
            String token = TokenSecurity.generateJwtToken(String.valueOf(user1.getId()));
            user1.setToken(token);

            //insert into bd

               // DB.setToken(user1.getId(),token);
                DB.update(user1,"user");



            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("access_token", token );
            return Response.status(Response.Status.OK).entity(map).build();

        }catch (DaoException.NotFoundException ex){

            Error error = new Error("login or password incorect");
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();

        }catch (Exception ex){
            Error error = new Error(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    public Response create(@NotNull(message = "Post body must not empty")
                               @Valid @ValidPassword User user)  {

        try {
            user.setRole("user");
            if(!DB.ExistPseudo(user.getPseudo())){
                User user1 = DaoFactory.getUserDao().create(user,"user");
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


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user"})
    @Path("{pseudo}/password")
    public Response updatePassword(
            @PathParam("pseudo") String pseudo,
            @NotNull(message = "Post body must not empty")
            @Valid UserPasswordUpdate user) {

        try {

            if(!user.getNewPassword().equals(user.getNewPassword())){
                return Response.status(Response.Status.OK).entity(
                        new Error("New Password does not match")
                ).build();
            }

            User user1 = DaoFactory.getUserDao().getWithPseudo(pseudo);

            DaoFactory.getUserDao().authenticate(user.getActualPassword(), user1.getPassword());

            DaoFactory.getUserDao().updatePassord(user1,user.getNewPassword());
            return Response.status(Response.Status.OK).build();

        }catch (DaoException.NotFoundException ex){

            Error error = new Error("actual password incorrect");
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();

        }catch (Exception ex){
            Error error = new Error(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }

    }

}

package com.genie3.eventsLocation.filters;

import com.genie3.eventsLocation.dao.Dao;
import com.genie3.eventsLocation.dao.UserDaoInterface;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.Error;
import com.genie3.eventsLocation.models.User;
import org.apache.log4j.Logger;
import org.jose4j.jwt.consumer.ErrorCodeValidator;
import org.jose4j.jwt.consumer.InvalidJwtException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.container.ResourceInfo;
import java.lang.reflect.Method;
import java.util.*;

@Provider
public class AuthentificationFilter implements ContainerRequestFilter {


    @Context
    private ResourceInfo resourceInfo;


    final static Logger logger = Logger.getLogger( AuthentificationFilter.class );
    public static String AUTHORIZATION_PROPERTY = "Authorization";
    public static String HEADER_PROPERTY_ID = "id";
    public static final String ACCESS_REFRESH = "Token expired. Please authenticate again!";
    public static final String ACCESS_INVALID_TOKEN = "Token invalid. Please authenticate again!";
    public static final String ACCESS_DENIED = "Not allowed to access this resource!";
    public static final String ACCESS_FORBIDDEN = "Access forbidden!";



    public void filter( ContainerRequestContext requestContext )
    {



        Method method = resourceInfo.getResourceMethod();

        // nobody can access
        if( method.isAnnotationPresent( DenyAll.class ) )
        {
            Response r = Response
                    .status(Response.Status.FORBIDDEN)
                    .type(MediaType.TEXT_PLAIN)
                    .build();
            requestContext.abortWith(r);
            return;
        }


        // everybody can access (e.g. user/create or user/authenticate)
        if(method.isAnnotationPresent(  RolesAllowed.class  ))
        {


            // get request headers to extract jwt token
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
            final List<String> authProperty = headers.get( AUTHORIZATION_PROPERTY );

            // block access if no authorization information is provided
            if( authProperty == null || authProperty.isEmpty() )
            {
                logger.warn("No token provided!");
                Error error = new Error("No token provided!");
                Response r = Response
                        .status(Response.Status.UNAUTHORIZED)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(error)
                        .build();
                requestContext.abortWith(r);
                return;
            }

            String id = null ;
            String jwt = authProperty.get(0).replaceAll("Bearer","").trim();

            // try to decode the jwt - deny access if no valid token provided
            try {
                id = TokenSecurity.validateJwtToken( jwt );
            } catch ( InvalidJwtException e ) {
                logger.warn("Invalid token provided!");
                HashMap<String ,List<ErrorCodeValidator.Error>> error = new HashMap<String, List<ErrorCodeValidator.Error>>();
                error.put("message",e.getErrorDetails());
                Response r = Response
                        .status(Response.Status.UNAUTHORIZED)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(error)
                        .build();


                requestContext.abortWith(r);
                return;
            }

            // check if token matches an user token (set in user/authenticate)
            UserDaoInterface userDao = Dao.getUserDao();
            User user = null;
            try {
                user = userDao.get(id);
                user.setRole("user");
                user.setToken("eyJraWQiOiJrMSIsImFsZyI6IlJTMjU2In0.eyJpc3MiOiJldmVudHMuY29tIiwiZXhwIjoxNTI4MjgyMjU5LCJqdGkiOiJ3bUtzSnNMQTlQeVhCYzYtckphM1d3IiwiaWF0IjoxNTIwNTA2MjU5LCJuYmYiOjE1MjA1MDYyNTksImlkIjoiMSJ9.e1H95NLY09UCqf57-hD5Pun9ZKZ8VE37BOYFZjG4ru36sC4nO5xKwU2Jey2xPVnwgxOAyI5oBG4RTeUuvpA-CeJfVFm6uPRGFxFH97NtPZBa37KJL0d7FGPqWxBhuvVvMN9v_Rzd4fBIlVX1ASriSnRSPMMMbYXszsNkuETyDqYtkuRddIRPCh17cHETIcoddN50ieBG5axo_shEBbBkmnmAt8ihoTvOBh34lYA0tLgJahJWVlAWhBqZbH38wtCU7io-f7XKwfvf5WUYmxOCmequVt0NA6S2TTdcm_F6XC8LYBKD_VPeG4xh_LPHN7CTrCCt0kEXKHDB1eN106CmJg");
            }
            catch ( DaoException.NotFoundException e ) {
                logger.warn("Token missmatch!");

                Error error = new Error("Token missmatch!");
                Response r = Response
                        .status(Response.Status.UNAUTHORIZED)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(error)
                        .build();

                requestContext.abortWith(r);
                return;
            }




                // token does not match with token stored in database - enforce re authentication
                if( !user.getToken().equals( jwt ) ) {
                    logger.warn("Token expired!");
                    Error error = new Error("Token not equal to token of server !! Expired");
                    Response r = Response
                            .status(Response.Status.UNAUTHORIZED)
                            .type(MediaType.APPLICATION_JSON)
                            .entity(error)
                            .build();
                    requestContext.abortWith(
                            r
                    );
                    return;
                }


               /* if( method.isAnnotationPresent( RolesAllowed.class ) )
                {*/
                    // get annotated roles
                    RolesAllowed rolesAnnotation = method.getAnnotation( RolesAllowed.class );
                    Set<String> rolesSet = new HashSet<String>( Arrays.asList( rolesAnnotation.value() ) );

                    // user valid?
                    if( !isUserAllowed( user.getRole(), rolesSet ) )
                    {

                        logger.warn("User does not have the rights to acces this resource!");
                        Error error = new Error("User does not have the rights to acces this resource!");
                        Response r = Response
                                .status(Response.Status.UNAUTHORIZED)
                                .type(MediaType.APPLICATION_JSON)
                                .entity(error)
                                .build();
                        requestContext.abortWith(
                                r
                        );
                        return;
                    }
                //}




            // verify user access from provided roles ("admin", "user", "guest")


            // set header param email for user identification in rest service - do not decode jwt twice in rest services
            List<String> idList = new ArrayList<String>();
            idList.add( id );
            headers.put( HEADER_PROPERTY_ID, idList );
        }


    }

    private boolean isUserAllowed( final String userRole, final Set<String> rolesSet )
    {
        boolean isAllowed = false;

        if( rolesSet.contains( userRole ) )
        {
            isAllowed = true;
        }

        return isAllowed;
    }
}

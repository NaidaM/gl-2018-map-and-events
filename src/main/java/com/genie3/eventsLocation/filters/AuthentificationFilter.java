package com.genie3.eventsLocation.filters;

import com.genie3.eventsLocation.dao.DaoFactory;
import com.genie3.eventsLocation.dao.UserDao;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.entities.Error;
import com.genie3.eventsLocation.entities.User;
import org.apache.log4j.Logger;
import org.jose4j.jwt.consumer.ErrorCodeValidator;
import org.jose4j.jwt.consumer.InvalidJwtException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.*;

@Provider
public class AuthentificationFilter implements ContainerRequestFilter {


    @Context
    private ResourceInfo resourceInfo =null;


    final static Logger logger = Logger.getLogger( AuthentificationFilter.class );
    public static String AUTHORIZATION_PROPERTY = "Authorization";
    public static String HEADER_PROPERTY_ID = "id";


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
            UserDao userDao = DaoFactory.getUserDao();
            User user = null;
            try {
                user = userDao.get(id,"user");
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

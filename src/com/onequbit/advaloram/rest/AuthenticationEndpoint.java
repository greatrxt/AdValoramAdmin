package com.onequbit.advaloram.rest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.onequbit.advaloram.hibernate.entity.AdValUser;
import com.onequbit.advaloram.hibernate.entity.Role;
import com.onequbit.advaloram.util.HibernateUtil;

@Path("/authentication")
public class AuthenticationEndpoint {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username, 
                                     @FormParam("password") String password) {

        try {

            // Authenticate the user using the credentials provided
            String token = authenticateAndGenerateToken(username, password);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    @Secured({Role.ADMINISTRATOR})	//secured URL to test whether user has been authenticated
    @GET
    public Response isUserAuthenticated() {

		try {
			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}      
	}
    
    private String authenticateAndGenerateToken(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(AdValUser.class);
			criteria.add(Restrictions.eq("username", username));
			criteria.add(Restrictions.eq("password", password));

			List<AdValUser> list = criteria.list();
			if(list.size() == 1){
				AdValUser user = list.iterator().next();
	            // Issue a token for the user
	            String token = issueToken(username);
	            user.setToken(token);
	            session.update(user);
	            session.getTransaction().commit();
				return token;
			} 
			
		} catch(Exception e){
			e.printStackTrace();
			throw e;
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		throw new NotAuthorizedException("Username and password combination not found");
    }

    private String issueToken(String username) {
		
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    	Random random = new SecureRandom();
    	String token = new BigInteger(1030, random).toString(32);
    	return token;
    }
}
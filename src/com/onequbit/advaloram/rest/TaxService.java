package com.onequbit.advaloram.rest;

import java.io.InputStream;
import java.security.Principal;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.TaxDao;
import com.onequbit.advaloram.hibernate.entity.Role;
import com.onequbit.advaloram.util.SystemUtils;

@Secured({Role.ADMINISTRATOR})
@Path("tax")
public class TaxService {

	final static Logger logger = Logger.getLogger(TaxService.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllTaxs(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		JSONObject result;
		try {
			result = new JSONObject();
			result = TaxDao.getAllTaxes();
		} catch (Exception e) {
			result = new JSONObject();
			result.put(Application.RESULT, Application.ERROR);
			result.put(Application.ERROR_MESSAGE, e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.toString()).build();
		}
		
		return Response.status(Response.Status.OK).entity(result.toString()).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response createTax(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @Context SecurityContext securityContext){
		
		JSONObject inputStreamArray = SystemUtils.convertInputStreamToJSON(is);
		logger.info("\n\n\n\nReceived Request to create tax. Incoming JSON : " +inputStreamArray);		
		
		JSONObject result;
		try {
			Principal principal = securityContext.getUserPrincipal();
			Long userId = Long.valueOf(principal.getName());
			
			result = new JSONObject();
			result = TaxDao.createOrUpdateTax((long) -1, inputStreamArray, userId);
		} catch (Exception e) {
			result = new JSONObject();
			result.put(Application.RESULT, Application.ERROR);
			result.put(Application.ERROR_MESSAGE, e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.toString()).build();
		}
		
		return Response.status(Response.Status.OK).entity(result.toString()).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response updateTax(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("id") Long id){
		
		JSONObject inputStreamArray = SystemUtils.convertInputStreamToJSON(is);
		logger.info("\n\n\n\nReceived Request to update tax. Incoming JSON : " +inputStreamArray);		
		
		JSONObject result;
		try {
			result = new JSONObject();
			result = TaxDao.createOrUpdateTax(id, inputStreamArray, (long) -1);	//not saving. So no userId required
		} catch (Exception e) {
			result = new JSONObject();
			result.put(Application.RESULT, Application.ERROR);
			result.put(Application.ERROR_MESSAGE, e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.toString()).build();
		}
		
		return Response.status(Response.Status.OK).entity(result.toString()).build();
	}
}

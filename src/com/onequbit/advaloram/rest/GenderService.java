package com.onequbit.advaloram.rest;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.GenderDao;
import com.onequbit.advaloram.util.SystemUtils;

@Path("gender")
public class GenderService {

	final static Logger logger = Logger.getLogger(GenderService.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllGenders(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		JSONObject result;
		try {
			result = new JSONObject();
			result = GenderDao.getAllGendersJson();
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
	public static Response createGender(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		
		JSONObject inputStreamArray = SystemUtils.convertInputStreamToJSON(is);
		logger.info("\n\n\n\nReceived Request to create gender. Incoming JSON : " +inputStreamArray);		
		
		JSONObject result;
		try {
			result = new JSONObject();
			//result = GenderDao.createGender(inputStreamArray);
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

package com.onequbit.advaloram.rest;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.AbstractEntityDao;
import com.onequbit.advaloram.hibernate.entity.Role;
import com.onequbit.advaloram.util.SystemUtils;
@Secured({Role.ADMINISTRATOR})
@Path("common")
public class CommonService {

	final static Logger logger = Logger.getLogger(CommonService.class);
	
	@GET
	@Path("/{entityClass}")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("entityClass") String entityClass){
		JSONObject result;
		try {
			result = new JSONObject();
			result = AbstractEntityDao.getJsonForAll(entityClass);
		} catch (Exception e) {
			result = new JSONObject();
			result.put(Application.RESULT, Application.ERROR);
			result.put(Application.ERROR_MESSAGE, e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.toString()).build();
		}
		
		return Response.status(Response.Status.OK).entity(result.toString()).build();
	}
	
	@GET
	@Path("/{entityClass}/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getUsingId(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("entityClass") String entityClass,  @PathParam("id") Long id){
		JSONObject result;
		try {
			result = new JSONObject();
			result = AbstractEntityDao.getEntityUsingId(entityClass, id);
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
	@Path("/{entityClass}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response createCommon(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("entityClass") String entityClass){
		
		JSONObject inputStreamArray = SystemUtils.convertInputStreamToJSON(is);
		logger.info("\n\n\n\nReceived Request to create common. Incoming JSON : " +inputStreamArray);		
		
		JSONObject result;
		try {
			result = new JSONObject();
			//result = CommonDao.createEntity(entityClass, inputStreamArray);
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

package com.onequbit.advaloram.rest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import main.RunIt;

@Path("/product")
public class ProductService {

	@Path("/test")
	@GET
	public static Response test(@Context HttpServletRequest request, @Context ServletContext context){
		RunIt.main();
		return Response.status(Response.Status.OK).entity("fn").build();
	}
}

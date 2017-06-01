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
import com.onequbit.advaloram.hibernate.dao.InvoiceDao;
import com.onequbit.advaloram.hibernate.entity.Role;
import com.onequbit.advaloram.util.SystemUtils;

@Secured({Role.ADMINISTRATOR})
@Path("invoice")
public class InvoiceService {

	final static Logger logger = Logger.getLogger(InvoiceService.class);
	
	@GET
	@Path("/packingListId/invoiceStatus/{packingListInternalId}/")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response invoiceStatusForPackingList(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("packingListInternalId") Long packingListInternalId){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getInvoiceStatusForPackingList(packingListInternalId);
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
	@Path("/packingListId/{packingListInternalId}")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getInvoiceLinkedToPackingListId(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("packingListInternalId") Long packingListInternalId){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getInvoiceLinkedToPackingList(packingListInternalId);
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
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllInvoices(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getAllInvoices();
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
	@Path("/monthlySales")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getMonthlySales(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getMonthlySales();
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
	@Path("/open/count")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getOpenInvoicesCount(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getOpenInvoicesCount();
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
	@Path("/{invoiceId}")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getInvoiceUsingId(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("invoiceId") Long invoiceId){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getInvoiceJson(invoiceId);
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
	@Path("/internalId/{internalId}")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getInvoiceUsingInternalId(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("internalId") Long internalId){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getInvoiceJsonUsingInternalId(internalId);
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
	@Path("/{invoiceId}/edit")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response editInvoice(@Context HttpServletRequest request, 
			@Context ServletContext servletContext, @PathParam("invoiceId") Long invoiceId, @Context SecurityContext securityContext){
		
		JSONObject result;
		try {
			Principal principal = securityContext.getUserPrincipal();
			Long userId = Long.valueOf(principal.getName());
			
			result = new JSONObject();
			result = InvoiceDao.createNewRevisionOfInvoice(invoiceId, userId);
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
	@Path("/{invoiceId}/confirm")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response confirmInvoice(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("invoiceId") Long invoiceId){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.confirmInvoice(invoiceId);
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
	@Path("/idlist")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllInvoiceIds(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getAllInvoicesId();
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
	@Path("/customer/{customerId}/idlist")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllInvoiceIdsForClient(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("customerId") Long customerId){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getAllInvoicesIdForCustomer(customerId);
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
	@Path("/nextid")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getNextInvoiceId(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext){
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.getNextInvoiceId();
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
	public static Response createInvoice(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @Context SecurityContext securityContext){
		
		JSONObject inputStreamArray = SystemUtils.convertInputStreamToJSON(is);
		logger.info("\n\n\n\nReceived Request to create invoice. Incoming JSON : " +inputStreamArray);		
		
		JSONObject result;
		try {
			Principal principal = securityContext.getUserPrincipal();
			Long userId = Long.valueOf(principal.getName());
			
			result = new JSONObject();
			result = InvoiceDao.createOrUpdateInvoice((long) -1, inputStreamArray, userId);
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
	public static Response updateInvoice(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("id") Long id){
		
		JSONObject inputStreamArray = SystemUtils.convertInputStreamToJSON(is);
		logger.info("\n\n\n\nReceived Request to update invoice. Incoming JSON : " +inputStreamArray);		
		
		JSONObject result;
		try {
			result = new JSONObject();
			result = InvoiceDao.createOrUpdateInvoice(id, inputStreamArray, (long) -1);	//not saving. So no userId required
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
	@Path("/{id}/lrNo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response addLrNumberToInvoice(@Context HttpServletRequest request, 
			InputStream is, @Context ServletContext servletContext, @PathParam("id") Long id){
		
		JSONObject inputStreamArray = SystemUtils.convertInputStreamToJSON(is);
		logger.info("\n\n\n\nReceived Request to add LR Number to invoice. Incoming JSON : " +inputStreamArray);		
		
		JSONObject result;
		try {
			result = new JSONObject();
			if(inputStreamArray.has("lorryReceiptNumber")){
				if(!inputStreamArray.getString("lorryReceiptNumber").trim().isEmpty()){
					String lrNo = inputStreamArray.getString("lorryReceiptNumber").trim();
					result = InvoiceDao.addLrNumberToInvoice(id, lrNo);
					return Response.status(Response.Status.OK).entity(result.toString()).build();
				}
			} 
				
			result.put(Application.RESULT, Application.ERROR);
			result.put(Application.ERROR_MESSAGE, "Invalid Lorry Receipt Number");		
			
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

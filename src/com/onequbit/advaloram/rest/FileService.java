package com.onequbit.advaloram.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.FileDao;
import com.onequbit.advaloram.hibernate.dao.SalesOrderDao;
import com.onequbit.advaloram.hibernate.entity.AdValUser;
import com.onequbit.advaloram.hibernate.entity.Role;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
import com.onequbit.advaloram.util.ExcelUtil;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

//@Secured({Role.ADMINISTRATOR})
@Path("fileService")	//same as UPLOAD_FOLDER_NAME
public class FileService {

	final static Logger logger = Logger.getLogger(FileService.class);
	
	public static final String UPLOAD_FOLDER_NAME = "fileService";
	
    private AdValUser getUser(String token)  {
		Session session = null;
		if(!token.trim().isEmpty()){
			try {
				
				session = HibernateUtil.getSessionAnnotationFactory().openSession();
				session.beginTransaction();
				
				Criteria criteria = session.createCriteria(AdValUser.class);
				criteria.add(Restrictions.eq("token", token));

				List<AdValUser> list = criteria.list();
				if(list.size() == 1){
					AdValUser user = list.iterator().next();
					return user;
				} 
				
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				if(session!=null){
					session.close();
				}
			}
		}
		return null;
    }
	
	@GET  
    @Path("/basicSalesOrderReport/{token}")  
    public Response getBasicReport(@Context ServletContext servletContext, @PathParam("token") String token) { 
		AdValUser user = getUser(token);
		if(user!=null){
			List<SalesOrder> salesOrders = SalesOrderDao.getAllSalesOrdersList();
			Collections.sort(salesOrders);
			File fileToDownload = ExcelUtil.getSalesOrderReport(salesOrders);
	        ResponseBuilder response = Response.ok((Object) fileToDownload);  
	        response.header("Content-Disposition","attachment; filename=\"" + fileToDownload.getName() + "\"");  
	        return response.build();  
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    } 

	
	@GET  
    @Path("/{entityClass}/{id}/{fileName}")  
    public Response getFile(@Context ServletContext servletContext, 
    		@PathParam("entityClass") String entityClass,  @PathParam("id") Long id, @PathParam("fileName") String fileName) { 
		
		File uploadFolder = 
				new File(servletContext.getRealPath(UPLOAD_FOLDER_NAME) + File.separator + entityClass + File.separator + id);
		String fileLocation = uploadFolder.getAbsolutePath() + File.separator + fileName; 
		
		File fileToDownload = new File(fileLocation);
        ResponseBuilder response = Response.ok((Object) fileToDownload);  
        response.header("Content-Disposition","attachment; filename=\""+fileName+"\"");  
        return response.build();  
   
    }  

	@DELETE  
    @Path("/{entityClass}/{id}/{fileId}")  
	@Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(@Context ServletContext servletContext, @Context UriInfo uriInfo,
    		@PathParam("entityClass") String entityClass,  @PathParam("id") Long id, @PathParam("fileId") Long fileId) { 
		
		JSONObject result = new JSONObject();
		
		try {
			File uploadFolder = 
					new File(servletContext.getRealPath(UPLOAD_FOLDER_NAME) + File.separator + entityClass + File.separator + id);
			
			FileDao.deleteEntry(entityClass, id, fileId, uploadFolder);
			result.put(Application.RESULT, Application.SUCCESS);
			
		} catch(Exception e){
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
    public Response listFiles(@Context ServletContext servletContext, @Context UriInfo uriInfo,
    		@PathParam("entityClass") String entityClass,  @PathParam("id") Long id) { 
		
		JSONObject result = new JSONObject();
		
		try {
			
			URI baseUri = uriInfo.getBaseUri();
			
			File uploadFolder = 
					new File(servletContext.getRealPath(UPLOAD_FOLDER_NAME) + File.separator + entityClass + File.separator + id);
			if(!uploadFolder.exists()){
				throw new FileNotFoundException("File Not Found");
			}
			File[] files = uploadFolder.listFiles();
			JSONArray filesArray = new JSONArray();
			for(int f = 0; f < files.length; f++){
				File file = files[f];
				JSONObject fileJson = new JSONObject();
				fileJson.put("name", file.getName());
				fileJson.put("uri", baseUri + UPLOAD_FOLDER_NAME + "/" + entityClass + "/" + id + "/" + file.getName());
				filesArray.put(fileJson);
			}
			
			result.put(Application.RESULT, filesArray);
			
		} catch(Exception e){
			result = new JSONObject();
			result.put(Application.RESULT, Application.ERROR);
			result.put(Application.ERROR_MESSAGE, e.getMessage());
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.toString()).build();
		}
		
		return Response.status(Response.Status.OK).entity(result.toString()).build();
   
    } 
	
	@POST  
	@Path("/{entityClass}/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)  
	@Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(  
            @FormDataParam("file") InputStream uploadedInputStream, @Context UriInfo uriInfo,  
            @FormDataParam("file") FormDataContentDisposition fileDetail, @Context ServletContext servletContext,
            @PathParam("entityClass") String entityClass,  @PathParam("id") Long id) {  

			JSONObject result = new JSONObject();
		
			File uploadFolder = 
					new File(servletContext.getRealPath(UPLOAD_FOLDER_NAME) + File.separator + entityClass 
							+ File.separator + id);
			
			if(!uploadFolder.exists()){
				uploadFolder.mkdirs();
			}
			
            String fileLocation = uploadFolder.getAbsolutePath() + File.separator + fileDetail.getFileName();  
                    //saving file  
            try {  
                FileOutputStream out = new FileOutputStream(new File(fileLocation));  
                int read = 0;  
                byte[] bytes = new byte[1024];  
                out = new FileOutputStream(new File(fileLocation));  
                while ((read = uploadedInputStream.read(bytes)) != -1) {  
                    out.write(bytes, 0, read);  
                }  
                out.flush();  
                out.close(); 
                
                URI baseUri = uriInfo.getBaseUri();
                String fileUri = baseUri + UPLOAD_FOLDER_NAME + "/" + entityClass + "/" + id + "/" + fileDetail.getFileName();
                FileDao.makeEntry(entityClass, id, fileDetail.getFileName(), fileUri);
    			result.put(Application.RESULT, "Successfully uploaded to "+ fileLocation);
    		} catch (Exception e) {
    			result = new JSONObject();
    			result.put(Application.RESULT, Application.ERROR);
    			result.put(Application.ERROR_MESSAGE, e.getMessage());
    			e.printStackTrace();
    			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result.toString()).build();
    		}
    		
    		return Response.status(Response.Status.OK).entity(result.toString()).build();
        }  
	
	@Path("/files2")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFiles(final FormDataMultiPart multiPart) {

		List<FormDataBodyPart> bodyParts = multiPart.getFields("files");

		StringBuffer fileDetails = new StringBuffer("");

		/* Save multiple files */
		for (int i = 0; i < bodyParts.size(); i++) {
			BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
			String fileName = bodyParts.get(i).getContentDisposition().getFileName();
			saveFile(bodyPartEntity.getInputStream(), fileName);
			fileDetails.append(" File saved at /Volumes/Drive2/temp/file/" + fileName);
		}

		/* Save File 2 */

		BodyPartEntity bodyPartEntity = ((BodyPartEntity) multiPart.getField("file2").getEntity());
		String file2Name = multiPart.getField("file2").getFormDataContentDisposition().getFileName();
		saveFile(bodyPartEntity.getInputStream(), file2Name);
		fileDetails.append(" File saved at /Volumes/Drive2/temp/file/" + file2Name);

		fileDetails.append(" Tag Details : " + multiPart.getField("tags").getValue());
		System.out.println(fileDetails);

		return Response.ok(fileDetails.toString()).build();
	}

	private void saveFile(InputStream file, String name) {
		try {
			/* Change directory path */
			java.nio.file.Path path = FileSystems.getDefault().getPath("/Volumes/Drive2/temp/file/" + name);
			/* Save InputStream as file */
			Files.copy(file, path);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
}

package com.onequbit.advaloram.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.FileDao;
import com.onequbit.advaloram.hibernate.entity.Role;

@Secured({Role.ADMINISTRATOR})
@Path("upload")	//same as UPLOAD_FOLDER_NAME
public class FileService {

	final static Logger logger = Logger.getLogger(FileService.class);
	
	public static final String UPLOAD_FOLDER_NAME = "upload";
	
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
}

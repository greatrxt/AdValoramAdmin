package com.onequbit.advaloram.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onequbit.advaloram.hibernate.entity.Bank;
import com.onequbit.advaloram.hibernate.entity.Brand;
import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Gender;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.ProductCategory;
import com.onequbit.advaloram.hibernate.entity.Season;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.hibernate.entity.Transporter;
import com.onequbit.advaloram.hibernate.entity.UnitOfMeasurement;


public class HibernateUtil {


	
	public static final Integer DB_VERSION = 0;
	//Annotation based configuration
	private static SessionFactory sessionAnnotationFactory;
	
	/**
	 * Fetch all objects of particular class
	 * @param entity
	 * @return
	 */
	public static List<Object> getAll(Class entity){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(entity);			
			List<Object> list = criteria.list();
			return list;
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param entity
	 * @param entityJson
	 */
	public static void setDataFromJson(Object entity, JSONObject entityJson){
		Field[] fields = entity.getClass().getDeclaredFields();
		for(int f = 0; f < fields.length; f++){
			Field field = fields[f];
			try {
				if(entityJson.has(field.getName())){
					System.out.println("Comparing ... "+field.getType().getName() + " " + field.getName().getClass().getName());
					if(field.getType().getName().equals(entityJson.get(field.getName()).getClass().getName())){
						field.set(entity, entityJson.get(field.getName()));
					}
				}
			} catch (JSONException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public static JSONObject getJsonFromHibernateEntity(Object entity){
		JSONObject entityJson = new JSONObject();
		Field[] fields = entity.getClass().getDeclaredFields();
		loadEntityDataIntoJson(entity, fields, entityJson);
		
		Field[] superClassFields = entity.getClass().getSuperclass().getDeclaredFields();
		loadEntityDataIntoJson(entity, superClassFields, entityJson);
		
		return entityJson;
	}
	
	/**
	 * 
	 * @param entity
	 * @param fields
	 * @param entityJson
	 */
	private static void loadEntityDataIntoJson(Object entity, Field[] fields, JSONObject entityJson){
		for(int f = 0; f < fields.length; f++){
			Field field = fields[f];
			try {
				if(field.getName().equals("serialVersionUID")){
					continue;
				}
				
				if(field.get(entity) == null){
					entityJson.put(field.getName(), "No Data");
					continue;
				}
				
				if(field.getType().isAssignableFrom(String.class) || 
						field.getType().isAssignableFrom(Long.class) ||
						field.getType().isAssignableFrom(Integer.class)) {
					
						entityJson.put(field.getName(), field.get(entity));
					
				} else if(field.getType().isAssignableFrom(Set.class)){
					//Hibernate entity
					JSONArray collectionArray = new JSONArray();
					
					Set<Object> subEntityCollection = (Set) field.get(entity);
					for(Object subEntity: subEntityCollection){
						JSONObject subEntityJson = new JSONObject();
						loadEntityDataIntoJson(subEntity, subEntity.getClass().getDeclaredFields(), subEntityJson);
						collectionArray.put(subEntityJson);
					}
					entityJson.put(field.getName(), collectionArray);
				} else {
					
					JSONObject subEntityJson = new JSONObject();
					loadEntityDataIntoJson(field.get(entity), field.get(entity).getClass().getDeclaredFields(), subEntityJson);
					entityJson.put(field.getName(), subEntityJson);
					
				}
			} catch (JSONException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 
	 * @return
	 */
    private static SessionFactory buildSessionAnnotationFactory() {
    	try {
    		
            // Create the SessionFactory from hibernate.cfg.xml
        	Configuration configuration = new Configuration();
        	configuration.configure("hibernate.cfg.xml");   
        	//configuration.setProperty("hibernate.default_schema", schemaName);
        	System.out.println("Hibernate Annotation Configuration loaded");      	
        	ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        	System.out.println("Hibernate Annotation serviceRegistry created");
        	
        	configuration.addAnnotatedClass(Bank.class);
        	configuration.addAnnotatedClass(Brand.class);
        	configuration.addAnnotatedClass(Color.class);
        	configuration.addAnnotatedClass(Customer.class);
        	configuration.addAnnotatedClass(Gender.class);
        	configuration.addAnnotatedClass(Location.class);
        	configuration.addAnnotatedClass(Product.class);
        	configuration.addAnnotatedClass(ProductCategory.class);
        	configuration.addAnnotatedClass(Season.class);
        	configuration.addAnnotatedClass(Size.class);
        	//configuration.addAnnotatedClass(Style.class);
        	configuration.addAnnotatedClass(Transporter.class);
        	configuration.addAnnotatedClass(UnitOfMeasurement.class);        	
        	
        	SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        	
            return sessionFactory;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
	}

	
	public static SessionFactory getSessionAnnotationFactory() {			
		if(sessionAnnotationFactory == null) {
			sessionAnnotationFactory = buildSessionAnnotationFactory();
		}		
        return sessionAnnotationFactory;
    }
	
	public static void CloseSessionFactory() {
		getSessionAnnotationFactory().close();
    }	
}
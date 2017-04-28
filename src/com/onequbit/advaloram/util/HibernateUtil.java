package com.onequbit.advaloram.util;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.collection.internal.PersistentMap;
import org.hibernate.service.ServiceRegistry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onequbit.advaloram.hibernate.entity.AbstractAdValoramEntity;
import com.onequbit.advaloram.hibernate.entity.Bank;
import com.onequbit.advaloram.hibernate.entity.Brand;
import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.hibernate.entity.ColorCode;
import com.onequbit.advaloram.hibernate.entity.CreditNote;
import com.onequbit.advaloram.hibernate.entity.CreditNoteEntry;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.File;
import com.onequbit.advaloram.hibernate.entity.Gender;
import com.onequbit.advaloram.hibernate.entity.Invoice;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.PackingList;
import com.onequbit.advaloram.hibernate.entity.PackingListEntry;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.ProductCategory;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
import com.onequbit.advaloram.hibernate.entity.SalesOrderEntry;
import com.onequbit.advaloram.hibernate.entity.Season;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
import com.onequbit.advaloram.hibernate.entity.Tax;
import com.onequbit.advaloram.hibernate.entity.Transporter;
import com.onequbit.advaloram.hibernate.entity.UnitOfMeasurement;
import com.onequbit.advaloram.hibernate.entity.AdValUser;


public class HibernateUtil {
	
	public static final Integer DB_VERSION = 1;
	//Annotation based configuration
	private static SessionFactory sessionAnnotationFactory;
	
	/**
	 * Fetch all objects of particular class
	 * @param entity
	 * @return
	 * @throws Exception 
	 */
	public static List<Object> getAll(Class entity) throws Exception{
		Session session = null;
		try {
			if(entity.equals(SalesOrder.class)){
				throw new Exception("Sales Order should not be fetched using common API. Please use API for SalesOrder which uses 'salesOrderId' (instead of internal ID)"
						+ " to retrieve Sales Orders ");
			}
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			/* Deprecated. Hence commented out.
			 */Criteria criteria = session.createCriteria(entity);			
			List<Object> list = criteria.list();
			
			return list;
			
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		} finally {
			if(session!=null){
				session.close();
			}
		}
	}
	
	/**
	 * Fetch entity using ID
	 * @param entityType
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public static Object getUsingId(Class entityType, long id) throws Exception{
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			if(entityType.equals(SalesOrder.class)){
				throw new Exception("Sales Order should not be fetched using common API. Please use API for SalesOrder which uses 'salesOrderId' (instead of internal ID)"
						+ " to retrieve Sales Orders ");
			}
			return session.get(entityType, id);			
			
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		} finally {
			if(session!=null){
				session.close();
			}
		}
	}
	
	/**
	 * incomplete
	 * @param entity
	 * @param entityJson
	 * @throws Exception 
	 */
	public static void setDataFromJson(AbstractAdValoramEntity entity, JSONObject entityJson) throws Exception{
		Field[] fields = entity.getClass().getDeclaredFields();
		for(int f = 0; f < fields.length; f++){
			Field field = fields[f];
			try {
				if(field.getName().equals("serialVersionUID") 
						|| field.getName().equals("id")
						|| field.getName().equals("stockKeepingUnits") 
						|| field.getName().equals("associatedFiles")){
					continue;
				}
				
				if(entity instanceof SalesOrder && (
						field.getName().equals("salesOrderId")
						|| field.getName().equals("linkedCustomer")
						//|| field.getName().equals("clientNameOnSalesOrderDate")
						|| field.getName().equals("referredByEmployee")
						|| field.getName().equals("entry")
						|| field.getName().equals("status")
						|| field.getName().equals("salesOrderRevisionNumber")
						|| field.getName().equals("salesOrderDate"))){
					continue;
				}
				
				if(entity instanceof PackingList && (field.getName().equals("packingListId")
													|| field.getName().equals("linkedCustomer")
													|| field.getName().equals("packingListRevisionNumber")
													|| field.getName().equals("packingListDate")
													|| field.getName().equals("linkedSalesOrder")
													|| field.getName().equals("entry")
													|| field.getName().equals("status"))){
					continue;
				}
				
				
				if(entity instanceof Invoice && (field.getName().equals("linkedSalesOrder")
													|| field.getName().equals("linkedPackingList")
													|| field.getName().equals("invoiceId")
													|| field.getName().equals("salesEmployee")
													|| field.getName().equals("refereePartnerName")
													|| field.getName().equals("invoiceRevisionNumber")
													|| field.getName().equals("status")
													|| field.getName().equals("invoiceDate"))){
					continue;
				}
				
				if(entity instanceof CreditNote && (field.getName().equals("linkedInvoice")
						|| field.getName().equals("creditNoteId")
						|| field.getName().equals("creditNoteRevisionNumber")
						|| field.getName().equals("status")
						|| field.getName().equals("creditNoteDate")
						|| field.getName().equals("entry"))){
					continue;
				}
				
				if(entityJson.has(field.getName())){
					System.out.println("Storing "+ "Field " + field.getName() + " of class " + field.getType().getCanonicalName());
					Object entityObject = entityJson.get(field.getName());
					//if(field.getType().getName().equals(entityJson.get(field.getName()).getClass().getName())){
					if(field.getType().isAssignableFrom(String.class)) {
						field.set(entity, String.valueOf(entityObject));						
					} else if (field.getType().isAssignableFrom(long.class) 
							|| field.getType().isAssignableFrom(Long.class)){
						try {
							field.set(entity, Long.parseLong(String.valueOf(entityObject)));
						} catch(NumberFormatException n){
							field.set(entity, 0);
						}
						
					} else if (field.getType().isAssignableFrom(int.class) 
							|| field.getType().isAssignableFrom(Integer.class)){
						try {
							field.set(entity, Integer.parseInt(String.valueOf(entityObject)));
						} catch(NumberFormatException n){
							field.set(entity, 0);
						}
					} else if (field.getType().isAssignableFrom(float.class) 
							|| field.getType().isAssignableFrom(Float.class)){
						try {
							field.set(entity, Float.parseFloat(String.valueOf(entityObject)));
						} catch(NumberFormatException n){
							field.set(entity, 0);
						}
					} else if (field.getType().isAssignableFrom(double.class) 
							|| field.getType().isAssignableFrom(Double.class)){
						try {
							field.set(entity, Double.parseDouble(String.valueOf(entityObject)));
						} catch(NumberFormatException n){
							field.set(entity, 0);
						}
					} else if (field.getType().isPrimitive()){
						field.set(entity, entityObject);	
					} else if (field.getType().isAssignableFrom(Map.class)){
						
					} else if (field.getType().isAssignableFrom(Set.class)){
						
					} else {
						//throw new Exception("Field " + field.getName() + " of class " + field.getType().getCanonicalName() + " could not be stored");
						System.out.println("Field " + field.getName() + " of class " + field.getType().getCanonicalName() + " could not be stored");
					}
				} else {
					throw new Exception("Field " + field.getName() + " not found");
					//System.out.println("Field " + field.getName() + " not found");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		//entity.setVersion(DB_VERSION);
		entity.setLastUpdate(SystemUtils.getFormattedDate());
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public static JSONObject getJsonFromHibernateEntity(Object entity) {
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
			//System.out.println("loadEntityDataIntoJson : "+ entity.getClass().getSimpleName() + " " +field.getName() +" - f = " + f +"/"+fields.length+ "\n JSON = "+entityJson);
			try {
				if(field.getName().equals("serialVersionUID")){
					continue;
				}
				
				if(field.get(entity) == null){
					if(!entityJson.has(field.getName())){
						entityJson.put(field.getName(), "No Data");
					}
					continue;
				}
				
				//circular reference
				if((entity instanceof Employee && field.getType().isAssignableFrom(Employee.class))){
						entityJson.put(field.getName(), ((Employee)field.get(entity)).getId());
					continue;
				}
				
				//avoid bi directional relationships
				if((entity instanceof Product && field.getType().isAssignableFrom(StockKeepingUnit.class)) ||
						(entity instanceof Customer && field.getType().isAssignableFrom(Customer.class))){
					continue;
				}
				if(entity instanceof StockKeepingUnit && field.getType().isAssignableFrom(Product.class)){
					entityJson.put(field.getName(), ((Product)field.get(entity)).getId());
					entityJson.put("mrp", ((Product)field.get(entity)).getMrp());
					continue;
				}
				
				if(entity instanceof PackingList && field.getType().isAssignableFrom(SalesOrder.class)){
					continue;
				}
				
				if(field.getType().isAssignableFrom(SalesOrder.Status.class)
						||  field.getType().isAssignableFrom(PackingList.Status.class)
						|| field.getType().isAssignableFrom(Invoice.Status.class)
						|| field.getType().isAssignableFrom(CreditNote.Status.class)){
					entityJson.put(field.getName(), field.get(entity));
					continue;
				}
									
				if(field.getType().isAssignableFrom(String.class) || 
						field.getType().isAssignableFrom(Long.class) ||
						field.getType().isAssignableFrom(Date.class) ||
						field.getType().isAssignableFrom(Integer.class) ||
						field.getType().isPrimitive()) {
					
						entityJson.put(field.getName(), field.get(entity));
					
				} else if(field.getType().isAssignableFrom(Set.class)){
					//Hibernate entity
					JSONArray collectionArray = new JSONArray();
					
					Set<Object> subEntityCollection = (Set) field.get(entity);
					int i = 0;
					for(Object subEntity: subEntityCollection){
						JSONObject subEntityJson = new JSONObject();
						//System.out.println("INSIDE FOR "  + (i++)+" "+subEntityCollection.size() + " F = "+f+"/"+fields.length);
						loadEntityDataIntoJson(subEntity, subEntity.getClass().getDeclaredFields(), subEntityJson);
						collectionArray.put(subEntityJson);
					}
					
					entityJson.put(field.getName(), collectionArray);
					
				} else if(field.getType().isAssignableFrom(HashMap.class)){
					
					JSONArray collectionArray = new JSONArray();
					
					PersistentMap entityMap = (PersistentMap) field.get(entity);
					Iterator<String> iterator = entityMap.keySet().iterator();
					
					while(iterator.hasNext()){
						JSONObject subEntityJson = new JSONObject();
						//String key = iterator.next();
						Object objectKey = iterator.next();
						Object subEntity = entityMap.get(objectKey);
						
						JSONObject subSubEntity = new JSONObject();
						loadEntityDataIntoJson(subEntity, subEntity.getClass().getDeclaredFields(), subSubEntity);
						String key = "";
						if(objectKey instanceof ColorCode){
							key = ((ColorCode) objectKey).getColorCode();
						} else if (key instanceof String){
							key = (String) objectKey;
						}
						subEntityJson.put(key, subSubEntity);
						
						collectionArray.put(subEntityJson);
					}
					
					entityJson.put(field.getName(), collectionArray);
					
				} else if(field.getType().getSuperclass().isAssignableFrom(AbstractAdValoramEntity.class)){
					
					JSONObject subEntityJson = new JSONObject();
					loadEntityDataIntoJson(field.get(entity), field.get(entity).getClass().getDeclaredFields(), subEntityJson);
					entityJson.put(field.getName(), subEntityJson);
					
				} else {
					System.out.println("Error for field - " + field.getName() + " Class Name - " + field.get(entity).getClass());
				}
			} catch (Exception e) {
				System.out.println("Error for field - " + field.getName());
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 
	 * @return
	 */
    public static SessionFactory buildSessionAnnotationFactory() {
    	try {
    		
            // Create the SessionFactory from hibernate.cfg.xml
        	Configuration configuration = new Configuration();
        	configuration.configure("hibernate.cfg.xml");   
        	System.out.println("Hibernate Annotation Configuration loaded");      	
        	ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        	System.out.println("Hibernate Annotation serviceRegistry created");
        	
        	configuration.addAnnotatedClass(AdValUser.class);      
        	configuration.addAnnotatedClass(Bank.class);
        	configuration.addAnnotatedClass(Brand.class);
        	configuration.addAnnotatedClass(CreditNote.class);
        	configuration.addAnnotatedClass(CreditNoteEntry.class);
        	configuration.addAnnotatedClass(Color.class);
        	configuration.addAnnotatedClass(ColorCode.class);
        	configuration.addAnnotatedClass(Customer.class);
        	configuration.addAnnotatedClass(Employee.class);
        	configuration.addAnnotatedClass(File.class);
        	configuration.addAnnotatedClass(Gender.class);
        	configuration.addAnnotatedClass(Invoice.class);
        	configuration.addAnnotatedClass(Location.class);
        	configuration.addAnnotatedClass(Product.class);
        	configuration.addAnnotatedClass(ProductCategory.class);
        	configuration.addAnnotatedClass(Season.class);
        	configuration.addAnnotatedClass(SalesOrder.class);
        	configuration.addAnnotatedClass(SalesOrderEntry.class);
        	configuration.addAnnotatedClass(PackingList.class);
        	configuration.addAnnotatedClass(PackingListEntry.class);
        	configuration.addAnnotatedClass(Size.class);
        	configuration.addAnnotatedClass(StockKeepingUnit.class);
        	configuration.addAnnotatedClass(Tax.class);
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

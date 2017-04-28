package com.onequbit.advaloram.hibernate.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.Transporter;
import com.onequbit.advaloram.hibernate.entity.AdValUser;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class CustomerDao {
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	public static Customer getCustomer(Customer customer){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Customer.class);
			criteria.add(Restrictions.eq("companyName", customer.getCompanyName()).ignoreCase());

			List<Customer> list = criteria.list();
			if(list.size() > 0){
				return list.iterator().next();
			} 
			
		} catch(Exception e){
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
	 * @return
	 */
	public static JSONObject getAllCustomersIdAndName(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {		
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Customer.class);
			criteria.setProjection(Projections.projectionList().add(Projections.property("id")).add(Projections.property("companyName")));
			List<Customer> styleCodes = criteria.list();		

			if(styleCodes.size() == 0){
				//No Customer found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Customers Found");
			} else {
				Iterator iterator = styleCodes.iterator();
				while(iterator.hasNext()){
					JSONObject customer = new JSONObject();
					Object[] result = (Object[]) iterator.next();
					
					customer.put("id", result[0]);
					customer.put("companyName", result[1]);
					resultArray.put(customer);
				}
				
				resultsJson.put(Application.RESULT, resultArray);
			}
			
		} catch(Exception e){
			e.printStackTrace();
			resultsJson = SystemUtils.generateErrorMessage(e);
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return resultsJson;
	}
	
	/**
	 * Get all Customers
	 * @return
	 */
	public static JSONObject getAllCustomers(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Customer> customersList = (List<Customer>)(Object)HibernateUtil.getAll(Customer.class);

			if(customersList.size() == 0){
				//No Customer found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Customer found");
			} else {
				Iterator<Customer> iterator = customersList.iterator();
				while(iterator.hasNext()){
					Customer customer = iterator.next();
					JSONObject customerJson = HibernateUtil.getJsonFromHibernateEntity(customer);
					resultArray.put(customerJson);
				}
				resultsJson.put(Application.RESULT, resultArray);
			}
			
		} catch(Exception e){
			e.printStackTrace();
			resultsJson = SystemUtils.generateErrorMessage(e);
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return resultsJson;
	}
	
	/**
	 * Get all Customers of type
	 * @return
	 */
	public static JSONObject getAllCustomersOfType(String type){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Customer> customersList;
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Customer.class);		
			criteria.add(Restrictions.eq("customerType", type).ignoreCase());
			customersList = criteria.list();


			if(customersList.size() == 0){
				//No Customer found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Customer found");
			} else {
				Iterator<Customer> iterator = customersList.iterator();
				while(iterator.hasNext()){
					Customer customer = iterator.next();
					JSONObject customerJson = HibernateUtil.getJsonFromHibernateEntity(customer);
					resultArray.put(customerJson);
				}
				resultsJson.put(Application.RESULT, resultArray);
			}
			
		} catch(Exception e){
			e.printStackTrace();
			resultsJson = SystemUtils.generateErrorMessage(e);
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return resultsJson;
	}
	
	public static final class Tag {
		public static final String LINKED_TRANSPORTER = "linkedTransporter";
	}
	
	/**
	 * 
	 * @param customerJson
	 * @return
	 */
	public static JSONObject createOrUpdateCustomer(Long id, JSONObject customerJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Customer customer = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			if(id < 0){
				customer = new Customer();
			} else {
				customer = session.get(Customer.class, id);
			}
			
			HibernateUtil.setDataFromJson(customer, customerJson);
			
			if(getCustomer(customer) != null && id < 0){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "customer " + customerJson.toString() + " already exists");
			} else {
			
				
				session.beginTransaction();
				customer.setCity(session.load(Location.class, Long.parseLong(customerJson.getString("city"))));
				if(customerJson.has(Tag.LINKED_TRANSPORTER)){
					customer.setLinkedTransporter(session.load(Transporter.class, Long.parseLong(String.valueOf((customerJson.get(Tag.LINKED_TRANSPORTER))))));
				}
				
							
				if(id < 0){
					customer.setRecordCreationTime(SystemUtils.getFormattedDate());		
					customer.setCreatedBy(session.get(AdValUser.class, userId));
					session.save(customer);					
				} else {
					session.update(customer);
				}
				session.getTransaction().commit();						
				result.put(Application.RESULT, Application.SUCCESS);				
			}		
		} catch(Exception e){
			e.printStackTrace();
			result = SystemUtils.generateErrorMessage(e);
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return result;
	}
}

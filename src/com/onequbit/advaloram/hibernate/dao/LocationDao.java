package com.onequbit.advaloram.hibernate.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.AdValUser;
import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class LocationDao {
	
	
	/**
	 * Fetch user with particular location from DB
	 * @param username
	 * @return
	 */
	public static Location getLocation(Location location){
	
		JSONObject result = new JSONObject();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Location.class);
			if(!location.getCityName().trim().isEmpty()) criteria.add(Restrictions.eq("cityName", location.getCityName()).ignoreCase());
			if(!location.getDistrict().trim().isEmpty()) criteria.add(Restrictions.eq("district", location.getDistrict()).ignoreCase());
			if(!location.getTaluka().trim().isEmpty()) criteria.add(Restrictions.eq("taluka", location.getTaluka()).ignoreCase());
			if(!location.getState().trim().isEmpty()) criteria.add(Restrictions.eq("state", location.getState()).ignoreCase());
			if(!location.getCountry().trim().isEmpty()) criteria.add(Restrictions.eq("country", location.getCountry()).ignoreCase());
			
			List<Location> list = criteria.list();
			if(list.size() > 0){
				return list.iterator().next();
			} 
			
		} catch(Exception e){
			e.printStackTrace();
			result = SystemUtils.generateErrorMessage(e);
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return null;
	}
	
	/**
	 * Get all locations
	 * @return
	 */
	public static JSONObject getAllLocations(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Location> locationList = (List<Location>)(Object)HibernateUtil.getAll(Location.class);

			if(locationList.size() == 0){
				//No location found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No location found");
			} else {
				Iterator<Location> iterator = locationList.iterator();
				while(iterator.hasNext()){
					Location location = iterator.next();
					JSONObject locationJson = HibernateUtil.getJsonFromHibernateEntity(location);
					resultArray.put(locationJson);
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
	 * 
	 * @param locationJson
	 * @return
	 */
	public static JSONObject createOrUpdateLocation(Long id, JSONObject locationJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Location location = null;
		
		try {		
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction(); 
			if(id > 0){
				location = session.get(Location.class, id);
			} else {
				location = new Location();
			}
			
			HibernateUtil.setDataFromJson(location, locationJson);
			
			if(getLocation(location) != null && id < 0){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "Location " + locationJson.toString() + " already exists");
			} else {
				if(id > 0){
					session.update(location);
				} else {
					location.setRecordCreationTime(SystemUtils.getFormattedDate());	
					location.setCreatedBy(session.get(AdValUser.class, userId));
					session.save(location);
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

	/*
	 * Delete location. Only administrator allowed
	 */
	public static JSONObject deleteLocation(Long id){
		
		JSONObject result = new JSONObject();
		Session session = null;				
		try {		
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction(); 
			session.delete(session.load(Location.class, id));
			session.getTransaction().commit();
			result.put(Application.RESULT, Application.SUCCESS);	
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

package com.onequbit.advaloram.hibernate.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.Transporter;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class TransporterDao {
	
	/**
	 * 
	 * @param transporter
	 * @return
	 */
	public static Transporter getTransporter(Transporter transporter){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Transporter.class);
			criteria.add(Restrictions.eq("companyName", transporter.getCompanyName()).ignoreCase());

			List<Transporter> list = criteria.list();
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
	 * Get all Transporters
	 * @return
	 */
	public static JSONObject getAllTransporters(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Transporter> transportersList = (List<Transporter>)(Object)HibernateUtil.getAll(Transporter.class);

			if(transportersList.size() == 0){
				//No Transporter found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Transporter found");
			} else {
				Iterator<Transporter> iterator = transportersList.iterator();
				while(iterator.hasNext()){
					Transporter transporter = iterator.next();
					JSONObject transporterJson = HibernateUtil.getJsonFromHibernateEntity(transporter);
					resultArray.put(transporterJson);
				}
				resultsJson.put(Application.RESULT, resultArray);
			}
			
		} catch(Exception e){
			e.printStackTrace();
			resultsJson = SystemUtils.generateErrorMessage(e.getMessage());
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return resultsJson;
	}
	
	
	/**
	 * 
	 * @param transporterJson
	 * @return
	 */
	public static JSONObject createTransporter(JSONObject transporterJson){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		
		try {			
			Transporter transporter = new Transporter();
			HibernateUtil.setDataFromJson(transporter, transporterJson);
			
			if(getTransporter(transporter) != null){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "transporter " + transporterJson.toString() + " already exists");
			} else {
			
				session = HibernateUtil.getSessionAnnotationFactory().openSession();
				session.beginTransaction();
				transporter.setCity(session.load(Location.class, Long.parseLong(transporterJson.getString("city"))));
				transporter.setRecordCreationTime(SystemUtils.getFormattedDate());					
				session.save(transporter);					
				session.getTransaction().commit();						
				result.put(Application.RESULT, Application.SUCCESS);				
			}		
		} catch(Exception e){
			e.printStackTrace();
			result = SystemUtils.generateErrorMessage(e.getMessage());
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return result;
	}
}

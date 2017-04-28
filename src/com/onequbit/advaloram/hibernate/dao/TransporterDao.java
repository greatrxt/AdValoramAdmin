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
import com.onequbit.advaloram.hibernate.entity.AdValUser;
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
	 * @param transporterJson
	 * @return
	 */
	public static JSONObject createOrUpdateTransporter(Long id, JSONObject transporterJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Transporter transporter = null;
		
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			
			if(id < 0){
				transporter = new Transporter();
			} else {
				transporter = session.get(Transporter.class, id);
			}
			
			HibernateUtil.setDataFromJson(transporter, transporterJson);
			
			if(getTransporter(transporter) != null && id < 0){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "transporter " + transporterJson.toString() + " already exists");
			} else {
				session.beginTransaction();
				transporter.setCity(session.load(Location.class, Long.parseLong(transporterJson.getString("city"))));
								
				if(id < 0){
					transporter.setRecordCreationTime(SystemUtils.getFormattedDate());	
					transporter.setCreatedBy(session.get(AdValUser.class, userId));
					session.save(transporter);
				} else {
					session.update(transporter);
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

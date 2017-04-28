package com.onequbit.advaloram.hibernate.dao;

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
import com.onequbit.advaloram.hibernate.entity.Tax;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class TaxDao {
	
	/**
	 * 
	 * @param tax
	 * @return
	 */
	public static Tax getTax(Tax tax){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Tax.class);
			criteria.add(Restrictions.eq("taxName", tax.getTaxName()).ignoreCase());

			List<Tax> list = criteria.list();
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
	 * Get all Taxes
	 * @return
	 */
	public static JSONObject getAllTaxes(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Tax> taxesList = (List<Tax>)(Object)HibernateUtil.getAll(Tax.class);

			if(taxesList.size() == 0){
				//No Tax found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Tax found");
			} else {
				Iterator<Tax> iterator = taxesList.iterator();
				while(iterator.hasNext()){
					Tax tax = iterator.next();
					JSONObject taxJson = HibernateUtil.getJsonFromHibernateEntity(tax);
					resultArray.put(taxJson);
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
	 * @param taxJson
	 * @return
	 */
	public static JSONObject createOrUpdateTax(Long id, JSONObject taxJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Tax tax = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			if(id > 0){
				tax = session.get(Tax.class, id); 
			} else {
				tax = new Tax();
			}			
			
			HibernateUtil.setDataFromJson(tax, taxJson);			
			
			if(getTax(tax) != null && id < 0){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "tax " + taxJson.toString() + " already exists");
			} else {			
				if(id > 0){
					session.update(tax);
				} else {
					tax.setRecordCreationTime(SystemUtils.getFormattedDate());
					tax.setCreatedBy(session.get(AdValUser.class, userId));
					session.save(tax);					
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

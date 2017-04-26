package com.onequbit.advaloram.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.Gender;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class GenderDao {

	public static Gender getGender(String genderCode){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Gender.class);
			criteria.add(Restrictions.eq("genderCode", genderCode));
			
			List<Gender> list = criteria.list();
			if(list.size() > 0){
				return list.get(0);
			}
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
	 * Return all available genders
	 * @return
	 */
	public static List<Gender> getAllGenders(){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Gender.class);			
			List<Gender> list = criteria.list();
			if(list.size() > 0){
				return list;
			}
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
	 * Get all Genders
	 * @return
	 */
	public static JSONObject getAllGendersJson(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Gender> gendersList = (List<Gender>)(Object)HibernateUtil.getAll(Gender.class);

			if(gendersList.size() == 0){
				//No Gender found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Gender found");
			} else {
				Iterator<Gender> iterator = gendersList.iterator();
				while(iterator.hasNext()){
					Gender gender = iterator.next();
					JSONObject genderJson = HibernateUtil.getJsonFromHibernateEntity(gender);
					resultArray.put(genderJson);
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
}

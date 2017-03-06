package com.onequbit.advaloram.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class SizeDao {

	public static Size getSize(String sizeCode){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Size.class);
			criteria.add(Restrictions.eq("sizeCode", sizeCode));
			
			List<Size> list = criteria.list();
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
	 * Return all available sizes
	 * @return
	 */
	public static List<Size> getAllSizes(){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Size.class);			
			List<Size> list = criteria.list();
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
	 * Get all Sizes
	 * @return
	 */
	public static JSONObject getAllSizesJson(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Size> sizesList = (List<Size>)(Object)HibernateUtil.getAll(Size.class);

			if(sizesList.size() == 0){
				//No Size found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Size found");
			} else {
				Iterator<Size> iterator = sizesList.iterator();
				while(iterator.hasNext()){
					Size size = iterator.next();
					JSONObject sizeJson = HibernateUtil.getJsonFromHibernateEntity(size);
					resultArray.put(sizeJson);
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
}

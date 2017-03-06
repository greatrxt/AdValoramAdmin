package com.onequbit.advaloram.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class ColorDao {

	public static Color getColor(String colorCode){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Color.class);
			criteria.add(Restrictions.eq("colorCode", colorCode));
			
			List<Color> list = criteria.list();
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
	 * Return all available colors
	 * @return
	 */
	public static List<Color> getAllColors(){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Color.class);			
			List<Color> list = criteria.list();
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
	 * Get all Colors
	 * @return
	 */
	public static JSONObject getAllColorsJson(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Color> colorsList = (List<Color>)(Object)HibernateUtil.getAll(Color.class);

			if(colorsList.size() == 0){
				//No Color found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Color found");
			} else {
				Iterator<Color> iterator = colorsList.iterator();
				while(iterator.hasNext()){
					Color color = iterator.next();
					JSONObject colorJson = HibernateUtil.getJsonFromHibernateEntity(color);
					resultArray.put(colorJson);
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

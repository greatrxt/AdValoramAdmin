package com.onequbit.advaloram.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.ColorCode;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class ColorCodeDao {

	public static ColorCode getColorCodeUsingColorCodeName(String colorCode){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(ColorCode.class);
			criteria.add(Restrictions.eq("colorCode", colorCode));
			
			List<ColorCode> list = criteria.list();
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
	 * Return all available colorCodes
	 * @return
	 */
	public static List<ColorCode> getAllColorCodes(){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(ColorCode.class);			
			List<ColorCode> list = criteria.list();
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
	 * Get all ColorCodes
	 * @return
	 */
	public static JSONObject getAllColorCodesJson(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<ColorCode> colorCodesList = (List<ColorCode>)(Object)HibernateUtil.getAll(ColorCode.class);

			if(colorCodesList.size() == 0){
				//No ColorCode found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No ColorCode found");
			} else {
				Iterator<ColorCode> iterator = colorCodesList.iterator();
				while(iterator.hasNext()){
					ColorCode colorCode = iterator.next();
					JSONObject colorCodeJson = HibernateUtil.getJsonFromHibernateEntity(colorCode);
					resultArray.put(colorCodeJson);
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

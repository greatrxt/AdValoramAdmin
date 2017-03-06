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
import com.onequbit.advaloram.hibernate.entity.Bank;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class BankDao {
	
	/**
	 * 
	 * @param bank
	 * @return
	 */
	public static Bank getBank(Bank bank){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Bank.class);
			criteria.add(Restrictions.eq("bankName", bank.getBankName()).ignoreCase());

			List<Bank> list = criteria.list();
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
	 * Get all Banks
	 * @return
	 */
	public static JSONObject getAllBanks(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Bank> banksList = (List<Bank>)(Object)HibernateUtil.getAll(Bank.class);

			if(banksList.size() == 0){
				//No Bank found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Bank found");
			} else {
				Iterator<Bank> iterator = banksList.iterator();
				while(iterator.hasNext()){
					Bank bank = iterator.next();
					JSONObject bankJson = HibernateUtil.getJsonFromHibernateEntity(bank);
					resultArray.put(bankJson);
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
	 * @param bankJson
	 * @return
	 */
	public static JSONObject createBank(JSONObject bankJson){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		
		try {			
			Bank bank = new Bank();
			HibernateUtil.setDataFromJson(bank, bankJson);
			
			if(getBank(bank) != null){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "bank " + bankJson.toString() + " already exists");
			} else {
			
				session = HibernateUtil.getSessionAnnotationFactory().openSession();
				session.beginTransaction();
				bank.setCity(session.load(Location.class, Long.parseLong(bankJson.getString("city"))));
				bank.setRecordCreationTime(SystemUtils.getFormattedDate());					
				session.save(bank);					
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

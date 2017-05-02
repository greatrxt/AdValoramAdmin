package com.onequbit.advaloram.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.AdValUser;
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
	 * @param bankJson
	 * @return
	 */
	public static JSONObject createOrUpdateBank(Long id, JSONObject bankJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Bank bank = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			if(id > 0){
				bank = session.get(Bank.class, id); 
			} else {
				bank = new Bank();
			}			
			
			HibernateUtil.setDataFromJson(bank, bankJson);			
			
			if(getBank(bank) != null && id < 0){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "bank " + bankJson.toString() + " already exists");
			} else {
				bank.setCity(session.load(Location.class, Long.parseLong(bankJson.getString("city"))));				
				if(id > 0){
					session.update(bank);
				} else {
					bank.setRecordCreationTime(SystemUtils.getFormattedDate());	
					bank.setCreatedBy(session.get(AdValUser.class, userId));
					session.save(bank);					
				}
				session.getTransaction().commit();						
				result.put(Application.RESULT, Application.SUCCESS);
				result.put("objectId", bank.getId());
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

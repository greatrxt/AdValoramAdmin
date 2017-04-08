package com.onequbit.advaloram.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.ProductDao.Tag;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class SalesOrderDao {
	
	/**
	 * 
	 * @param salesOrder
	 * @return
	 */
	public static SalesOrder getSalesOrder(SalesOrder salesOrder){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(SalesOrder.class);
			criteria.add(Restrictions.eq("salesOrderId", salesOrder.getSalesOrderId()).ignoreCase());

			List<SalesOrder> list = criteria.list();
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
	 * Get all SalesOrders
	 * @return
	 */
	public static JSONObject getAllSalesOrders(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<SalesOrder> salesOrdersList = (List<SalesOrder>)(Object)HibernateUtil.getAll(SalesOrder.class);

			if(salesOrdersList.size() == 0){
				//No SalesOrder found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No SalesOrder found");
			} else {
				Iterator<SalesOrder> iterator = salesOrdersList.iterator();
				while(iterator.hasNext()){
					SalesOrder salesOrder = iterator.next();
					JSONObject salesOrderJson = HibernateUtil.getJsonFromHibernateEntity(salesOrder);
					resultArray.put(salesOrderJson);
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
	
	public static class Tag {
		public static final String CUSTOMER_ID = "customerId", EMPLOYEE_ID = "employeeId";
	}
	/**
	 * 
	 * @param salesOrderJson
	 * @return
	 */
	public static JSONObject createOrUpdateSalesOrder(Long id, JSONObject salesOrderJson){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		SalesOrder salesOrder = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			if(id > 0){
				salesOrder = session.get(SalesOrder.class, id); 
			} else {
				salesOrder = new SalesOrder();
			}			
			
			HibernateUtil.setDataFromJson(salesOrder, salesOrderJson);			
			
			SalesOrder previousSalesOrderRevision = getSalesOrder(salesOrder);
			int salesOrderRevisionNumber = 0;
			
			if(previousSalesOrderRevision != null){
				salesOrderRevisionNumber = previousSalesOrderRevision.getSalesOrderRevisionNumber() + 1;
			}
			
			salesOrder.setSalesOrderRevisionNumber(salesOrderRevisionNumber);
			salesOrder.setLinkedCustomer(session.load(Customer.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.CUSTOMER_ID)))));
			salesOrder.setReferredByEmployee(session.load(Employee.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.EMPLOYEE_ID)))));
			
			salesOrder.setRecordCreationTime(SystemUtils.getFormattedDate());	
			
			session.save(salesOrder);					
			session.getTransaction().commit();						
			result.put(Application.RESULT, Application.SUCCESS);
			
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

package com.onequbit.advaloram.hibernate.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.ProductDao.Tag;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
import com.onequbit.advaloram.hibernate.entity.SalesOrderEntry;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class SalesOrderDao {

	
	/**
	 * 
	 * @param salesOrder
	 * @return
	 */
	public static JSONObject getNextSalesOrderId(){

		JSONObject resultsJson = new JSONObject();
		try {

			resultsJson.put(Application.RESULT, getNextSalesOrderIdAsLong());
			
		} catch(Exception e){
			e.printStackTrace();
			resultsJson = SystemUtils.generateErrorMessage(e.getMessage());
		} 
		
		return resultsJson;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static long getNextSalesOrderIdAsLong() throws Exception {
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(SalesOrder.class);
			criteria.setProjection(Projections.max("salesOrderId"));
			
			List<Long> list = criteria.list();
			long nextSalesOrderId = 1;
			if(list.size() > 0){
				Long salesOrder = list.iterator().next();
				if(salesOrder == null)
					return 1;
				
				nextSalesOrderId = salesOrder + 1;
			}
			
			return nextSalesOrderId;
		} catch(Exception e){
			throw e;
		} finally {
			if(session!=null){
				session.close();
			}
		}
	}

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
			criteria.setProjection(Projections.max("salesOrderRevisionNumber"));
			
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
		public static final String CUSTOMER_ID = "linkedCustomer", EMPLOYEE_ID = "referredByEmployee", PRODUCT_LIST = "productList", STYLE_CODE = "styleCode", COLOR_CODE = "colorCode",
				GENDER_CODE = "genderCode",
				QTY_SIZE_28 = "quantityForSize28",
						QTY_SIZE_30 = "quantityForSize30",
								QTY_SIZE_32 = "quantityForSize32",
										QTY_SIZE_34 = "quantityForSize34",
												QTY_SIZE_36 = "quantityForSize36",
														QTY_SIZE_38 = "quantityForSize38",
																QTY_SIZE_40 = "quantityForSize40",
																		QTY_SIZE_42 = "quantityForSize42",
																				QTY_SIZE_44 = "quantityForSize44",
																						QTY_SIZE_46 = "quantityForSize46",
																								QTY_SIZE_48 = "quantityForSize48",
																								ENTRY_ID = "id";
	}
	
	/**
	 * 
	 * @param salesOrderJson
	 * Update does not make changes in existing record. Update creates a new record with +1 revision number. 
	 * @return
	 */
	public static JSONObject createOrUpdateSalesOrder(Long id, JSONObject salesOrderJson){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		SalesOrder salesOrder = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			int salesOrderRevisionNumber = 0;
			
			if(id > 0){
				salesOrder = session.get(SalesOrder.class, id); 
				
				SalesOrder previousSalesOrderRevision = getSalesOrder(salesOrder);
							
				if(previousSalesOrderRevision != null){
					salesOrderRevisionNumber = previousSalesOrderRevision.getSalesOrderRevisionNumber() + 1;
				}
				
			} else {
				salesOrder = new SalesOrder();
				salesOrder.setSalesOrderId(getNextSalesOrderIdAsLong());
			}			
			
			HibernateUtil.setDataFromJson(salesOrder, salesOrderJson);			

			
			JSONArray productList = salesOrderJson.getJSONArray(Tag.PRODUCT_LIST);
			Set<SalesOrderEntry> entry = new HashSet<>();
			for(int p = 0; p < productList.length(); p++){
				JSONObject productJson = productList.getJSONObject(p);
				int entryId =Integer.valueOf(productJson.getString(Tag.ENTRY_ID));
				String styleCode = productJson.getString(Tag.STYLE_CODE);
				String colorCode = productJson.getString(Tag.COLOR_CODE);
				String genderCode = productJson.getString(Tag.GENDER_CODE);
				
				//shortcut. dirty coding
				for(int size = 28; size <=48; size = size+2){
					String prefix = "quantityForSize";
					String tag = prefix + size;
					
					if(!productJson.has(tag))
						continue;
					
					if(!productJson.getString(tag).trim().isEmpty()){
						int quantity = Integer.valueOf(productJson.getString(tag).trim());
						if(quantity > 0){
							StockKeepingUnit sku = StockKeepingUnitDao.getStockKeepingUnit(styleCode, colorCode, genderCode, String.valueOf(size));
							SalesOrderEntry salesOrderEntry = new SalesOrderEntry();
							salesOrderEntry.setEntryId(entryId);
							salesOrderEntry.setStockKeepingUnit(sku);
							salesOrderEntry.setQuantity(quantity);
							entry.add(salesOrderEntry);
						}
					}
				}
			}
			
			salesOrder.setEntry(entry);
			
			salesOrder.setSalesOrderRevisionNumber(salesOrderRevisionNumber);
			salesOrder.setLinkedCustomer(session.load(Customer.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.CUSTOMER_ID)))));
			salesOrder.setReferredByEmployee(session.load(Employee.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.EMPLOYEE_ID)))));
			salesOrder.setStatus(salesOrder.status.OPEN);
			
			salesOrder.setRecordCreationTime(SystemUtils.getFormattedDate());	
			salesOrder.setSalesOrderDate(SystemUtils.getFormattedDate());
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

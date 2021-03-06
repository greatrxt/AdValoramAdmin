package com.onequbit.advaloram.hibernate.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.ProductDao.Tag;
import com.onequbit.advaloram.hibernate.entity.AdValUser;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.PackingList;
import com.onequbit.advaloram.hibernate.entity.PackingListEntry;
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
			resultsJson = SystemUtils.generateErrorMessage(e);
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
	public static SalesOrder getSalesOrderLatestRevision(Long salesOrderId){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(SalesOrder.class);
			criteria.add(Restrictions.eq("salesOrderId", salesOrderId));
			//criteria.setProjection(Projections.max("salesOrderRevisionNumber"));
			criteria.addOrder(Order.desc("salesOrderRevisionNumber"));
			criteria.setMaxResults(1);
			
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
	 * 
	 * @param salesOrder
	 * @return
	 */
	public static JSONObject getSalesOrderJson(long salesOrderId){

		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(SalesOrder.class);
			criteria.add(Restrictions.eq("salesOrderId", salesOrderId));
			criteria.addOrder(Order.desc("salesOrderRevisionNumber"));//comment this line to send all sales order
			criteria.setMaxResults(1);//comment this line to send all sales order
			
			List<SalesOrder> salesOrdersList = criteria.list();
			
			if(salesOrdersList.size() == 0){
				//No SalesOrder found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Sales Order found");
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
			resultsJson = SystemUtils.generateErrorMessage(e);
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return resultsJson;
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
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(SalesOrder.class)
				           .setProjection(Projections.distinct(Projections.property("salesOrderId")));
			
			List<Long> salesOrdersIdList = criteria.list();
			
			if(salesOrdersIdList.size() == 0){
				//No SalesOrder found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No SalesOrder found");
			} else {
				Iterator<Long> iterator = salesOrdersIdList.iterator();
				while(iterator.hasNext()){
					SalesOrder salesOrder = getSalesOrderLatestRevision(iterator.next());
					JSONObject salesOrderJson = HibernateUtil.getJsonFromHibernateEntity(salesOrder);
					resultArray.put(salesOrderJson);
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
	 * Get all SalesOrders
	 * @return
	 */
	public static List<SalesOrder> getAllSalesOrdersList(){

		Session session = null;
		List<SalesOrder> salesOrderList = new ArrayList<>();
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
					
			Criteria criteria = 
				    session.createCriteria(SalesOrder.class)
				           .setProjection(Projections.distinct(Projections.property("salesOrderId")));
			
			List<Long> salesOrdersIdList = criteria.list();

			Iterator<Long> iterator = salesOrdersIdList.iterator();
			while(iterator.hasNext()){
				SalesOrder salesOrder = getSalesOrderLatestRevision(iterator.next());
				salesOrderList.add(salesOrder);
			}

		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return salesOrderList;
	}
	
	/**
	 * Get all SalesOrders
	 * @return
	 */
	public static JSONObject getOpenSalesOrdersCount(){
		JSONObject resultsJson = new JSONObject();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(SalesOrder.class)
				           .setProjection(Projections.distinct(Projections.property("salesOrderId")));
			
			List<Long> salesOrdersIdList = criteria.list();
			
			if(salesOrdersIdList.size() == 0){
				//No SalesOrder found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No SalesOrder found");
			} else {
				int openSalesOrderCount = 0;
				Iterator<Long> iterator = salesOrdersIdList.iterator();
				while(iterator.hasNext()){
					SalesOrder salesOrder = getSalesOrderLatestRevision(iterator.next());
					if(salesOrder.getStatus().equals(SalesOrder.Status.OPEN)){
						openSalesOrderCount++;
					}
				}
				resultsJson.put(Application.RESULT, openSalesOrderCount);
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
	
	public static class Tag {
		public static final String REFERRED_BY_CUSTOMER = "referredByCustomer", REFEREE_PARTNER = "refereePartner", CUSTOMER_ID = "linkedCustomer", EMPLOYEE_ID = "referredByEmployee", PRODUCT_LIST = "productList", STYLE_CODE = "styleCode", COLOR_CODE = "colorCode",
				GENDER_CODE = "genderCode",
				PRODUCT_CATEGORY = "productCategory",
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
	 * When user clicks on edit button
	 * @param id
	 * @param salesOrderJson
	 * @param userId
	 * @return
	 */
	public static JSONObject createNewRevisionOfSalesOrder(Long salesOrderId, Long userId){
		Session session = null;		
		JSONObject result = new JSONObject();
		SalesOrder salesOrder = null;
		try {	
			
			salesOrder = getSalesOrderLatestRevision(salesOrderId);
			if(salesOrder == null){
				throw new Exception("Sales order with ID " + salesOrderId + " not found" );					
			}
			
			//check if record has already been created
			if(salesOrder.getStatus().equals(SalesOrder.Status.OPEN)
					|| salesOrder.getStatus().equals(SalesOrder.Status.UNDER_REVISION)){
				result.put(Application.RESULT, Application.SUCCESS);
				result.put("objectId", salesOrder.getId());
				result.put("salesOrderId", salesOrder.getSalesOrderId());
				return result;
			}
			
			int salesOrderRevisionNumber = salesOrder.getSalesOrderRevisionNumber() + 1;
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			SalesOrder newSalesOrder = HibernateUtil.clone(SalesOrder.class, salesOrder);
			newSalesOrder.setSalesOrderRevisionNumber(salesOrderRevisionNumber);
			newSalesOrder.setStatus(SalesOrder.Status.UNDER_REVISION);
			newSalesOrder.setId(null);
			newSalesOrder.setRecordCreationTime(SystemUtils.getFormattedDate());	
			newSalesOrder.setCreatedBy(session.get(AdValUser.class, userId));
			Iterator<SalesOrderEntry> iterator = salesOrder.getEntry().iterator();
			Set<SalesOrderEntry> newEntries = new HashSet<>();
			while(iterator.hasNext()){
				SalesOrderEntry entry = iterator.next();
				SalesOrderEntry newEntry = HibernateUtil.clone(SalesOrderEntry.class, entry);
				newEntry.setId(null);
				newEntries.add(newEntry);
			}
			newSalesOrder.setEntry(newEntries);

			session.beginTransaction();
			session.save(newSalesOrder);			
			session.getTransaction().commit();
			
			result.put(Application.RESULT, Application.SUCCESS);
			result.put("objectId", salesOrder.getId());
			result.put("salesOrderId", salesOrder.getSalesOrderId());
		} catch(Exception e){
			e.printStackTrace();
			result = SystemUtils.generateErrorMessage(e);
		}
		
		return result;

	}
	/**
	 * 
	 * @param salesOrderJson
	 * Update does not make changes in existing record. Update creates a new record with +1 revision number. 
	 * @return
	 */
	public static JSONObject createOrUpdateSalesOrder(Long id, JSONObject salesOrderJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		SalesOrder salesOrder = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			int salesOrderRevisionNumber = 0;
			
			if(id > 0){
				salesOrder = getSalesOrderLatestRevision(id);
							
				if(salesOrder != null){
					if(!salesOrder.getStatus().equals(SalesOrder.Status.OPEN)){	//increase revision number only if SO not open
						salesOrderRevisionNumber = salesOrder.getSalesOrderRevisionNumber() + 1;
					}
					if(salesOrder.getStatus().equals(SalesOrder.Status.UNDER_REVISION)){	//revert back to original revision number if under revision
						salesOrderRevisionNumber = salesOrder.getSalesOrderRevisionNumber();
					}
				}
				
			} else {
				salesOrder = new SalesOrder();
				salesOrder.setStatus(SalesOrder.Status.OPEN);
				salesOrder.setSalesOrderDate(SystemUtils.getFormattedDate());
				salesOrder.setSalesOrderId(getNextSalesOrderIdAsLong());
			}			
			
			HibernateUtil.setDataFromJson(salesOrder, salesOrderJson);			

			if(!(salesOrder.vatIsApplicableOnSalesOrderDate 
					|| salesOrder.cstIsApplicableOnSalesOrderDate
					|| salesOrder.gstIsApplicableOnSalesOrderDate)){
				throw new Exception("At least 1 tax should be applicable");
			}
			
			JSONArray productList = salesOrderJson.getJSONArray(Tag.PRODUCT_LIST);
			Set<SalesOrderEntry> entry = new HashSet<>();
			for(int p = 0; p < productList.length(); p++){
				JSONObject productJson = productList.getJSONObject(p);
				int entryId = Integer.valueOf(String.valueOf(productJson.get(Tag.ENTRY_ID)));
				String styleCode = productJson.getString(Tag.STYLE_CODE);
				String colorCode = productJson.getString(Tag.COLOR_CODE);
				String genderCode = productJson.getString(Tag.GENDER_CODE);
				Long productCategory = Long.valueOf(String.valueOf(productJson.get(Tag.PRODUCT_CATEGORY)));
				
				//shortcut. dirty coding
				for(int size = 28; size <=48; size = size+2){
					String prefix = "quantityForSize";
					String tag = prefix + size;
					
					if(!productJson.has(tag))
						continue;
					
					if(!String.valueOf(productJson.get(tag)).trim().isEmpty()){
						int quantity = Integer.valueOf(String.valueOf(productJson.get(tag)).trim());
						if(quantity > 0){
							StockKeepingUnit sku = StockKeepingUnitDao.getStockKeepingUnit(styleCode, colorCode, genderCode, String.valueOf(size), productCategory);
							if(sku == null){
								continue;
							}
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
			if(salesOrderJson.has(Tag.CUSTOMER_ID)){
				if(!String.valueOf(salesOrderJson.get(Tag.CUSTOMER_ID)).trim().isEmpty())
					salesOrder.setLinkedCustomer(session.load(Customer.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.CUSTOMER_ID)))));
			}
			
			if(salesOrderJson.has(Tag.REFERRED_BY_CUSTOMER)){
				if(!String.valueOf(salesOrderJson.get(Tag.REFERRED_BY_CUSTOMER)).trim().isEmpty())
					salesOrder.setReferredByCustomer(session.load(Customer.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.REFERRED_BY_CUSTOMER)))));
			}
			
			if(salesOrderJson.has(Tag.REFEREE_PARTNER)){
				if(!String.valueOf(salesOrderJson.get(Tag.REFEREE_PARTNER)).trim().isEmpty())
					salesOrder.setRefereePartner(session.load(Customer.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.REFEREE_PARTNER)))));
			}
			if(salesOrderJson.has(Tag.EMPLOYEE_ID)){
				if(!String.valueOf(salesOrderJson.get(Tag.EMPLOYEE_ID)).trim().isEmpty())
					salesOrder.setReferredByEmployee(session.load(Employee.class, Long.parseLong(String.valueOf(salesOrderJson.get(Tag.EMPLOYEE_ID)))));
			}

			if(id > 0 && (salesOrder.getStatus().equals(SalesOrder.Status.OPEN)
					|| salesOrder.getStatus().equals(SalesOrder.Status.UNDER_REVISION))){
				session.update(salesOrder);
			} else {
				salesOrder.setRecordCreationTime(SystemUtils.getFormattedDate());	
				salesOrder.setCreatedBy(session.get(AdValUser.class, userId));
				session.save(salesOrder);
			}
			session.getTransaction().commit();						
			result.put(Application.RESULT, Application.SUCCESS);
			result.put("objectId", salesOrder.getId());
			result.put("salesOrderId", salesOrder.getSalesOrderId());
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

	/**
	 * Change status of Sales Order to CONFIRM
	 * @param salesOrderId
	 * @return
	 */
	public static JSONObject confirmSalesOrder(Long salesOrderId) {
		
		Session session = null;		
		JSONObject resultsJson = new JSONObject();
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(SalesOrder.class);
			criteria.add(Restrictions.eq("salesOrderId", salesOrderId));
			
			List<SalesOrder> list = criteria.list();
			if(list.size() > 0){
				Iterator<SalesOrder> salesOrderIterator = list.iterator();
				while(salesOrderIterator.hasNext()){
					SalesOrder salesOrder = salesOrderIterator.next();
					salesOrder.setStatus(salesOrder.status.CONFIRMED);
					session.update(salesOrder);
				}
				session.getTransaction().commit();
				resultsJson.put(Application.RESULT, Application.SUCCESS);
			} else {
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No SalesOrder found");
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
	 * Get sales order and linked packing lists
	 * @param salesOrderId
	 * @return
	 */
	public static JSONObject getSalesOrderAndLinkedPackingLists(Long salesOrderId) {
		JSONObject result = new JSONObject();
		JSONObject salesOrder = getSalesOrderJson(salesOrderId);	//max results set to 1. So only 1 sales order can be expected ( only the latest revision )
		
		if(!salesOrder.get(Application.RESULT).equals(Application.ERROR)){
			JSONObject salesOrderLatestRevision = salesOrder.getJSONArray(Application.RESULT).getJSONObject(0);
			JSONArray linkedPackingLists = new JSONArray();
			
			if(PackingListDao.getAllLatestRevisionOfPackingListsForSalesOrder(salesOrderId).get(Application.RESULT)
					instanceof JSONArray){
				linkedPackingLists = PackingListDao.getAllLatestRevisionOfPackingListsForSalesOrder(salesOrderId).getJSONArray(Application.RESULT);
			}
			
			salesOrderLatestRevision.put("linkedPackingLists", linkedPackingLists);
			result.put(Application.RESULT, salesOrderLatestRevision);
			return result;
		}
		
		return salesOrder; // return no sales order found message
	}
}

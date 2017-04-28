package com.onequbit.advaloram.hibernate.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.AdValUser;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.PackingList;
import com.onequbit.advaloram.hibernate.entity.PackingListEntry;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class PackingListDao {

	
	/**
	 * 
	 * @param packingList
	 * @return
	 */
	public static JSONObject getNextPackingListId(){

		JSONObject resultsJson = new JSONObject();
		try {

			resultsJson.put(Application.RESULT, getNextPackingListIdAsLong());
			
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
	public static long getNextPackingListIdAsLong() throws Exception {
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(PackingList.class);
			criteria.setProjection(Projections.max("packingListId"));
			
			List<Long> list = criteria.list();
			long nextPackingListId = 1;
			if(list.size() > 0){
				Long packingList = list.iterator().next();
				if(packingList == null)
					return 1;
				
				nextPackingListId = packingList + 1;
			}
			
			return nextPackingListId;
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
	 * @param packingList
	 * @return
	 */
	public static PackingList getPackingListLatestRevision(Long packingListId){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(PackingList.class);
			criteria.add(Restrictions.eq("packingListId", packingListId));
			criteria.addOrder(Order.desc("packingListRevisionNumber"));
			criteria.setMaxResults(1);
			
			List<PackingList> list = criteria.list();
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
	 * @param packingList
	 * @return
	 */
	public static JSONObject getPackingListLatestRevisionAsJson(long packingListId){

		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(PackingList.class);
			criteria.add(Restrictions.eq("packingListId", packingListId));
			criteria.addOrder(Order.desc("packingListRevisionNumber"));//comment this line to send all packing list
			criteria.setMaxResults(1);//comment this line to send all packing list
			
			List<PackingList> packingListsList = criteria.list();
			
			if(packingListsList.size() == 0){
				//No PackingList found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No PackingList found");
			} else {
				Iterator<PackingList> iterator = packingListsList.iterator();
				while(iterator.hasNext()){
					PackingList packingList = iterator.next();
					JSONObject packingListJson = HibernateUtil.getJsonFromHibernateEntity(packingList);
					resultArray.put(packingListJson);
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
	 * @param packingListId
	 * @return
	 */
	public static JSONObject getPackingListAndLinkedSalesOrderUsingPackingListId(long packingListId){

		JSONObject resultsJson = new JSONObject();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(PackingList.class);
			criteria.add(Restrictions.eq("packingListId", packingListId));
			criteria.addOrder(Order.desc("packingListRevisionNumber"));//comment this line to send all packing list
			criteria.setMaxResults(1);//comment this line to send all packing list
			
			List<PackingList> packingListsList = criteria.list();
			
			if(packingListsList.size() == 0){
				//No PackingList found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Packing List found");
			} else {
				PackingList packingList = packingListsList.get(0);
				//Long linkedSalesOrderId = packingList.getLinkedSalesOrderId();
				Long linkedSalesOrderId = (long) -1;
				if(packingList.getLinkedSalesOrder()!=null){
					linkedSalesOrderId = packingList.getLinkedSalesOrder().getSalesOrderId();
				}
				if(linkedSalesOrderId > 0){
					return SalesOrderDao.getSalesOrderAndLinkedPackingLists(linkedSalesOrderId);
				} else {
					JSONObject result = new JSONObject();
					result.put("salesOrderId", -1);
					result.put("packingList", PackingListDao.getPackingListLatestRevisionAsJson(packingListId).getJSONArray(Application.RESULT));
					resultsJson.put(Application.RESULT, result);
					return resultsJson;
				}
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
	 * Get all latest revisions of all PackingLists 
	 * @return
	 */
	public static JSONObject getAllPackingLists(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(PackingList.class)
				           .setProjection(Projections.distinct(Projections.property("packingListId")));
			
			List<Long> packingListsIdList = criteria.list();
			
			if(packingListsIdList.size() == 0){
				//No PackingList found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No PackingList found");
			} else {
				Iterator<Long> iterator = packingListsIdList.iterator();
				while(iterator.hasNext()){
					PackingList packingList = getPackingListLatestRevision(iterator.next());
					JSONObject packingListJson = HibernateUtil.getJsonFromHibernateEntity(packingList);
					resultArray.put(packingListJson);
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
	 * Get all latest revisions of all PackingLists for a certain Sales Order
	 * @return
	 */
	public static JSONObject getAllLatestRevisionOfPackingListsForSalesOrder(Long salesOrderId){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(PackingList.class)
				           .setProjection(Projections.distinct(Projections.property("packingListId")));

			criteria.add(Restrictions.eq("linkedSalesOrderId", salesOrderId));
			
			List<Long> packingListsIdList = criteria.list();
			
			if(packingListsIdList.size() == 0){
				//No PackingList found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No PackingList found");
			} else {
				Iterator<Long> iterator = packingListsIdList.iterator();
				while(iterator.hasNext()){
					PackingList packingList = getPackingListLatestRevision(iterator.next());
					JSONObject packingListJson = HibernateUtil.getJsonFromHibernateEntity(packingList);
					resultArray.put(packingListJson);
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
	public static class Tag {
		public static final String CUSTOMER_ID = "linkedCustomer", PRODUCT_LIST = "productList", STYLE_CODE = "styleCode", LINKED_SALES_ORDER_INTERNAL_ID = "linkedSalesOrderInternalId",
				LINKED_SALES_ORDER_ID = "linkedSalesOrderId",
				GENDER_CODE = "genderCode", COLOR_CODE = "colorCode",
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
	 * @param packingListJson
	 * Update does not make changes in existing record. Update creates a new record with +1 revision number. 
	 * @return
	 */
	public static JSONObject createOrUpdatePackingList(Long id, JSONObject packingListJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		PackingList packingList = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			int packingListRevisionNumber = 0;
			
			if(id > 0){
				packingList = getPackingListLatestRevision(id);
							
				if(packingList != null){
					packingListRevisionNumber = packingList.getPackingListRevisionNumber() + 1;
				}
				
			} else {
				packingList = new PackingList();
				packingList.setStatus(packingList.status.OPEN);
				packingList.setPackingListId(getNextPackingListIdAsLong());
			}			
			
			HibernateUtil.setDataFromJson(packingList, packingListJson);			

			
			JSONArray productList = packingListJson.getJSONArray(Tag.PRODUCT_LIST);
			Set<PackingListEntry> entry = new HashSet<>();
			for(int p = 0; p < productList.length(); p++){
				JSONObject productJson = productList.getJSONObject(p);
				int entryId = Integer.valueOf(String.valueOf(productJson.get(Tag.ENTRY_ID)));
				String styleCode = productJson.getString(Tag.STYLE_CODE);
				String colorCode = productJson.getString(Tag.COLOR_CODE);
				String genderCode = productJson.getString(Tag.GENDER_CODE);
				
				//shortcut. dirty coding
				for(int size = 28; size <=48; size = size+2){
					String prefix = "quantityForSize";
					String tag = prefix + size;
					
					if(!productJson.has(tag))
						continue;
					
					if(!String.valueOf(productJson.get(tag)).trim().isEmpty()){
						int quantity = Integer.valueOf(String.valueOf(productJson.get(tag)).trim());
						if(quantity > 0){
							StockKeepingUnit sku = StockKeepingUnitDao.getStockKeepingUnit(styleCode, colorCode, genderCode, String.valueOf(size));
							PackingListEntry packingListEntry = new PackingListEntry();
							packingListEntry.setEntryId(entryId);
							packingListEntry.setStockKeepingUnit(sku);
							packingListEntry.setQuantity(quantity);
							entry.add(packingListEntry);
						}
					}
				}
			}
			
			packingList.setEntry(entry);
			
			if(packingListJson.has(Tag.LINKED_SALES_ORDER_ID)){
				if(!String.valueOf(packingListJson.get(Tag.LINKED_SALES_ORDER_ID)).trim().isEmpty()){
					Long linkedSalesOrderId = Long.valueOf(String.valueOf(packingListJson.get(Tag.LINKED_SALES_ORDER_ID)));
					if(linkedSalesOrderId > 0){
						packingList.setLinkedSalesOrderId(linkedSalesOrderId);
					}
				}
			}
			
			if(packingListJson.has(Tag.LINKED_SALES_ORDER_INTERNAL_ID)){
				if(!String.valueOf(packingListJson.get(Tag.LINKED_SALES_ORDER_INTERNAL_ID)).trim().isEmpty()){
					Long linkedSalesOrderId = Long.valueOf(String.valueOf(packingListJson.get(Tag.LINKED_SALES_ORDER_INTERNAL_ID)));
					if(linkedSalesOrderId > 0){
						packingList.setLinkedSalesOrder(session.get(SalesOrder.class, linkedSalesOrderId));
					}
				}
			}
			
			if(packingListJson.has(Tag.CUSTOMER_ID)){
				if(!String.valueOf(packingListJson.get(Tag.CUSTOMER_ID)).trim().isEmpty()){
					Long linkedCustomerId = Long.valueOf(String.valueOf(packingListJson.get(Tag.CUSTOMER_ID)));
					if(linkedCustomerId > 0){
						Customer customer = session.load(Customer.class, linkedCustomerId);
						packingList.setLinkedCustomer(customer);
					}
				}
			}
			
			packingList.setPackingListRevisionNumber(packingListRevisionNumber);
			
			packingList.setRecordCreationTime(SystemUtils.getFormattedDate());	
			packingList.setPackingListDate(SystemUtils.getFormattedDate());
			if(id > 0 && packingList.getStatus().equals(PackingList.Status.OPEN)){
				session.update(packingList);
			} else {
				packingList.setCreatedBy(session.get(AdValUser.class, userId));
				session.save(packingList);					
			}
			session.getTransaction().commit();						
			result.put(Application.RESULT, Application.SUCCESS);
			result.put("objectId", packingList.getId());
			result.put("packingListId", packingList.getPackingListId());
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
	 * Change status of Packing List to CONFIRM
	 * @param packingListId
	 * @return
	 */
	public static JSONObject confirmPackingList(Long packingListId) {
		
		Session session = null;		
		JSONObject resultsJson = new JSONObject();
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(PackingList.class);
			criteria.add(Restrictions.eq("packingListId", packingListId));
			
			List<PackingList> list = criteria.list();
			if(list.size() > 0){
				Iterator<PackingList> packingListIterator = list.iterator();
				while(packingListIterator.hasNext()){
					PackingList packingList = packingListIterator.next();
					packingList.setStatus(packingList.status.CONFIRMED);
					session.update(packingList);
				}
				session.getTransaction().commit();
				resultsJson.put(Application.RESULT, Application.SUCCESS);
			} else {
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Packing List found");
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

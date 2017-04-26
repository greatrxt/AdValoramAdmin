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
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.Invoice;
import com.onequbit.advaloram.hibernate.entity.CreditNote;
import com.onequbit.advaloram.hibernate.entity.CreditNoteEntry;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class CreditNoteDao {

	
	/**
	 * 
	 * @param creditNote
	 * @return
	 */
	public static JSONObject getNextCreditNoteId(){

		JSONObject resultsJson = new JSONObject();
		try {

			resultsJson.put(Application.RESULT, getNextCreditNoteIdAsLong());
			
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
	public static long getNextCreditNoteIdAsLong() throws Exception {
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(CreditNote.class);
			criteria.setProjection(Projections.max("creditNoteId"));
			
			List<Long> list = criteria.list();
			long nextCreditNoteId = 1;
			if(list.size() > 0){
				Long creditNote = list.iterator().next();
				if(creditNote == null)
					return 1;
				
				nextCreditNoteId = creditNote + 1;
			}
			
			return nextCreditNoteId;
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
	 * @param creditNote
	 * @return
	 */
	public static CreditNote getCreditNoteLatestRevision(Long creditNoteId){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(CreditNote.class);
			criteria.add(Restrictions.eq("creditNoteId", creditNoteId));
			//criteria.setProjection(Projections.max("creditNoteRevisionNumber"));
			criteria.addOrder(Order.desc("creditNoteRevisionNumber"));
			criteria.setMaxResults(1);
			
			List<CreditNote> list = criteria.list();
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
	 * @param creditNote
	 * @return
	 */
	public static JSONObject getCreditNoteJson(long creditNoteId){

		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(CreditNote.class);
			criteria.add(Restrictions.eq("creditNoteId", creditNoteId));
			criteria.addOrder(Order.desc("creditNoteRevisionNumber"));//comment this line to send all sales order
			criteria.setMaxResults(1);//comment this line to send all sales order
			
			List<CreditNote> creditNotesList = criteria.list();
			
			if(creditNotesList.size() == 0){
				//No CreditNote found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Sales Order found");
			} else {
				Iterator<CreditNote> iterator = creditNotesList.iterator();
				while(iterator.hasNext()){
					CreditNote creditNote = iterator.next();
					JSONObject creditNoteJson = HibernateUtil.getJsonFromHibernateEntity(creditNote);
					resultArray.put(creditNoteJson);
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
	 * Get all CreditNotes
	 * @return
	 */
	public static JSONObject getAllCreditNotes(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(CreditNote.class)
				           .setProjection(Projections.distinct(Projections.property("creditNoteId")));
			
			List<Long> creditNotesIdList = criteria.list();
			
			if(creditNotesIdList.size() == 0){
				//No CreditNote found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No CreditNote found");
			} else {
				Iterator<Long> iterator = creditNotesIdList.iterator();
				while(iterator.hasNext()){
					CreditNote creditNote = getCreditNoteLatestRevision(iterator.next());
					JSONObject creditNoteJson = HibernateUtil.getJsonFromHibernateEntity(creditNote);
					resultArray.put(creditNoteJson);
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
		public static final String CUSTOMER_ID = "linkedCustomer", EMPLOYEE_ID = "referredByEmployee", PRODUCT_LIST = "productList", STYLE_CODE = "styleCode", COLOR_CODE = "colorCode",
				GENDER_CODE = "genderCode", LINKED_INVOICE_ID = "linkedInvoiceId",
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
	 * @param creditNoteJson
	 * Update does not make changes in existing record. Update creates a new record with +1 revision number. 
	 * @return
	 */
	public static JSONObject createOrUpdateCreditNote(Long id, JSONObject creditNoteJson){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		CreditNote creditNote = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			int creditNoteRevisionNumber = 0;
			
			if(id > 0){
				creditNote = getCreditNoteLatestRevision(id);
							
				if(creditNote != null){
					creditNoteRevisionNumber = creditNote.getCreditNoteRevisionNumber() + 1;
				}
				
			} else {
				creditNote = new CreditNote();
				creditNote.setStatus(creditNote.status.OPEN);
				creditNote.setCreditNoteId(getNextCreditNoteIdAsLong());
			}			
			
			HibernateUtil.setDataFromJson(creditNote, creditNoteJson);			

			
			JSONArray productList = creditNoteJson.getJSONArray(Tag.PRODUCT_LIST);
			Set<CreditNoteEntry> entry = new HashSet<>();
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
							CreditNoteEntry creditNoteEntry = new CreditNoteEntry();
							creditNoteEntry.setEntryId(entryId);
							creditNoteEntry.setStockKeepingUnit(sku);
							creditNoteEntry.setQuantity(quantity);
							entry.add(creditNoteEntry);
						}
					}
				}
			}
			
			creditNote.setEntry(entry);
			
			creditNote.setCreditNoteRevisionNumber(creditNoteRevisionNumber);
			creditNote.setLinkedInvoice(InvoiceDao.getInvoiceLatestRevision(Long.valueOf(String.valueOf(creditNoteJson.get(Tag.LINKED_INVOICE_ID)))));
			
			if(id > 0 && creditNote.getStatus().equals(CreditNote.Status.OPEN)){
				session.update(creditNote);
			} else {
				creditNote.setRecordCreationTime(SystemUtils.getFormattedDate());	
				creditNote.setCreditNoteDate(SystemUtils.getFormattedDate());
				session.save(creditNote);
			}

			session.getTransaction().commit();						
			result.put(Application.RESULT, Application.SUCCESS);
			result.put("objectId", creditNote.getId());
			result.put("creditNoteId", creditNote.getCreditNoteId());
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
	 * @param creditNoteId
	 * @return
	 */
	public static JSONObject issueCreditNote(Long creditNoteId) {
		
		Session session = null;		
		JSONObject resultsJson = new JSONObject();
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(CreditNote.class);
			criteria.add(Restrictions.eq("creditNoteId", creditNoteId));
			
			List<CreditNote> list = criteria.list();
			if(list.size() > 0){
				Iterator<CreditNote> creditNoteIterator = list.iterator();
				while(creditNoteIterator.hasNext()){
					CreditNote creditNote = creditNoteIterator.next();
					creditNote.setStatus(creditNote.status.ISSUED);
					session.update(creditNote);
				}
				session.getTransaction().commit();
				resultsJson.put(Application.RESULT, Application.SUCCESS);
			} else {
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No CreditNote found");
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

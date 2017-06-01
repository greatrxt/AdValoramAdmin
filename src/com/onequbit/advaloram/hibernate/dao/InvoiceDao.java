package com.onequbit.advaloram.hibernate.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.PackingList;
import com.onequbit.advaloram.hibernate.entity.PackingListEntry;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
import com.onequbit.advaloram.hibernate.entity.Invoice;
import com.onequbit.advaloram.hibernate.entity.Invoice.Status;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class InvoiceDao {

	
	/**
	 * 
	 * @param invoice
	 * @return
	 */
	public static JSONObject getNextInvoiceId(){

		JSONObject resultsJson = new JSONObject();
		try {

			resultsJson.put(Application.RESULT, getNextInvoiceIdAsLong());
			
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
	public static long getNextInvoiceIdAsLong() throws Exception {
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.setProjection(Projections.max("invoiceId"));
			
			List<Long> list = criteria.list();
			long nextInvoiceId = 1;
			if(list.size() > 0){
				Long invoice = list.iterator().next();
				if(invoice == null)
					return 1;
				
				nextInvoiceId = invoice + 1;
			}
			
			return nextInvoiceId;
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
	 * @param invoice
	 * @return
	 */
	public static Invoice getInvoiceLatestRevision(Long invoiceId){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.add(Restrictions.eq("invoiceId", invoiceId));
			//criteria.setProjection(Projections.max("invoiceRevisionNumber"));
			criteria.addOrder(Order.desc("invoiceRevisionNumber"));
			criteria.setMaxResults(1);
			
			List<Invoice> list = criteria.list();
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
	 * @param invoice
	 * @return
	 */
	public static JSONObject getInvoiceJson(long invoiceId){

		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.add(Restrictions.eq("invoiceId", invoiceId));
			criteria.addOrder(Order.desc("invoiceRevisionNumber"));//comment this line to send all sales order
			criteria.setMaxResults(1);//comment this line to send all sales order
			
			List<Invoice> invoicesList = criteria.list();
			
			if(invoicesList.size() == 0){
				//No Invoice found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice with invoiceID '" + invoiceId + "' found");
			} else {
				Iterator<Invoice> iterator = invoicesList.iterator();
				while(iterator.hasNext()){
					Invoice invoice = iterator.next();
					JSONObject invoiceJson = HibernateUtil.getJsonFromHibernateEntity(invoice);
					resultArray.put(invoiceJson);
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
	 * Get all Invoices ID
	 * @return
	 */
	public static JSONObject getAllInvoicesId(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(Invoice.class)
				           .setProjection(Projections.distinct(Projections.property("invoiceId")));
			
			List<Long> invoicesIdList = criteria.list();
			
			if(invoicesIdList.size() == 0){
				//No Invoice found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice found");
			} else {
				Iterator<Long> iterator = invoicesIdList.iterator();
				while(iterator.hasNext()){
					resultArray.put(iterator.next());
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
	 * Get all Invoices ID
	 * @return
	 */
	public static JSONObject getAllInvoicesIdForCustomer(Long customerId){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(Invoice.class)
				           .setProjection(Projections.distinct(Projections.property("invoiceId")));
			//criteria.add(Restrictions.eq("linkedPackingList.linkedCustomer", session.load(Customer.class, customerId)));
			
			criteria.createAlias("linkedPackingList", "packingList").add(Restrictions.eq("packingList.linkedCustomer", session.load(Customer.class, customerId)));
			
			List<Long> invoicesIdList = criteria.list();
			
			if(invoicesIdList.size() == 0){
				//No Invoice found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice found");
			} else {
				Iterator<Long> iterator = invoicesIdList.iterator();
				while(iterator.hasNext()){
					resultArray.put(iterator.next());
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
	 * Get all Invoices
	 * @return
	 */
	public static JSONObject getMonthlySales(){
		JSONObject resultsJson = new JSONObject();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(Invoice.class)
				           .setProjection(Projections.distinct(Projections.property("invoiceId")));
			
			List<Long> invoicesIdList = criteria.list();
			
			if(invoicesIdList.size() == 0){
				//No Invoice found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice found");
			} else {
				Iterator<Long> iterator = invoicesIdList.iterator();
				Double monthlySales = (double) 0;
				while(iterator.hasNext()){
					Invoice invoice = getInvoiceLatestRevision(iterator.next());
					
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, -1);
					Date result = cal.getTime();
					
					if(invoice.getInvoiceDate().before(result)){
						continue;
					}
					Iterator<PackingListEntry> entries = invoice.getLinkedPackingList().getEntry().iterator();
					while(entries.hasNext()){
						PackingListEntry entry = entries.next();
						monthlySales+=(entry.getStockKeepingUnit().getProduct().getMrp()  * entry.getQuantity());
					}
				}
				resultsJson.put(Application.RESULT, monthlySales);
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
	 * Get all Invoices
	 * @return
	 */
	public static JSONObject getAllInvoices(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(Invoice.class)
				           .setProjection(Projections.distinct(Projections.property("invoiceId")));
			
			List<Long> invoicesIdList = criteria.list();
			
			if(invoicesIdList.size() == 0){
				//No Invoice found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice found");
			} else {
				Iterator<Long> iterator = invoicesIdList.iterator();
				while(iterator.hasNext()){
					Invoice invoice = getInvoiceLatestRevision(iterator.next());
					JSONObject invoiceJson = HibernateUtil.getJsonFromHibernateEntity(invoice);
					resultArray.put(invoiceJson);
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
	 * Get count of open invoices
	 * @return
	 */
	public static JSONObject getOpenInvoicesCount(){
		JSONObject resultsJson = new JSONObject();
		Session session = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();		
			
			
			Criteria criteria = 
				    session.createCriteria(Invoice.class)
				           .setProjection(Projections.distinct(Projections.property("invoiceId")));
			
			List<Long> invoicesIdList = criteria.list();
			
			if(invoicesIdList.size() == 0){
				//No Invoice found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice found");
			} else {
				int openInvoiceCount = 0;
				Iterator<Long> iterator = invoicesIdList.iterator();
				while(iterator.hasNext()){
					Invoice invoice = getInvoiceLatestRevision(iterator.next());
					if(invoice.getStatus().equals(Invoice.Status.OPEN)){
						openInvoiceCount++;
					}
				}
				resultsJson.put(Application.RESULT, openInvoiceCount);
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
		public static final String REFEREE_PARTNER_ID = "refereePartnerId", SALES_EMPLOYEE_ID = "salesEmployeeId",
				LINKED_SALES_ORDER_INTERNAL_ID = "linkedSalesOrderInternalId", LINKED_PACKING_LIST_INTERNAL_ID = "linkedPackingListInternalId";
	}
	
	/**
	 * When user clicks on edit button
	 * @param id
	 * @param invoiceJson
	 * @param userId
	 * @return
	 */
	public static JSONObject createNewRevisionOfInvoice(Long invoiceId, Long userId){
		Session session = null;		
		JSONObject result = new JSONObject();
		Invoice invoice = null;
		try {	
			
			invoice = getInvoiceLatestRevision(invoiceId);
			if(invoice == null){
				throw new Exception("Invoice with ID " + invoiceId + " not found" );					
			}
			
			//check if record has already been created
			if(invoice.getStatus().equals(Invoice.Status.OPEN)
					|| invoice.getStatus().equals(Invoice.Status.UNDER_REVISION)){
				result.put(Application.RESULT, Application.SUCCESS);
				result.put("objectId", invoice.getId());
				result.put("invoiceId", invoice.getInvoiceId());
				return result;
			}
			
			int invoiceRevisionNumber = invoice.getInvoiceRevisionNumber() + 1;
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			Invoice newInvoice = HibernateUtil.clone(Invoice.class, invoice);
			newInvoice.setInvoiceRevisionNumber(invoiceRevisionNumber);
			newInvoice.setStatus(Invoice.Status.UNDER_REVISION);
			newInvoice.setId(null);
			newInvoice.setRecordCreationTime(SystemUtils.getFormattedDate());	
			newInvoice.setCreatedBy(session.get(AdValUser.class, userId));

			session.beginTransaction();
			session.save(newInvoice);			
			session.getTransaction().commit();
			
			result.put(Application.RESULT, Application.SUCCESS);
			result.put("objectId", invoice.getId());
			result.put("invoiceId", invoice.getInvoiceId());
		} catch(Exception e){
			e.printStackTrace();
			result = SystemUtils.generateErrorMessage(e);
		}
		
		return result;

	}
	
	/**
	 * 
	 * @param invoiceJson
	 * Update does not make changes in existing record. Update creates a new record with +1 revision number. 
	 * @return
	 */
	public static JSONObject createOrUpdateInvoice(Long id, JSONObject invoiceJson, Long userId){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Invoice invoice = null;
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			int invoiceRevisionNumber = 0;
			
			if(id > 0){
				invoice = getInvoiceLatestRevision(id);
							
				if(invoice != null){
					if(!invoice.getStatus().equals(Invoice.Status.OPEN)){	//increase revision number only if Invoice not open
						invoiceRevisionNumber = invoice.getInvoiceRevisionNumber() + 1;
					}
					
					if(invoice.getStatus().equals(Invoice.Status.UNDER_REVISION)){	
						invoiceRevisionNumber = invoice.getInvoiceRevisionNumber();
					}
				}
				
			} else {
				invoice = new Invoice();
				invoice.setStatus(Status.OPEN);
				invoice.setInvoiceId(getNextInvoiceIdAsLong());
			}			
			
			HibernateUtil.setDataFromJson(invoice, invoiceJson);			
		
			invoice.setInvoiceRevisionNumber(invoiceRevisionNumber);
			if(invoiceJson.has(Tag.REFEREE_PARTNER_ID)){
				if(!String.valueOf(invoiceJson.get(Tag.REFEREE_PARTNER_ID)).trim().isEmpty())
					invoice.setRefereePartnerName(session.load(Customer.class, Long.parseLong(String.valueOf(invoiceJson.get(Tag.REFEREE_PARTNER_ID)))));
			}
			if(invoiceJson.has(Tag.SALES_EMPLOYEE_ID)){
				if(!String.valueOf(invoiceJson.get(Tag.SALES_EMPLOYEE_ID)).trim().isEmpty())
					invoice.setSalesEmployee(session.load(Employee.class, Long.parseLong(String.valueOf(invoiceJson.get(Tag.SALES_EMPLOYEE_ID)))));
			}
			
			invoice.setLinkedSalesOrder(session.get(SalesOrder.class, Long.valueOf(String.valueOf(invoiceJson.get(Tag.LINKED_SALES_ORDER_INTERNAL_ID)))));
			invoice.setLinkedPackingList(session.get(PackingList.class, Long.valueOf(String.valueOf(invoiceJson.get(Tag.LINKED_PACKING_LIST_INTERNAL_ID)))));
			
			if(id > 0 && (invoice.getStatus().equals(Invoice.Status.OPEN)
					|| invoice.getStatus().equals(Invoice.Status.UNDER_REVISION))){
				session.update(invoice);
			} else {
				invoice.setRecordCreationTime(SystemUtils.getFormattedDate());	
				invoice.setInvoiceDate(SystemUtils.getFormattedDate());
				invoice.setCreatedBy(session.get(AdValUser.class, userId));
				session.save(invoice);	//create new revision only if invoice is not open
			}					
			session.getTransaction().commit();						
			result.put(Application.RESULT, Application.SUCCESS);
			result.put("objectId", invoice.getId());
			result.put("invoiceId", invoice.getInvoiceId());
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
	 * Change status of Invoice to CONFIRM
	 * @param invoiceId
	 * @return
	 */
	public static JSONObject confirmInvoice(Long invoiceId) {
		
		Session session = null;		
		JSONObject resultsJson = new JSONObject();
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.add(Restrictions.eq("invoiceId", invoiceId));
			
			List<Invoice> list = criteria.list();
			if(list.size() > 0){
				Iterator<Invoice> invoiceIterator = list.iterator();
				while(invoiceIterator.hasNext()){
					Invoice invoice = invoiceIterator.next();
					invoice.setStatus(Status.CONFIRMED);
					session.update(invoice);
				}
				session.getTransaction().commit();
				resultsJson.put(Application.RESULT, Application.SUCCESS);
			} else {
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice found");
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
	 * Uses internal ID i.e ID column to fetch Invoice
	 * @param internalId
	 * @return
	 */
	public static JSONObject getInvoiceJsonUsingInternalId(Long internalId) {
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Invoice invoice = session.get(Invoice.class, internalId);
			JSONObject invoiceJson = HibernateUtil.getJsonFromHibernateEntity(invoice);
			resultArray.put(invoiceJson);
			resultsJson.put(Application.RESULT, resultArray);
			
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
	 * Get invoice linked to a particular packing list
	 * @param packingListInternalId
	 * @return
	 */
	public static JSONObject getInvoiceStatusForPackingList(Long packingListInternalId) {
		JSONObject resultsJson = new JSONObject();
		resultsJson.put(Application.RESULT, false);	//default - false
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.add(Restrictions.eq("linkedPackingList", session.load(PackingList.class, packingListInternalId)));
			
			List<Invoice> invoicesList = criteria.list();
			
			if(invoicesList.size() > 0){
				resultsJson.put(Application.RESULT, true);
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
	 * Get invoice linked to a particular packing list
	 * @param packingListInternalId
	 * @return
	 */
	public static JSONObject getInvoiceLinkedToPackingList(Long packingListInternalId) {
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.add(Restrictions.eq("linkedPackingList", session.load(PackingList.class, packingListInternalId)));
			
			List<Invoice> invoicesList = criteria.list();
			
			if(invoicesList.size() == 0){
				//No Invoice found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice linked to packingListId '" + packingListInternalId + "' found");
			} else {
				Iterator<Invoice> iterator = invoicesList.iterator();
				while(iterator.hasNext()){
					Invoice invoice = iterator.next();
					JSONObject invoiceJson = HibernateUtil.getJsonFromHibernateEntity(invoice);
					resultArray.put(invoiceJson);
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
	 * @param invoiceId
	 * @param inputStreamArray
	 * @param lrNo
	 * @return
	 */
	public static JSONObject addLrNumberToInvoice(Long invoiceId, String lrNo) {
		
		Session session = null;		
		JSONObject resultsJson = new JSONObject();
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Invoice.class);
			criteria.add(Restrictions.eq("invoiceId", invoiceId));
			
			List<Invoice> list = criteria.list();
			if(list.size() > 0){
				Iterator<Invoice> invoiceIterator = list.iterator();
				while(invoiceIterator.hasNext()){
					Invoice invoice = invoiceIterator.next();
					invoice.setLorryReceiptNumber(lrNo);
					session.update(invoice);
				}
				session.getTransaction().commit();
				resultsJson.put(Application.RESULT, Application.SUCCESS);
			} else {
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Invoice found");
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

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
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.dao.ProductDao.Tag;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.Invoice;
import com.onequbit.advaloram.hibernate.entity.Invoice.Status;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
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
			resultsJson = SystemUtils.generateErrorMessage(e.getMessage());
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
				resultsJson.put(Application.ERROR_MESSAGE, "No Sales Order found");
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
			resultsJson = SystemUtils.generateErrorMessage(e.getMessage());
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
			resultsJson = SystemUtils.generateErrorMessage(e.getMessage());
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return resultsJson;
	}
	
	public static class Tag {
		public static final String REFEREE_PARTNER_ID = "refereePartnerId", SALES_EMPLOYEE_ID = "salesEmployeeId";
	}
	
	/**
	 * 
	 * @param invoiceJson
	 * Update does not make changes in existing record. Update creates a new record with +1 revision number. 
	 * @return
	 */
	public static JSONObject createOrUpdateInvoice(Long id, JSONObject invoiceJson){
		
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
					invoiceRevisionNumber = invoice.getInvoiceRevisionNumber() + 1;
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
			
			invoice.setRecordCreationTime(SystemUtils.getFormattedDate());	
			invoice.setInvoiceDate(SystemUtils.getFormattedDate());
			session.save(invoice);					
			session.getTransaction().commit();						
			result.put(Application.RESULT, Application.SUCCESS);
			result.put("objectId", invoice.getId());
			result.put("invoiceId", invoice.getInvoiceId());
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

	/**
	 * Change status of Sales Order to CONFIRM
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
					invoice.setStatus(Status.ISSUED);
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
			resultsJson = SystemUtils.generateErrorMessage(e.getMessage());
		} finally {
			if(session!=null){
				session.close();
			}
		}
		
		return resultsJson;
	}
}

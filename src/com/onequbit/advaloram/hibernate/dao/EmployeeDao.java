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
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class EmployeeDao {
	
	/**
	 * 
	 * @param employee
	 * @return
	 */
	public static Employee getEmployee(Employee employee){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Employee.class);
			criteria.add(Restrictions.eq("firstName", employee.getFirstName()).ignoreCase());

			List<Employee> list = criteria.list();
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
	 * Get all Employees
	 * @return
	 */
	public static JSONObject getAllEmployees(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Employee> employeesList = (List<Employee>)(Object)HibernateUtil.getAll(Employee.class);

			if(employeesList.size() == 0){
				//No Employee found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Employee found");
			} else {
				Iterator<Employee> iterator = employeesList.iterator();
				while(iterator.hasNext()){
					Employee employee = iterator.next();
					JSONObject employeeJson = HibernateUtil.getJsonFromHibernateEntity(employee);
					resultArray.put(employeeJson);
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
	 * @param employeeJson
	 * @return
	 */
	public static JSONObject createOrUpdateEmployee(Long id, JSONObject employeeJson){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Employee employee = null;
		
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			if(id < 0){
				employee = new Employee();
			} else {
				employee = session.get(Employee.class, id);
			}
			HibernateUtil.setDataFromJson(employee, employeeJson);
			
			if(getEmployee(employee) != null && id < 0){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "employee " + employeeJson.toString() + " already exists");
			} else {
			
				session.beginTransaction();
				employee.setCity(session.load(Location.class, Long.parseLong(employeeJson.getString("city"))));
				if(!employeeJson.getString("reportingTo").trim().isEmpty()){
					employee.setReportingTo(session.load(Employee.class, Long.parseLong(employeeJson.getString("reportingTo"))));
				}
			
				if(id < 0){
					employee.setRecordCreationTime(SystemUtils.getFormattedDate());					
					session.save(employee);
				} else {
					session.update(employee);
				}
									
				session.getTransaction().commit();						
				result.put(Application.RESULT, Application.SUCCESS);				
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

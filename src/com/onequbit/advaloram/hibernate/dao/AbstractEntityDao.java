package com.onequbit.advaloram.hibernate.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.AbstractAdValoramEntity;
import com.onequbit.advaloram.hibernate.entity.Bank;
import com.onequbit.advaloram.hibernate.entity.Brand;
import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.Gender;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.ProductCategory;
import com.onequbit.advaloram.hibernate.entity.Season;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
import com.onequbit.advaloram.hibernate.entity.Style;
import com.onequbit.advaloram.hibernate.entity.Tax;
import com.onequbit.advaloram.hibernate.entity.Transporter;
import com.onequbit.advaloram.hibernate.entity.UnitOfMeasurement;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class AbstractEntityDao {
	
	public final static String BANK = "bank", 
			BRAND = "brand", 
			COLOR = "color", 
			CUSTOMER = "customer", 
			EMPLOYEE = "employee", 
			GENDER = "gender", 
			LOCATION = "location",
			PRODUCT = "product",
			PRODUCT_CATEGORY = "product_category",
			SEASON = "season",
			SIZE = "size",
			STYLE = "style",
			STOCK_KEEPING_UNIT = "stockKeepingUnit",
			TAX = "tax",
			TRANSPORTER = "transporter",
			UNIT_OF_MEASUREMENT = "unitOfMeasurement";
	
	/**
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static JSONObject getEntityUsingId(String entityClass, long id){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			Object entity = null;			
			switch(entityClass){
				case BANK:				
					entity = HibernateUtil.getUsingId(Bank.class, id);
					break;
				case BRAND:
					entity = HibernateUtil.getUsingId(Brand.class, id);
					break;
				case COLOR:
					entity = HibernateUtil.getUsingId(Color.class, id);
					break;
				case CUSTOMER:
					entity = HibernateUtil.getUsingId(Customer.class, id);
					break;
				case EMPLOYEE:
					entity = HibernateUtil.getUsingId(Employee.class, id);
					break;
				case GENDER:
					entity = HibernateUtil.getUsingId(Gender.class, id);
					break;
				case LOCATION:
					entity = HibernateUtil.getUsingId(Location.class, id);
					break;
				case PRODUCT:
					entity = HibernateUtil.getUsingId(Product.class, id);
					break;
				case PRODUCT_CATEGORY:
					entity = HibernateUtil.getUsingId(ProductCategory.class, id);
					break;
				case SEASON:
					entity = HibernateUtil.getUsingId(Season.class, id);
					break;
				case SIZE:
					entity = HibernateUtil.getUsingId(Size.class, id);
					break;
				case STYLE:
					entity = HibernateUtil.getUsingId(Style.class, id);
					break;
				case STOCK_KEEPING_UNIT:
					entity = HibernateUtil.getUsingId(StockKeepingUnit.class, id);
					break;
				case TAX:
					entity = HibernateUtil.getUsingId(Tax.class, id);
					break;
				case TRANSPORTER:
					entity = HibernateUtil.getUsingId(Transporter.class, id);
					break;
				case UNIT_OF_MEASUREMENT:
					entity = HibernateUtil.getUsingId(UnitOfMeasurement.class, id);
					break;

				default:
					throw new Exception("Class" + entityClass + "not found");
			}
			

			if(entity == null){
				//No Object found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No "+ entityClass + " found");
			} else {
				JSONObject entityJson = HibernateUtil.getJsonFromHibernateEntity(entity);
				resultArray.put(entityJson);				
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
	 * Get all Objects
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonForAll(String entityClass){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			List<Object> entityList = null;			
			switch(entityClass){
				case BANK:				
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Bank.class);
					break;
				case BRAND:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Brand.class);
					break;
				case COLOR:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Color.class);
					break;
				case CUSTOMER:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Customer.class);
					break;
				case EMPLOYEE:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Employee.class);
					break;
				case GENDER:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Gender.class);
					break;
				case LOCATION:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Location.class);
					break;
				case PRODUCT:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Product.class);
					break;
				case PRODUCT_CATEGORY:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(ProductCategory.class);
					break;
				case SEASON:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Season.class);
					break;
				case SIZE:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Size.class);
					break;
				case STOCK_KEEPING_UNIT:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(StockKeepingUnit.class);
					break;
				case STYLE:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Style.class);
					break;
				case TAX:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Tax.class);
					break;
				case TRANSPORTER:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(Transporter.class);
					break;
				case UNIT_OF_MEASUREMENT:
					entityList = (List<Object>)(Object)HibernateUtil.getAll(UnitOfMeasurement.class);
					break;

				default:
					throw new Exception("Class" + entityClass + "not found");
			}
			

			if(entityList.size() == 0){
				//No Object found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No "+ entityClass + " found");
			} else {
				Iterator<Object> iterator = entityList.iterator();
				while(iterator.hasNext()){
					Object entity = iterator.next();
					JSONObject entityJson = HibernateUtil.getJsonFromHibernateEntity(entity);
					resultArray.put(entityJson);
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
	 * @param entityClass
	 * @param entity
	 * @return
	 */
	private static Object getEntity(String entityClass, Object entity){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = null;
			
			switch(entityClass){
			case BANK:				
				criteria = session.createCriteria(Bank.class);
				criteria.add(Restrictions.eq("bankName", ((Bank) entity).getBankName()).ignoreCase());
				break;
			case BRAND:
				
				break;
			case COLOR:
				criteria = session.createCriteria(Color.class);
				criteria.add(Restrictions.eq("colorName", ((Color) entity).getColorName()).ignoreCase());
				break;
			case CUSTOMER:
				criteria = session.createCriteria(Customer.class);
				criteria.add(Restrictions.eq("companyName", ((Customer) entity).getCompanyName()).ignoreCase());
				break;
			case EMPLOYEE:
				
				break;
			case GENDER:
				
				break;
			case LOCATION:
				
				break;
			case PRODUCT:
				
				break;
			case PRODUCT_CATEGORY:
				
				break;
			case SEASON:
				
				break;
			case SIZE:
				criteria = session.createCriteria(Size.class);
				criteria.add(Restrictions.eq("sizeCode", ((Size) entity).getSizeCode()).ignoreCase());
				break;
			case STYLE:
				
				break;
			case TRANSPORTER:
				criteria = session.createCriteria(Transporter.class);
				criteria.add(Restrictions.eq("companyName", ((Transporter) entity).getCompanyName()).ignoreCase());
				break;
			case UNIT_OF_MEASUREMENT:
				
				break;
			default:
				throw new Exception("Class" + entityClass + "not found");
		}
			
		if(criteria!=null){
			List<Color> list = criteria.list();
			return list.get(0);
		}
			
		} catch (Exception e){
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
	 * @param entityClass
	 * @param entityJson
	 * @return
	 */
	public static JSONObject createEntity(String entityClass, JSONObject entityJson){
		
		Session session = null;		
		session = HibernateUtil.getSessionAnnotationFactory().openSession();
		JSONObject result = new JSONObject();
		AbstractAdValoramEntity entity = null;
		try {			
			switch(entityClass){
				case BANK:				
					entity = new Bank();
					((Bank) entity).setCity(session.load(Location.class, Long.parseLong(entityJson.getString("city"))));
					break;
				case BRAND:
					entity = new Brand();
					break;
				case COLOR:
					entity = new Color();
					break;
				case CUSTOMER:
					entity = new Customer();
					break;
				case EMPLOYEE:
					entity = new Employee();
					break;
				case GENDER:
					entity = new Gender();
					break;
				case LOCATION:
					entity = new Location();
					break;
				case PRODUCT:
					entity = new Product();
					break;
				case PRODUCT_CATEGORY:
					entity = new ProductCategory();
					break;
				case SEASON:
					entity = new Season();
					break;
				case SIZE:
					entity = new Size();
					break;
				case STYLE:
					entity = new Style();
					break;
				case TRANSPORTER:
					entity = new Transporter();
					break;
				case UNIT_OF_MEASUREMENT:
					entity = new UnitOfMeasurement();
					break;
				default:
					throw new Exception("Class" + entityClass + "not found");
			}
			
			HibernateUtil.setDataFromJson(entity, entityJson);
			entity.setRecordCreationTime(SystemUtils.getFormattedDate());
			
			if(getEntity(entityClass, entity) != null){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "entity " + entityJson.toString() + " already exists");
			} else {			
				session.beginTransaction();					
				session.save(entity);					
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

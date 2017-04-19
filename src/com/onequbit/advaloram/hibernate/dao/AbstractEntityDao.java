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
import com.onequbit.advaloram.hibernate.entity.File;
import com.onequbit.advaloram.hibernate.entity.Gender;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.PackingList;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.ProductCategory;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
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
			FILE = "file",
			GENDER = "gender", 
			LOCATION = "location",
			PACKING_LIST = "packingList",
			PRODUCT = "product",
			PRODUCT_CATEGORY = "productCategory",
			SALES_ORDER = "salesOrder",	//sales order not to be fetched using common API to avoid sales order being fetched by internal ID. Use field "salesOrderId" instead.
			SEASON = "season",
			SIZE = "size",
			STYLE = "style",
			STOCK_KEEPING_UNIT = "stockKeepingUnit",
			TAX = "tax",
			TRANSPORTER = "transporter",
			UNIT_OF_MEASUREMENT = "unitOfMeasurement";
	
	/**
	 * 
	 * @param entityClassName
	 * @return
	 * @throws Exception 
	 */
	public static Class getClassByName(String entityClassName) throws Exception{
		switch(entityClassName){
			case BANK:				
				return Bank.class;
			case BRAND:
				return Brand.class;
			case COLOR:
				return Color.class;
			case CUSTOMER:
				return Customer.class;
			case EMPLOYEE:
				return Employee.class;
			case FILE:
				return File.class;
			case GENDER:
				return Gender.class;
			case LOCATION:
				return Location.class;
			case PACKING_LIST:
				return PackingList.class;
			case PRODUCT:
				return Product.class;
			case PRODUCT_CATEGORY:
				return ProductCategory.class;
			case SALES_ORDER:
				return SalesOrder.class;
			case SEASON:
				return Season.class;
			case SIZE:
				return Size.class;
			case STYLE:
				return Style.class;
			case STOCK_KEEPING_UNIT:
				return StockKeepingUnit.class;
			case TAX:
				return Tax.class;
			case TRANSPORTER:
				return Transporter.class;
			case UNIT_OF_MEASUREMENT:
				return UnitOfMeasurement.class;
		}
		
		throw new Exception("Class" + entityClassName + "not found");
	}
	/**
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public static JSONObject getEntityUsingId(String entityClassName, long id){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			Object entity = HibernateUtil.getUsingId(getClassByName(entityClassName), id);		
			JSONObject entityJson = HibernateUtil.getJsonFromHibernateEntity(entity);
			resultArray.put(entityJson);				
			resultsJson.put(Application.RESULT, resultArray);
			
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
	public static JSONObject getJsonForAll(String entityClassName){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
			List<Object> entityList = (List<Object>)(Object)HibernateUtil.getAll(getClassByName(entityClassName));			

			Iterator<Object> iterator = entityList.iterator();
			while(iterator.hasNext()){
				Object entity = iterator.next();
				JSONObject entityJson = HibernateUtil.getJsonFromHibernateEntity(entity);
				resultArray.put(entityJson);
			}
			resultsJson.put(Application.RESULT, resultArray);
				
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

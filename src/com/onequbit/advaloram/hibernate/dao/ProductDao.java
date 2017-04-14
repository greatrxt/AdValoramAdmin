package com.onequbit.advaloram.hibernate.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.json.JSONArray;
import org.json.JSONObject;

import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.Brand;
import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.hibernate.entity.ColorCode;
import com.onequbit.advaloram.hibernate.entity.Gender;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.ProductCategory;
import com.onequbit.advaloram.hibernate.entity.Season;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
import com.onequbit.advaloram.hibernate.entity.UnitOfMeasurement;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class ProductDao {	
	
	public static class Tag {
		public static final String STYLE_CODE = "styleCode", SEASON_CODE = "seasonCode", BRAND = "brand", PRODUCT_CATEGORY = "productCategory", UOM = "unitOfMeasurement",
				COLORS = "colors", GENDER_CODES = "genderCodes", SIZE_CODES = "sizeCodes";
		public static final class SKU {
			public static final String EAN_CODE = "eanCode";
		}
	}
	
	/**
	 * 
	 * @param product
	 * @return
	 */
	public static Product getProduct(Product product){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Product.class);
			criteria.add(Restrictions.eq(Tag.STYLE_CODE, product.getStyleCode()).ignoreCase());

			List<Product> list = criteria.list();
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
	 * Get all Products
	 * @return
	 */
	public static JSONObject getAllStyleCodes(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {		
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Product.class);
			criteria.setProjection(Projections.projectionList().add(Projections.property("id")).add(Projections.property("styleCode")));
			List styleCodes = criteria.list();		

			if(styleCodes.size() == 0){
				//No Product found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Style Codes Found");
			} else {
				Iterator iterator = styleCodes.iterator();
				while(iterator.hasNext()){
					JSONObject styleCode = new JSONObject();
					Object[] result = (Object[]) iterator.next();
					
					styleCode.put("id", result[0]);
					styleCode.put("styleCode", result[1]);
					resultArray.put(styleCode);
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
	 * Get all Products
	 * @return
	 */
	public static JSONObject getAllProducts(){
		JSONObject resultsJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		Session session = null;
		try {
						
			List<Product> productsList = (List<Product>)(Object)HibernateUtil.getAll(Product.class);

			if(productsList.size() == 0){
				//No Product found
				resultsJson.put(Application.RESULT, Application.ERROR);
				resultsJson.put(Application.ERROR_MESSAGE, "No Product found");
			} else {
				Iterator<Product> iterator = productsList.iterator();
				while(iterator.hasNext()){
					Product product = iterator.next();
					JSONObject productJson = HibernateUtil.getJsonFromHibernateEntity(product);
					resultArray.put(productJson);
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
	 * @param productJson
	 * @return
	 */
	public static JSONObject createOrUpdateProduct(Long id, JSONObject productJson){
		
		Session session = null;		
		JSONObject result = new JSONObject();
		Product product = null;
		
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			
			if(id < 0){
				product = new Product();
			} else {
				product = session.get(Product.class, id);
			}
			HibernateUtil.setDataFromJson(product, productJson);
			
			if(getProduct(product) != null && id < 0){
				result.put(Application.RESULT, Application.ERROR);
				result.put(Application.ERROR_MESSAGE, "product " + productJson.toString() + " already exists");
			} else {
			
				
				session.beginTransaction();
				
				product.setSeasonCode(session.load(Season.class, Long.parseLong(String.valueOf(productJson.get(Tag.SEASON_CODE)))));
				product.setBrand(session.load(Brand.class, Long.parseLong(String.valueOf(productJson.get(Tag.BRAND)))));
				product.setProductCategory(session.load(ProductCategory.class, Long.parseLong(String.valueOf(productJson.get(Tag.PRODUCT_CATEGORY)))));
				product.setUnitOfMeasurement(session.load(UnitOfMeasurement.class, Long.parseLong(String.valueOf(productJson.get(Tag.UOM)))));
				
				JSONArray colorCodesArray = productJson.getJSONArray(Tag.COLORS);
				HashMap<ColorCode, Color> colors = new HashMap<>();
				for(int c = 0; c < colorCodesArray.length(); c++){
					JSONObject colorJson = colorCodesArray.getJSONObject(c);
					Iterator<String> keys = colorJson.keys();
					while(keys.hasNext()){
						String code = keys.next();
						String colorName = colorJson.getString(code);
						colors.put(ColorCodeDao.getColorCodeUsingColorCodeName(code), ColorDao.getColorUsingColorName(colorName));
					}
				}
				product.setColors(colors);
				
				JSONArray genderArray = productJson.getJSONArray(Tag.GENDER_CODES);
				Set<Gender> genders = new HashSet<>();
				for(int g = 0; g < genderArray.length(); g++){
					genders.add(session.load(Gender.class, Long.parseLong(String.valueOf(genderArray.get(g)))));
				}
				
				product.setGenderCodes(genders);
				JSONArray sizeArray = productJson.getJSONArray(Tag.SIZE_CODES);
				Set<Size> sizes = new HashSet<>();
				for(int s = 0; s < sizeArray.length(); s++){
					sizes.add(session.load(Size.class, Long.parseLong(String.valueOf(sizeArray.get(s)))));
				}
				product.setSizeCodes(sizes);
				if(id < 0){
					product.setRecordCreationTime(SystemUtils.getFormattedDate());
					session.save(product);					
				} else {
					session.update(product);
				}
				
				session.getTransaction().commit();
				
				if(session!=null){
					session.close();
				}
				
				Long idForSku = product.getId();			
				createStockKeepingUnitsFor(idForSku, productJson);
				result.put(Application.RESULT, Application.SUCCESS);	
				result.put(Application.OBJECT_ID, idForSku);
			}	
			
		} catch(Exception e){
			e.printStackTrace();
			result = SystemUtils.generateErrorMessage(e.getMessage());
		}
		
		return result;
	}

	/**
	 * 
	 * @param id
	 * @param productJson
	 */
	private static void createStockKeepingUnitsFor(Long id, JSONObject productJson) {
		
		Session session = null;
		
		try {
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			JSONArray colorCodesArray = productJson.getJSONArray(Tag.COLORS);
			HashMap<ColorCode, Color> colors = new HashMap<>();
			for(int c = 0; c < colorCodesArray.length(); c++){
				JSONObject colorJson = colorCodesArray.getJSONObject(c);
				Iterator<String> keys = colorJson.keys();
				while(keys.hasNext()){
					String code = keys.next();
					String colorName = colorJson.getString(code);
					colors.put(ColorCodeDao.getColorCodeUsingColorCodeName(code), ColorDao.getColorUsingColorName(colorName));
				}
			}
			
			JSONArray genderArray = productJson.getJSONArray(Tag.GENDER_CODES);
			Set<Gender> genders = new HashSet<>();
			for(int g = 0; g < genderArray.length(); g++){
				genders.add(session.load(Gender.class, Long.parseLong(String.valueOf(genderArray.get(g)))));
			}
			
			JSONArray sizeArray = productJson.getJSONArray(Tag.SIZE_CODES);
			Set<Size> sizes = new HashSet<>();
			for(int s = 0; s < sizeArray.length(); s++){
				sizes.add(session.load(Size.class, Long.parseLong(String.valueOf(sizeArray.get(s)))));
			}
			
			
			Product product = session.load(Product.class, id);
			String styleCode = productJson.getString(Tag.STYLE_CODE);
			Set<StockKeepingUnit> skus = new HashSet<>();
			Iterator<ColorCode> colorCodes = colors.keySet().iterator();
			
			while(colorCodes.hasNext()){
				ColorCode colorCode = colorCodes.next();			
				Iterator<Gender> genderCodes = genders.iterator();
				while(genderCodes.hasNext()){				
					Gender genderCode = genderCodes.next();
					
					Iterator<Size> sizeCodes = sizes.iterator();
					while(sizeCodes.hasNext()){
						Size sizeCode = sizeCodes.next();
						StockKeepingUnit sku = new StockKeepingUnit(styleCode, colorCode, genderCode, sizeCode);
						sku.setProduct(product);
						if(StockKeepingUnitDao.getStockKeepingUnit(sku) == null){
							skus.add(sku);
							sku.setRecordCreationTime(SystemUtils.getFormattedDate());
							session.save(sku);
						}
					}
				}
			}	

			product.setStockKeepingUnits(skus);
			session.update(product);
			session.getTransaction().commit();
			
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
	 * @param genders 
	 * @param sizes 
	 * @param colors2 
	 * @param styleCode 
	 * @param id 
	 * @param product
	 * @param session
	 */
/*	private static void createStockKeepingUnitsFor(Long id, String styleCode, HashMap<ColorCode, Color> colors, Set<Size> sizes, Set<Gender> genders) {

		Session session = null;
		try {
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Set<StockKeepingUnit> skus = new HashSet<>();
			Iterator<ColorCode> colorCodes = colors.keySet().iterator();
			while(colorCodes.hasNext()){
				ColorCode colorCode = colorCodes.next();			
				Iterator<Gender> genderCodes = genders.iterator();
				while(genderCodes.hasNext()){				
					Gender genderCode = genderCodes.next();
					
					Iterator<Size> sizeCodes = sizes.iterator();
					while(sizeCodes.hasNext()){
						Size sizeCode = sizeCodes.next();
						StockKeepingUnit sku = new StockKeepingUnit(styleCode, colorCode, genderCode, sizeCode);
						sku.setProduct(session.load(Product.class, product.getId()));
						skus.add(sku);
						session.save(sku);
					}
				}
			}	
			//Product productToUpdate = getProduct(product);
			product.setStockKeepingUnits(skus);
			session.update(productToUpdate);
			session.getTransaction().commit();
		} catch(Exception e){
			throw e;
		} finally {
			if(session!=null){
				session.close();
			}
		}
	}*/
}

package com.onequbit.advaloram.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.onequbit.advaloram.hibernate.dao.ProductDao.Tag;
import com.onequbit.advaloram.hibernate.entity.ColorCode;
import com.onequbit.advaloram.hibernate.entity.Gender;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.hibernate.entity.StockKeepingUnit;
import com.onequbit.advaloram.util.HibernateUtil;

public class StockKeepingUnitDao {
	/**
	 * 
	 * @param product
	 * @return
	 */
	
	public static StockKeepingUnit getStockKeepingUnit(StockKeepingUnit sku){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(StockKeepingUnit.class);
			criteria.add(Restrictions.eq(Tag.SKU.SKU_CODE, sku.getSkuCode()).ignoreCase());

			List<StockKeepingUnit> list = criteria.list();
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
	 * @param product
	 * @return
	 */
	
	public static StockKeepingUnit getStockKeepingUnit(String styleCode, String colorCode, String genderCode, String sizeCode, Long productCategory){

		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			String sku = StockKeepingUnit.generateSkuCode(productCategory, colorCode, styleCode, sizeCode, genderCode);
			Criteria criteria = session.createCriteria(StockKeepingUnit.class);
			criteria.add(Restrictions.eq(Tag.SKU.SKU_CODE, sku).ignoreCase());

			List<StockKeepingUnit> list = criteria.list();
			if(list.size() > 0){
				if(list.size()!=1){
					//something is wrong
					throw new Exception("ERROR - More than entry for EAN Code " + sku + " found");
				}
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
}

package com.onequbit.advaloram.hibernate.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.hibernate.entity.Style;
import com.onequbit.advaloram.util.HibernateUtil;

public class StyleDao {

	/**
	 * Return all available sizes
	 * @return
	 */
	public static List<Size> getAllStyles(){
		Session session = null;
		try {
			
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Style.class);			
			List<Size> list = criteria.list();
			if(list.size() > 0){
				return list;
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
}

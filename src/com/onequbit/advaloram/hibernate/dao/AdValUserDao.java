package com.onequbit.advaloram.hibernate.dao;

import org.hibernate.Session;

import com.onequbit.advaloram.hibernate.entity.AdValUser;
import com.onequbit.advaloram.util.HibernateUtil;

public class AdValUserDao {

	public static AdValUser getUserWithUserId(Long userId){
		Session session = null;	
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			return session.get(AdValUser.class, userId);
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

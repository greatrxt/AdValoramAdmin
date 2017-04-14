package com.onequbit.advaloram.hibernate.dao;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.hibernate.Session;

import com.onequbit.advaloram.hibernate.entity.AbstractAdValoramEntity;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.File;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;
import com.onequbit.advaloram.hibernate.entity.Transporter;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class FileDao {

	public static void makeEntry(String entityClass, Long id, 
									String fileName, String fileUri) {
		Session session = null;		
		
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			File file = new File();
			file.setFileLocation(fileUri);
			String format = fileName.split(Pattern.quote(".")).length > 1 ? fileName.split(Pattern.quote("."))[fileName.split(Pattern.quote(".")).length  - 1]:"";
			file.setFormat(format);
			file.setRecordCreationTime(SystemUtils.getFormattedDate());
			file.setName(fileName);
			file.setVersion(HibernateUtil.DB_VERSION);
			session.save(file);
			session.getTransaction().commit();
			
			
			@SuppressWarnings("unchecked")
			AbstractAdValoramEntity entity = (AbstractAdValoramEntity) session.get(AbstractEntityDao.getClassByName(entityClass), id);
			if(entity!=null){
				if(entity instanceof Product){
					Product product =  ((Product) entity);
					Set<File> associatedFiles = product.getAssociatedFiles(); 
					if(associatedFiles == null){
						associatedFiles = new HashSet<>();
					}
					associatedFiles.add(file);
					product.setAssociatedFiles(associatedFiles);
				} else if(entity instanceof Customer){
					Customer customer =  ((Customer) entity);
					Set<File> associatedFiles = customer.getAssociatedFiles(); 
					if(associatedFiles == null){
						associatedFiles = new HashSet<>();
					}
					associatedFiles.add(file);
					customer.setAssociatedFiles(associatedFiles);
				} else if(entity instanceof Transporter){
					Transporter transporter =  ((Transporter) entity);
					Set<File> associatedFiles = transporter.getAssociatedFiles(); 
					if(associatedFiles == null){
						associatedFiles = new HashSet<>();
					}
					associatedFiles.add(file);
					transporter.setAssociatedFiles(associatedFiles);
				} else if(entity instanceof Employee){
					Employee employee =  ((Employee) entity);
					Set<File> associatedFiles = employee.getAssociatedFiles(); 
					if(associatedFiles == null){
						associatedFiles = new HashSet<>();
					}
					associatedFiles.add(file);
					employee.setAssociatedFiles(associatedFiles);
				} else if(entity instanceof SalesOrder){
					SalesOrder salesOrder =  ((SalesOrder) entity);
					Set<File> associatedFiles = salesOrder.getAssociatedFiles(); 
					if(associatedFiles == null){
						associatedFiles = new HashSet<>();
					}
					associatedFiles.add(file);
					salesOrder.setAssociatedFiles(associatedFiles);
				}
				
				session.beginTransaction();
				session.update(entity);
				session.getTransaction().commit();
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(session!=null){
				session.close();
			}
		}
	}

}

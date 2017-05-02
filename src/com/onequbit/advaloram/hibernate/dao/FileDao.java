package com.onequbit.advaloram.hibernate.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.hibernate.Session;

import com.onequbit.advaloram.hibernate.entity.AbstractAdValoramEntity;
import com.onequbit.advaloram.hibernate.entity.Customer;
import com.onequbit.advaloram.hibernate.entity.Employee;
import com.onequbit.advaloram.hibernate.entity.File;
import com.onequbit.advaloram.hibernate.entity.PackingList;
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
			//file.setVersion(HibernateUtil.DB_VERSION);
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
				} else if(entity instanceof PackingList){
					PackingList packingList = ((PackingList) entity);
					Set<File> associatedFiles = packingList.getAssociatedFiles(); 
					if(associatedFiles == null){
						associatedFiles = new HashSet<>();
					}
					associatedFiles.add(file);
					packingList.setAssociatedFiles(associatedFiles);
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

	private static void removeFile(Set<File> associatedFiles, long fileId){
		Iterator<File> fileIterator = associatedFiles.iterator();
		File fileToRemove = null;
		while (fileIterator.hasNext()) {
			File currentFile = (File) fileIterator.next();
			if(currentFile.getId() == fileId){
				fileToRemove = currentFile;
			}
		}
		if(fileToRemove!=null){
			associatedFiles.remove(fileToRemove);
		}
	}
	
	/**
	 * 
	 * @param entityClass
	 * @param id
	 * @param fileId
	 * @param uploadFolder 
	 * @throws Exception 
	 */
	public static void deleteEntry(String entityClass, Long id, Long fileId, java.io.File uploadFolder) throws Exception {
		
		Session session = null;		
		
		try {	
			session = HibernateUtil.getSessionAnnotationFactory().openSession();
			session.beginTransaction();
			
			@SuppressWarnings("unchecked")
			AbstractAdValoramEntity entity = (AbstractAdValoramEntity) session.get(AbstractEntityDao.getClassByName(entityClass), id);
			if(entity!=null){
				if(entity instanceof Product){
					Product product =  ((Product) entity);
					Set<File> associatedFiles = product.getAssociatedFiles(); 
					if(associatedFiles != null){
						removeFile(associatedFiles, fileId);
						product.setAssociatedFiles(associatedFiles);
					}
				} else if(entity instanceof Customer){
					Customer customer =  ((Customer) entity);
					Set<File> associatedFiles = customer.getAssociatedFiles(); 
					if(associatedFiles != null){
						removeFile(associatedFiles, fileId);
						customer.setAssociatedFiles(associatedFiles);
					}
				} else if(entity instanceof Transporter){
					Transporter transporter =  ((Transporter) entity);
					Set<File> associatedFiles = transporter.getAssociatedFiles(); 
					if(associatedFiles != null){
						removeFile(associatedFiles, fileId);
						transporter.setAssociatedFiles(associatedFiles);
					}
				} else if(entity instanceof Employee){
					Employee employee =  ((Employee) entity);
					Set<File> associatedFiles = employee.getAssociatedFiles(); 
					if(associatedFiles != null){
						removeFile(associatedFiles, fileId);
						employee.setAssociatedFiles(associatedFiles);
					}
				} else if(entity instanceof SalesOrder){
					SalesOrder salesOrder =  ((SalesOrder) entity);	
					Set<File> associatedFiles = salesOrder.getAssociatedFiles(); 
					if(associatedFiles != null){
						removeFile(associatedFiles, fileId);
						salesOrder.setAssociatedFiles(associatedFiles);
					}
				}  else if(entity instanceof PackingList){
					PackingList packingList =  ((PackingList) entity);
					Set<File> associatedFiles = packingList.getAssociatedFiles(); 
					if(associatedFiles != null){
						removeFile(associatedFiles, fileId);
						packingList.setAssociatedFiles(associatedFiles);
					}
				}
				
				session.update(entity);
				session.getTransaction().commit();
				
				session.beginTransaction();
				File file = session.get(File.class, fileId);
				java.io.File fileToDelete = new java.io.File(uploadFolder.getAbsolutePath() + java.io.File.separator + file.getName());
				fileToDelete.delete();
				session.delete(file);
				session.getTransaction().commit();
				
			}
		} catch(Exception e){
			throw e;
		} finally {
			if(session!=null){
				session.close();
			}
		}
	}

}

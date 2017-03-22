package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.onequbit.advaloram.hibernate.dao.SizeDao;
import com.onequbit.advaloram.hibernate.entity.Brand;
import com.onequbit.advaloram.hibernate.entity.Color;
import com.onequbit.advaloram.hibernate.entity.Location;
import com.onequbit.advaloram.hibernate.entity.Product;
import com.onequbit.advaloram.hibernate.entity.Size;
import com.onequbit.advaloram.util.HibernateUtil;
import com.onequbit.advaloram.util.SystemUtils;

public class RunIt {

	public static void main() {
		System.out.println("Hibernate many to many (Annotation)");
		Session session = HibernateUtil.getSessionAnnotationFactory().openSession();

		session.beginTransaction();

		Product stock = new Product();
        stock.setProductName("MyJeans");
        stock.setEanCode("5894375876578");        
        
        stock.setBrand(session.load(Brand.class, Long.parseLong("1")));
        
        Set<Size> sizes = new HashSet<Size>();
        sizes.add(session.load(Size.class, Long.parseLong("2")));
        sizes.add(session.load(Size.class, Long.parseLong("5")));
        
        HashMap<String, Color> colors = new HashMap<>();
        colors.put("C1", session.load(Color.class, Long.parseLong("1")));
        colors.put("C2", session.load(Color.class, Long.parseLong("3")));
        
        //stock.setColor(colors);
        stock.setRecordCreationTime(SystemUtils.getFormattedDate());
        stock.setLastUpdate(SystemUtils.getFormattedDate());
        stock.setVersion(0);
        
        session.save(stock);

		session.getTransaction().commit();
		System.out.println("Done");

	}

}

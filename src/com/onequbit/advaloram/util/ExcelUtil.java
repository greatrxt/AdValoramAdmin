package com.onequbit.advaloram.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.onequbit.advaloram.application.Application;
import com.onequbit.advaloram.hibernate.entity.SalesOrder;

public class ExcelUtil {

	public static File getSalesOrderReport(List<SalesOrder> salesOrders){
	    
	    try {
	    	Workbook wb = new XSSFWorkbook();
	    	File basicReportFile = new File(Application.getTempFolder() + File.separator + "sales_orders_basic.xlsx");
	    	FileOutputStream fileOut = new FileOutputStream(basicReportFile);
	    	
	    	CreationHelper createHelper = wb.getCreationHelper();
	        Sheet sheet = wb.createSheet("Sheet 1");

	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("Sales Order ID");
	        header.createCell(1).setCellValue("Customer Name");
	        header.createCell(2).setCellValue("Date");
	        header.createCell(3).setCellValue("Status");
	        
	        Iterator<SalesOrder> iterator = salesOrders.iterator();
	        int i = 0;
	        while(iterator.hasNext()){
	        	SalesOrder salesOrder = iterator.next();
		        // Create a row and put some cells in it. Rows are 0 based.
		        Row row = sheet.createRow((short) ++i);

		        row.createCell(0).setCellValue(salesOrder.getSalesOrderId());
		        row.createCell(1).setCellValue(createHelper.createRichTextString(salesOrder.getClientNameOnSalesOrderDate()));
		        row.createCell(2).setCellValue(createHelper.createRichTextString(salesOrder.getSalesOrderDate().toString()));
		        row.createCell(3).setCellValue(createHelper.createRichTextString(salesOrder.getStatus().toString()));
	        }
	    	
			wb.write(fileOut);
			fileOut.close();	
			wb.close();
			return basicReportFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		return null;    
	}
}

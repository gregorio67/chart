package dymn.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class ExcelXlsView extends  AbstractXlsView {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelXlsView.class);
	
	private static final String _nullDefault = " ";
	private String nullDefault;
	private int colWidth;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> excelData = (Map<String, Object>)model.get("excelData");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> header = (Map<String, Object>) excelData.get("header");
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) excelData.get("data");
		
		HSSFSheet sheet = (HSSFSheet) workbook.createSheet(header.get("sheetName") != null ? String.valueOf(header.get("sheetName")) : "Sheet1");
		sheet.setDefaultColumnWidth(colWidth);

        Font font = workbook.createFont();
        font.setFontName("Arial");

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderTop(BorderStyle.THIN);
		headerStyle.setBorderBottom(BorderStyle.THIN);
		headerStyle.setBorderLeft(BorderStyle.THIN);
		headerStyle.setBorderRight(BorderStyle.THIN);
		headerStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.index);
		headerStyle.setFont(font);
		
		CellStyle dataStyle = workbook.createCellStyle();
		dataStyle.setBorderTop(BorderStyle.THIN);
		dataStyle.setBorderBottom(BorderStyle.THIN);
		dataStyle.setBorderLeft(BorderStyle.THIN);
		dataStyle.setBorderRight(BorderStyle.THIN);
		dataStyle.setFont(font);
		
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
//        font.setBoldweight(HSSFFont.COLOR_NORMAL..BOLDWEIGHT_BOLD);
        
        HSSFRow headerRow = sheet.createRow(0);
        
        /** Create Excel Header  **/ 
        String[] headerNames = (String[]) header.get("headerNames");
        int col = 0;
        for (String headerName : headerNames) {
            headerRow.createCell(col).setCellValue(headerName);
            headerRow.getCell(col).setCellStyle(headerStyle);
            col++;
        }
        
        /** Create Excel Data **/
        int idx = 1;
        
        for (Map<String, Object> map : data) {
        	Iterator<String> itr = map.keySet().iterator();
        	HSSFRow aRow = sheet.createRow(idx++);
        	col = 0;
        	while(itr.hasNext()) {
        		String key = itr.next();
        		aRow.createCell(col).setCellValue(map.get(key) != null ? String.valueOf(map.get(key)): nullDefault);
        		aRow.getCell(col).setCellStyle(dataStyle);
        		col++;
        	}
        	
        }
        
        String fileName = header.get("fileName") != null ? String.valueOf(header.get("fileName")):"download.xls";
        LOGGER.info("{} file successfully downloaded", fileName);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	}

	public void setNullDefault(String nullDefault) {
		if (nullDefault == null) {
			this.nullDefault = _nullDefault;
		}
		else {
			this.nullDefault = nullDefault;			
		}
	}

	public void setColWidth(int colWidth) {
		this.colWidth = colWidth;
	}
}

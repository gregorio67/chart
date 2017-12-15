package dymn.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	public <T> List<T> readExcel(String fileName, Class<T> clazz) throws Exception {
		InputStream is = new FileInputStream(fileName);
		return readExcel(is, clazz);
	}
	
	
	public <T> List<T> readExcel(InputStream is, Class<T> clazz) throws Exception {
		Workbook workbook = null;
		List<T> result = new ArrayList<T>();

		try {
			workbook = new XSSFWorkbook(is);
			int sheetCnt = workbook.getNumberOfSheets();
			int loop = 0;
			while(loop < sheetCnt) {
				Sheet sheet = workbook.getSheetAt(loop);
				for (Row row : sheet) {
//					int cellCnt = row.get
				}
				loop++;
			}
			
		}
		catch(Exception ex) {
			
		}
		finally {
			workbook.close();
		}
		
		workbook.close();
		
		return result;
		
	}
	
	public static Workbook getWorkBook(String fileName) throws Exception {
		FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        
        Workbook wb = null;
        
        /*
         * 파일의 확장자를 체크해서 .XLS 라면 HSSFWorkbook에
         * .XLSX라면 XSSFWorkbook에 각각 초기화 한다.
         */
        if(fileName.toUpperCase().endsWith(".XLS")) {
            try {
                wb = new HSSFWorkbook(fis);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        else if(fileName.toUpperCase().endsWith(".XLSX")) {
            try {
                wb = new XSSFWorkbook(fis);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        
        return wb;
        
	}
}


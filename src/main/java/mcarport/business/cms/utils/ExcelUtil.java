package mcarport.business.cms.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelUtil {
	
	
	public static void outputExcel(String title, String[] headers,
	            List<Map<String, Object>> diaochas,HttpServletResponse response
	           ) throws IOException {
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        //createSheet(excel工作表名)
	        HSSFSheet sheet = workbook.createSheet(title);
	        //下面是设置excel表中标题的样式
	        HSSFCellStyle title_style = workbook.createCellStyle();
	        title_style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
	        title_style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        title_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        title_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        title_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        title_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        title_style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        HSSFFont title_font = workbook.createFont();
	        title_style.setFont(title_font);
	        //内容的样式
	        HSSFCellStyle content_style = workbook.createCellStyle();
	        content_style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
	        content_style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	        content_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	        content_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	        content_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	        content_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        content_style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	        content_style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	        HSSFFont content_font = workbook.createFont();
	        content_style.setFont(content_font);
	        //填充标题内容
	        HSSFRow row = sheet.createRow(0);
	        for (int i = 0; i < headers.length; i++) {
	            //设置标题的宽度自适应
	            sheet.setColumnWidth(i, headers[i].getBytes().length * 2 * 256);
	            HSSFCell cell = row.createCell(i);
	            cell.setCellStyle(title_style);
	            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	            cell.setCellValue(text);
	        }
	        if(null!=diaochas && !diaochas.isEmpty()){
	        	for (int i = 0; i < diaochas.size(); i++) {
	        		row = sheet.createRow(i + 1);
	        		Map<String,Object> datas = diaochas.get(i);
	        		
	        		int  j = 0;
	        		for (Entry<String, Object> entry : datas.entrySet()) {
	        			if(!entry.getKey().equals("isvalid")){
	        				HSSFCell cell = row.createCell(j++);
	        				cell.setCellStyle(content_style);
	        				HSSFRichTextString richString = new HSSFRichTextString(
	        						null== entry.getValue()?"":entry.getValue().toString());
	        				cell.setCellValue(richString);
	        			}
	        		}
	        	}
	        }
	        response.setContentType("application/vnd.ms-excel");
	   String    downloadFileName = new String(URLDecoder.decode(title,"UTF-8").getBytes(),"ISO8859-1");
	        response.setHeader("Content-disposition", "attachment;filename="
	                + downloadFileName + ".xls");
	        OutputStream ouputStream = response.getOutputStream();
	        workbook.write(ouputStream);
	        ouputStream.flush();
	        ouputStream.close();
	    }
	
}


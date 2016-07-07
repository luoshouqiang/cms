package mcarport.business.cms.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.util.StringUtils;

public class OrderUtils {
	
	/**
	 *  生成线下码
	 * @return
	 */
	public static  String generateOffline(){
		StringBuffer sb = new StringBuffer(System.currentTimeMillis()+"");
		sb.reverse();
		return sb.substring(0, 10);
	}
	
	
	public  static  Double  calcPrice(Double price ,int monthes){
		BigDecimal b1 = new BigDecimal(price);
		BigDecimal b2 = new BigDecimal(monthes);
		return  b1.multiply(b2).doubleValue();
	}
	
	public static  class Pattern{
		public static  String  YMD  = "yyyy-MM-dd";
		
		public static  String  YMDHMS  = "yyyy-MM-dd HH:mm:ss";
	}
	
	public static Calendar toDate(String source){
		return toDate(source,Pattern.YMD);
	}
	
	public static Calendar toDate(String source,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		
		if(StringUtils.isEmpty(source)){
			return cal;
		}
		
		try {
			cal.setTime(sdf.parse(source));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return cal;
	}
	
	public static  String toStrDate(Calendar cal){
		 return toStrDate(cal,Pattern.YMD);
	}
	
	public static  String toStrDate(Calendar cal,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String strTime = sdf.format(new Date(cal.getTimeInMillis()));
		return strTime;
	}
	
	public  static void  addDays(Calendar cal,int days){
		cal.add(Calendar.DATE, days);
	}
	
public  static Calendar  getCalendarByPros(String pros){
		
		Calendar cla = Calendar.getInstance();
		if(pros.equalsIgnoreCase("curend")){//这个月月末
			cla.set(Calendar.DATE, 1);//当月的一号
			int curMonth = cla.get(Calendar.MONTH);
			cla.set(Calendar.MONTH, curMonth+1);//下个月的一号
			cla.add(Calendar.DATE, -1);
		}
		else  if(pros.equalsIgnoreCase("preend")){//上个月的月末
			cla.set(Calendar.DATE, 1);//当月的一号
			cla.add(Calendar.DATE, -1);
			
		}
		return cla;
		
	}


	public static String randomHMS(){
		
		Random r = new Random();
		int hour =  r.nextInt(15)+6;
		StringBuilder time = new StringBuilder();
		genneratePrefix(hour, time);
		time.append(":");
		int minute =  r.nextInt(60);
		genneratePrefix(minute, time);
		time.append(":");
		int second =  r.nextInt(59);
		genneratePrefix(second, time);
		return time.toString();
	}


	private static void genneratePrefix(int num, StringBuilder time) {
		if(num<10){
			time.append("0"+num);
		}else{
			time.append(""+num);
		}
	}


	public static String getPrefix(int floor){
		String temp_cus_code = "";
		if( floor == 2) {temp_cus_code = "A2-" ;
		}else if (   floor == 1){ temp_cus_code = "A1-";
		}else if(     floor == 0){
			temp_cus_code = "B0-";
		}
		else if (    floor == -1){
			temp_cus_code = "B1-";

		}else if( floor == -2){
			temp_cus_code = "B2-";
		}else if(floor == -3){
			temp_cus_code = "B3-";
		}else{
			temp_cus_code = "-1";
		}
	    return temp_cus_code;
	     
	}


public static void main(String[] args) {
	
System.out.println( randomHMS());
	
}

	
}

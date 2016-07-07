package mcarport.business.cms.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Controller 
@RequestMapping("/test")  
public class TestController {
	
	public  class  Student{
		
		private String name;
		
		private String no;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}
		
		
	}
	
	public static void main(String[] args) {
		
		
		
		
		
		String json = "{{\"name\":\"lulup\",\"no\":\"3\"},{\"name\":\"lulup34\",\"no\":\"2\"}}";
		
		Gson g = new Gson();
//		List<TestController.Student> s =  g.fromJson(json,  new TypeToken<List<Student>>() {}.getType());  
//		System.out.println(s.get(1).getName());
		System.out.println(System.getProperty("user.dir"));
		
	}
	
}

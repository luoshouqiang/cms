package mcarport.business.cms.action;

import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
  public class  CommonInterceptor implements HandlerInterceptor
  {  
  
    private Logger LOG = LoggerFactory.getLogger(HandlerInterceptor.class);
      
    public CommonInterceptor() {  
    }  
  
  
    /** 
     * 在业务处理器处理请求之前被调用 
     * 如果返回false 
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     *  
     * 如果返回true 
     *    执行下一个拦截器,直到所有的拦截器都执行完毕 
     *    再执行被拦截的Controller 
     *    然后进入拦截器链, 
     *    从最后一个拦截器往回执行所有的postHandle() 
     *    接着再从最后一个拦截器往回执行所有的afterCompletion() 
     */  
    @Override  
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception {  
    	if(handler instanceof HandlerMethod){
    		HttpSession session = request.getSession(false);
    		String loginName = "";
    		if(null!=session){
    			Object  obj = session.getAttribute("loginName");
    			if(null!=obj){
    				loginName = obj.toString();
    			}
    		}
    		
    		String path =  request.getServletPath();
    		if(! StringUtils.isEmpty(path) && path.equals("/user/upload") ){
    			return true;
    		}
    		
    		
    		Enumeration<String> params = request.getParameterNames();
    		HandlerMethod method = (HandlerMethod) handler;
    		StringBuilder log = new StringBuilder();
    		log.append("用户"+loginName);
    		log.append("调用方法"+ method.getMethod().getDeclaringClass() + "."+ method.getMethod().getName());
    		log.append("参数");
    		if(params.hasMoreElements()){
    			while(params.hasMoreElements()){
    				String key =  params.nextElement();
    				log.append("[" + key+ ":"+request.getParameter(key).toString()+"]");
    			}
    		}else{
    			InputStream is = request.getInputStream();
    			byte[] buffer = new byte[1024];
    			int len = -1;
    			while( -1 != (len =  is.read(buffer, 0, 1024))  ){
    				log.append(new String(buffer,0,len,"UTF-8"));
    			}
    			
    		}
    		LOG.info(log.toString());
    	}
    	
        return true;  
    }  
  
    @Override  
    public void postHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler,  
            ModelAndView modelAndView) throws Exception {  
    }  
  
     
    @Override  
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object handler, Exception ex)  
            throws Exception {  
    	
    	
    }  
  
} 


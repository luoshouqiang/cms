package mcarport.business.cms.action;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.poifs.property.Child;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginFilter implements Filter{
    
    private static final Logger LOG = LoggerFactory.getLogger(LoginFilter.class);
//    
//    @Autowired
//    private UserLoginService    userLoginService;
    


	public void destroy() {
    }
    
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest servletRequest=(HttpServletRequest)request;
        HttpServletResponse servletResponse=(HttpServletResponse)response;
        ServletRequest  requestWrapper = new MAPIHttpServletRequestWrapper((HttpServletRequest) request); 
    	String servletPath = servletRequest.getServletPath().toLowerCase();
    	
    	if(servletPath.contains("user/upload")){
    		chain.doFilter(request, response);
    		return ;
    	}
    	
    	if (servletPath.endsWith("login.html")||servletPath.endsWith(".css")||servletPath.endsWith("/login")
    			||servletPath.endsWith(".js")||servletPath.endsWith(".png")||servletPath.endsWith(".jpg") ||
    			servletPath.contains("cbdhtml") || servletPath.contains("outer")
    			) {
    		chain.doFilter(requestWrapper, response);
            return ;
    	}
//        
//        LOG.debug("-----------filter page \"" + servletPath + "\"in LoginInterceptor-----------");
//       
        HttpSession session=servletRequest.getSession(false);
         if(session==null){
        	 servletResponse.sendRedirect(((HttpServletRequest)request).getContextPath() + "/html/login.html?type=1");         
         }else{
        	 chain.doFilter(requestWrapper, response);
         }
        
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
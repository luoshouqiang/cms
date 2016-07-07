package mcarport.business.cms.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.poi.util.IOUtils;

public class MAPIHttpServletRequestWrapper extends HttpServletRequestWrapper {  
   
	private ByteArrayOutputStream cachedBytes;
	 public MAPIHttpServletRequestWrapper(HttpServletRequest request) {
	 super(request);
	 }
	 @Override
	 public ServletInputStream getInputStream() throws IOException {
	 if (cachedBytes == null)
	  cacheInputStream();
	  return new CachedServletInputStream();
  
} 
	 
	 
	 private void cacheInputStream() throws IOException {
		 /* Cache the inputstream in order to read it multiple times. For
		  * convenience, I use apache.commons IOUtils
		  */
		 cachedBytes = new ByteArrayOutputStream();
		 IOUtils.copy(super.getInputStream(), cachedBytes);
		 }
		 /* An inputstream which reads the cached request body */
		 public class CachedServletInputStream extends ServletInputStream {
		 private ByteArrayInputStream input;
		 public CachedServletInputStream() {
		  input = new ByteArrayInputStream(cachedBytes.toByteArray());
		 }
		 @Override
		 public int read() throws IOException {
		  return input.read();
		 }
		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void setReadListener(ReadListener readListener) {
			// TODO Auto-generated method stub
			
		}
		 } 
	 
}
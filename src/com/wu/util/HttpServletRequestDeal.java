/**
 * @ c2015-2025 All right reserve by WUDeHong
 */
package com.wu.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author WDH
 * @Date 2016-1-5
 * 
 */
public class HttpServletRequestDeal extends HttpServletRequestWrapper{

	private String encode;
	
	public HttpServletRequestDeal(HttpServletRequest request,String encode) {
		super(request);
		this.encode = encode;
	}
	
	@Override
	public String getParameter(String name) {
		String result = super.getParameter(name);
		try {
			result = new String(result.getBytes("ISO-8859-1"),encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}

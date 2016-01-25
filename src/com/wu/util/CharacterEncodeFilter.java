/**
 * @ c2015-2025 All right reserve by WUDeHong
 */
package com.wu.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WDH
 * @Date 2016-1-5
 * 
 */
public class CharacterEncodeFilter implements Filter{

	private String encode;
	public void destroy() {
	}
	
	public void doFilter(ServletRequest srequest, ServletResponse sresponse,
			FilterChain filterChain) throws IOException, ServletException {
		if(encode!=null){
			HttpServletRequest request = (HttpServletRequest) srequest;
			HttpServletResponse response = (HttpServletResponse) sresponse;
			request.setCharacterEncoding(encode);
			response.setCharacterEncoding(encode);
			filterChain.doFilter(request, response);
			if(request.getMethod().equalsIgnoreCase("get")){
				request = new HttpServletRequestDeal(request, encode);
			}
		}
		
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
		encode = filterConfig.getInitParameter("encode");
	}

}

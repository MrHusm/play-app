package com.play.base.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


public class AppUtil {
	private static ThreadLocal<HttpServletRequest> requestMap = new ThreadLocal<HttpServletRequest>();

	private static ThreadLocal<HttpServletResponse> responseMap = new ThreadLocal<HttpServletResponse>();

	public static HttpServletRequest getRequest() {
		return requestMap.get();
	}

	public static HttpServletResponse getResponse() {
		return responseMap.get();
	}

	public static void setHttp(HttpServletRequest request,
			HttpServletResponse response) {
		requestMap.set(request);
		responseMap.set(response);
	}

	public static HttpSession getSession() {
		return requestMap.get().getSession();
	}

//	public static User getUser() {
//		return (User) getSession().getAttribute("user");
//	}

	public static boolean isEn() {
		boolean flag = false;
		Object isEn = getSession().getAttribute("isEn");
		if (isEn != null)
			flag = Boolean.parseBoolean(isEn.toString());
		return flag;
	}

	public static void removeAll() {
		requestMap.remove();
		responseMap.remove();
	}
	
	public static Object getObjectFromApplication(ServletContext servletContext,
			String beanName) {
			ApplicationContext application = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			return application.getBean(beanName);
	}

	/**
	 * 设置cookie
	 * @param response
	 * @param name  cookie名字
	 * @param value cookie值
	 * @param maxAge cookie生命周期  以秒为单位
	 */
	public static void addCookie(HttpServletResponse response,String name,String value,int maxAge){
		Cookie cookie = new Cookie(name,value);
		cookie.setPath("/");
		if(maxAge>0)  cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static String getCookieValueByName(HttpServletRequest request,String name){
		Map<String,Cookie> cookieMap = readCookieMap(request);
		if(cookieMap.containsKey(name)){
			Cookie cookie = (Cookie)cookieMap.get(name);
			return cookie.getValue();
		}else{
			return null;
		}
	}

	/**
	 * 从request中获取值，并放入cookie
	 * @param request
	 * @param response
	 * @param name
	 * @param maxAge
	 * @return
	 */
	public static String getValueAndSetCookie(HttpServletRequest request,HttpServletResponse response,String name,int maxAge){
		String value = request.getParameter(name);
		if(StringUtils.isBlank(value)){
			value = AppUtil.getCookieValueByName(request, name);
		}else{
			AppUtil.addCookie(response,name,value,maxAge);
		}
		return value;
	}

	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> readCookieMap(HttpServletRequest request){
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
		Cookie[] cookies = request.getCookies();
		if(null!=cookies){
			for(Cookie cookie : cookies){
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

}

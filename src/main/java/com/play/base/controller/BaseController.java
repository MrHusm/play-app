package com.play.base.controller;

import com.play.base.utils.ResultResponse;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController{

	protected static final String TOKEN = "token";

	protected ResultResponse resultResponse = new ResultResponse();

	public Long getUserId(){
		return 1L;
	}


	/**
	 * 
	 * Description: 灏嗕竴涓? JavaBean 瀵硅薄杞寲涓轰竴涓?  Map
	 * @Version1.0 2014骞?11鏈?15鏃? 涓嬪崍5:18:22 by 寮犲鏂岋紙zhangxianbin@dangdang.com锛夊垱寤?
     * @param bean 瑕佽浆鍖栫殑JavaBean 瀵硅薄
     * @return 杞寲鍑烘潵鐨?  Map 瀵硅薄
     * @throws IntrospectionException 濡傛灉鍒嗘瀽绫诲睘鎬уけ璐?
     * @throws IllegalAccessException 濡傛灉瀹炰緥鍖? JavaBean 澶辫触
     * @throws InvocationTargetException 濡傛灉璋冪敤灞炴?х殑 setter 鏂规硶澶辫触
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map<String, Object> convertBeanToMap(Object bean){ 
		Map<String, Object> returnMap = new HashMap<String, Object>(); 
		if(bean == null){
			return returnMap;
		}
		if(bean instanceof Map){
			return (Map<String, Object>)bean;
		}
        Class type = bean.getClass(); 
        try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type); 
			PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors(); 
			for (int i = 0; i< propertyDescriptors.length; i++) { 
			    PropertyDescriptor descriptor = propertyDescriptors[i]; 
			    String propertyName = descriptor.getName(); 
			    if (!propertyName.equals("class")) { 
			        Method readMethod = descriptor.getReadMethod(); 
			        Object result = readMethod.invoke(bean, new Object[0]); 
			        if (result != null) { 
			        	if(result instanceof String && StringUtils.isEmpty(result.toString())){
			        		continue;
			        	}
			            returnMap.put(propertyName, result); 
			        }
			    } 
			}
		} catch (Exception e) {
			throw new RuntimeException("灏? JavaBean 杞寲涓? Map澶辫触锛?");
		}
        return returnMap; 
    } 
	
	/**
	 * Description:灏唕equest鏁版嵁灏佽鎴怣ap銆傚弬鏁板悕浣滀负key锛屽弬鏁板?间綔涓簐alue銆? 
	 * @Version1.0 2015-6-25 涓婂崍10:39:00 by 鍌呬綔榄侊紙fuzuokui@dangdang.com锛夊垱寤?
	 * @param request
	 * @return
	 */
	public Map<String, Object> getMap( HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String paramName;
		Enumeration<?> e = request.getParameterNames();
		while ( e.hasMoreElements()) {
			paramName = e.nextElement().toString();
			String[] paramValues = request.getParameterValues( paramName);
			if ( paramValues != null && paramValues.length == 1){
				String value = request.getParameter(paramName);
				if(StringUtils.isNotBlank(value)) {
					map.put( paramName, value);
				}
			} else {
				map.put( paramName, request.getParameterValues( paramName));
			}
		}
		return map;
	}

}

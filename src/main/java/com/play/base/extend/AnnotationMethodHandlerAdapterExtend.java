package com.play.base.extend;

import com.play.base.utils.AppUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("deprecation")
public class AnnotationMethodHandlerAdapterExtend extends
		AnnotationMethodHandlerAdapter {
	@Override
	protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		AppUtil.setHttp(request, response);
		ModelAndView mav = super.invokeHandlerMethod(request, response, handler);
		AppUtil.removeAll();
		return mav;
	}
}

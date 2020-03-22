package com.play.interceptor;

import com.play.base.contants.Constants;
import com.play.base.exception.MediaSecurityException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SecurityInterceptor implements HandlerInterceptor{

	private List<String> excludedUrls;
	public static boolean interceptLoginEnabled = true;


	public void setExcludedUrls(List<String> excludedUrls) {
		this.excludedUrls = excludedUrls;
	}

    @Override
    public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
    	
    	if(request.getSession().getAttribute("interceptLoginEnabled") == null){
    		request.getSession().setAttribute("interceptLoginEnabled", interceptLoginEnabled);
    	}
    	
    	if(!interceptLoginEnabled){
    		return true;
    	}
    	
        String requestUri = request.getRequestURI();
        for (String url : excludedUrls) {
            if (requestUri.startsWith(url)) {
                return true;
            }
        }
		Object UserCms =  request.getSession().getAttribute(Constants.CMS_USER_INFO_STORED_IN_SESSION);

        if (UserCms == null) {
            throw new MediaSecurityException();
			//return false;
        } else {
            return true;
        }
    }

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}
	
}

package com.play.base.interceptor;

import com.alibaba.fastjson.JSON;
import com.play.base.utils.ResultMessage;
import com.play.base.utils.ResultResponse;
import com.play.ucenter.model.User;
import com.play.ucenter.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IUserService userService;

    private List<String> excludedUrls;

    public void setExcludedUrls(List<String> excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 同意AJAX进行跨域请求时的预检，兼容angular
        if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }

        String requestUri = request.getRequestURI();
        for (String url : excludedUrls) {
            if (requestUri.startsWith(url)) {
                return true;
            }
        }

        String token = request.getHeader("x-auth-token");
        if (!StringUtils.isEmpty(token)) {
            User user = userService.get(1L);
            return true;
        }
        //如果是接口请求，返回json数据
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(ResultMessage.F4010.getCode());
        response.getWriter().write(JSON.toJSONString((new ResultResponse().failure(ResultMessage.F4010))));
        response.getWriter().flush();
        return false;
    }
}

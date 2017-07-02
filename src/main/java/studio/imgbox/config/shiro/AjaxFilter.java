package studio.imgbox.config.shiro;

import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * shiro的ajax登录拦截器 
 * 重新onLoginSuccess方法，防止302跳转
 * @auth liuruiyang
 * @date 2017-07-02
 */
public class AjaxFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"))) { // 不是ajax请求
			issueSuccessRedirect(request, response);
		} else {
			// 直接输出json文件 而不是302跳转
			httpServletResponse.setCharacterEncoding("UTF-8");
			PrintWriter out = httpServletResponse.getWriter();
			out.println("{\"msg\":\"ajax\",\"ali\":\"true\"}");
			out.flush();
			out.close();
		}
		return false;
	}
	
}

package studio.imgbox.config.shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

/**
 * Shiro 配置
 */
@Configuration
public class ShiroConfig {


	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。
	 * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在 初始化ShiroFilterFactoryBean的时候需要注入
	 * SecurityManager
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 登录页面链接和表单链接必须一致
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/");
		shiroFilterFactoryBean.setUnauthorizedUrl("/assets/ep/403.html");

		// 拦截器 filterChainDefinitionMap
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
		filterChainDefinitionMap.put("/logout", "logout");

		filterChainDefinitionMap.put("/action/**", "authc");
		filterChainDefinitionMap.put("/view/**", "authc");
		
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		// 添加自定义拦截器
		Map<String, Filter> map = new HashMap<String, Filter>();
		map.put("ajax", ajaxFilter()); // 自定义拦截器覆盖了FormAuthenticationFilter登录拦截器所用的拦截器名authc
		shiroFilterFactoryBean.setFilters(map); // 添加自定义拦截器

		return shiroFilterFactoryBean;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 将shiroRealm注入到securityManager中
		securityManager.setRealm(shiroRealm());
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**
	 * 别忘记注入自己写的Realm
	 * @return
	 */
	@Bean
	public ShiroRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		// 注入凭证器
		shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return shiroRealm;
	}

	/**
	 * Session
	 * @return
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(10L * 60000L); // 十分钟不活动自动退出
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;
	}

	/**
	 * 凭证匹配器
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5"); // 散列算法:这里使用MD5算法
		return hashedCredentialsMatcher;
	}

	/**
	 * 自定义拦截器
	 * @return
	 */
	@Bean
	public AjaxFilter ajaxFilter() {
		return new AjaxFilter();
	}
}

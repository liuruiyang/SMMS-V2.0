package studio.imgbox.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import studio.imgbox.domain.User;
import studio.imgbox.service.UserService;

/**
 * 自定义一个身份校验类 shiro的认证最终由Realm进行执行
 */
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验

		String username = (String) token.getPrincipal();

		User user = userService.getInfoLogin(username);

		if (null != user) {
			if ("true".equals(user.getUserStatus())) {
				SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserEmail(),
						user.getUserPassword(), getName());
				clearCache(info.getPrincipals());
				return info;
			} else if("false".equals(user.getUserStatus())) {
				throw new DisabledAccountException("帐号未激活");
			} else if("dead".equals(user.getUserStatus())){
				throw new LockedAccountException("帐号已失效");
			}
		}

		return null;

	}

}

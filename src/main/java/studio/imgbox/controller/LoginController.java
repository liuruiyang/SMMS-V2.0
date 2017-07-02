package studio.imgbox.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import studio.imgbox.config.properties.GlobalProperties;
import studio.imgbox.domain.User;
import studio.imgbox.service.MailService;
import studio.imgbox.service.UserService;
import studio.imgbox.utils.TmtUtil;
import studio.imgbox.utils.encryption.Md5;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailService mailService;
	@Autowired
	private GlobalProperties globalDefine;

	@RequestMapping("/")
	public String index(Model model) {
		if (!StringUtils.isEmpty(TmtUtil.loginPrincipal())) {
			model.addAttribute("ali", true);
			model.addAttribute("usr", TmtUtil.loginEmail());
			return "upload";
		}
		model.addAttribute("ali", false);
		return "upload";
	}

	// 登录页面链接和登录表单链接必须一致
	@GetMapping("/login")
	public String login() {
		return "redirect:/";
	}
	
	@PostMapping("/login")
	@ResponseBody
	public String loginAction(HttpServletRequest req) {

		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "重复登录";
		boolean ali = false;

		String exception = (String) req.getAttribute("shiroLoginFailure");
		if (exception != null) {
			if (UnknownAccountException.class.getName().equals(exception)) {
				msg = "账号不存在";
			} else if (IncorrectCredentialsException.class.getName().equals(exception)) {
				msg = "密码不正确";
			} else if (DisabledAccountException.class.getName().equals(exception)) {
				msg = "帐号未激活";
			} else if (LockedAccountException.class.getName().equals(exception)) {
				msg = "帐号已失效";
			} else {
				msg = "登录失败";
			}
		} else {
			ali = true;
		}

		map.put("msg", msg);
		map.put("ali", ali);

		return new Gson().toJson(map);
	}

	@RequestMapping("/register")
	@ResponseBody
	public String register(HttpServletRequest req) {
		
		if("false".equals(globalDefine.getAllowRegister())) {
			return TmtUtil.rest("false", "你也许需要一个邀请码,请联系管理员");
		}
		
		String username = req.getParameter("username");
		String password1 = req.getParameter("password1");
		String password2 = req.getParameter("password2");

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password1) || StringUtils.isEmpty(password2)) {
			return TmtUtil.rest("false", "参数错误:NULL");
		} else if (!TmtUtil.isEmail(username)) {
			return TmtUtil.rest("false", "参数错误:EMAIL_ILLEGAL");
		} else if (!password1.equals(password2)) {
			return TmtUtil.rest("false", "参数错误:PASSWORD_NOT_REPEAT");
		} else if (password1.length() != 32) {
			return TmtUtil.rest("false", "参数错误:PASSWORD_ILLEGAL");
		} else if (null != userService.getInfoLogin(username)) { // 如果邮箱已经在数据库了
			User user = userService.getInfosByEmail(username);
			if ("false".equals(user.getUserStatus()) && user.getUserBlack() < 3) {
				return TmtUtil.rest("false", "参数错误:EMAIL_USED_WAITING_CONFIRM");
			}
			if ("dead".equals(user.getUserStatus()) && user.getUserBlack() >= 3) {
				return TmtUtil.rest("false", "参数错误:EMAIL_TIMES_BANNED");
			}
			if ("dead".equals(user.getUserStatus()) && user.getUserBlack() < 3) { // 可以注册的另一种情况
				user.setUserStatus("false");
				user.setUserTime(new Date());
				try {
					user.setUserPassword(Md5.Bit32(password1));
				} catch (Exception e) {
					return TmtUtil.rest("false", "内部错误:PASSWORD_ILLEGAL_ENCRYPTION");
				}
				int cow = userService.updateRegister(user);

				if (cow > 0) {
					try {
						String tokenUrl = TmtUtil.cTokenFalse(user);
						String code = mailService.sendMail(user.getUserId(), tokenUrl, username);
						if ("200".equals(code)) {
							return TmtUtil.rest("true", "激活邮件发送成功,请前往邮箱激活");
						} else {
							return TmtUtil.rest("false", "激活邮件发送失败,请联系管理员");
						}
					} catch (Exception e) {
						return TmtUtil.rest("false", "激活邮件生成失败,请联系管理员");
					}
				}
				return TmtUtil.rest("false", "创建帐号失败,请联系管理员");
			}
			return TmtUtil.rest("false", "参数错误:EMAIL_ALREADY_USED");
		} else {
			User user = new User();
			user.setUserEmail(username);
			user.setUserStatus("false");
			user.setUserTime(new Date());
			user.setUserBlack(0);
			try {
				user.setUserPassword(Md5.Bit32(password1));
			} catch (Exception e) {
				return TmtUtil.rest("false", "内部错误:PASSWORD_ILLEGAL_ENCRYPTION");
			}
			String uid = userService.register(user);

			if (!StringUtils.isEmpty(uid)) {
				try {
					String tokenUrl = TmtUtil.cTokenFalse(user);
					String code = mailService.sendMail(uid, tokenUrl, username);
					System.out.println("https://localhost/confirm?uid=" + user.getUserId() + "&token=" + tokenUrl);
					if ("200".equals(code)) {
						return TmtUtil.rest("true", "激活邮件发送成功,请前往邮箱激活");
					} else {
						return TmtUtil.rest("false", "激活邮件发送失败,请联系管理员");
					}
				} catch (Exception e) {
					return TmtUtil.rest("false", "激活邮件生成失败,请联系管理员");
				}
			}
			return TmtUtil.rest("false", "创建帐号失败,请联系管理员");
		}
	}

	@RequestMapping("/confirm")
	@ResponseBody
	public String confirm(String uid, String token) {
		String urlToken = token.substring(0, token.length() - 18); // 去掉后16位多余字符
		User user = userService.getInfosById(uid);

		long start = user.getUserTime().getTime();
		long end = System.currentTimeMillis();

		if ((end - start) > 3600000) { // 一小时过期
			user.setUserBlack(user.getUserBlack() + 1);
			user.setUserStatus("dead");
			userService.changeBlack(user);
			return "<p>激活失败 4425,邀请码已过期</p>";
		} else {
			try {
				String tokenTrue = TmtUtil.cTokenTrue(user);
				if (urlToken.equals(tokenTrue)) {
					user.setUserStatus("true");
					int n = userService.changeStatus(user);
					if (n > 0) {
						return "<p>激活成功 点击<a href=\"https://localhost/\" target=\"_self\">这里</a>进行登录</p>";
					} else {
						return "<p>激活失败 4485,请联系管理员</p>";
					}
				} else {
					return "<p>激活失败 4040,请联系管理员</p>";
				}
			} catch (Exception e) {
				return "<p>激活失败 4088,请联系管理员</p>";
			}
		}
	}

}
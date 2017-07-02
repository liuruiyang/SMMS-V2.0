package studio.imgbox.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.shiro.SecurityUtils;

import com.google.gson.Gson;

import studio.imgbox.domain.User;
import studio.imgbox.utils.encryption.Md5;

public class TmtUtil {

	public static String rest(String tmt, String msg) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("tmt", tmt);
		map.put("msg", msg);
		return new Gson().toJson(map);
	}

	public static boolean isEmail(String email) {
		Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = emailPattern.matcher(email);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static String formatDateToString() {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sd.format(new Date()).toString();
	}

	public static String cTokenTrue(User user) throws Exception {
		return Md5.Bit32(user.getUserId().substring(8) + user.getUserEmail() + user.getUserPassword().substring(0, 16))
				.toLowerCase();
	}

	public static String cTokenFalse(User user) throws Exception {
		return cTokenTrue(user) + Md5.Bit16(UUID.randomUUID().toString()).toLowerCase() + "==";
	}
	
	public static String encodeEmail(String email) {
		return isEmail(email) ? email.substring(0, 3)+"***"+email.substring(email.indexOf("@")) : email;
	}
	
	public static String loginEmail() {
		return encodeEmail(loginPrincipal());
	}
	
	public static String loginPrincipal() {
		return (null == SecurityUtils.getSubject().getPrincipal()) ? "" : SecurityUtils.getSubject().getPrincipal().toString();
	}
	
	public static String formatFileSize(float size) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (size < 1024.0) {
            return df.format((double) size) + "B";
        } else if (size < 1048576) {
            return df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            return df.format((double) size / 1048576) + "M";
        } else {
            return df.format((double) size / 1073741824) + "G";
        }
    }
	
}

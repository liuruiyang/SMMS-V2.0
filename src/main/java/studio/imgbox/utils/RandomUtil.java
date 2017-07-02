package studio.imgbox.utils;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import studio.imgbox.utils.encryption.Md5;
import studio.imgbox.utils.encryption.TripleDES;

public class RandomUtil {

	/** The FieldPosition. */
	private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

	/** This Format for format the data to special format. */
	private final static Format dateFormat = new SimpleDateFormat("MMddHHmmssS");

	/** This Format for format the number to special format. */
	private final static NumberFormat numberFormat = new DecimalFormat("0000");

	/** This int is the sequence number ,the default value is 0. */
	private static int seq = 0;

	private static final int MAX = 9999;

	/**
	 * 生成随机文件名(16位MD5加密)
	 * 
	 * @param fileName
	 * @return
	 */
	public static String random(String fileName) {
		Calendar now = Calendar.getInstance();
		StringBuffer sb = new StringBuffer();
		dateFormat.format(now.getTime(), sb, HELPER_POSITION);
		numberFormat.format(seq, sb, HELPER_POSITION);
		if (seq == MAX) {
			seq = 0;
		} else {
			seq++;
		}
		try {
			return dateFormat() + Md5.Bit16(sb.toString() + fileName).toLowerCase();
		} catch (Exception e) {
			return dateFormat() + (sb.toString().substring(7) + random(6)).toLowerCase();
		}
	}

	/**
	 * 根据IP和时间生成保存的文件名
	 *
	 * @param req
	 * @return
	 */
	public static String random(HttpServletRequest req) {
		String ip = getIpAddr(req);
		String time = dateFormat.format(Calendar.getInstance().getTime());
		String kk = "h=" + ip + "&k=" + time + "&n=" + random(6);
		String fire = null;
		try {
			fire = TripleDES.encryption(kk); // 加密方法
		} catch (Exception e) {
			fire = "exp" + UUID.randomUUID().toString().replaceAll("-", random(6));
		}
		return fire;
	}

	/**
	 * 获得访问IP
	 * 
	 * @param req
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return req.getRemoteAddr();
		}
	}

	/**
	 * 生成n位随机字符
	 * 
	 * @return
	 */
	public static String random(int length) {
		String rs = "";
		for (int i = 0; i < length; i++) {
			int intVal = (int) (Math.random() * 26 + 97);
			rs = rs + (char) intVal;
		}
		return rs;
	}

	/**
	 * 获取当天的日期格式化
	 * 
	 * @return
	 */
	public static String dateFormat() {
		SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
		return format.format(new Date()).substring(2);
	}
	
}

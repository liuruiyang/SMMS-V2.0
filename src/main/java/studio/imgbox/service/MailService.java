package studio.imgbox.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import studio.imgbox.config.properties.MailProperties;

@Service
public class MailService {

	@Autowired
	private MailProperties define;

	/**
	 * 邮件发送服务
	 * @param mailBody
	 * @param mailTo
	 * @return
	 */
	public String sendMail(String uid, String tokenUrl, String mailTo) {

		String mailBody = htmlBody(uid, tokenUrl);

		RequestBody reqBody = new FormBody.Builder().add("apiUser", define.getApiUser())
				.add("apiKey", define.getApiKey()).add("from", define.getFrom()).add("fromName", define.getFromName())
				.add("html", mailBody).add("to", mailTo).add("subject", "imgbox.studio注册激活邮件").build();

		OkHttpClient client = new OkHttpClient.Builder().build();

		Request request = new Request.Builder().url(define.getApi()).post(reqBody).build();

		try {
			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				String resp = response.body().string();
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readTree(resp).get("statusCode").toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String htmlBody(String uid, String url) {
		return define.getHtmlHead() + "<p>" + "https://localhost/confirm?uid=" + uid + "&token=" + url + "</p>"
				+ define.getHtmlFooter();
	}
}

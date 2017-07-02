package studio.imgbox.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.Json;

import studio.imgbox.config.properties.QiniuProperties;
import studio.imgbox.domain.Storage;
import studio.imgbox.service.StorageService;
import studio.imgbox.utils.RandomUtil;
import studio.imgbox.utils.TmtUtil;

@Controller
@RequestMapping("/action")
public class UploadController {

	@Autowired
	private QiniuProperties define;
	@Autowired
	private StorageService storageService;

	@PostMapping("/qiniu")
	@ResponseBody
	public String uploadDefault(@RequestParam("file") MultipartFile[] file, HttpServletRequest req) {

		Map<String, String> map = new HashMap<String, String>();

		if (null != file && file.length > 0) {
			for (int i = 0; i < file.length; i++) {

				String fileName = file[i].getOriginalFilename();
				String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase(); // 上传文件的后缀名

				// 如果是合法后缀
				if (define.getAllowedTypes().contains(fileExtension)) {

					// https://developer.qiniu.com/kodo/sdk/1239/java
					Auth auth = Auth.create(define.getAk(), define.getSk());
					String upToken = auth.uploadToken(define.getBucket());
					Configuration cfg = new Configuration(Zone.autoZone());
					UploadManager uploadManager = new UploadManager(cfg);

					try {
						Response res = uploadManager.put(file[i].getBytes(),
								RandomUtil.random(fileName) + "." + fileExtension, upToken);

						String key = res.jsonToMap().get("key").toString();

						String imgLink = define.getBucketUrl() + res.jsonToMap().get("key"); // 完整图片链接

						Storage storage = new Storage();
						storage.setStorageHash(key.substring(0, key.indexOf(".")));
						storage.setStorageUser(TmtUtil.loginPrincipal());
						storage.setStorageTime(new Date());
						storage.setStorageIp(RandomUtil.getIpAddr(req));
						storage.setStorageMime(fileExtension);
						storage.setStorageSize(TmtUtil.formatFileSize(file[i].getSize()));

						storageService.upload(storage);

						map.put("link", imgLink);
						map.put("style", define.getSeperator() + define.getImgStyle());
						map.put("status", "200");

					} catch (IOException e) {
						map.put("status", "ERROR::NET_ERROR_QINIU");
					} catch (SQLException ee) {
						map.put("status", "ERROR::SQL_ERROR");
					}
				} else {
					map.put("status", "ERROR::BAD_FILE_TYPE");
				}
			}
		} else {
			map.put("status", "ERROR::EMPTY_FILE");
		}
		return Json.encode(map);
	}

}

package studio.imgbox.controller.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.util.Json;

import studio.imgbox.config.properties.GlobalProperties;
import studio.imgbox.config.properties.LocalProperties;
import studio.imgbox.utils.RandomUtil;

/**
 * 上传到本地的简单实例
 * @auth liuruiyang
 * @date 2017-06-30
 */
@Controller
@RequestMapping("/demo")
public class SimpleDemo {

	@Autowired
	private LocalProperties localDefine;
	@Autowired
	private GlobalProperties globalDefine;

	// 本地上传配置

	@RequestMapping("")
	public String normal() {
		return "html5";
	}

	@PostMapping("/local")
	@ResponseBody
	public String uploadLocal(@RequestParam("file") MultipartFile[] file, HttpServletRequest req) {
		
		if("false".equals(globalDefine.getAllowLocal())) {
			return "目前不支持上传到本地测试环境";
		}

		System.out.println(file.length);

		Map<String, Object> map = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();

		if (null != file && file.length > 0) {

			File path = new File(localDefine.getPath());
			if (!path.exists()) {
				path.mkdirs();
				System.out.println("创建了一个文件夹");
			}

			for (int i = 0; i < file.length; i++) {

				String fileName = file[i].getOriginalFilename();
				String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

				// 存放到本地时候要注意特殊字符 / 无法使用
				String reName = RandomUtil.random(fileName).substring(9) + "." + fileExtension;

				if (localDefine.getAllowedTypes().contains(fileExtension)) {
					File fileSource = new File(path, reName);
					try {
						file[i].transferTo(fileSource);
						map.put("status", "OK");
					} catch (IllegalStateException e) {
						if (!list.contains("IllegalStateException")) {
							list.add("EXCEPTION_ILLEGALSTATE");
						}
					} catch (IOException e) {
						if (!list.contains("IOException")) {
							list.add("EXCEPTION_IO");
						}
					}
				} else {
					if (!list.contains("BAD_FILE_TYPE")) {
						list.add("BAD_FILE_TYPE");
					}
				}
			}
		} else {
			if (!list.contains("FILE_EMPTY")) {
				list.add("FILE_EMPTY");
			}
		}
		map.put("hasErrors", list);
		return Json.encode(map);
	}
}

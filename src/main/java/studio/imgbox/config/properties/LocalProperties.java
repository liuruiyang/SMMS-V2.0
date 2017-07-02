package studio.imgbox.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 本地测试上传配置文件
 * @auth liuruiyang
 * @date 2017-07-02
 */
@Component
@ConfigurationProperties(prefix = "upload.local")
@PropertySource("file:/home/conf/qiniu.properties")
public class LocalProperties {
	private String path;
	private String allowedTypes;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAllowedTypes() {
		return allowedTypes;
	}

	public void setAllowedTypes(String allowedTypes) {
		this.allowedTypes = allowedTypes;
	}

}

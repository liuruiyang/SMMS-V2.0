package studio.imgbox.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 站点全局配置文件
 * @auth liuruiyang
 * @date 2017-07-02
 */
@Component
@ConfigurationProperties(prefix = "global")
@PropertySource("file:/home/conf/global.properties")
public class GlobalProperties {

	private String allowRegister;
	private String allowLocal;

	public String getAllowRegister() {
		return allowRegister;
	}

	public void setAllowRegister(String allowRegister) {
		this.allowRegister = allowRegister;
	}

	public String getAllowLocal() {
		return allowLocal;
	}

	public void setAllowLocal(String allowLocal) {
		this.allowLocal = allowLocal;
	}

}

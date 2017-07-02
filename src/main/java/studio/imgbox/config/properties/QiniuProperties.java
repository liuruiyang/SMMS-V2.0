package studio.imgbox.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 七牛云上传配置文件
 * @auth liuruiyang
 * @date 2017-07-02
 */
@Component
@ConfigurationProperties(prefix = "upload.qiniu")
@PropertySource("file:/home/conf/qiniu.properties")
public class QiniuProperties {
	// 七牛配置参数
	private String ak;
	private String sk;
	private String bucket;
	private String bucketUrl;
	private String seperator;
	private String imgStyle;

	// 后台设置的允许上传类型
	private String allowedTypes;

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getBucketUrl() {
		return bucketUrl;
	}

	public void setBucketUrl(String bucketUrl) {
		this.bucketUrl = bucketUrl;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public String getImgStyle() {
		return imgStyle;
	}

	public void setImgStyle(String imgStyle) {
		this.imgStyle = imgStyle;
	}

	public String getAllowedTypes() {
		return allowedTypes;
	}

	public void setAllowedTypes(String allowedTypes) {
		this.allowedTypes = allowedTypes;
	}

}

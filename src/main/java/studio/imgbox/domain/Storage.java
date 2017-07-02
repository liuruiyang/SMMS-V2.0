package studio.imgbox.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Storage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String storageHash;
	@JsonIgnore
	private String storageUser;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date storageTime;
	@JsonIgnore
	private String storageIp;
	private String storageMime;
	private String storageSize;

	public String getStorageHash() {
		return storageHash;
	}

	public void setStorageHash(String storageHash) {
		this.storageHash = storageHash;
	}

	public String getStorageUser() {
		return storageUser;
	}

	public void setStorageUser(String storageUser) {
		this.storageUser = storageUser;
	}

	public Date getStorageTime() {
		return storageTime;
	}

	public void setStorageTime(Date storageTime) {
		this.storageTime = storageTime;
	}

	public String getStorageIp() {
		return storageIp;
	}

	public void setStorageIp(String storageIp) {
		this.storageIp = storageIp;
	}

	public String getStorageMime() {
		return storageMime;
	}

	public void setStorageMime(String storageMime) {
		this.storageMime = storageMime;
	}

	public String getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(String storageSize) {
		this.storageSize = storageSize;
	}

}

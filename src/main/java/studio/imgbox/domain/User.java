package studio.imgbox.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;
	private String userEmail;
	@JsonIgnore
	private String userPassword;
	@JsonIgnore
	private String userStatus;
	@JsonIgnore
	private Date userTime;
	@JsonIgnore
	private int userBlack;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	
	public Date getUserTime() {
		return userTime;
	}

	public void setUserTime(Date userTime) {
		this.userTime = userTime;
	}
	
	public int getUserBlack() {
		return userBlack;
	}

	public void setUserBlack(int userBlack) {
		this.userBlack = userBlack;
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userEmail=" + userEmail + ", userPassword=" + userPassword
				+ ", userStatus=" + userStatus + ", userTime=" + userTime + ", userBlack=" + userBlack + "]";
	}
}
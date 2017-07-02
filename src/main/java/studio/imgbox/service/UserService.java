package studio.imgbox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import studio.imgbox.domain.User;
import studio.imgbox.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public User getInfoLogin(String userEmail) {
		return userMapper.findInfoLogin(userEmail);
	}
	
	public User getInfosById(String uid) {
		return userMapper.findInfosById(uid);
	}
	
	public User getInfosByEmail(String email) {
		return userMapper.findInfosByEmail(email);
	}

	@Transactional
	public String register(User user) {
		userMapper.insertRegister(user);
		return user.getUserId();
	}
	
	@Transactional
	public int updateRegister(User user) {
		return userMapper.updateRegister(user);
	}
	
	@Transactional
	public int changeStatus(User user) {
		return userMapper.updateComfirmStatus(user);
	}
	
	@Transactional
	public int changeBlack(User user) {
		return userMapper.updateComfirmSBlack(user);
	}

}

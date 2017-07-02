package studio.imgbox.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import studio.imgbox.domain.User;

public interface UserMapper {

	// 登录查询:只要密码和状态 {"true":"允许登录","false":"帐号未激活","dead":"帐号不存在"}
	@Select("SELECT user_email,user_password,user_status FROM user WHERE user_email = #{userEmail}")
	User findInfoLogin(String userEmail);

	@Select("SELECT * FROM user WHERE user_id = #{userId}")
	User findInfosById(String userId);

	@Select("SELECT * FROM user WHERE user_email = #{userEmail}")
	User findInfosByEmail(String userEmail);

	@Insert("INSERT INTO user(user_id,user_email,user_password,user_status,user_time,user_black) VALUES(#{userId}, #{userEmail}, #{userPassword}, #{userStatus}, #{userTime}, #{userBlack})")
	@SelectKey(statement = "select replace(uuid(),'-','') from dual", keyProperty = "userId", before = true, resultType = String.class)
	int insertRegister(User user);

	@Update("UPDATE user SET user_password=#{userPassword},user_status=#{userStatus},user_time=#{userTime} WHERE user_id =#{userId}")
	int updateRegister(User user);

	// 激活更新:状态从true -> false
	// 创建更新:状态从false-> dead black+1
	@Update("UPDATE user SET user_status=#{userStatus} WHERE user_id =#{userId}")
	int updateComfirmStatus(User user);

	@Update("UPDATE user SET user_black=#{userBlack} WHERE user_id =#{userId}")
	int updateComfirmSBlack(User user);

}
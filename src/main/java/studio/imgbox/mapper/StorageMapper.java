package studio.imgbox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import studio.imgbox.domain.Storage;

public interface StorageMapper {

	// 登录查询:只要密码和状态 {"true":"允许登录","false":"帐号未激活","dead":"帐号不存在"}
	@Select("SELECT storage_hash,storage_time,storage_mime,storage_size FROM storage WHERE storage_user = #{storageUser}")
	List<Storage> findUserStorage(String storageUser);

	@Insert("INSERT INTO storage(storage_hash,storage_user,storage_time,storage_ip,storage_mime,storage_size) VALUES(#{storageHash}, #{storageUser}, #{storageTime}, #{storageIp}, #{storageMime}, #{storageSize})")
	int insertUpload(Storage storage);

}
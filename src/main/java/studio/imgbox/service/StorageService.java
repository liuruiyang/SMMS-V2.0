package studio.imgbox.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import studio.imgbox.domain.Storage;
import studio.imgbox.mapper.StorageMapper;

@Service
public class StorageService {

	@Autowired
	private StorageMapper storageMapper;

	@Transactional
	public int upload(Storage storage) throws SQLException {
		return storageMapper.insertUpload(storage);
	}

	public List<Storage> listUserUpload(String userEmail) {
		return storageMapper.findUserStorage(userEmail);
	}
}

package studio.imgbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import studio.imgbox.domain.Storage;
import studio.imgbox.service.StorageService;
import studio.imgbox.utils.TmtUtil;

@RestController
public class ViewController {

	@Autowired
	private StorageService storageService;

	@RequestMapping("/view/{storageUser}")
	public Storage[] view(String storageUser) {
		return storageService.listUserUpload(TmtUtil.loginPrincipal());
	}
}

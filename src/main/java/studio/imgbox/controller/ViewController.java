package studio.imgbox.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import studio.imgbox.domain.Storage;
import studio.imgbox.service.StorageService;
import studio.imgbox.utils.TmtUtil;

@RestController
public class ViewController {

	@Autowired
	private StorageService storageService;

	@RequestMapping("/view")
	public PageInfo<Storage> view(int pageNum, int pageSize) {
		// return storageService.listUserUpload(TmtUtil.loginPrincipal());
		PageHelper.startPage(pageNum, pageSize);
		List<Storage> list = storageService.listUserUpload(TmtUtil.loginPrincipal());
		return new PageInfo<Storage>(list);
	}
}

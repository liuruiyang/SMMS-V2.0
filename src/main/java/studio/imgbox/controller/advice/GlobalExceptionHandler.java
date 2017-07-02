package studio.imgbox.controller.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * 捕捉上传文件异常
	 * @param e
	 * @param redirectAttributes
	 */
	@ExceptionHandler(MultipartException.class)
	public String handleMultipartException(MultipartException e) {
		return "error/500"; //直接重定向
	}
	
}

package studio.imgbox;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("studio.imgbox.mapper")
public class ImgApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImgApplication.class, args);
	}
	
}

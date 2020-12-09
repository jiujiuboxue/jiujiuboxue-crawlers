package com.jiujiuboxue.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScans(value = {
						@ComponentScan(value = "com.jiujiuboxue.common"),
		        		@ComponentScan(value = "com.jiujiuboxue.module.tiku.*")})
@EnableJpaRepositories(basePackages = "com.jiujiuboxue.module.tiku.*")
		@EntityScan(basePackages = "com.jiujiuboxue.module.tiku")
public class JiujiuboxueCrawlersApplication {



	public static void main(String[] args) {
		SpringApplication.run(JiujiuboxueCrawlersApplication.class, args);
	}

}

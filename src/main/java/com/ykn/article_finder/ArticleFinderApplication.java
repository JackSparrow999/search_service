package com.ykn.article_finder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.ykn.article_finder.*"}, exclude = {ContextStackAutoConfiguration.class})
public class ArticleFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticleFinderApplication.class, args);
	}

}

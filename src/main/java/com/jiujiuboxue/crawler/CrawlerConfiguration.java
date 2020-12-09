package com.jiujiuboxue.crawler;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author wayne
 */
@Component
@Configuration()
@ConfigurationProperties()
@Data
public class CrawlerConfiguration {

    @Value("${jiujiuboxue.crawler.question.image.path}")
    private String questionImagePath;

}

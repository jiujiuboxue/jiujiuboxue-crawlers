package com.jiujiuboxue.crawler;

import com.jiujiuboxue.crawler.web.impl.CrawlerBase;
import com.jiujiuboxue.crawler.web.impl.cn21.CrawlerFrom21Crawler;
import com.jiujiuboxue.crawler.web.impl.cn21.CrawlerListFrom21Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * @author wayne
 */
@SpringBootApplication
@EnableJpaAuditing
@ComponentScans(value = {
        @ComponentScan(value = "com.jiujiuboxue.common"),
        @ComponentScan(value = "com.jiujiuboxue.module.tiku.service")})
@EnableJpaRepositories(basePackages = "com.jiujiuboxue.module.tiku.repository")
@EntityScan(basePackages = "com.jiujiuboxue.module.tiku.entity")
public class JiujiuboxueCrawlersApplication  implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(JiujiuboxueCrawlersApplication.class, args);
    }


    @Autowired
    public CrawlerFrom21Crawler crawlerFrom21Crawler;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        CrawlerListFrom21Crawler crawlerListFrom21Crawler = new CrawlerListFrom21Crawler();
        List<String> urlList = crawlerListFrom21Crawler.getUrlList("http://tiku.21cnjy.com/tiku.php?mod=quest&channel=3&cid=0&type=1&xd=2");

        if (urlList != null && urlList.size() > 0) {
            for (String url : urlList) {
             //CrawlerFrom21Crawler crawlerFrom21Crawler = new CrawlerFrom21Crawler();
             crawlerFrom21Crawler.crawlerQuestion(url);
            }
        }
    }
}

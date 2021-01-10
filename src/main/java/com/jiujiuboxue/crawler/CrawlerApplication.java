package com.jiujiuboxue.crawler;

import com.jiujiuboxue.crawler.web.impl.cn21.CrawlerFrom21Crawler;
import com.jiujiuboxue.crawler.web.impl.cn21.CrawlerListFrom21Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

/**
 * @author wayne
 */
public class CrawlerApplication {

    /**
     * SchoolType: 01 小学  02 初中 03 高中
     * Grade: 01 ~ 06 一年级 ～ 六年级
     * Subject: 01 ~ 09 语文 数学 英语 物理 生物 化学 政治 历史 地理
     */

    @Autowired
    private CrawlerFrom21Crawler crawlerFrom21Crawler;

    public void setCrawlerFrom21Crawler(CrawlerFrom21Crawler crawlerFrom21Crawler){
        this.crawlerFrom21Crawler = this.crawlerFrom21Crawler;
    }


    public void run(String... args) throws Exception {

        CrawlerListFrom21Crawler crawlerListFrom21Crawler = new CrawlerListFrom21Crawler();
        List<String> urlList = crawlerListFrom21Crawler.getUrlList("http://tiku.21cnjy.com/tiku.php?mod=quest&channel=3&cid=0&type=1&xd=2");

        if (urlList != null && urlList.size() > 0) {
            for (String url : urlList) {
               // crawlerFrom21Crawler.crawlerQuestion(url);
                crawlerFrom21Crawler.crawlerQuestion(url, "02", "", "02");
            }
        }
    }
}

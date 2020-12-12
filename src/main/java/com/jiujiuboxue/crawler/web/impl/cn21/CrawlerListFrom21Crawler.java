package com.jiujiuboxue.crawler.web.impl.cn21;

import com.jiujiuboxue.crawler.web.QuestionListCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wayne
 */
public class CrawlerListFrom21Crawler implements QuestionListCrawler {

    private static String PREURL = "http://tiku.21cnjy.com/";

    @Override
    public List<String> getUrlList(String url) throws IOException {

        List<String> urlList = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select("p.btns a");
        for (Element element : elements) {
            urlList.add(PREURL + element.attributes().get("href"));
        }
        return urlList;
    }
}

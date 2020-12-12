package com.jiujiuboxue.crawler.web;

import java.io.IOException;
import java.util.List;

/**
 * @author wayne
 */
public interface QuestionListCrawler {

    /**
     * Crawler Question URL List
     * @param url
     * @return
     */
    List<String> getUrlList(String url) throws IOException;
}

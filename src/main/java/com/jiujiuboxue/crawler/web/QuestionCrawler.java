package com.jiujiuboxue.crawler.web;

import com.jiujiuboxue.modules.tiku.entity.Question;
import com.jiujiuboxue.modules.tiku.entity.Category;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.util.List;

/**
 * @author wayne
 */
public interface QuestionCrawler {

    /**
     * Get Object from url
     * @param url
     * @param query
     * @return
     */
    List<Question> crawler(String url, Category category) throws IOException, XPatherException;

}


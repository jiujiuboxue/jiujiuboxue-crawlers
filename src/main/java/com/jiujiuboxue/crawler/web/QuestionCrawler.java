package com.jiujiuboxue.crawler.web;

import com.jiujiuboxue.module.tiku.entity.Grade;
import com.jiujiuboxue.module.tiku.entity.Question;
import com.jiujiuboxue.module.tiku.entity.SchoolType;
import com.jiujiuboxue.module.tiku.entity.Subject;
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
     * @param schoolType
     * @param grade
     * @param subject
     * @return
     */
    void crawler(String url, SchoolType schoolType, Grade grade, Subject subject) throws IOException, XPatherException;

}


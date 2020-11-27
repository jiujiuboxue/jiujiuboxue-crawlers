package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.modules.tiku.entity.QuestionImage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wayne
 */
public class QuestionImageWarpper {

    private List<QuestionImage> questionImageList = new ArrayList<>();

    private String content;

    public List<QuestionImage> getQuestionImageList() {
        return questionImageList;
    }

    public void setQuestionImageList(List<QuestionImage> questionImageList) {
        this.questionImageList = questionImageList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

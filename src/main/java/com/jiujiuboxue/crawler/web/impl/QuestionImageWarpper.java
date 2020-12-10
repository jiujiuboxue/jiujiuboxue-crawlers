package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.module.tiku.entity.QuestionImage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wayne
 */
public class QuestionImageWarpper {

    private List<QuestionImage> questionImageList = new ArrayList();

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


    public void addQuestionImage(QuestionImage questionImage) {
        this.questionImageList.add(questionImage);
    }

}

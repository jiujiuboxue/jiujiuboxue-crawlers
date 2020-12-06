package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.common.utils.StringUtil;
import com.jiujiuboxue.crawler.util.ImageUtil;
import com.jiujiuboxue.crawler.web.CrawlerBase;
import com.jiujiuboxue.crawler.web.QuestionCrawler;
import com.jiujiuboxue.module.tiku.entity.*;
import org.htmlcleaner.XPatherException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author wayne
 */
@Component
public class CrawlerFrom21Crawler extends CrawlerBase implements QuestionCrawler {

    private static String PREURL = "http://tiku.21cnjy.com/";

    /**
     * Get Question URL list
     *
     * @param url
     * @param query
     * @return
     * @throws IOException
     */
    public List<String> getUrlList(String url, String query) throws IOException {
        List<String> urlList = new ArrayList<>();
        Elements elements = getElements(url, query);
        for (Element element : elements) {
            urlList.add(PREURL + element.attributes().get("href"));
        }
        return urlList;
    }

    public String getQuestonContent(String fullContent) {
        String content = "";
        return content;
    }

    public String getChoiceItemContent(String fullChoiceContent) {
        String choiceItemContent = "";
        return choiceItemContent;
    }

    public String getAnalysis(String fullAnalysis) {
        String analysis = "";
        return analysis;
    }

    public String getAnswer(String fullAnswer) {
        String answer = "";
        return answer;
    }


    public Question getQuestionContent(String url) throws IOException {

        Document doc = getDocument(url);

        Elements contentElements = doc.select(".answer_detail dl dt  p:nth-child(1)");
        Element element = contentElements.first();
        if (element == null) {
            return null;
        }

        String questionFullContent = element.toString();
        String content = StringUtil.removeHtmlTag(questionFullContent);

        Question question = Question.builder()
                .fullContent(questionFullContent)
                .content(StringUtil.removeHtmlTag(questionFullContent))
                .id(getSHA256(content))
                .build();

        QuestionImageWarpper questionImageWarpper = getImageList(element, question.getId(), IMAGETYPE.QUESTIONCONTENT, questionFullContent);
        question.setQuestionImageSet(questionImageWarpper.getQuestionImageList());
        question.setFullContent(StringUtil.removeHtmlTag(questionImageWarpper.getContent()));


        //Answer
        Set<QuestionAnswer> questionAnswerList = new HashSet<>();
        Elements answerElements = doc.select("div.answer_detail dl dd p:nth-child(1) i");
        if (answerElements != null) {
            String answerFullContent = answerElements.toString();
            String answerContent = StringUtil.removeHtmlTag(answerFullContent);
            QuestionAnswer questionAnswer = QuestionAnswer.builder()
                    .answer(answerContent)
                    .id(getSHA256(answerContent))
                    .build();

            QuestionImageWarpper questionAnswerWarpper = getImageList(contentElements.first(),questionAnswer.getId(),IMAGETYPE.QUESTIONANSWER,answerFullContent);
             questionAnswer.setFullAnswer(questionAnswerWarpper.getContent());
             questionAnswer.setQuestionAnswerImageSet(questionImageWarpper.getQuestionImageList());
        }
//        if (questionAnswerList != null && questionAnswerList.size() > 0) {
//                question.setQuestionAnswerList(questionAnswerList);
//        }

//        //Anaysis
//        List<QuestionAnalysis> questionAnalysisList = new ArrayList<>();
//        Elements anaysisElements = doc.select("");
//        if(anaysisElements!=null)
//        {
//            String analysisFullContent = anaysisElements.toString();
//            QuestionAnalysis questionAnalysis = new QuestionAnalysis();
//            questionAnalysis.setAnalysis(StringUtil.removeHtmlTag(StringUtil.RemoveString(analysisFullContent,"解析")));
//            questionAnalysis.setId(getSHA256(questionAnalysis.getAnalysis()));
//            QuestionImageWarpper analysisQuestionImageWarpper = getImageList(anaysisElements.first(),questionAnalysis.getId(), IMAGETYPE.QUESTIONANALYSIS,analysisFullContent);
//            questionAnalysis.setQuestionAnalysisImageList(analysisQuestionImageWarpper.getQuestionImageList());
//            questionAnalysis.setFullAnalysis(analysisQuestionImageWarpper.getContent());
//            questionAnalysisList.add(questionAnalysis);
//        }
//        if(questionAnalysisList!=null && questionAnalysisList.size()>0) {
//            question.setQuestionAnalysisList(questionAnalysisList);
//        }
        return question;
    }

    @Override
    public List<Question> crawler(String url,SchoolType schoolType, Grade grade, Subject subject) throws IOException, XPatherException {

        List<Question> questionList = new ArrayList<>();
        //Fetch question url list
        List<String> urlList = getUrlList(url, "p.btns a");

        for (String urlTmp : urlList) {
           Question question = getQuestionContent(urlTmp);
           questionList.add(question);
        }

        return questionList;
    }


}

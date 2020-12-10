package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.common.utils.StringUtil;
import com.jiujiuboxue.crawler.web.QuestionCrawler;
import com.jiujiuboxue.module.tiku.entity.*;
import org.htmlcleaner.XPatherException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Wrapper;
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
                .content(content)
                .id(getId(content))
                .build();

        QuestionImageWarpper questionImageWrapper = getImageList(element, question.getId(), IMAGETYPE.QUESTIONCONTENT, questionFullContent);
        if (questionImageWrapper != null) {
            if (questionImageWrapper.getQuestionImageList() != null && questionImageWrapper.getQuestionImageList().size() > 0) {
                question.setQuestionImageList(questionImageWrapper.getQuestionImageList());
            }
            question.setFullContent(StringUtil.removeHtmlTag(questionImageWrapper.getContent()));
        } else {
            question.setFullContent(questionFullContent);
        }

        //Answer
        List<QuestionAnswer> questionAnswerList = new ArrayList<>();
        Elements answerElements = doc.select("div.answer_detail dl dd p:nth-child(1) i");
        if (answerElements != null) {
            String answerFullContent = answerElements.first().toString();
            String answerContent = StringUtil.removeHtmlTag(answerFullContent);
            QuestionAnswer questionAnswer = QuestionAnswer.builder()
                    .answer(answerContent)
                    .id(question.getId().concat("-").concat(getId(answerContent)))
                    .question(question)
                    .build();

            QuestionImageWarpper questionAnswerImageWrapper = getImageList(answerElements.first(), questionAnswer.getId(), IMAGETYPE.QUESTIONANSWER, answerFullContent);
            if (questionImageWrapper != null) {
                if (questionAnswerImageWrapper.getQuestionImageList() != null && questionAnswerImageWrapper.getQuestionImageList().size() > 0) {
                    questionAnswer.setQuestionAnswerImageList(questionAnswerImageWrapper.getQuestionImageList());
                }
                questionAnswer.setFullAnswer(questionAnswerImageWrapper.getContent());
            } else {
                questionAnswer.setFullAnswer(answerFullContent);
            }
            questionAnswerList.add(questionAnswer);
        }


        if (questionAnswerList != null && questionAnswerList.size() > 0) {
            question.setQuestionAnswerList(questionAnswerList);
        }

        //Anaysis
        List<QuestionAnalysis> questionAnalysisList = new ArrayList<>();
        Elements analysisElements = doc.select("div.answer_detail dl dd p:nth-child(2) i");
        if (analysisElements != null) {
            String analysisFullContent = analysisElements.first().toString();
            String analysisContent = StringUtil.removeHtmlTag(StringUtil.RemoveString(analysisFullContent, "解析"));

            QuestionAnalysis questionAnalysis = QuestionAnalysis.builder()
                    .id(question.getId().concat("-").concat(getId(analysisContent)))
                    .analysis(analysisContent)
                    .question(question)
                    .build();

            QuestionImageWarpper analysisQuestionImageWrapper = getImageList(analysisElements.first(), questionAnalysis.getId(), IMAGETYPE.QUESTIONANALYSIS, analysisFullContent);
            if (analysisQuestionImageWrapper != null) {
                if (analysisQuestionImageWrapper.getQuestionImageList() != null && analysisQuestionImageWrapper.getQuestionImageList().size() > 0) {
                    questionAnalysis.setQuestionAnalysisImageList(analysisQuestionImageWrapper.getQuestionImageList());
                }
                questionAnalysis.setFullAnalysis(questionImageWrapper.getContent());
            } else {
                questionAnalysis.setFullAnalysis(analysisFullContent);
            }
        }

        if (questionAnalysisList != null && questionAnalysisList.size() > 0) {
            question.setQuestionAnalysisList(questionAnalysisList);
        }
        return question;
    }

    @Override
    public List<Question> crawler(String url, SchoolType schoolType, Grade grade, Subject subject) throws IOException, XPatherException {

        List<Question> questionList = new ArrayList<>();
        List<String> urlList = getUrlList(url, "p.btns a");
        for (String urlTmp : urlList) {
            Question question = getQuestionContent(urlTmp);
            questionList.add(question);
        }
        return questionList;
    }


}

package com.jiujiuboxue.crawler.web.impl.cn21;

import com.jiujiuboxue.common.utils.StringUtil;
import com.jiujiuboxue.crawler.web.impl.CrawlerBase;
import com.jiujiuboxue.module.tiku.entity.*;

import com.jiujiuboxue.module.tiku.service.QuestionAnalysisService;
import com.jiujiuboxue.module.tiku.service.QuestionAnswerService;
import com.jiujiuboxue.module.tiku.service.QuestionImageService;
import com.jiujiuboxue.module.tiku.service.QuestionService;
import com.jiujiuboxue.module.tiku.service.impl.QuestionAnalysisServiceImpl;
import com.jiujiuboxue.module.tiku.service.impl.QuestionAnswerServiceImpl;
import com.jiujiuboxue.module.tiku.service.impl.QuestionImageServiceImpl;
import com.jiujiuboxue.module.tiku.service.impl.QuestionServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wayne
 */
@Component
public class CrawlerFrom21Crawler extends CrawlerBase {


    public QuestionService questionService;
    public QuestionAnswerService questionAnswerService;
    public QuestionAnalysisService questionAnalysisService;
    public QuestionImageService questionImageService;

    @Autowired
    public void setQuestionService(QuestionServiceImpl questionService) {
        this.questionService = questionService;
    }

    @Autowired
    public void setQuestionAnswerService(QuestionAnswerServiceImpl questionAnswerService) {
        this.questionAnswerService = questionAnswerService;
    }

    @Autowired
    public void setQuestionAnalysisService(QuestionAnalysisServiceImpl questionAnalysisService) {
        this.questionAnalysisService = questionAnalysisService;
    }

    @Autowired
    public void setQuestionImageService(QuestionImageServiceImpl questionImageService) {
        this.questionImageService = questionImageService;
    }


    public void crawlerQuestion(String url) throws IOException {
        super.clean();
        Document doc = Jsoup.connect(url).get();

        Elements contentElements = doc.select(".answer_detail dl dt  p:nth-child(1)");
        Element element = contentElements.first();
        if (element == null) {
            return;
        }

        String questionFullContent = element.toString();
        String content = StringUtil.removeHtmlTag(questionFullContent);

        question = Question.builder()
                .fullContent(questionFullContent)
                .content(content)
                .id(getId(content))
                .build();

        crawleQuestionImage(element);
        crawleQuestionAnswer(doc);
        crawleQuestionAnalysis(doc);
        crawleQuestionChoiceItem(doc);
        save();

    }

    protected void crawleQuestionAnswer(Document doc) throws IOException {
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
                    .fullAnswer(answerFullContent)
                    .build();

            crawleQuestionAnswerImage(answerElements.first(), questionAnswer);
        }
    }

    protected void crawleQuestionAnalysis(Document doc) throws IOException {
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
                    .fullAnalysis(analysisFullContent)
                    .build();

            crawleQuestionAnalysisImage(analysisElements.first(), questionAnalysis);
        }
    }


    protected void crawleQuestionChoiceItem(Document doc) throws IOException {
        List<QuestionChoiceItem> questionChoiceItemList = new ArrayList<>();
        Elements choiceItems = doc.select("");
        if(choiceItems!=null){
            String choiceItemFullContent = choiceItems.first().toString();
            String choiceItemContent = StringUtil.removeHtmlTag(choiceItemFullContent);

            QuestionChoiceItem questionChoiceItem = QuestionChoiceItem.builder()
                    .id(question.getId().concat("-").concat(getId(choiceItemContent)))
                    .content(choiceItemContent)
                    .question(question)
                    .fullContent(choiceItemFullContent)
                    .build();
            crawleQuestionChoiceItemImage(choiceItems.first(),questionChoiceItem);
        }


    }

    protected void save() {
        if (question != null) {
            this.questionService.save(question);
        }

        if (questionAnswerList != null) {
            this.questionAnswerService.save(this.questionAnswerList);
        }

        if (questionAnalysisList != null && questionAnalysisList.size() > 0) {
            this.questionAnalysisService.save(questionAnalysisList);
        }

        if (questionImageList != null && questionImageList.size() > 0) {
            this.questionImageService.save(questionImageList);
        }
    }

}

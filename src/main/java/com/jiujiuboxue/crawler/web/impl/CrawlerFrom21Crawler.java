package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.common.utils.StringUtil;
import com.jiujiuboxue.crawler.util.ImageUtil;
import com.jiujiuboxue.crawler.web.CrawlerBase;
import com.jiujiuboxue.crawler.web.QuestionCrawler;
import com.jiujiuboxue.modules.tiku.IMAGETYPE;
import com.jiujiuboxue.modules.tiku.entity.*;
import org.htmlcleaner.XPatherException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    public QuestionImageWarpper getImageList(Element element,
                                             String parentId,
                                             IMAGETYPE imageType, String fullContent) throws IOException {

        QuestionImageWarpper questionImageWarpper = new QuestionImageWarpper();
        Elements imgElements = element.getElementsByTag("img");

        int index = 1;
        for (Element imgElement : imgElements) {
            String imgUrl = imgElement.attributes().get("src");
            QuestionImage questionImage = ImageUtil.getQuestionImageFromUrl(imgUrl);
            if (questionImage == null) {
                continue;
            }

            switch (imageType) {
                case QUESTIONANSWER:
                    questionImage.setImageOwnerType(String.valueOf(IMAGETYPE.QUESTIONANSWER));
                    break;
                case QUESTIONANALYSIS:
                    questionImage.setImageOwnerType(String.valueOf(IMAGETYPE.QUESTIONANALYSIS));
                    break;
                case QUESTIONCONTENT:
                    questionImage.setImageOwnerType(String.valueOf(IMAGETYPE.QUESTIONCONTENT));
                    break;
            }
            questionImage.setId(parentId.concat(String.valueOf(index)));
            fullContent = fullContent.replace(imgElement.toString(), String.format("@%s@", questionImage.getId()));
            index++;
            questionImage.setImageOwnerType(imageType.toString());
            questionImageWarpper.getQuestionImageList().add(questionImage);
        }
        questionImageWarpper.setContent(fullContent);
        return questionImageWarpper;
    }


    public Question getQuestionContent(String url) throws IOException {

        Document doc = getDocument(url);

        Elements contentElements = doc.select(".answer_detail dl dt  p:nth-child(1)");
        Element element = contentElements.first();
        if (element == null) {
            return null;
        }
        Question question = new Question();
        question.setSource(url);
        String questionFullContent = element.toString();
        question.setContent(StringUtil.removeHtmlTag(questionFullContent));
        question.setId(getSHA256(question.getContent()));
        QuestionImageWarpper questionImageWarpper = getImageList(element, question.getId(), IMAGETYPE.QUESTIONCONTENT, questionFullContent);
        question.setQuestionImageList(questionImageWarpper.getQuestionImageList());
        question.setFullContent(StringUtil.removeHtmlTag(questionImageWarpper.getContent()));
        //question.setContent(question.getFullContent().replaceAll("</?[^>]+>", ""));

        //Answer
        List<QuestionAnswer> questionAnswerList = new ArrayList<>();
        Elements answerElements = doc.select("div.answer_detail dl dd p:nth-child(1) i");
        if (answerElements != null) {
            String answerFullContent = answerElements.toString();
            String answerContent = StringUtil.removeHtmlTag(answerFullContent);
            if(answerContent!=null&&answerContent.length()>0) {
                QuestionAnswer questionAnswer = new QuestionAnswer();
                questionAnswer.setAnswer(StringUtil.removeHtmlTag(StringUtil.RemoveString(answerFullContent, "答案")));
                questionAnswer.setId(getSHA256(questionAnswer.getAnswer()));
                questionAnswerList.add(questionAnswer);
            }
        }
        if (questionAnswerList != null && questionAnswerList.size() > 0) {
                question.setQuestionAnswerList(questionAnswerList);
        }

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
    public List<Question> crawler(String url, Category category) throws IOException, XPatherException {

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

package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.crawler.CrawlerConfiguration;
import com.jiujiuboxue.crawler.util.ImageHelper;
import com.jiujiuboxue.module.tiku.entity.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wayne
 */
public class CrawlerBase {

    protected void clean()
    {
        this.question=null;
        this.questionAnalysisList.clear();
        this.questionAnswerList.clear();
        this.questionImageList.clear();
        this.questionChoiceItemList.clear();
    }

    @Autowired
    private CrawlerConfiguration crawlerConfiguration;

    protected Question question = new Question();
    protected List<QuestionImage> questionImageList = new ArrayList<>();
    protected List<QuestionAnswer> questionAnswerList = new ArrayList<>();
    protected List<QuestionAnalysis> questionAnalysisList = new ArrayList<>();
    protected List<QuestionChoiceItem> questionChoiceItemList = new ArrayList<>();


    protected void crawleQuestionImage(Element element) throws IOException {

        QuestionImageWrapper questionImageWrapper = getImageList(element, this.question.getId(), IMAGETYPE.QUESTIONCONTENT, question.getFullContent());
        if (questionImageWrapper != null) {
            question.setFullContent(questionImageWrapper.getContent());
            if (questionImageWrapper.getQuestionImageList() != null && questionImageWrapper.getQuestionImageList().size() > 0) {
                List<QuestionImage> questionImageList = questionImageWrapper.getQuestionImageList();
                for (QuestionImage questionImage : questionImageList) {
                     questionImage.setQuestion(question);
                     this.questionImageList.add(questionImage);
                }
            }
        }
    }

    protected void crawleQuestionAnswerImage(Element element, QuestionAnswer questionAnswer) throws IOException {

        QuestionImageWrapper questionImageWrapper = getImageList(element, questionAnswer.getId(), IMAGETYPE.QUESTIONANSWER, questionAnswer.getFullAnswer());
        if (questionImageWrapper != null) {
            questionAnswer.setFullAnswer(questionImageWrapper.getContent());
            if (questionImageWrapper.getQuestionImageList() != null && questionImageWrapper.getQuestionImageList().size() > 0) {
                List<QuestionImage> questionImageList = questionImageWrapper.getQuestionImageList();
                for (QuestionImage questionImage : questionImageList) {
                    questionImage.setQuestionAnswer(questionAnswer);
                    this.questionImageList.add(questionImage);
                }
            }
        }
        this.questionAnswerList.add(questionAnswer);
    }

    protected  void crawleQuestionAnalysisImage(Element element,QuestionAnalysis questionAnalysis) throws IOException {
        QuestionImageWrapper questionImageWrapper = getImageList(element,questionAnalysis.getId(),IMAGETYPE.QUESTIONANALYSIS,questionAnalysis.getFullAnalysis());
        if(questionImageWrapper !=null) {
            questionAnalysis.setFullAnalysis(questionImageWrapper.getContent());
            if (questionImageWrapper.getQuestionImageList() != null && questionImageWrapper.getQuestionImageList().size() > 0) {
                List<QuestionImage> questionImageList = questionImageWrapper.getQuestionImageList();
                for (QuestionImage questionImage : questionImageList) {
                    questionImage.setQuestionAnalysis(questionAnalysis);
                    this.questionImageList.add(questionImage);
                }
            }
        }
        this.questionAnalysisList.add(questionAnalysis);
    }


    protected void crawleQuestionChoiceItemImage(Element element,QuestionChoiceItem questionChoiceItem) throws IOException {
        QuestionImageWrapper questionImageWrapper = getImageList(element,questionChoiceItem.getId(),IMAGETYPE.QUESTIONCHOICEITEM,questionChoiceItem.getFullContent());
        if(questionImageWrapper!=null)
        {
            questionChoiceItem.setFullContent(questionImageWrapper.getContent());
            if(questionImageWrapper.getQuestionImageList()!=null && questionImageWrapper.getQuestionImageList().size() >0 )
            {
                List<QuestionImage> questionImageList = questionImageWrapper.getQuestionImageList();
                for(QuestionImage questionImage:questionImageList) {
                    questionImage.setQuestionChoiceItem(questionChoiceItem);
                    this.questionImageList.add(questionImage);
                }
            }
        }
        this.questionChoiceItemList.add(questionChoiceItem);
    }

    public QuestionImageWrapper getImageList(Element element,
                                             String parentId,
                                             IMAGETYPE imageType, String fullContent) throws IOException {

        QuestionImageWrapper questionImageWrapper = new QuestionImageWrapper();
        Elements imgElements = element.getElementsByTag("img");

        int index = 1;
        for (Element imgElement : imgElements) {
            String imgUrl = imgElement.attributes().get("src");
            QuestionImage questionImage = ImageHelper.getQuestionImageFromUrl(imgUrl);
            if (questionImage == null) {
                continue;
            }

            questionImage.setId(parentId.concat("-").concat(String.valueOf(index)));
            fullContent = fullContent.replace(imgElement.toString(), String.format("@%s@", questionImage.getId()));

            switch (imageType) {
                case QUESTIONANSWER:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONANSWER));
                    break;
                case QUESTIONANALYSIS:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONANALYSIS));
                    break;
                case QUESTIONCONTENT:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONCONTENT));
                case QUESTIONCHOICEITEM:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONCHOICEITEM));
                default:
                    break;
            }

            if (crawlerConfiguration != null && crawlerConfiguration.getQuestionImagePath().length() > 0) {
                String fileName = questionImage.getId().concat(".").concat(questionImage.getExtensionName());
                String filePath = crawlerConfiguration.getQuestionImagePath().concat(File.separator).concat(questionImage.getType().toLowerCase().toString());

                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (imageSave(filePath.concat(File.separator).concat(fileName), questionImage.getImage())) {
                    if(File.separator.equals("\\")){
                        questionImage.setFileName(filePath.concat(File.separator).concat(fileName).replace("\\","/"));
                    }else {
                        questionImage.setFileName(filePath.concat(File.separator).concat(fileName));
                    }
                }
            }
            questionImageWrapper.addQuestionImage(questionImage);
            index++;
        }

        questionImageWrapper.setContent(fullContent);

        return questionImageWrapper;
    }


    protected static String getId(String content) {
        if (content == null || content.length() <= 0) {
            return "";
        }
        return String.valueOf(Math.abs(content.hashCode()));
    }

    private boolean imageSave(String imagePath, byte[] imageByte) throws IOException {

        if (imageByte.length < 3 || imageByte.equals("")) {
            return false;
        }
        try {
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(imagePath));
            imageOutput.write(imageByte, 0, imageByte.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + imagePath);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
            return false;
        }
        return true;
    }


}

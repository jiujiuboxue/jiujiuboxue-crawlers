package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.crawler.CrawlerConfiguration;
import com.jiujiuboxue.crawler.util.ImageHelper;
import com.jiujiuboxue.module.tiku.entity.IMAGETYPE;
import com.jiujiuboxue.module.tiku.entity.QuestionImage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * @author wayne
 */
public class CrawlerBase {

    @Autowired
    private CrawlerConfiguration crawlerConfiguration;

    protected Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    protected Elements getElements(String url, String query) throws IOException {
        Document doc = getDocument(url);
        return doc.select(query);
    }


    protected Elements getElementsFromStringByTag(String content, String tag) {
        Document doc = Jsoup.parse(content);
        return doc.getElementsByTag(tag);
    }

    public QuestionImageWarpper getImageList(Element element,
                                             String parentId,
                                             IMAGETYPE imageType, String fullContent) throws IOException {

        QuestionImageWarpper questionImageWarpper = new QuestionImageWarpper();
        Elements imgElements = element.getElementsByTag("img");

        int index = 1;
        for (Element imgElement : imgElements) {
            String imgUrl = imgElement.attributes().get("src");
            QuestionImage questionImage = ImageHelper.getQuestionImageFromUrl(imgUrl);
            if (questionImage == null) {
                continue;
            }

            switch (imageType) {
                case QUESTIONANSWER:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONANSWER));
                    break;
                case QUESTIONANALYSIS:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONANALYSIS));
                    break;
                case QUESTIONCONTENT:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONCONTENT));
                    break;
            }

            questionImage.setId(parentId.concat("-").concat(String.valueOf(index)));
            fullContent = fullContent.replace(imgElement.toString(), String.format("@%s@", questionImage.getId()));
            index++;
            questionImage.setType(imageType.toString());

            questionImageWarpper.addQuestionImage(questionImage);

            if (crawlerConfiguration != null && crawlerConfiguration.getQuestionImagePath().length() > 0) {

                String fileName = questionImage.getId().concat(".").concat(questionImage.getExtensionName());
                String filePath = crawlerConfiguration.getQuestionImagePath().concat(File.separator).concat(questionImage.getType().toLowerCase().toString());

                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                if(imageSave(filePath.concat(File.separator).concat(fileName),questionImage.getImage()))
                {
                    questionImage.setPath(fileName);
                }
            }
        }
        questionImageWarpper.setContent(fullContent);
        return questionImageWarpper;
    }


    public static String getId(String content) {
        if (content == null || content.length() <= 0) {
            return "";
        }
        return String.valueOf(Math.abs(content.hashCode()));
    }

    public boolean imageSave(String imagePath, byte[] imageByte) throws IOException {

        if(imageByte.length<3||imageByte.equals("")) {
            return false;
        }
        try{
                FileImageOutputStream imageOutput = new FileImageOutputStream(new File(imagePath));
                imageOutput.write(imageByte, 0, imageByte.length);
                imageOutput.close();
                System.out.println("Make Picture success,Please find image in " + imagePath);
            } catch(Exception ex) {
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
                return false;
            }
        return true;
    }
}

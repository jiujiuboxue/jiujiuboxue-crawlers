package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.common.utils.ImageUtil;
import com.jiujiuboxue.crawler.CrawlerConfiguration;
import com.jiujiuboxue.crawler.util.ImageHelper;
import com.jiujiuboxue.module.tiku.entity.IMAGETYPE;
import com.jiujiuboxue.module.tiku.entity.QuestionImage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

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


//    public static String getSHA256(String str) {
//        MessageDigest messageDigest;
//        String encodestr = str;
//        try {
//            messageDigest = MessageDigest.getInstance("SHA-256");
//            messageDigest.update(str.getBytes("UTF-8"));
//            encodestr = byte2Hex(messageDigest.digest());
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return encodestr;
//    }
//
//    private static String byte2Hex(byte[] bytes){
//        StringBuffer stringBuffer = new StringBuffer();
//        String temp = null;
//        for (int i=0;i<bytes.length;i++){
//            temp = Integer.toHexString(bytes[i] & 0xFF);
//            if (temp.length()==1){
//                stringBuffer.append("0");
//            }
//            stringBuffer.append(temp);
//        }
//        return stringBuffer.toString();
//    }

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
                File file = new File(crawlerConfiguration.getQuestionImagePath());
                if (!file.exists()) {
                    file.mkdir();
                }

                String fileName = questionImage.getId().concat(".").concat(questionImage.getExtensionName());
                String filePath = crawlerConfiguration.getQuestionImagePath().concat(File.pathSeparator).concat(questionImage.getType().toString());
                if(imageSave(filePath,fileName,questionImage.getImage()))
                {
                    questionImage.setPath(filePath.concat(File.pathSeparator).concat(fileName));
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


    public boolean imageSave(String imagePath, String imageName, byte[] imageByte) throws IOException {
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdir();
        }

        return ImageUtil.generateImage(new String(imageByte),imagePath,imageName);

    }
}

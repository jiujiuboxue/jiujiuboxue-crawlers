package com.jiujiuboxue.crawler.web.impl;

import com.jiujiuboxue.crawler.util.ImageUtil;
import com.jiujiuboxue.module.tiku.entity.IMAGETYPE;
import com.jiujiuboxue.module.tiku.entity.QuestionImage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author wayne
 */
public class CrawlerBase {

    protected Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    protected Elements getElements(String url, String query) throws IOException {
        Document doc = getDocument(url);
        return doc.select(query);
    }


    protected Elements getElementsFromStringByTag(String content,String tag)
    {
       Document doc = Jsoup.parse(content);
       return doc.getElementsByTag(tag);
    }


    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = str;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
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
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONANSWER));
                    break;
                case QUESTIONANALYSIS:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONANALYSIS));
                    break;
                case QUESTIONCONTENT:
                    questionImage.setType(String.valueOf(IMAGETYPE.QUESTIONCONTENT));
                    break;
            }
            questionImage.setId(parentId.concat(String.valueOf(index)));
            fullContent = fullContent.replace(imgElement.toString(), String.format("@%s@", questionImage.getId()));
            index++;
            questionImage.setType(imageType.toString());
            questionImageWarpper.getQuestionImageList().add(questionImage);
        }
        questionImageWarpper.setContent(fullContent);
        return questionImageWarpper;
    }




}

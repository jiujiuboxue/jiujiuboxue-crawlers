package com.jiujiuboxue.crawler.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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


}

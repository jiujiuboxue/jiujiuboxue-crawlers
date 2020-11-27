package com.jiujiuboxue.crawler.util;

import com.jiujiuboxue.modules.tiku.entity.QuestionImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author wayne
 */
public class ImageUtil {

    public static QuestionImage getQuestionImageFromUrl(String imgUrl) {
        int retry = 0;
        boolean success = false;
        byte[] bytes = null;
        String imagesExtends = null;
        QuestionImage questionImage = new QuestionImage();
        while (success != true && retry <= 3) {
            try {
                String[] tmpList = imgUrl.split("\\.");
                imagesExtends = (tmpList == null || tmpList.length <= 0) ? "" : tmpList[tmpList.length - 1];
                URL url = new URL(imgUrl);
                BufferedImage image = ImageIO.read(url);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, imagesExtends, out);
                bytes = out.toByteArray();
                success = true;
                questionImage = new QuestionImage();
                questionImage.setExtensionName(imagesExtends);
                questionImage.setImage(bytes);

            } catch (Exception ex) {
              System.out.println(ex.toString());
            }
            retry++;
        }

        return questionImage;
    }


}

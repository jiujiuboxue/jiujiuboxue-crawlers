package com.jiujiuboxue.crawler;

import com.jiujiuboxue.module.tiku.entity.QuestionAnswer;
import com.jiujiuboxue.module.tiku.service.QuestionAnalysisService;
import com.jiujiuboxue.module.tiku.service.QuestionAnswerService;
import com.jiujiuboxue.module.tiku.service.QuestionService;
import com.jiujiuboxue.module.tiku.service.impl.QuestionAnalysisServiceImpl;
import com.jiujiuboxue.module.tiku.service.impl.QuestionAnswerServiceImpl;
import com.jiujiuboxue.module.tiku.service.impl.QuestionServiceImpl;
import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wayne
 */
@Component
@Configuration()
@ConfigurationProperties()
@Data
public class CrawlerConfiguration {

    @Value("${jiujiuboxue.crawler.question.image.path}")
    private String questionImagePath;

//    @Bean
//    public QuestionService getQuestionService()
//    {
//        return new QuestionServiceImpl();
//    }
//
//    @Bean
//    public QuestionAnalysisService getQuestionAnalysisService()
//    {
//        return new QuestionAnalysisServiceImpl();
//    }
//
//    @Bean
//    public QuestionAnswerService getQuestionAnswer(){
//        return new QuestionAnswerServiceImpl();
//    }


}

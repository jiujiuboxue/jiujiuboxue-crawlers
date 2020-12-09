package com.jiujiuboxue.crawler;


import com.jiujiuboxue.crawler.web.impl.CrawlerFrom21Crawler;
import com.jiujiuboxue.module.tiku.entity.Question;
import com.jiujiuboxue.module.tiku.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author wayne
 */
@Component
public class CrawlerRunner implements CommandLineRunner {


    private CrawlerFrom21Crawler crawler;

    private QuestionService questionService;


    @Autowired
    public void setQuestionService(QuestionService questionService)
    {
        this.questionService= questionService;
    }

    @Autowired
    public void setWebClient(CrawlerFrom21Crawler crawler) {
        this.crawler = crawler;
    }

    @Override
    public void run(String... args) throws Exception {

        List<Question> questionList = crawler.crawler("http://tiku.21cnjy.com/tiku.php?mod=quest&channel=3&cid=0&type=1&xd=2", null,null,null);

        for (Question question :
                questionList) {
            try{
                questionService.save(question);
            }catch (Exception ex)
            {
                System.out.println(ex.toString());
            }


        }
    }
}

package com.bigsai.wind;

import com.bigsai.wind.dao.govMapper;
import com.bigsai.wind.pojo.gov;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@SpringBootTest
class WindApplicationTests {

    @Autowired(required = false)
    private com.bigsai.wind.parse.pageUtils pageUtils;
    @Autowired(required = false)
    private govMapper govMapper;

    @Test
    void contextLoads() throws IOException, InterruptedException, ParseException {
//        String url="http://sthjt.sc.gov.cn/sthjt/c100456/zclistf.shtml";

  gov gov=new gov("a","a","s","d",new Date(),new Date());
  govMapper.updategov(gov);
    }

}

package com.bigsai.wind.parse;

import com.alibaba.fastjson.JSON;
import com.bigsai.wind.dao.govMapper;
import com.bigsai.wind.pojo.gov;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class pageAnalyse implements Runnable
{
    @Value("${wind.path}")
    String path;

    private govMapper govmapper=SpringContextUtil.getBean(govMapper.class);
    private Logger log= LoggerFactory.getLogger(pageAnalyse.class);

    private String url;
    private File file;
    //暴露在外使用
    public pageAnalyse(File file)
    {
        this.file=file;
    }
    /**
     * 解析当前文件 三个步骤
     * 1：从zip文件提取html页面字符
     * 2：从html页面提取我们需要的内容
     * 3.持久化储存
     * @throws IOException
     */
    public void doAnalyse() throws IOException, ParseException {
        String html=analyseZipFile();
        gov gov=analyseHtml(html);
        gov team=govmapper.getgovbyurl(gov.getUrl());
        if(team!=null)
            govmapper.updategov(gov);
        else
            govmapper.insertgov(gov);

    }
    private String analyseZipFile() throws IOException {
        ZipFile zf=new ZipFile(file);
        InputStream inputStream =new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zipin=new ZipInputStream(inputStream);
        ZipEntry zipEntry;
        while ((zipEntry=zipin.getNextEntry())!=null) {//多个文件，但实际只有一个文件
            url=zipEntry.getName();
            BufferedReader bReader=new BufferedReader(new InputStreamReader(zf.getInputStream(zipEntry),"UTF-8"));
            StringBuilder sBuilder=new StringBuilder();
            String team="";
            while ((team=bReader.readLine())!=null) {
                sBuilder.append(team+"\n");
            }
           return sBuilder.toString();
        }
        return  null;
    }

    /**
     * 从html页面中提取需要的信息，返回一个gov对象
     * @param html
     * @return gov
     */
    private gov analyseHtml(String html) throws ParseException {
        Document doc= Jsoup.parse(html);
        Element element=doc.getElementsByClass("p_chusxl").first();
        String title=element.selectFirst("h3[align=center]").text();//标题

        String releaseTime=element.select(".fr").first().text().substring(6,25);//发行时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date releasedate = sdf.parse(releaseTime);//转化格式

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        Date operateTime = calendar.getTime();

        String name=url.split("\\.")[0];//name
        String originalurl=govmapper.getOriginalurl(name);//url

        Map<String,Object> contentMap=new HashMap<>();
        contentMap.put("title",title);
        contentMap.put("releaseTime",releaseTime.substring(0,10));
        contentMap.put("URL",originalurl);
        contentMap.put("zipName",name);
        Map<String,String>tableContent=new HashMap<>();

        Elements elements=doc.select("tr");//去掉第一个标题

        if(elements.size()==2)//上下摆放
        {
            Elements leftnodes=elements.first().select("td");
            Elements rightnodes=elements.last().select("td");
            Iterator<Element> lit=leftnodes.iterator();
            Iterator<Element> rit=rightnodes.iterator();
            while (lit.hasNext())
            {
                String left=lit.next().text();
                //left=EngChWordChangeUtils.chinaToEnglish(left);
                String right=rit.next().text();
                tableContent.put(left,right);
            }
        }
        else {
//            if(elements.first().select("td").size()==1)//个别第一个会是标题
//            elements = elements.next();
            for (Element e : elements) {
                String left = e.select("td").first().text();
                //left = EngChWordChangeUtils.chinaToEnglish(left);
                String right = e.select("td").next().text();
                if(left!=null&&!"".equals(left.trim()))
                tableContent.put(left, right);
            }
        }
        contentMap.put("content",tableContent);
        String contentjsonStr= JSON.toJSONString(contentMap);
        String caseNumber="";//行政处罚决定书名称及文号
        if(tableContent.containsKey("行政处罚决定书文号"))caseNumber=tableContent.get("行政处罚决定书文号");
        if(tableContent.containsKey("行政处罚决定书名称及文号"))caseNumber=tableContent.get("行政处罚决定书名称及文号");
        gov gov=new gov(caseNumber,title,contentjsonStr,originalurl,releasedate,operateTime);
        return  gov;
    }
    @Override
    public void run() {
        try {
            doAnalyse();
            log.info(Thread.currentThread().getName()+" 解析"+url+"成功");
        } catch (IOException e) {
           log.info(e.toString());
        } catch (ParseException e) {
            log.info(e.toString());
        }
    }
}

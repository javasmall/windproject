package com.bigsai.wind.parse;

import com.bigsai.wind.dao.govMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class pageUtils {
    @Value("${wind.path}")
    String path;
    @Autowired(required = false)
    private govMapper govMapper;

    /**
     * 对外暴露下载 分两部：1：获取所有urls   2：下载对应url
     * 借助多线程类pageDownload完成
     * @throws IOException
     * @throws InterruptedException
     */
    public void download() throws IOException, InterruptedException {
        List<String>urls=getAllpages();
        pagedownload(urls);
    }
    /**
     * 解析zip文件得内容 借助多线程类pageAnalyse完成
     * @throws IOException
     * @throws ParseException
     */
    public void readFile() throws IOException, ParseException {
        ExecutorService ex= Executors.newFixedThreadPool(6);
        File file=new File(path);
        File [] files=file.listFiles();
        for(int i=0;i<files.length;i++)
        {
            pageAnalyse pageAnalyse=new pageAnalyse(files[i]);
            ex.execute(pageAnalyse);
        }
        ex.shutdown();
        while (true) {//等待所有任务都执行结束
            if (ex.isTerminated()) {//所有的子线程都结束了
                break;
            }
        }
    }

    private List<String>getAllpages() throws IOException {
        List<String>pageList=new ArrayList<>();//4个主页面
        List<String>pageurls=new ArrayList<>();//返回页面的url
        pageList.add("http://sthjt.sc.gov.cn/sthjt/c100456/zclistf.shtml");
        pageList.add("http://sthjt.sc.gov.cn/sthjt/c100456/zclistf_2.shtml");
        pageList.add("http://sthjt.sc.gov.cn/sthjt/c100456/zclistf_3.shtml");
        pageList.add("http://sthjt.sc.gov.cn/sthjt/c100456/zclistf_4.shtml");

        for(int i=0;i<pageList.size();i++)
        {
            Document document= Jsoup.connect(pageList.get(i)).get();
            Elements docs=document.getElementsByClass("p_ty_list").select("a[href]");
            for(Element element:docs)
            {
                String urlstr=element.absUrl("href");
                pageurls.add(urlstr);
            }
        }

        return pageurls;
    }

    private void pagedownload(List<String>urls) throws InterruptedException {
        ExecutorService ex= Executors.newFixedThreadPool(6);
        for(int i=0;i<urls.size();i++)
        {
            //System.out.println(path);
            //文件url 入库
            String url=urls.get(i);
            String u[]=url.split("/");
            String filename=u[u.length-1].split("\\.")[0];//efaagdagadgagadgadg等 系列字符串
            //url可能存在
            try {
                govMapper.inserturl(filename,url);
            }
            catch (Exception e){}

            pageDownload download=new pageDownload(urls.get(i),path);
            ex.execute(download);

        }
        ex.shutdown();
        while (true) {//等待所有任务都执行结束
            if (ex.isTerminated()) {//所有的子线程都结束了
                break;
            }
        }


    }


}

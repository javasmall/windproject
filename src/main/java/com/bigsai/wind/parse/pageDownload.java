package com.bigsai.wind.parse;



import com.bigsai.wind.dao.govMapper;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class pageDownload implements Runnable{

    private Logger log= LoggerFactory.getLogger(pageDownload.class);
    private  String url="";
    private String path="";
    public pageDownload(String url,String path){this.url=url;this.path=path;}
    public void  downloadpage() throws IOException {
               Response response = Jsoup.connect(url)
                .method(Method.GET)
                .execute();
               BufferedInputStream buff=response.bodyStream();//文本流
               String u[]=url.split("/");
               String htmlfilename=u[u.length-1];//efaagdagadgagadgadg等 系列字符串
               String zipfilename=path+"\\"+htmlfilename.split("\\.")[0]+".zip";



               //文件保存
               File file=new File(path);
               if(!file.exists())
               {
                   file.mkdirs();
                   //file.createNewFile();
               }
                OutputStream outputStream=new FileOutputStream(zipfilename);//zip
                ZipOutputStream zipout=new ZipOutputStream(outputStream);//zip 流


                zipout.putNextEntry(new ZipEntry(htmlfilename));
                byte b[]=new byte[1024*5];
                int a=0;
                while((a=buff.read(b))!=-1)
                {
                    zipout.write(b);
                }

                zipout.finish();
                zipout.close();
                outputStream.close();
                buff.close();

    }

    public void setPath(String path)
    {
        this.path=path;
    }
    public String getPath()
    {
        return  path;
    }
    @Override
    public void run() {
        try {
            downloadpage();
            log.info(Thread.currentThread().getName()+" 下载"+url+"成功");
        } catch (IOException e) {
            log.info(e.toString());
        }
    }
}

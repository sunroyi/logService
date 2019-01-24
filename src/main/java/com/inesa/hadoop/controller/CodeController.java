package com.inesa.hadoop.controller;

import com.inesa.common.entity.RestfulResult;
import com.inesa.common.utils.CommUtils;
import com.inesa.common.utils.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * 自动代码生成Controller
 * 
 * @author sun
 * @version 2018-06-01
 */
@RestController
@RequestMapping(value = "code")
public class CodeController {

    private static String HADOOP_SERVER = "hdfs://192.168.8.80:9000";
    private static String CHARSET = "UTF-8";
    private static String HADOOP_USER_NAME = "hadoop";

    private static Logger logger = Logger.getLogger(CodeController.class.getName());

    @RequestMapping(value = "test")
	public void test(HttpServletRequest request, HttpServletResponse response) {
		RestfulResult restfulResult = new RestfulResult();
		
		try{
			restfulResult.setData("Test");

		}catch(Exception e){
			restfulResult.setResult(Constants.RESULT_STATE_ERROR);
			restfulResult.setMessage(e.getMessage());
		}
		
    	CommUtils.printDataJason(response, restfulResult);
	}

    @RequestMapping(value = "upload")
    public void upload(HttpServletRequest request, HttpServletResponse response
            /*, @RequestParam("file") File file*/) {
        RestfulResult restfulResult = new RestfulResult();

        try{
            System.setProperty("HADOOP_USER_NAME", HADOOP_USER_NAME);

            Configuration config = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(HADOOP_SERVER), config);
            Path resP = new Path("D://SUN//movie//谍影迷情//谍影迷情.S01E01.mkv");
            Path destP = new Path("/user/sun/upload");
            if(!fs.exists(destP)){
                fs.mkdirs(destP);
            }

            fs.copyFromLocalFile(resP, destP);
            fs.close();

        }catch(Exception e){
            restfulResult.setResult(Constants.RESULT_STATE_ERROR);
            restfulResult.setMessage(e.getMessage());
        }

        CommUtils.printDataJason(response, restfulResult);
    }

    @RequestMapping(value = "download")
    public void download(HttpServletRequest request, HttpServletResponse response
            /* ,@RequestParam("path") String path*/) {
        RestfulResult restfulResult = new RestfulResult();

        try {
            // TODO

        } catch (Exception e) {
            restfulResult.setResult(Constants.RESULT_STATE_ERROR);
            restfulResult.setMessage(e.getMessage());
        }

        CommUtils.printDataJason(response, restfulResult);
    }

    @RequestMapping(value = "writeLog")
    public void writeLog(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("text") String text) {

        RestfulResult restfulResult = new RestfulResult();

        try{
            System.setProperty("HADOOP_USER_NAME", HADOOP_USER_NAME);

            Path destP = new Path("/user/sun/log");
            Path logP = new Path("/user/sun/log/new.log");

            Configuration config = new Configuration();
            FileSystem fs = FileSystem.get(URI.create(HADOOP_SERVER), config);
            FSDataOutputStream os;
            if(!fs.exists(destP)){
                fs.mkdirs(destP);
            }

            // 列出hdfs上/user/sun/目录下的所有文件和目录
            FileStatus[] statuses = fs.listStatus(destP);
            for (FileStatus status : statuses) {
                System.out.println(status);
            }

            if(!fs.exists(logP)){
                //创建文件数据的输出流
                os=fs.create(logP);
                //通过输出流往hdfs中写入数据
                os.write(text.getBytes(CHARSET),0,text.getBytes(CHARSET).length);
                os.write("\n".getBytes(CHARSET),0,"\n".getBytes(CHARSET).length);
                os.flush();
                os.close();
            }else{
                //往文件中追加数据
                os=fs.append(logP);
                os.write(text.getBytes(CHARSET),0,text.getBytes(CHARSET).length);
                os.write("\n".getBytes(CHARSET),0,"\n".getBytes(CHARSET).length);
                os.flush();
                os.close();
            }

            // 显示在hdfs的/user/sun下指定文件的内容
            InputStream is = fs.open(logP);
            IOUtils.copyBytes(is, System.out, 1024, true);

            fs.close();

        }catch(Exception e){
            restfulResult.setResult(Constants.RESULT_STATE_ERROR);
            restfulResult.setMessage(e.getMessage());
        }

        CommUtils.printDataJason(response, restfulResult);
    }

    /*
     * 从hdfs中读取数据
     */
    @RequestMapping(value = "readLog")
    public void readLog(HttpServletRequest request, HttpServletResponse response){
        RestfulResult restfulResult = new RestfulResult();

        Path logP = new Path("/user/sun/log/new.log");

        Configuration conf=new Configuration();
        try {
            FileSystem fs=FileSystem.get(URI.create(HADOOP_SERVER),conf);
            if(fs.exists(logP)){
                //打开文件数据输入流
                FSDataInputStream fsDataInputStream=fs.open(logP);
                //创建文件输入
                InputStreamReader inputStreamReader=new InputStreamReader(fsDataInputStream,CHARSET);
                String line=null;
                //把数据读入到缓冲区中
                BufferedReader reader=null;
                reader=new BufferedReader(inputStreamReader);
                //从缓冲区中读取数据
                StringBuilder sb = new StringBuilder();
                while((line=reader.readLine())!=null){
                    // System.out.println("line="+line);
                    sb.append(line).append("\r\n");;
                }
                restfulResult.setData(sb.toString());
            }
            fs.close();
        } catch (Exception e) {
            restfulResult.setResult(Constants.RESULT_STATE_ERROR);
            restfulResult.setMessage(e.getMessage());
        }

        CommUtils.printDataJason(response, restfulResult);
    }

    @RequestMapping(value = "log")
    public void log(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam("text") String text) {

        RestfulResult restfulResult = new RestfulResult();

        try{
            logger.info(text);
        }catch(Exception e){
            logger.error(e.getMessage());
            restfulResult.setResult(Constants.RESULT_STATE_ERROR);
            restfulResult.setMessage(e.getMessage());
        }

        CommUtils.printDataJason(response, restfulResult);
    }
}
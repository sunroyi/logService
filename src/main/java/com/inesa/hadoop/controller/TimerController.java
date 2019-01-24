/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.inesa.hadoop.controller;

import com.alibaba.druid.util.StringUtils;
import com.inesa.hadoop.entity.FileReadLogEntity;
import com.inesa.hadoop.entity.LogEntity;
import com.inesa.hadoop.service.FileReadLogService;
import com.inesa.hadoop.service.LogService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import scala.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;

/**
 * 定时器Controller
 * @author sun
 * @version 2019-01-09
 */
@Controller
@RequestMapping(value = "timer")
public class TimerController implements Serializable {

    @Autowired
    private LogService logService;

    @Autowired
    private FileReadLogService fileReadLogService;

	public void execute(HttpServletRequest request, HttpServletResponse response) {

    	try{
			System.out.println("timer execute success" + new java.sql.Timestamp(System.currentTimeMillis()).toString());

    	}catch(Exception e){
    		System.out.println("Error:" + e.getMessage());
    	}
	}

    @Scheduled(cron = "0 * * * * *")    // 每隔1分钟执行一次
    public void hdfsToMssql() {
        System.out.println("hdfsToMssql Start:" + new java.sql.Timestamp(System.currentTimeMillis()).toString());

        SparkConf conf=new SparkConf().setMaster("local").setAppName("sunTest");
        JavaSparkContext sc =new JavaSparkContext(conf);

        try{
            //String mod = "test";
            String mod = "centos";

            if (mod.equals("test")){
                String path = "D:/tmp/";
                // 获得指定文件对象
                File file = new File(path);
                // 获得该文件夹内的所有文件
                File[] array = file.listFiles();
                for(int i=0;i<array.length;i++) {
                    //如果是文件夹
                    if(array[i].isDirectory()) {
                        if (!existTable(array[i].getName()))
                            createTable(array[i].getName());

                        // 获取此目录下的文件列表
                        String pathSub = array[i] + "/";
                        // 获得指定文件对象
                        File fileSub = new File(pathSub);
                        // 获得该文件夹内的所有文件
                        File[] arraySub = fileSub.listFiles();

                        // 获得文件读取履历
                        boolean blnRead = true;     // 是否能够读取此文件
                        FileReadLogEntity fileReadLog = new FileReadLogEntity();
                        fileReadLog.setApp(array[i].getName());
                        fileReadLog.setService("all");
                        List<FileReadLogEntity> logList = fileReadLogService.findList(fileReadLog);
                        if (logList.size()>0) {
                            fileReadLog = logList.get(0);
                            blnRead = false;
                        }

                        for(int j=0;j<arraySub.length;j++) {
                            //如果是文件
                            if(arraySub[j].isFile())
                            {
                                if (arraySub[j].getName().indexOf(".tmp")>0) {
                                    continue;
                                }

                                // 判断已读取过的日志的日期是否在当前文件之前
                                if (Long.parseLong(fileReadLog.getName().replace("FlumeData", "").replace(".txt", ""))
                                        < Long.parseLong(arraySub[j].getName().replace("FlumeData", "").replace(".txt", "")))
                                    blnRead = true;

                                // 如果可以读取
                                if (blnRead) {
                                    fileProcess(conf, sc, pathSub, arraySub[j].getName(), array[i].getName());
                                    //System.out.println(pathSub + arraySub[j].getName());

                                    // 插入文件读取履历
                                    fileReadLog.setName(arraySub[j].getName());
                                    fileReadLogService.save(fileReadLog);
                                }else{
                                    // 找到上次读取过的文件以后，下一个文件开始就可以进行读取
                                    if (fileReadLog.getName().equals(arraySub[j].getName()))
                                        blnRead = true;
                                }
                            }
                        }
                    }
                }
            }else if (mod.equals("centos")){
                String path = "hdfs://hadoop:9000/";
                Configuration configuration = new Configuration();
                FileSystem fs = FileSystem.get(URI.create(path), configuration);
                FileStatus[] status = fs.listStatus(new Path(path));
                for (FileStatus file : status) {
                    if (file.isDirectory()){
                        if (!existTable(file.getPath().getName()))
                            createTable(file.getPath().getName());

                        // 获取此目录下的文件列表
                        String pathSub = file.getPath() + "/";
                        // 获得指定文件对象
                        FileSystem fsSub = FileSystem.get(URI.create(pathSub), configuration);
                        // 获得该文件夹内的所有文件
                        FileStatus[] statusSub = fsSub.listStatus(new Path(pathSub));

                        // 获得文件读取履历
                        boolean blnRead = true;     // 是否能够读取此文件
                        FileReadLogEntity fileReadLog = new FileReadLogEntity();
                        fileReadLog.setApp(file.getPath().getName());
                        fileReadLog.setService("all");
                        List<FileReadLogEntity> logList = fileReadLogService.findList(fileReadLog);
                        if (logList.size()>0) {
                            fileReadLog = logList.get(0);
                            blnRead = false;
                        }

                        for (FileStatus fileSub : statusSub) {
                            //如果是文件
                            if(fileSub.isFile())
                            {
                                if (fileSub.getPath().getName().indexOf(".tmp")>0)
                                    continue;

                                if (StringUtils.isEmpty(fileSub.getPath().getName())
                                        || !StringUtils.isNumber(fileSub.getPath().getName().replace("FlumeData.", "")))
                                    continue;

                                // 判断已读取过的日志的日期是否在当前文件之前
                                if (Long.parseLong(fileSub.getPath().getName().replace("FlumeData.", ""))
                                        < Long.parseLong(fileSub.getPath().getName().replace("FlumeData.", "")))
                                    blnRead = true;

                                if (blnRead) {
                                    fileProcess(conf, sc, pathSub, fileSub.getPath().getName(), file.getPath().getName());
                                    //System.out.println(pathSub + fileSub.getPath().getName());

                                    // 插入文件读取履历
                                    fileReadLog.setName(fileSub.getPath().getName());
                                    fileReadLogService.save(fileReadLog);
                                }else{
                                    // 找到上次读取过的文件以后，下一个文件开始就可以进行读取
                                    if (fileReadLog.getName().equals(fileSub.getPath().getName()))
                                        blnRead = true;
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("hdfsToMssql Success:" + new java.sql.Timestamp(System.currentTimeMillis()).toString());

        }catch(Exception e){
            System.out.println("hdfsToMssql Error:" + e.getMessage());
        }finally{
            sc.close();
        }
    }

    private void fileProcess(SparkConf conf, JavaSparkContext sc,String path, String fileName, String folderName) throws UnsupportedEncodingException {
        fileProcess(conf, sc, path, fileName, folderName, "");
    }

    private void fileProcess(SparkConf conf, JavaSparkContext sc,String path, String fileName, String folderName, String startName) throws UnsupportedEncodingException {

        JavaRDD<String> logData=sc.textFile(path + fileName).cache();

/*        JavaRDD<String> result = logData.filter(new Function<String, Boolean>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Boolean call(String s) throws Exception {
                return s.startsWith("[format:1]");
            }
        });*/

/*            result.foreach(new VoidFunction<String>(){
                private static final long serialVersionUID = 1L;

                int i=1;

                @Override
                public void call(String s) throws Exception {
                    System.out.println("Line"+ i +":" + s);
                    i++;
                }
            });*/

        for(String line:logData.collect()){
            //line = getUTF8StringFromGBKString(line);

            if (!line.startsWith("[format:1]"))
                continue;

            LogEntity logEntity = new LogEntity();
            String[] lineArray = line.split("]\\[");
            for(int i=0;i<lineArray.length;i++){
                // 清理前后括号
                if(i==0)
                    lineArray[i] = lineArray[i].substring(1);
                else if (i==lineArray.length-1)
                    lineArray[i] = lineArray[i].substring(0, lineArray[i].length()-1);

                // 分离key和value
                int index = lineArray[i].indexOf(":");
                String key = lineArray[i].substring(0, index);
                key=key.substring(0,1).toUpperCase().concat(key.substring(1));
                String value = lineArray[i].substring(index+1);
                Field field = null;
                try {
/*                    if (key.indexOf("Date")>0) {
                        Method m = logEntity.getClass().getDeclaredMethod("set" + key, Date.class);
                        m.setAccessible(true);
                        m.invoke(logEntity, value);
                    }
                    else{*/
                    if (key.equals("UserName")
                            || key.indexOf("param") > 0
                            || key.indexOf("Param") > 0){
                        Method m = logEntity.getClass().getDeclaredMethod("set" + key, String.class);
                        m.setAccessible(true);
                        value = value.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B");
                        //System.out.println(value);
                        m.invoke(logEntity, URLDecoder.decode(value, "UTF-8"));
                    }else {
                        Method m = logEntity.getClass().getDeclaredMethod("set" + key, String.class);
                        m.setAccessible(true);
                        m.invoke(logEntity, value);
                    }
                    //}
/*                    field = logEntity.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    if (key.indexOf("Date")>0)
                        field.set(logEntity, Date.valueOf(value));  // Date类型字段设置
                    else
                        field.set(logEntity, value);    // String类型字段设置*/
                }catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            logEntity.setApp(folderName);
            logEntity.setFileName(fileName);
            line = line.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B");
            logEntity.setDetail(URLDecoder.decode(line, "UTF-8"));
            logService.save(logEntity);
        }
    }

    private void createTable(String folderName){
        LogEntity logEntity = new LogEntity();
        logEntity.setApp(folderName);
        logService.createTable(logEntity);
    }

    private boolean existTable(String folderName){
        LogEntity logEntity = new LogEntity();
        logEntity.setApp(folderName);
        List<LogEntity> tableList = logService.findTable(logEntity);
        if (tableList.size()>0)
            return true;
        else
            return false;
    }
    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }
}
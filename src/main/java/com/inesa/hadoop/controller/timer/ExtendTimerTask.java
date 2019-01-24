package com.inesa.hadoop.controller.timer;

import com.inesa.common.utils.PropertyUtil;
import com.inesa.common.utils.RestUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimerTask;

public class ExtendTimerTask extends TimerTask {
	
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    @Override
    public void run() {
        try {
            String path = System.getProperty("user.dir");
            System.out.println(path);
            Properties properties = PropertyUtil.getProperties("application.properties");

        	System.out.println("Address:" + properties.getProperty("timerPath") + "timer/execute");
        	System.out.println("Message Count:" + RestUtil.getRest(properties.getProperty("timerPath") + "timer/execute"));
            System.out.println("Execute time:" + formatter.format(Calendar.getInstance().getTime()));
        } catch (Exception e) {
            System.out.println("Timer Error");
            System.out.println(e.getMessage());
        }
    }
}
package com.inesa.hadoop.controller.timer;
 
import com.inesa.common.utils.PropertyUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

public class TimerManager {
     
	public TimerManager() {
		
		Date date = addSecond(30);
   
		Timer timer = new Timer();
   
		ExtendTimerTask task = new ExtendTimerTask();
	  
		//Execute task
		Properties properties = PropertyUtil.getProperties("application.properties");
		long INTERVAL = Integer.parseInt(properties.getProperty("timerInterval")) * 1000;
		timer.schedule(task, date, INTERVAL);
	}

	public Date addSecond(int num) {
		Date date = new Date();
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.SECOND, num);
		return startDT.getTime();
	}
}
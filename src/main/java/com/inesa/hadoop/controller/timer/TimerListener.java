package com.inesa.hadoop.controller.timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*@WebListener*/
public class TimerListener implements ServletContextListener{
 
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new TimerManager();
		sce.getServletContext().log("已经添加任务调度表");    
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().log("定时器销毁");    
	}
}
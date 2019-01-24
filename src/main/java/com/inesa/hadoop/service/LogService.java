/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.inesa.hadoop.service;

import com.inesa.common.service.CrudService;
import com.inesa.hadoop.dao.LogDao;
import com.inesa.hadoop.entity.LogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * LogService
 * @author sun
 * @version 2019-01-08
 */
@Service
@Transactional(readOnly = true)
public class LogService extends CrudService<LogDao, LogEntity> {

	@Autowired
	protected LogDao logDao;
	
	public LogEntity get(String id) {
		LogEntity logEntity = new LogEntity();
		logEntity.setId(id);
		return super.get(logEntity);
	}
	
	public List<LogEntity> findList(LogEntity logEntity) {
		return super.findList(logEntity);
	}

	public List<LogEntity> findTable(LogEntity logEntity) {
		return logDao.findTable(logEntity);
	}

	@Transactional(readOnly = false)
	public void createTable(LogEntity logEntity) { logDao.createTable(logEntity); }
}
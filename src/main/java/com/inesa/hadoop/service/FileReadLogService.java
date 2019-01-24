/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.inesa.hadoop.service;

import com.inesa.common.service.CrudService;
import com.inesa.hadoop.dao.FileReadLogDao;
import com.inesa.hadoop.entity.FileReadLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * FileReadLogService
 * @author sun
 * @version 2019-01-11
 */
@Service
@Transactional(readOnly = true)
public class FileReadLogService extends CrudService<FileReadLogDao, FileReadLogEntity> {

	@Autowired
	protected FileReadLogDao fileReadLogDao;

	public List<FileReadLogEntity> findList(FileReadLogEntity fileReadLogEntity) {
		return super.findList(fileReadLogEntity);
	}

	@Transactional(readOnly = false)
	public void save(FileReadLogEntity fileReadLogEntity) {
		super.save(fileReadLogEntity);
	}
}
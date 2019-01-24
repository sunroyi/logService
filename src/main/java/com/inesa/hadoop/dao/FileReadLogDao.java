/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.inesa.hadoop.dao;

import com.inesa.common.persistence.CrudDao;
import com.inesa.hadoop.entity.FileReadLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
/**
 * FileReadLogLogDAO接口
 * @author sun
 * @version 2019-01-11
 */
public interface FileReadLogDao extends CrudDao<FileReadLogEntity> {

}
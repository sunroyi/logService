package com.inesa.hadoop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inesa.common.entity.RestfulResult;
import com.inesa.common.utils.CommUtils;
import com.inesa.hadoop.entity.LogEntity;
import com.inesa.hadoop.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 自动代码生成Controller
 * 
 * @author sun
 * @version 2018-06-01
 */
@RestController
@RequestMapping(value = "log")
public class LogController {

	@Autowired
	private LogService logService;

    @RequestMapping(value = "search")
	public void search(HttpServletRequest request, HttpServletResponse response,
					   @RequestBody LogEntity logEntity) {
		RestfulResult restfulResult = new RestfulResult();

		try {
			PageHelper.startPage(logEntity.getPageNo(), logEntity.getPageSize());

			List<LogEntity> logList = logService.findList(logEntity);

			PageInfo<LogEntity> pageInfo = new PageInfo<LogEntity>(logList);

			restfulResult.setData(logList);
			restfulResult.setCntData(pageInfo.getTotal());

		} catch (Exception e) {
			restfulResult.setResult("Error");
			restfulResult.setMessage(e.getMessage());
		}

		CommUtils.printDataJason(response, restfulResult);
	}
}
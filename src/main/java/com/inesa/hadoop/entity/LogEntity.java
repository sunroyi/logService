package com.inesa.hadoop.entity;

import com.inesa.common.entity.BaseEntity;

public class LogEntity extends BaseEntity {

	// 基本信息
	private String id;
	
	private String format="1";		// 日志格式的版本
    private String app;            // 调用类型 如：[app:ExamCenter]
    private String type;	        // 类型 如：[type:sql][type:interfaceIn][type:interfaceOut][type:exception]
    private String userAgent;		// 客户端
    private String userId;          // 用户ID
    private String userName;        // 用户名
    private String insertDate;      // 调用日期

    // 请求信息
    private String domain;       	// 域名
    private String interFace;       // 接口
    private String interFaceParam; // 接口参数
    
    // 返回结果
    private String success;          // 1:调用成功 0：调用失败
    private String message;         // 调用信息
    private String parameter;       // 用户自定义参数：[p1:abc][p2:123][p3:xxx]
    private String result;			// 返回结果
    
    // SQL
    private String sqlIn;			// SQL语句
    private String sqlParameter;	// SQL参数
    private String sqlOut;			// SQL结果
    
    // 错误信息
    private String exception;		// 错误信息

    // 原始文件信息
    private String detail;      // 原始日志记录
    private String fileName;    // 原始日志文件路径+名称

    private String startDate;
    private String endDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getInterFace() {
        return interFace;
    }

    public void setInterFace(String interFace) {
        this.interFace = interFace;
    }

    public String getInterFaceParam() {
        return interFaceParam;
    }

    public void setInterFaceParam(String interFaceParam) {
        this.interFaceParam = interFaceParam;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getSqlIn() {
		return sqlIn;
	}

	public void setSqlIn(String sqlIn) {
		this.sqlIn = sqlIn;
	}

	public String getSqlOut() {
		return sqlOut;
	}

	public void setSqlOut(String sqlOut) {
		this.sqlOut = sqlOut;
	}

	public String getSqlParameter() {
		return sqlParameter;
	}

	public void setSqlParameter(String sqlParameter) {
		this.sqlParameter = sqlParameter;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

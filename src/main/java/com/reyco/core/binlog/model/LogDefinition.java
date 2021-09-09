package com.reyco.core.binlog.model;

import com.reyco.core.binlog.commons.ToString;

/**
 * 日志定义对象
 * 		可以非常清楚的描述一次操作事件
 * @author reyco
 *
 */
public class LogDefinition extends ToString	{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7887823061703570253L;
	/**数据库*/
	private String databaseName;
	/**表名*/
	private String tableName;
	/**操作记录的id*/
	private Long recordId;
	/**事件类型(insert、delete、update)*/
	private String eventType;
	/**操作成功与否*/
	private Boolean updated;
	/**操作字段*/
	private String column;
	/**操作字段的描述信息*/
	private String columnDesc;
	/**操作前value*/
	private Object beforeValue;
	/**操作后value*/
	private Object aftreValue;
	/**操作人id*/
	private Long operationId;
	/**操作人名称*/
	private String operationName;
	/**操作时间*/
	private Long operationTime;
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Object getBeforeValue() {
		return beforeValue;
	}
	public void setBeforeValue(Object beforeValue) {
		this.beforeValue = beforeValue;
	}
	public Object getAftreValue() {
		return aftreValue;
	}
	public void setAftreValue(Object aftreValue) {
		this.aftreValue = aftreValue;
	}
	public Long getOperationId() {
		return operationId;
	}
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public Long getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(Long operationTime) {
		this.operationTime = operationTime;
	}
	public Boolean getUpdated() {
		return updated;
	}
	public void setUpdated(Boolean updated) {
		this.updated = updated;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public String getColumnDesc() {
		return columnDesc;
	}
	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}
}
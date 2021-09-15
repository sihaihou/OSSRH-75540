package com.reyco.core.binlog.model;


/** 
 * @author  reyco
 * @date    2021.09.15
 * @version v1.0.1 
 */
public abstract class AbstractLogDefinition implements LogDefinition {
	
	private static final long serialVersionUID = -7887823061703570253L;
	/**数据库*/
	private String databaseName;
	/**表名*/
	private String tableName;
	/**操作记录的id*/
	private Object recordId;
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
	private Object operationerId;
	/**操作人名称*/
	private String operationerName;
	/**操作时间*/
	private Object operationTime;
	
	@Override
	public String getDatabaseName() {
		return databaseName;
	}
	@Override
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	@Override
	public String getTableName() {
		return tableName;
	}
	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Override
	public Object getRecordId() {
		return recordId;
	}
	@Override
	public void setRecordId(Object recordId) {
		this.recordId = recordId;
	}
	
	@Override
	public String getEventType() {
		return eventType;
	}
	@Override
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	@Override
	public Boolean getUpdated() {
		return updated;
	}
	@Override
	public void setUpdated(Boolean updated) {
		this.updated = updated;
	}
	
	@Override
	public String getColumn() {
		return column;
	}
	@Override
	public void setColumn(String column) {
		this.column = column;
	}
	
	@Override
	public String getColumnDesc() {
		return columnDesc;
	}
	@Override
	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}
	
	@Override
	public Object getBeforeValue() {
		return beforeValue;
	}
	@Override
	public void setBeforeValue(Object beforeValue) {
		this.beforeValue = beforeValue;
	}
	
	@Override
	public Object getAftreValue() {
		return aftreValue;
	}
	@Override
	public void setAftreValue(Object aftreValue) {
		this.aftreValue = aftreValue;
	}
	
	@Override
	public Object getOperationerId() {
		return operationerId;
	}
	@Override
	public void setOperationerId(Object operationerId) {
		this.operationerId = operationerId;
	}
	
	@Override
	public String getOperationerName() {
		return operationerName;
	}
	@Override
	public void setOperationerName(String operationerName) {
		this.operationerName = operationerName;
	}
	
	@Override
	public Object getOperationTime() {
		return operationTime;
	}
	@Override
	public void setOperationTime(Object operationTime) {
		this.operationTime = operationTime;
	}
}

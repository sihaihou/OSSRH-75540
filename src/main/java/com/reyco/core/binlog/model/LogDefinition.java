package com.reyco.core.binlog.model;

/**
 * @author reyco
 * @date 2021.09.15
 * @version v1.0.1
 */
public interface LogDefinition {
	/**
	 * 获取事件数据库的库名
	 * @return
	 */
	String getDatabaseName();
	/**
	 * 重新设置事件数据库的库名
	 * @param databaseName
	 */
	void setDatabaseName(String databaseName);
	/**
	 * 获取事件表的表名
	 * @return
	 */
	String getTableName();
	/**
	 * 重新设置事件表的表名
	 * @param tableName
	 */
	void setTableName(String tableName);
	/**
	 * 获取事件类型(insert、delete、update)
	 * @return
	 */
	String getEventType();
	/**
	 * 重新设置事件类型(insert、delete、update)
	 * @param eventType
	 */
	void setEventType(String eventType);
	/**
	 * 获取操作数据的主键
	 * @return
	 */
	Object getRecordId();
	/**
	 * 重新设置操作数据的主键
	 * @param recordId
	 */
	void setRecordId(Object recordId);
	/**
	 * 获取事件操作前的value值
	 * @return
	 */
	Object getBeforeValue();
	/**
	 * 重新设置事件操作前的value值
	 * @param beforeValue
	 */
	void setBeforeValue(Object beforeValue);
	/**
	 * 获取事件操作后的value值
	 * @return
	 */
	Object getAftreValue();
	/**
	 * 重新设置事件操作后的value值
	 * @param aftreValue
	 */
	void setAftreValue(Object aftreValue);
	/**
	 * 获取操作人的主键
	 * @return
	 */
	Object getOperationerId();
	/**
	 * 重新设置操作人的主键
	 * @param operationId
	 */
	void setOperationerId(Object operationId);
	/**
	 * 获取操作人的名称
	 * @return
	 */
	String getOperationerName();
	/**
	 * 重新设置操作人的名称
	 * @param operationName
	 */
	void setOperationerName(String operationName);
	/**
	 * 获取操作的时间
	 * @return
	 */
	Object getOperationTime();
	/**
	 * 重新设置操作的时间
	 * @param operationTime
	 */
	void setOperationTime(Object operationTime);
	/**
	 * 获取操作是否成功
	 * @return
	 */
	Boolean getUpdated();
	/**
	 * 重新设置操作是否成功
	 * @param updated
	 */
	void setUpdated(Boolean updated);
	/**
	 * 获取操作字段
	 * @return
	 */
	String getColumn();
	/**
	 * 重新设置操作字段
	 * @param column
	 */
	void setColumn(String column);
	/**
	 * 获取操作字段的描述
	 * @return
	 */
	String getColumnDesc();
	/**
	 * 重新设置操作字段的描述
	 * @param columnDesc
	 */
	void setColumnDesc(String columnDesc);
}

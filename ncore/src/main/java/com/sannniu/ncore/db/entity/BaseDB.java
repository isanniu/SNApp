package com.sannniu.ncore.db.entity;


import com.sannniu.ncore.db.annotation.DatabaseColumn;
import com.sannniu.ncore.db.annotation.DatabasePrimaryKey;

/**
 * @description 数据库表实体基类
 */
public class BaseDB{
	@DatabasePrimaryKey
	@DatabaseColumn(name="_id")
	public long id;
}

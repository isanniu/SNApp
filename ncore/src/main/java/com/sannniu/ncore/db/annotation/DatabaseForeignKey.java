package com.sannniu.ncore.db.annotation;

import com.sannniu.ncore.db.entity.BaseDB;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @description 数据库外键
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseForeignKey {
	Class<? extends BaseDB> table();

	String tableFieldName() default "";
}

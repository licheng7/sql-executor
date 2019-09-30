package com.orange.sqlexecutor;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:application.properties", "classpath:log4j.properties", "classpath:h2.properties", "classpath:freemarker.properties", "classpath:mybatis.properties"})
@ImportResource(locations={"classpath:mybatis_config/sqlMapConfig.xml"})
@ComponentScan(value = "com.orange" )
@EnableCaching
@EnableAutoConfiguration
@MapperScan("com.orange.sqlexecutor.dao")
@ServletComponentScan
public class SqlExecutorApplication {

	static Logger logger = LoggerFactory.getLogger(SqlExecutorApplication.class);

	public static void main(String[] args) {
		logger.info("sql-executor start!");
		SpringApplication.run(SqlExecutorApplication.class, args);
	}

}

package com.komima;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 主应用程序类
 * 作者：Mizuatira
 * 日期：2026/5/20
 * 版本：1.2
 */
@SpringBootApplication
@MapperScan("com.komima.mapper")
@EnableTransactionManagement
@EnableScheduling
public class Application {

    /**
     * 应用程序入口
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

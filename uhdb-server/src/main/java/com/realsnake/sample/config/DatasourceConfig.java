package com.realsnake.sample.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>
 * Class Name : DatasourceConfig.java
 * Description : Description
 *
 * Modification Information
 *
 * Mod Date         Modifier    Description
 * -----------      --------    ---------------------------
 * 2016. 12. 2.     전강욱      Generation
 * </pre>
 *
 * @author 전강욱
 * @since 2016. 12. 2.
 * @version 1.0
 */
@Configuration
// @Lazy
// @EnableTransactionManagement
@MapperScan(basePackages = {"com.realsnake.sample.mapper"})
public class DatasourceConfig {

}

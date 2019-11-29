package com.learn.page.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.learn.page.plugin.MyPagePlugin;
import com.mysql.jdbc.Driver;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @Author: 张惊涛 on 2019/11/29 10:19
 * @Description: myBatis配置类
 */
@Configuration
@MapperScan({"com.learn.page.mapper"})
public class MyBatisConfig {

    /**
     * @Description: 扫描mapper.xml
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{getMyPagePlugin()});
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        String packageXMLConfigPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/mapper/*.xml";
        sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageXMLConfigPath));
        return sqlSessionFactoryBean;
    }


    /**
     * @Description: 配置分页插件的数据库类型 sqlId的正则 以ByPage结尾
     */
    public Interceptor getMyPagePlugin(){
         MyPagePlugin myPagePlugin = new MyPagePlugin();
         myPagePlugin.setDatabaseType("mysql");
         myPagePlugin.setPageSqlId(".*ByPage$");
         return myPagePlugin;
    }

    /**
     * @Description: 连接mysql
     */
    @Bean
    public DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(Driver.class.getName());
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("root");
        return  druidDataSource;
    }
}

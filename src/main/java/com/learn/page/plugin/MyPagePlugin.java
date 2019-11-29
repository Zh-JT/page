package com.learn.page.plugin;

import com.learn.page.util.PageUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;


/**
 * @Author: 张惊涛 on 2019/11/29 10:25
 * @Description: 自定义分页插件
 * 首先要实现mybatis的拦截器 在执行sql之前获取到sql
 * /@Signature为mybatis签名注解
 *  type 拦截对象
 *  method 拦截方法
 *  args 参数
 */
@Intercepts(@Signature(type = StatementHandler.class,method = "prepare",args={Connection.class,Integer.class}))
public class MyPagePlugin implements Interceptor {

    /*数据库类型 mysql*/
    String databaseType = "";

    /*sqlId=namespace+id*/
    String pageSqlId = "";

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getPageSqlId(String sql, PageUtil pageUtil) {
        return pageSqlId;
    }

    public void setPageSqlId(String pageSqlId) {
        this.pageSqlId = pageSqlId;
    }

    /*拦截器逻辑*/
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        /*statementHandler没有提供获取获取sql的api使用mybatis的metaObject反射类来获取*/
        MetaObject metaObject = MetaObject.forObject(
                statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory());
        /*获取sqlId
        *delegate.mappedStatement.id 为方法名
        */
        String sqlId = metaObject.getValue("delegate.mappedStatement.id").toString();
        /*判断是否分页 拿到连接 预编译sql 执行count语句 拼接start和limit  替换原来的SQL 拿到原来执行的SQL */
        if (sqlId.matches(pageSqlId)){
            ParameterHandler parameterHandler = statementHandler.getParameterHandler();
            /*原来应该执行的sql*/
            String sql = statementHandler.getBoundSql().getSql();
            /*执行一条count语句
            * 拿到数据库连接对象*/
            Connection connection = (Connection) invocation.getArgs()[0];
            String countSql = "select count(0) from ("+sql+") a";
System.out.println(countSql);
            /*渲染参数*/
            PreparedStatement preparedStatement = connection.prepareStatement(countSql);
            /*条件交给mybatis*/
            parameterHandler.setParameters(preparedStatement);
            /*执行sql*/
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            if (resultSet.next()){
                count = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            /*获取传进来的参数*/
            Map<String, Object> parameterObject = (Map<String, Object>)parameterHandler.getParameterObject();
            /*limit page*/
            PageUtil pageUtil = (PageUtil) parameterObject.get("page");
            /*limit 1 ,10  十条数据   总共可能有100   count 要的是 后面的100*/
            pageUtil.setCount(count);
            /*拼接分页语句(limit) 并且修改mysql本该执行的语句*/
            String pageSql = getPageSql(sql, pageUtil);
            metaObject.setValue("delegate.boundSql.sql",pageSql);
System.out.println(pageSql);
        }
        /*推进拦截器调用链*/
        return invocation.proceed();
    }

    /*根据不同的数据库配置分页语句*/
    private String getPageSql(String sql, PageUtil pageUtil) {
        if(databaseType.equals("mysql")){
            return sql+" limit "+pageUtil.getStart()+","+pageUtil.getLimit();
        }else if(databaseType.equals("oracle")){
            //拼接oracle的分语句
        }
        return sql+" limit "+pageUtil.getStart()+","+pageUtil.getLimit();
    }

    /*需要你返回一个动态代理后的对象  target :StatementHandler*/
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o,this);
    }

    /*会传入配置文件内容 用户可根据配置文件自定义*/
    @Override
    public void setProperties(Properties properties) {

    }
}

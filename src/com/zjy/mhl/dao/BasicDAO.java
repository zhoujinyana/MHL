package com.zjy.mhl.dao;

import com.zjy.mhl.utils.JDBCUtilsByDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//其他dao的父类
public class BasicDAO<T> {//泛型指定具体类型
    private  QueryRunner  qr = new QueryRunner();

    //开发通用的dml方法，针对任意的表
    public int update(String sql,Object... parameters){
        Connection connection = null;
        try{
            connection = JDBCUtilsByDruid.getConnection();
            int update = qr.update(connection, sql, parameters);
            return update;

        }catch(SQLException e){
            throw new RuntimeException();

        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }


    }
    /*
    sql sql语句，可以有?
    clazz传入一个类的Class对象比如 Actor.class
    parameters传入?的具体的值，可以是多个
    根据Actor.class 返回对应的 ArrayList集合

     */
    //返回多个对象(查询结果是多行)，针对任意表
    public List<T> queryMulti(String sql,Class<T> clazz,Object...parameters){
        Connection connection = null;
        try{
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection,sql,new BeanListHandler<>(clazz),parameters);

        }catch(SQLException e){
            throw new RuntimeException();

        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }


    }
    //查询单行结果
    public T querySingle(String sql,Class<T> clazz,Object... parameters){
        Connection connection = null;
        try{
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection,sql,new BeanHandler<>(clazz),parameters);

        }catch(SQLException e){
            throw new RuntimeException();

        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

    //查询单行单列（即返回单值）
    public Object queryScalar(String sql,Object... parameters){
        Connection connection = null;
        try{
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection,sql,new ScalarHandler(),parameters);

        }catch(SQLException e){
            throw new RuntimeException();

        }finally {
            JDBCUtilsByDruid.close(null,null,connection);
        }
    }

}

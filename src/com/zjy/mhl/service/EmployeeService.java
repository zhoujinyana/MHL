package com.zjy.mhl.service;

//该类完成对数据库表的各种操作，通过调用dao层完成

import com.zjy.mhl.dao.EmployeeDao;
import com.zjy.mhl.domain.Employee;

public class EmployeeService {
    private EmployeeDao employeeDao = new EmployeeDao();

    //根据id和密码返回一个employeeDAO对象
    public Employee getEmployeeByIdAndPwd(String empId,String pwd){
       Employee employee = employeeDao.querySingle("select * from employee where empId=? and pwd=md5(?)",Employee.class,empId,pwd);

       return employee;

    }

}

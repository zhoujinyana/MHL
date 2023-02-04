package com.zjy.mhl.service;

import com.zjy.mhl.dao.MenuDao;
import com.zjy.mhl.domain.Menu;

import java.util.List;

public class MenuService {
    private MenuDao menuDao = new MenuDao();

    //返回所有的菜品
    public List<Menu> list(){
        return menuDao.queryMulti("select * from menu",Menu.class);

    }

    //根据id返回menu对象
    public Menu getMenuById(int id){
        return menuDao.querySingle("select * from menu where id = ?",Menu.class,id);
    }


}

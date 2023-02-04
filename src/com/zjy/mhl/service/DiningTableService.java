package com.zjy.mhl.service;

import com.zjy.mhl.dao.DiningTableDAO;
import com.zjy.mhl.domain.DiningTable;

import java.util.List;

public class DiningTableService {

    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    //返回所有餐桌信息
    public List<DiningTable> list(){
        List<DiningTable> diningTables = diningTableDAO.queryMulti("select id,state from diningTable", DiningTable.class);

        return diningTables;

    }

    //根据id查询餐桌对象，如果返回null，表示对应餐桌不存在(判断预定的餐桌编号是存在的并且是空闲的)
    public DiningTable getDiningTableById(int id){

        return diningTableDAO.querySingle("select * from diningTable where id = ?",DiningTable.class,id);

    }
    //对餐桌状态进行更新，包括预定人的名字和电话
    public boolean orderDiningTable(int id,String orderName,String orderTel){

        int update = diningTableDAO.update("update diningTable set state='已经预定',orderName=?,orderTel=? where id =?",orderName, orderTel,id);
        return update > 0 ;
    }
    //更新餐桌状态
    public boolean updateDiningTableState(int id,String state){

        int update = diningTableDAO.update("update diningTable set state=? where id=?", state, id);
        return update>0;


    }
    //结账以后更新餐桌表，置为空闲
    public boolean updateDiningTableToFree(int id,String state){

        int update = diningTableDAO.update("update diningTable set state=?,orderName='',orderTel='' where id=?", state, id);

        return update>0;


    }


}

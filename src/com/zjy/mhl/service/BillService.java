package com.zjy.mhl.service;

import com.zjy.mhl.dao.BillDao;
import com.zjy.mhl.dao.MultiTableDao;
import com.zjy.mhl.domain.Bill;
import com.zjy.mhl.domain.MultiTableBean;

import java.util.List;
import java.util.UUID;

public class BillService {
    private BillDao billDao = new BillDao();
    private MenuService menuService = new MenuService();
    private DiningTableService diningTableService = new DiningTableService();
    private MultiTableDao multiTableDao = new MultiTableDao();

    //编写点餐的方法
    //生成账单
    //更新对应餐桌的状态
    public boolean orderMenu(int menuId,int nums,int diningTableId){
        String billId = UUID.randomUUID().toString();

        //将账单生成bill表
        int update = billDao.update("insert into bill values(null,?,?,?,?,?,now(),'未结账')",
                billId, menuId, nums, menuService.getMenuById(menuId).getPrice() * nums, diningTableId);

        if(update <= 0){
            return false;
        }
        return diningTableService.updateDiningTableState(diningTableId,"就餐中");

    }
    //返回账单
    public List<MultiTableBean> list(){

        return multiTableDao.queryMulti("select bill.*,name from bill,menu where bill.menuId = menu.id",MultiTableBean.class);

    }
    //查看某个餐桌是否有未结账
    public boolean hasPayBillByDiningTableId(int diningTableId){
        Bill bill =  billDao.querySingle("select * from bill where diningTableId = ? and state = '未结账' limit 0,1",Bill.class,diningTableId);
        return bill != null;

    }
    //修改bill表状态、diningTable表
    public boolean payBill(int diningTableId,String payMode){
        int update = billDao.update("update bill set state=? where diningTableId=? and state='未结账'",payMode,diningTableId);
        if(update <= 0){//更新没有成功
            return false;
        }
        //各司其职，在dining业务层写方法
        if(!diningTableService.updateDiningTableToFree(diningTableId,"空")){
            return false;
        }
        return true;

    }
}

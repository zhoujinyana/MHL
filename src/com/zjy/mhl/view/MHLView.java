package com.zjy.mhl.view;

import com.zjy.mhl.domain.*;
import com.zjy.mhl.service.BillService;
import com.zjy.mhl.service.DiningTableService;
import com.zjy.mhl.service.EmployeeService;
import com.zjy.mhl.service.MenuService;
import com.zjy.mhl.utils.Utility;
import org.junit.jupiter.api.Test;

import java.util.List;

//主界面
public class MHLView {

    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    @Test
   //完成结账任务
    public void payBill(){
        System.out.println("结账服务");
        System.out.println("请选择要结账的餐桌编号：");
        int diningTableId = Utility.readInt();
        if(diningTableId == -1){
            return;
        }
        DiningTable diningTable = diningTableService.getDiningTableById(diningTableId);
        if(diningTable == null){
            System.out.println("餐桌不存在");
            return;
        }
        //验证餐桌是否有需要结账的账单
        if(!billService.hasPayBillByDiningTableId(diningTableId)){
            System.out.println("不存在账单");
            return;
        }

        System.out.println("请选择结账方式（现金、支付宝、微信）：");
        String payMode = Utility.readString(20, "");
        if("".equals(payMode)){
            System.out.println("取消结账");
            return;
        }
        char key = Utility.readConfirmSelection();
        if(key == 'Y'){

           if(billService.payBill(diningTableId,payMode)){
               System.out.println("结账成功");
           }else{
               System.out.println("结账失败");
           }

        }else{
            System.out.println("取消结账");
        }

    }

    //完成点餐
    public void orderMenu(){
        System.out.println("请输入点餐的桌号：");
        int orderDiningTableId = Utility.readInt();
        if(orderDiningTableId == -1){
            System.out.println("取消点餐");
            return;
        }
        System.out.println("请输入点餐的菜品号：");
        int orderMenuId = Utility.readInt();
        if(orderMenuId == -1){
            System.out.println("取消点餐");
            return;
        }
        System.out.println("请输入点餐的菜品量：");
        int orderNums = Utility.readInt();
        if(orderNums == -1){
            System.out.println("取消点餐");
            return;
        }

        //验证阶段
        DiningTable diningTable = diningTableService.getDiningTableById(orderDiningTableId);
        if(diningTable == null){
            System.out.println("餐桌号不存在");
            return;
        }
        Menu menu = menuService.getMenuById(orderMenuId);
        if(menu == null){
            System.out.println("菜品不存在");
            return;
        }
        if(billService.orderMenu(orderMenuId,orderNums,orderDiningTableId)){
            System.out.println("点餐成功");
        }else{
            System.out.println("点餐失败");
        }
    }

    //显示账单
    @Test
    public void listBill(){
        List<MultiTableBean> multiTableBeans = billService.list();
        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t菜品名");
        for(MultiTableBean bill:multiTableBeans){
            System.out.println(bill);
        }

    }

    //显示所有菜品
    @Test
    public void listMenu(){
        List<Menu> list = menuService.list();
        System.out.println("\n菜品编号\t\t菜品名\t\t类别\t\t价格");
        for(Menu menu : list){
            System.out.println(menu);
        }

    }

    //显示所有餐桌状态
    public void listDiningTable(){
        List<DiningTable> list = diningTableService.list();
        System.out.println("\n餐桌编号\t\t餐桌状态");
        for(DiningTable diningTable:list){
            System.out.println(diningTable);
        }
    }
    //完成订桌
    public void orderDiningTable(){
        System.out.println("--------预定餐桌--------");
        System.out.print("请选择要预定的餐桌编号（-1表示退出）:");
        int orderId = Utility.readInt();
        if(orderId == -1){
            return;
        }
        //该方法得到的是y或者n
        char key = Utility.readConfirmSelection();
        if(key == 'Y'){

            //根据orderId返回对应的对象
            DiningTable diningTable = diningTableService.getDiningTableById(orderId);
            if(diningTable == null){
                System.out.println("预定餐桌不存在");
                return;
            }
            if(!("空".equals(diningTable.getState()))){
                System.out.println("餐桌在使用中");
                return;
            }
            System.out.println("预定人的名字：");
            String orderName = Utility.readString(50);
            System.out.println("预定人的电话：");
            String orderTel = Utility.readString(50);

            //可以预定
            if(diningTableService.orderDiningTable(orderId,orderName,orderTel)){
                System.out.println("预定成功");
            }else{
                System.out.println("预定失败");
            }


        }else{
            System.out.println("取消预订");
        }

    }


    //控制是否退出菜单
    private boolean loop = true;
    //接收用户输入的选择
    private String key = "";
    //定义EmployeeService对象
    private EmployeeService employeeService = new EmployeeService();
    private DiningTableService diningTableService = new DiningTableService();
    private MenuService menuService = new MenuService();
    private BillService billService = new BillService();

    //显示主菜单
    public void mainMenu(){
        while(loop){
            System.out.println("============满汉楼=============");
                    System.out.println("\t\t 1登录满汉楼");
                    System.out.println("\t\t 2退出满汉楼");
                    System.out.print("请输入你的选择:");
                    key = Utility.readString(1);
                    switch(key){
                        case "1":
                            System.out.println("请输入员工号：");
                            String empId = Utility.readString(50);
                            System.out.println("请输入密 码：");
                            String pwd = Utility.readString(50);
                            Employee employee = employeeService.getEmployeeByIdAndPwd(empId, pwd);

                            if(employee!=null){
                                System.out.println(employee.getJob()+employee.getName()+ ",登陆成功😁"+"\n");
                                //二级菜单，循环操作
                                while(loop){
                                    System.out.println("============满汉楼二级菜单=============");
                                    System.out.println("\t\t 1 显示餐桌状态");
                                    System.out.println("\t\t 2 预定餐桌");
                                    System.out.println("\t\t 3 显示所有菜品");
                                    System.out.println("\t\t 4 点餐服务");
                                    System.out.println("\t\t 5 查看账单");
                                    System.out.println("\t\t 6 结账");
                                    System.out.println("\t\t 7 退出满汉楼");
                                    System.out.println("请输入你的选择：");
                                    key = Utility.readString(1);
                                    switch (key){
                                        case "1":
                                            listDiningTable();
                                            //System.out.println("显示餐桌状态");
                                            break;
                                        case "2":
                                            orderDiningTable();
                                            //System.out.println("预定餐桌");
                                            break;
                                        case "3":
                                            listMenu();
                                            //System.out.println("显示所有菜品");
                                            break;
                                        case "4":
                                            orderMenu();
                                            //System.out.println("点餐服务");
                                            break;
                                        case "5":
                                            listBill();
                                            //System.out.println("查看账单");
                                            break;
                                        case "6":
                                            payBill();
                                            //System.out.println("结账");
                                            break;
                                        case "7":
                                            loop = false;
                                            break;
                                        default:
                                            System.out.println("输入有误，请重新输入");

                                    }
                                }
                            }else{
                                System.out.println("登陆失败😢");
                            }
                            break;
                        case "2":
                            //System.out.println("退出满汉楼");
                            loop = false;
                            break;
                        default:
                            System.out.println("输入有误，请重新输入");
                    }


        }

    }

}

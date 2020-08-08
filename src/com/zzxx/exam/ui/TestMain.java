package com.zzxx.exam.ui;

import com.zzxx.exam.controller.ClientContext;
import com.zzxx.exam.entity.EntityContext;
import com.zzxx.exam.service.ExamService;

public class TestMain {
    public static void main(String[] args) {
        //创建对象
        ClientContext controller = new ClientContext();
        ExamService service = new ExamService();
        EntityContext sql = new EntityContext();

        WelcomeWindow welcomeWindow = new WelcomeWindow();
        LoginFrame loginFrame = new LoginFrame();
        MenuFrame menuFrame = new MenuFrame();
        ExamFrame examFrame = new ExamFrame();
        MsgFrame msgFrame = new MsgFrame();
        //添加依赖
        //添加控制器的界面依赖
        controller.setWelcomeWindow(welcomeWindow);
        controller.setLoginFrame(loginFrame);
        controller.setMenuFrame(menuFrame);
        controller.setExamFrame(examFrame);
        controller.setMsgFrame(msgFrame);

        //添加控制器的服务器依赖
        controller.setService(service);
        //添加服务器的数据库依赖
        service.setEntityContext(sql);
        //添加服务器的控制器依赖
        service.setController(controller);
        //添加界面的控制器依赖
        welcomeWindow.setController(controller);
        loginFrame.setController(controller);
        menuFrame.setController(controller);
        examFrame.setController(controller);
        menuFrame.setController(controller);
        //业务开启
        controller.startWelcomeWindow();
        System.out.println("hzt");
    }
}

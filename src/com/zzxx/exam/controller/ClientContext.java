package com.zzxx.exam.controller;

import com.zzxx.exam.entity.ExamInfo;
import com.zzxx.exam.entity.QuestionInfo;
import com.zzxx.exam.entity.User;
import com.zzxx.exam.service.ExamService;
import com.zzxx.exam.service.IdOrPasswordError;
import com.zzxx.exam.ui.*;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 客户端控制器: 进行界面和业务模型之间的数据传递/交互
 */
public class ClientContext {
    //创建所有视图成员变量
    private ExamFrame examFrame;
    private LoginFrame loginFrame;
    private MenuFrame menuFrame;
    private MsgFrame msgFrame;
    private WelcomeWindow welcomeWindow;

    private ExamService service;
    private User user;
    private QuestionInfo questionInfoNow;
    //开启欢迎界面
    public void startWelcomeWindow(){
        //设置欢迎界面可见
        welcomeWindow.setVisible(true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //关闭欢迎界面
        welcomeWindow.setVisible(false);
        //开启登录界面
        loginFrame.setVisible(true);
    }
    //显示登录界面
//    public void startLoginFrame(){
//        loginFrame.setVisible(true);
//    }
    //登录方法
    public void login() {
        try{
            //获取登录界面的id和password文本框中的内容
            String id = loginFrame.getIdField().getText();
            String password = loginFrame.getPwdField().getText();
            //调用服务器模组的login方法
            user = service.login(id, password);
            //登录成功,更新菜单界面的欢迎信息
            menuFrame.updateInfo(user.getName() + " 同学您好!");
            //跳转界面至菜单界面
            loginFrame.setVisible(false);
            menuFrame.setVisible(true);
            //登录失败,抛出异常
        }catch (IdOrPasswordError e){
            //处理异常,更新登录界面提示信息为"编号或密码错误"
            loginFrame.updateMessage(e.getMessage());
        }
    }
    //开始考试
    public void startExam() {
        //更新考试界面信息
        ExamInfo examInfo = service.startExam();
        final int[] timeLimit = {examInfo.getTimeLimit()};
        examFrame.updateExamInfo(user.getName(), examInfo.getTitle(), timeLimit[0]);
        questionInfoNow = service.getQuestionInfoNow();
        examFrame.updateQuestionArea(questionInfoNow);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (timeLimit[0] > 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    examFrame.updateTime(--timeLimit[0]);
                }
                //超时交卷
                send();
            }
        });
        thread.start();
        //跳转页面
        menuFrame.setVisible(false);
        examFrame.setVisible(true);
    }


    //退出方法
    public void exit () {
        System.exit(0);
    }

    //跳转到上一题同时更新用户界面答案信息
    public void prevQuestion() {
        //跳转到上一题
        service.prevQuestion();
        questionInfoNow = service.getQuestionInfoNow();
        //更新题目序号
        examFrame.updateNumOfQuestion("总题数:20  题号:" + (questionInfoNow.getQuestionIndex() + 1));
        examFrame.updateQuestionArea(questionInfoNow);
    }
    //跳转到下一题同时更新用户界面答案信息
    public void nextQuestion() {
        service.nextQuestion();
        questionInfoNow = service.getQuestionInfoNow();
        examFrame.updateNumOfQuestion("总题数:20  题号:" + (questionInfoNow.getQuestionIndex() + 1));
        examFrame.updateQuestionArea(questionInfoNow);
    }
    //交卷
    public void send() {
        //更新菜单页面信息
        menuFrame.updateInfo(user.getName() + "同学 交卷成功!");
       //跳转页面
        examFrame.setVisible(false);
        menuFrame.setVisible(true);
    }

    //更新用户答案信息
    public void updateQuestionInfoAnswers(int value, boolean selected) {
        service.updateQuestionInfoAnswers(value, selected);
    }

    //查看结果
    public void result() {
        //计算分数
        int grade = service.count();
        msgFrame.showMsg("最终得分:" + grade);
        msgFrame.setVisible(true);
    }

    //查看考试规则
    public void examRules() {
        String rules = service.getRules();
        //更新页面信息
        msgFrame.showMsg(rules);
        //显示页面
        msgFrame.setVisible(true);
    }
    //set方法设置视图变量内容
    public void setExamFrame(ExamFrame examFrame) {
        this.examFrame = examFrame;
    }

    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    public void setMenuFrame(MenuFrame menuFrame) {
        this.menuFrame = menuFrame;
    }

    public void setMsgFrame(MsgFrame msgFrame) {
        this.msgFrame = msgFrame;
    }

    public void setWelcomeWindow(WelcomeWindow welcomeWindow) {
        this.welcomeWindow = welcomeWindow;
    }

    public void setService(ExamService service) {
        this.service = service;
    }

    public ExamFrame getExamFrame() {
        return examFrame;
    }

    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public MenuFrame getMenuFrame() {
        return menuFrame;
    }

    public MsgFrame getMsgFrame() {
        return msgFrame;
    }

    public WelcomeWindow getWelcomeWindow() {
        return welcomeWindow;
    }

    public ExamService getService() {
        return service;
    }
    public User getUser() {
        return user;
    }

}

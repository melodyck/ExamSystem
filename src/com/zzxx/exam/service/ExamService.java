package com.zzxx.exam.service;

import com.zzxx.exam.controller.ClientContext;
import com.zzxx.exam.entity.*;
import com.zzxx.exam.ui.*;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 所有的业务模型: 登录, 开始考试, 查看规则, 交卷, 上一题, 下一题...
 */
public class ExamService {
    private EntityContext entityContext;
    private ClientContext controller;
    private QuestionInfo questionInfoNow;
    public User login(String id, String password) throws IdOrPasswordError {
        // 在这里写登录的过程
        // 1.获得用户输入的账号, 密码
        // 2.在模拟数据库中的users 查找有没有对应的User对象
        User user = entityContext.getUserById(Integer.parseInt(id));
        // 3.如果有user, 密码正确, 登录成功, 界面跳转
        if (user != null) {
            // 判断密码
            if (password.equals(user.getPassword())) {
                return user;
            }
        }
        // 4.如果有user, 密码不正确, 提示信息
        throw new IdOrPasswordError("编号/密码错误");
        // 5.没有user, 提示信息
    }

    public void setEntityContext(EntityContext entityContext) {
        this.entityContext = entityContext;
    }




    public QuestionInfo getQuestionInfo(int i) {
        return entityContext.getQuestionInfo(i);
    }
//开始考试, 更新考试信息
    public ExamInfo startExam() {
        User user = controller.getUser();
        questionInfoNow = getQuestionInfo(0);
        return entityContext.getExamInfos(user.getId());
    }

    public void setController(ClientContext controller) {
        this.controller = controller;
    }

    public void prevQuestion() {
        //跳转到上一题
        int index = questionInfoNow.getQuestionIndex();
        if(index > 0){
            questionInfoNow = getQuestionInfo(index - 1);
        }

    }

    //交卷并计算分数
    public int count() {
        int grade = 0;
        //获取题目用户回答列表与问题正确答案逐一比较,计算分数
        for (int i = 0; i < entityContext.getQuestionSum(); i++) {
            QuestionInfo questionInfoOfUser = entityContext.getQuestionInfo(i);
            //获取正确答案列表
            List<Integer> rightKey = questionInfoOfUser.getQuestion().getAnswers();
//            System.out.println(rightKey);
            //获取用户答案列表
            List<Integer> answers = questionInfoOfUser.getUserAnswers();
//            System.out.println(answers);
            //如果没有选答案直接跳过,比较下一题
            if (answers.size() == 0){
                continue;
            }
            //如果两个集合都互相包涵所有元素,则判断为正确
            if(answers.containsAll(rightKey) && rightKey.containsAll(answers)){
                grade = grade + questionInfoOfUser.getQuestion().getScore();
            }
        }
//        System.out.println(grade);
        return grade;
    }

    public void nextQuestion() {
        //跳转到下一题
        int index = questionInfoNow.getQuestionIndex();
        if(index < entityContext.getQuestionSum() - 1){
            questionInfoNow = getQuestionInfo(index + 1);
        }
    }



    @Test
    public void updateQuestionInfoAnswers(int value, boolean isSelected) {
        List<Integer> answerList = questionInfoNow.getUserAnswers();
            //如果复选框被选中则将对应选项添加进用户回答列表
            if (isSelected){
                answerList.add(value);
            }else {
                //如果复选框未被选中则将对应选项从用户回答列表中删除所有i
                for (int i = 0; i < answerList.size(); i++) {
                    if(value == answerList.get(i)){
                        answerList.remove(i);
                        i--;
                    }
                }
            }
        questionInfoNow.setUserAnswers(answerList);
//        System.out.println(entityContext.getQuestionInfos().get(0).getUserAnswers());
    }
    //获取当前题目
    public QuestionInfo getQuestionInfoNow() {
        return questionInfoNow;
    }
    //获取规则列表
    public String getRules() {
        return listToString(entityContext.getRules());
    }

    private String listToString(List<String> list){
        StringBuilder sb = new StringBuilder();
        for (String s : list){
            sb.append(s + "\n");
        }
        return sb.toString();
    }
}


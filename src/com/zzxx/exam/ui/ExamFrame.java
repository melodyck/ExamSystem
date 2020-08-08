package com.zzxx.exam.ui;

import com.zzxx.exam.controller.ClientContext;
import com.zzxx.exam.entity.Question;
import com.zzxx.exam.entity.QuestionInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExamFrame extends JFrame {
    // 选项集合, 方便答案读取的处理
    private Option[] options = new Option[4];

    public ExamFrame() {
        init();
    }

    private void init() {
        setTitle("指针信息在线测评");
        setSize(600, 380);
        setContentPane(createContentPane());
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {// 正在关闭的时候

            }
        });
    }

    private JPanel createContentPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(new EmptyBorder(6, 6, 6, 6));
        ImageIcon icon = new ImageIcon(getClass().getResource("pic/exam_title.png"));

        pane.add(BorderLayout.NORTH, new JLabel(icon));

        pane.add(BorderLayout.CENTER, createCenterPane());

        pane.add(BorderLayout.SOUTH, createToolsPane());

        return pane;
    }

    private JPanel createCenterPane() {
        JPanel pane = new JPanel(new BorderLayout());
        // 注意!
        examInfo = new JLabel("姓名:XXX 考试:XXX 考试时间:XXX", JLabel.CENTER);
        pane.add(BorderLayout.NORTH, examInfo);

        pane.add(BorderLayout.CENTER, createQuestionPane());
        pane.add(BorderLayout.SOUTH, createOptionsPane());
        return pane;
    }

    private JPanel createOptionsPane() {
        JPanel pane = new JPanel();
        Option a = new Option(0, "A");
        Option b = new Option(1, "B");
        Option c = new Option(2, "C");
        Option d = new Option(3, "D");
        options[0] = a;
        options[1] = b;
        options[2] = c;
        options[3] = d;
        pane.add(a);
        pane.add(b);
        pane.add(c);
        pane.add(d);
        for(Option option: options){
            option.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.updateQuestionInfoAnswers(option.value, option.isSelected());
                }
            });
        }
        return pane;
    }

    private JScrollPane createQuestionPane() {
        JScrollPane pane = new JScrollPane();
        pane.setBorder(new TitledBorder("题目"));// 标题框

        // 注意!
        questionArea = new JTextArea();
        questionArea.setText("问题\nA.\nB.");
        questionArea.setLineWrap(true);// 允许折行显示
        questionArea.setEditable(false);// 不能够编辑内容
        // JTextArea 必须放到 JScrollPane 的视图区域中(Viewport)
        pane.getViewport().add(questionArea);
        return pane;
    }

    private JPanel createToolsPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(new EmptyBorder(0, 10, 0, 10));
        // 注意!
        questionCount = new JLabel("总题数:20  题号:1");

        timer = new JLabel("剩余时间:90分钟");

        pane.add(BorderLayout.WEST, questionCount);
        pane.add(BorderLayout.EAST, timer);
        pane.add(BorderLayout.CENTER, createBtnPane());
        return pane;
    }
    private ClientContext controller;
    private JPanel createBtnPane() {
        JPanel pane = new JPanel(new FlowLayout());
        prev = new JButton("上一题");
        next = new JButton("下一题");
        JButton send = new JButton("交卷");

        pane.add(prev);
        pane.add(next);
        pane.add(send);

        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //上一题
                controller.prevQuestion();
            }
        });

        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.nextQuestion();
            }
        });

        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.send();
            }
        });

        return pane;
    }

    /**
     * 使用内部类扩展了 JCheckBox 增加了val 属性, 代表答案值
     */
    public class Option extends JCheckBox {
         int value;

        public Option(int val, String txt) {
            super(txt);
            this.value = val;
        }
    }

    private JTextArea questionArea;

    private JButton next;

    private JButton prev;

    private JLabel questionCount;

    private JLabel timer;

    private JLabel examInfo;

    public void updateTime(long m) {
        String time = "剩余时间" + m + "分钟";
        if (m < 15) {
            timer.setForeground(new Color(0xC85848));
        } else {
            timer.setForeground(Color.blue);
        }
        timer.setText(time);
    }

    public void setController(ClientContext controller) {
        this.controller = controller;
    }

    public void updateExamInfo(String name, String project, int time){
        examInfo.setText("姓名:" + name + " 考试:" + project + " 考试时间" + time + "分钟");
    }
    public void updateNumOfQuestion(String order){
        questionCount.setText(order);
    }
    //更新题目区域内容
    public void updateQuestionArea(QuestionInfo questionInfo){
        Question question = questionInfo.getQuestion();
        //更新复选框信息
        //全部初始化
        for(Option option: options){
           option.setSelected(false);
       }
//        //将用户答案选项选中
        for (Integer num: questionInfo.getUserAnswers()){
            for (Option option: options){
                if (num == option.value){
                    option.setSelected(true);
                }
            }
        }
        //更新题目区域内容
        questionArea.setText(questionInfo.getQuestionIndex() + 1 + "." + question.toString());
    }

}
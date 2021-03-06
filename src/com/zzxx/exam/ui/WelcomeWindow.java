package com.zzxx.exam.ui;

import com.zzxx.exam.controller.ClientContext;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * 闪屏
 */
public class WelcomeWindow extends JWindow {

    public WelcomeWindow() {
        init();
    }
    private ClientContext controller;
    private void init() {
        setSize(430, 300);
        JPanel pane = new JPanel(new BorderLayout());
        ImageIcon ico = new ImageIcon(getClass().getResource("pic/welcome.png"));
        JLabel l = new JLabel(ico);
        pane.add(BorderLayout.CENTER, l);
        pane.setBorder(new LineBorder(Color.GRAY));
        setContentPane(pane);
        setLocationRelativeTo(null);
    }

    public void setController(ClientContext controller) {
        this.controller = controller;
    }
}

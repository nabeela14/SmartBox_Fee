package com.smartbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {
    public JPanel panel_login;
    private JTextField userName;
    private JPasswordField pass;
    private JButton btn_login;
    private JPanel title_lable;
    private JLabel welcome;
    private JLabel login_label;
    private JPanel btn_panell;
    private JButton signupbtn;
    private JLabel alert_label;


    public Login() {
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user=userName.getText();
                String password=pass.getText();
                if (user.trim().isEmpty()||password.trim().isEmpty()){
                    alert_label.setText("Invalid Username and password");
                }
               else{
                        uservalidation(user,password);
                }
            }

        });

        signupbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame SignUp=new JFrame();
                SignUp.setVisible(true);
                SignUp.setContentPane(new SignUp().panel_signup);
                SignUp.setPreferredSize(new Dimension(500,400));
                SignUp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                SignUp.pack();
                SignUp.setLocationRelativeTo(null);
                this.close();

            }

            private void close() {
                WindowEvent windowEvent=new WindowEvent(null,WindowEvent.WINDOW_CLOSED);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowEvent);
            }
        });
    }





    public void uservalidation(String user,String password){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/Smartbox","root","rootnabee");
            String query="select*from RegisterUser where Username=? and Password=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,user);
            preparedStatement.setString(2,password);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                JOptionPane.showMessageDialog(null,"Login successful");
                JFrame AddFee=new JFrame();
                AddFee.setVisible(true);
                AddFee.setContentPane(new AddFee().addfee);
                AddFee.setPreferredSize(new Dimension(900,650));
                AddFee.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                AddFee.pack();
                AddFee.setTitle("Add Fee");
                AddFee.setLocationRelativeTo(null);
                this.close();
            }
            else{
                JOptionPane.showMessageDialog(null,"Invalid credentials");
            }
        }catch (Exception e){

        }
    }

    private void close() {

            WindowEvent windowEvent = new WindowEvent(null, WindowEvent.WINDOW_CLOSED);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowEvent);

    }


    public static void main(String[] args){
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }

        JFrame frame=new JFrame("SmartBox");
        frame.setContentPane(new Login().panel_login);
        frame.setPreferredSize(new Dimension(500,400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

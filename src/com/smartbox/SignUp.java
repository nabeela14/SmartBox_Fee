package com.smartbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;


public class SignUp {
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton s_button;
    private JLabel user_label;
    private JLabel pass_label;
    private JPasswordField passwordField2;
    private JLabel cpass_label;
    private JLabel l_label;
    private JLabel title_label;
    public JPanel panel_signup;
    private JLabel alert_label;
    private JButton Log_btn;
    private JLabel alert_label2;
    private JTextField email_field;
    private JLabel email_label;

    String email,uname,pass,c_pass;
    int Id=0;
    public SignUp() {

        s_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validation()){
                    cpassMatch();
                    insertDetail();
                }
            }
        });

    passwordField1.addKeyListener(new KeyAdapter() { } );
        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                checkPassLength();
            }
        });
        Log_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Login=new JFrame();
                Login.setVisible(true);
                Login.setContentPane(new Login().panel_login);
                Login.setPreferredSize(new Dimension(500,400));
                Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Login.pack();
                Login.setLocationRelativeTo(null);
                this.close();
            }

            private void close() {
                WindowEvent windowEvent=new WindowEvent(null,WindowEvent.WINDOW_CLOSED);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowEvent);
            }

        });
    }

    public int getId(){
        ResultSet resultSet=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/Smartbox","root","root");
            String query="select max(Id) from RegisterUser ";
            Statement st=connection.createStatement();
            resultSet=st.executeQuery(query);
            while (resultSet.next()){
                Id=resultSet.getInt(1);
                Id++;
            }
        }catch (Exception e){

        }
        return Id;
    }
    void insertDetail(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/Smartbox","root","root");
            String query="insert into RegisterUser values(?,?,?,?)";
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setInt(1,getId());
            statement.setString(2,email);
            statement.setString(3,uname);
            statement.setString(4,pass);
            int i=statement.executeUpdate();
            if (i>0){
                JOptionPane.showMessageDialog(null,"Welcome to Smartbox!!");
            }else{
                JOptionPane.showMessageDialog(null,"record couldn't be inserted");
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Some error"+ex);
        }

    }
    boolean validation(){
        email=email_field.getText();
        uname=textField1.getText();
        pass=passwordField1.getText();
        c_pass=passwordField2.getText();

        if (uname.isEmpty()){
            JOptionPane.showMessageDialog(null ,"Please enter a Username");
            return false;
        }

        if (pass.isEmpty()){
            JOptionPane.showMessageDialog(null,"Please set a Password");
            return false;
        }

        if (c_pass.isEmpty()){
            JOptionPane.showMessageDialog(null,"Please confirm the password");
        }
        if (email.isEmpty()){
            JOptionPane.showMessageDialog(null,"Please enter email");
        }


        return true;
    }

    public void checkPassLength(){
        pass=passwordField1.getText();
        if (pass.length()<8){
            alert_label.setText("Password should be of 8 digits or more.");
        }
        else{
                alert_label.setText(" ");
        }
    }

    public void cpassMatch(){
        c_pass=passwordField2.getText();
        if (!c_pass.equals(pass)){
            alert_label2.setText("Passwords do not match");
        }
        else{
            alert_label2.setText(" ");
        }
    }




    public static void main(String[] args) {
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
        frame.setContentPane(new SignUp().panel_signup);
        frame.setPreferredSize(new Dimension(500,400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null );
        frame.setVisible(true);
    }

}

package com.smartbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class AddFee {
    public JPanel addfee;

    private JButton out;
    private JButton viewRecordButton;
    private JButton searchButton;
    private JButton viewReportButton;
    private JLabel re_no;
    private JTextField detail;
    private JTextField rupee;
    private JTextField r_total;
    private JTextField word_total;
    private JButton printButton;
    private JTextField receipt_no;
    private JComboBox payment_mode;
    private JTextField r_no;
    private JTextField Name;
    private JTextField date;
    private JTextField month_year;
    private JComboBox course_Select;
    private JTextField chq_no;
    private JTextField id_txt;
    private JLabel id_lbl;
    private JLabel chq_lbl;

    public AddFee() {
                CourCombo();
                int ReceiptNo=getReceiptNo();
                receipt_no.setText(Integer.toString(ReceiptNo));




            printButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (validate()==true){
                       String update= InsertData();
                       if (update.equalsIgnoreCase("Successful Updation")){
                           JOptionPane.showMessageDialog(null,"Updation Successful");

                           JFrame PrintReceipt=new JFrame();
                           PrintReceipt.setVisible(true);
                           PrintReceipt.setContentPane(new PrintReceipt().print_panel);
                           PrintReceipt.setPreferredSize(new Dimension(550,650));
                           PrintReceipt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                           PrintReceipt.pack();
                           PrintReceipt.setLocationRelativeTo(null);
                           PrintReceipt.setTitle("Print Receipt");
                       }else{
                           JOptionPane.showMessageDialog(null,"Updation Failed");
                       }
                    }

                }
            });

            payment_mode.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (payment_mode.getSelectedItem()=="Cheque"){
                        id_txt.setVisible(false);
                        id_lbl.setVisible(false);
                        chq_no.setVisible(true);
                        chq_lbl.setVisible(true);
                    }
                    if (payment_mode.getSelectedItem()=="NEFT/RTGS" || payment_mode.getSelectedItem()=="UPI/Paytm"){
                            chq_no.setVisible(false);
                            chq_lbl.setVisible(false);
                            id_lbl.setVisible(true);
                            id_txt.setVisible(true);
                    }
                    if (payment_mode.getSelectedItem()=="Cash" || payment_mode.getSelectedItem()=="Select"){
                        chq_no.setVisible(false);
                        chq_lbl.setVisible(false);
                        id_lbl.setVisible(false);
                        id_txt.setVisible(false);
                    }

                }
            });

            rupee.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    float r=Float.parseFloat(rupee.getText());
                    int s=0;
                    float total=r+s;
                    r_total.setText(Float.toString(total));
                    word_total.setText(NumberToWordsConverter.convert((int)total)+"Only");
                }
            });



    }

        public boolean validate(){
            if (detail.getText().isEmpty() || rupee.getText().isEmpty()  || receipt_no.getText().isEmpty() &&
                    r_no.getText().isEmpty() || Name.getText().isEmpty() || date.getText().isEmpty() || month_year.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Invalid");
                    return false;
            }
            if (rupee.getText().isEmpty() || rupee.getText().matches("(0-9)+")==true){
                JOptionPane.showMessageDialog(null,"Invalid Amount");
                return false;
            }
            if (r_total.getText().isEmpty()){
                return false;
            }
            if (payment_mode.getSelectedItem().toString().equalsIgnoreCase("UPI/Paytm") || payment_mode.getSelectedItem().toString().equalsIgnoreCase("NEFT/RTGS")){
                if (id_txt.getText().isEmpty()){
                JOptionPane.showMessageDialog(null,"Enter Transaction Id");
                }
                return false;
            }
            if (payment_mode.getSelectedItem().toString().equalsIgnoreCase("Cheque")){
                if (chq_no.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Enter Cheque no.");
                }
                return false;
            }

            return true;

        }

        public void CourCombo(){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/Smartbox","root","rootnabee");
                String query="select Course_Name from Course";
                PreparedStatement statement=connection.prepareStatement(query);
                ResultSet resultSet=statement.executeQuery();
                while (resultSet.next()){
                    course_Select.addItem(resultSet.getString("Course_Name"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public int getReceiptNo(){
            int ReceiptNo=0;
            try{
                Connection connect=DBconnection.getConnection();
                String query="select max(receipt_no) from Fee_Details";
                PreparedStatement preparedStatement=connect.prepareStatement(query);
                ResultSet resultSet=preparedStatement.executeQuery();

                if (resultSet.next()==true){
                    ReceiptNo=resultSet.getInt(1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return ReceiptNo+1;
        }

        public String InsertData(){
            String status="";

            int Receipt_No=Integer.parseInt(receipt_no.getText());
            String Recipient_Name=Name.getText();
            String Roll_no=r_no.getText();
            String Cheque_no=chq_no.getText();
            String Transaction_Id=id_txt.getText();
            float Total_Amount=Float.parseFloat(r_total.getText());
            int Period_Month_yr=Integer.parseInt(month_year.getText());
            String Payment_Mode=payment_mode.getSelectedItem().toString();
            String Course_Name=course_Select.getSelectedItem().toString();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-mm-dd");
            String Date=date.getText();
            String Total_in_Words=word_total.getText();
            String Payment_Detail=detail.getText();

            try{
                Connection connection=DBconnection.getConnection();
                String query="insert into fee_details values(?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                preparedStatement.setInt(1,Receipt_No);
                preparedStatement.setString(2,Recipient_Name);
                preparedStatement.setString(3,Roll_no);
                preparedStatement.setString(4,Payment_Mode);
                preparedStatement.setString(5,Cheque_no);
                preparedStatement.setString(6,Transaction_Id);
                preparedStatement.setString(7,Course_Name);
                preparedStatement.setFloat(8,Total_Amount);
                preparedStatement.setString(9,Date);
                preparedStatement.setInt(10,Period_Month_yr);
                preparedStatement.setString(11,Total_in_Words);
                preparedStatement.setString(12,Payment_Detail);

               int rowCount= preparedStatement.executeUpdate();
                if (rowCount==1){
                    status="Successful Updation";


                }else{
                    status="Updation failed";

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return status;
        }

        private void close() {
            JFrame frame=new JFrame();
            frame.setContentPane(new AddFee().addfee);
            WindowEvent windowEvent=new WindowEvent(frame,WindowEvent.WINDOW_CLOSED);
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
    }

}

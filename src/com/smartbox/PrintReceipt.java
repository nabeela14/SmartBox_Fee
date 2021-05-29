package com.smartbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrintReceipt {

    public JPanel print_panel;
    private JButton printButton;
    private JPanel panel_print;
    private JLabel r_no;
    private JLabel date;
    private JLabel payment;
    private JLabel chq;
    private JLabel name;
    private JLabel time;
    private JLabel coursename;
    private JLabel detail;
    private JLabel rupee;
    private JLabel totalrupee;
    private JLabel words;
    private JLabel t_id;
    private JLabel roll;
    private JLabel lbl_chq;
    private JLabel lbl_tid;


    public PrintReceipt() {
        getRecord();
        panel_print.setPreferredSize(new Dimension(500,600));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setJobName("Print Data");

                job.setPrintable(new Printable(){
                    public int print(Graphics pg, PageFormat pf, int pageNum){
                        pf.setOrientation(PageFormat.LANDSCAPE);
                        if(pageNum > 0){
                            return Printable.NO_SUCH_PAGE;
                        }

                        Graphics2D g2 = (Graphics2D)pg;
                        g2.translate(pf.getImageableX(), pf.getImageableY());
                        g2.scale(0.47,0.47);

                        panel_print.print(g2);


                        return Printable.PAGE_EXISTS;


                    }
                });
                boolean ok = job.printDialog();
                if(ok){
                    try{

                        job.print();
                    }
                    catch (PrinterException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    public void getRecord(){
        try{
            Connection connection=DBconnection.getConnection();
           String query="select*from fee_details order by Receipt_no desc limit 1";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            r_no.setText(resultSet.getString("Receipt_no"));
            date.setText(resultSet.getString("Date"));
            name.setText(resultSet.getString("Recipient_Name"));
            roll.setText(resultSet.getString("Roll_no"));
            detail.setText(resultSet.getString("Payment_Detail"));
            totalrupee.setText(String.valueOf(resultSet.getFloat("Total_Amount")));
            //rupee.setText(resultSet.getString(""));
            words.setText(resultSet.getString("Total_in_Words"));
            time.setText(String.valueOf(resultSet.getInt("Period_Month_Yr")));
            payment.setText(resultSet.getString("Payment_Mode"));
            String payment=resultSet.getString("Payment_Mode");
            if (payment.equalsIgnoreCase("Cash")){
                lbl_chq.setVisible(false);
                chq.setVisible(false);
                lbl_tid.setVisible(false);
                t_id.setVisible(false);
            }
            if(payment.equalsIgnoreCase("Cheque")){
                t_id.setVisible(false);
                lbl_tid.setVisible(false);
            }
            else{
                chq.setVisible(false);
                lbl_chq.setVisible(false);
            }
            coursename.setText(resultSet.getString("Course_Name"));

        }
        catch (Exception e){
e.printStackTrace();
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
    }
}

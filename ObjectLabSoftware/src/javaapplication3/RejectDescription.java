package javaapplication3;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Matt
 */
public class RejectDescription extends javax.swing.JFrame {

    private static SendEmail Sender;
    private static int pCount;
    private static boolean Canceled = false;
    private static boolean Continue = false;
    InstanceCall inst;
     String FileName;
     String dateSubmitted;
     String studentName;

    /**
     * @return the Canceled
     */
    public boolean isCanceled() {
        return Canceled;
    }

    /**
     * @param aCanceled the Canceled to set
     */
    public static void setCanceled(boolean aCanceled) {
        Canceled = aCanceled;
    }

    /**
     * @return the Continue
     */
    public boolean isContinue() {
        return Continue;
    }

    /**
     * @param aContinue the Continue to set
     */
    public static void setContinue(boolean aContinue) 
    {
        Continue = aContinue;
    }

    public void rejectDesc(int ProjectCount, String fileName, String StudentName, String dateSubmitted) {
        inst = new InstanceCall();
        pCount = ProjectCount;
        initComponents();
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            this.FileName = fileName;
            this.studentName = StudentName;
            this.dateSubmitted = dateSubmitted;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RejectDescription.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ErrorText = new javax.swing.JTextArea();
        SubmitButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rejected File");
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("This file is being rejected because:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, -1));

        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        getContentPane().add(CancelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 210, -1, -1));

        ErrorText.setColumns(20);
        ErrorText.setLineWrap(true);
        ErrorText.setRows(5);
        jScrollPane1.setViewportView(ErrorText);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 286, 122));

        SubmitButton.setText("Submit");
        SubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SubmitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(SubmitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, -1, -1));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 320, 10));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Rejected File Message");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication3/black and white bg.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -6, 360, 260));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_CancelButtonActionPerformed

    private void SubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SubmitButtonActionPerformed
        // TODO add your handling code here:
        String Student = "Student name from database";
        String Error = ErrorText.getText();
        System.out.println("Sending Email");

        System.out.println(FileName);
        String[] splited = studentName.split(" ");
        String firstName = splited[0];
        String lastName = splited[1];
        ResultSet results = PendingJobsView.dba.searchID("pendingjobs", firstName, lastName, FileName, dateSubmitted);
        try {
            if (results.next()) {
                Sender = new SendEmail(firstName, lastName, Error, FileName, results.getString("idJobs"));
                System.out.println(Error);
                Sender.Send();
                System.out.println(inst.getSubmission()+ FileName);
                System.out.println(inst.getRejected() + FileName);
                File newDir = new File(inst.getRejected());
                FileUtils.moveFileToDirectory(new File(inst.getSubmission() + FileName), newDir, true);

                PendingJobsView.dba.delete("pendingjobs", results.getString("idJobs"));
                JOptionPane.showMessageDialog(new JFrame(), "Email Sent Succesfully!");
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(RejectDescription.class.getName()).log(Level.SEVERE, null, ex);
        }

        dispose();
    }//GEN-LAST:event_SubmitButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    public static javax.swing.JTextArea ErrorText;
    private javax.swing.JButton SubmitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}

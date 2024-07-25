/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Classes.DBConnection;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.*;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author M AYAN LAPTOP
 */
public class manageUsers extends javax.swing.JFrame {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    /**
     * Creates new form manageUsers
     */
    public manageUsers() {        
        FlatDarkLaf.setup();
        initComponents();
        listUsers();
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void closeConnection(){
        try{
            if(con!=null){
                con.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    private void listUsers(){
        DefaultTableModel model=(DefaultTableModel) usersTable.getModel();
        model.setRowCount(0);
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("Select * from users");
            rs=pst.executeQuery();
            int count=1;
            while(rs.next()){
                String username=rs.getString("username");
                String type=rs.getString("userType");
                Object row[]={count,username,type};                
                model.addRow(row);
                count++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void addUser(){
        boolean verified=false;
        try{    
            String confirmPass=JOptionPane.showInputDialog(this, "Please enter an Admin Password: ", "Verification", JOptionPane.WARNING_MESSAGE);
            if(confirmPass!=null){
                con=DBConnection.connectDB();
                pst=con.prepareStatement("select * from users where userType='Admin'");
                rs=pst.executeQuery();
                while(rs.next()){
                    if(confirmPass.equals(rs.getString("password"))){
                        verified=true;
                        break;
                    }
                }
                if(!verified){
                    JOptionPane.showMessageDialog(this,"Failed to create user.","Verification failed",JOptionPane.ERROR_MESSAGE);
                }else{
                    JPanel panel = new JPanel();
                    JTextField usernameField = new JTextField(10);
                    usernameField.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
        //                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            if(e.getExtendedKeyCode()==KeyEvent.VK_SPACE){
                                usernameField.setEditable(false);
                            }else{
                                usernameField.setEditable(true);
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
        //                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        }
                    });

                    JPasswordField passwordField = new JPasswordField(10);
                    JComboBox<String> typeCB=new JComboBox<>();
                    typeCB.addItem("Admin");
                    typeCB.addItem("Emp");

                    panel.add(new JLabel("Username:"));
                    panel.add(usernameField);
                    panel.add(new JLabel("Password:"));
                    panel.add(passwordField);
                    panel.add(new JLabel("User Type:"));
                    panel.add(typeCB);

                    int option = JOptionPane.showOptionDialog(
                            this,
                            panel,
                            "Create New User",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String[]{"Create", "Cancel"},
                            "Create");

                    if (option == JOptionPane.OK_OPTION) {
                        String username = usernameField.getText();
                        String password = String.valueOf(passwordField.getPassword());
                        String type = typeCB.getSelectedItem().toString();
                        pst=con.prepareStatement("insert into users values(?,?,?)");
                        pst.setString(1, username);
                        pst.setString(2,password);
                        pst.setString(3,type);
                        pst.execute();
                        JOptionPane.showMessageDialog(this,"User \""+username+"\" successfully created.","New User Created",JOptionPane.INFORMATION_MESSAGE);
                        listUsers();
                    }
                } 
            }          
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Failed to create user.\nPlease check the username again.","User Creation failed",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void deleteUser(){
        boolean verified=false;
        try{
            String confirmPass=JOptionPane.showInputDialog(this, "Please enter an Admin Password: ", "Verification", JOptionPane.WARNING_MESSAGE);
            if(confirmPass!=null){
                con=DBConnection.connectDB();
                pst=con.prepareStatement("select * from users where userType='Admin'");
                rs=pst.executeQuery();
                while(rs.next()){
                    if(confirmPass.equals(rs.getString("password"))){
                        verified=true;
                        break;
                    }
                }
                if(!verified){
                    JOptionPane.showMessageDialog(this,"Failed to delete user.","Verification failed",JOptionPane.ERROR_MESSAGE);
                }else{
                    DefaultTableModel model=(DefaultTableModel) usersTable.getModel(); 
                    String selectedUsername= model.getValueAt(usersTable.getSelectedRow(), 1).toString();
                    pst=con.prepareStatement("delete from users where username='"+selectedUsername+"'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this,"User \""+selectedUsername+"\" was deleted successfully","User Deleted",JOptionPane.INFORMATION_MESSAGE);
                    listUsers();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void changeType(){
        boolean verified=false;
        try{
            String confirmPass=JOptionPane.showInputDialog(this, "Please enter an Admin Password: ", "Verification", JOptionPane.WARNING_MESSAGE);
            if(confirmPass!=null){
                con=DBConnection.connectDB();
                pst=con.prepareStatement("select * from users where userType='Admin'");
                rs=pst.executeQuery();
                while(rs.next()){
                    if(confirmPass.equals(rs.getString("password"))){
                        verified=true;
                        break;
                    }
                }
                if(!verified){
                    JOptionPane.showMessageDialog(this,"Failed to verify user.","Verification failed",JOptionPane.ERROR_MESSAGE);
                }else{
                    DefaultTableModel model=(DefaultTableModel) usersTable.getModel(); 
                    String selectedUsername= model.getValueAt(usersTable.getSelectedRow(), 1).toString();
                    String currentType=model.getValueAt(usersTable.getSelectedRow(), 2).toString();
                    Object output=JOptionPane.showInputDialog(this, "Please choose the new user type:", "Change User Type", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Admin","Emp"}, currentType);
                    if(output!=null){
                        String selectedType=String.valueOf(output);
                        pst=con.prepareStatement("update users set userType='"+selectedType+"' where username='"+selectedUsername+"'");
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(this,"User type for \""+selectedUsername+"\" was updated successfully","User Type Updated",JOptionPane.INFORMATION_MESSAGE);
                        listUsers();
                    }
                }
            }        
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void changePassword(){
        boolean verified=false;
        try{
            String confirmPass=JOptionPane.showInputDialog(this, "Please enter an Admin Password: ", "Verification", JOptionPane.WARNING_MESSAGE);
            if(confirmPass!=null){
                con=DBConnection.connectDB();
                pst=con.prepareStatement("select * from users where userType='Admin'");
                rs=pst.executeQuery();
                while(rs.next()){
                    if(confirmPass.equals(rs.getString("password"))){
                        verified=true;
                        break;
                    }
                }
                if(!verified){
                    JOptionPane.showMessageDialog(this,"Failed to verify user.","Verification failed",JOptionPane.ERROR_MESSAGE);
                }else{
                    DefaultTableModel model=(DefaultTableModel) usersTable.getModel(); 
                    String selectedUsername= model.getValueAt(usersTable.getSelectedRow(), 1).toString();
                    Object output=JOptionPane.showInputDialog(this, "Please enter the new user password:", "Change User Password", JOptionPane.QUESTION_MESSAGE);
                    if(output!=null){
                        String newPassword=String.valueOf(output);
                        pst=con.prepareStatement("update users set password='"+newPassword+"' where username='"+selectedUsername+"'");
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(this,"User password for \""+selectedUsername+"\" was updated successfully","User Password Updated",JOptionPane.INFORMATION_MESSAGE);
                        listUsers();
                    }
                }
            }        
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        createUserBtn = new javax.swing.JButton();
        changePasswordBtn = new javax.swing.JButton();
        changeTypeBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        usersTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "S/No", "Username", "Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usersTable.getTableHeader().setReorderingAllowed(false);
        usersTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                usersTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(usersTable);
        if (usersTable.getColumnModel().getColumnCount() > 0) {
            usersTable.getColumnModel().getColumn(0).setResizable(false);
            usersTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            usersTable.getColumnModel().getColumn(1).setResizable(false);
            usersTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            usersTable.getColumnModel().getColumn(2).setResizable(false);
            usersTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        }

        createUserBtn.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        createUserBtn.setText("Create New User");
        createUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createUserBtnActionPerformed(evt);
            }
        });

        changePasswordBtn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        changePasswordBtn.setText("Change Password");
        changePasswordBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePasswordBtnActionPerformed(evt);
            }
        });

        changeTypeBtn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        changeTypeBtn.setText("Change Type");
        changeTypeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeTypeBtnActionPerformed(evt);
            }
        });

        exitBtn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        exitBtn.setText("Save & Exit");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(changeTypeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(changePasswordBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(createUserBtn))))
                .addContainerGap(57, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changePasswordBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(changeTypeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_exitBtnActionPerformed

    private void createUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createUserBtnActionPerformed
        // TODO add your handling code here:
        addUser();
    }//GEN-LAST:event_createUserBtnActionPerformed

    private void usersTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usersTableKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DELETE){
            deleteUser();
        }
    }//GEN-LAST:event_usersTableKeyPressed

    private void changeTypeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeTypeBtnActionPerformed
        // TODO add your handling code here:
        if(usersTable.getSelectedRow()>=0){
            changeType();
        }else{
            JOptionPane.showMessageDialog(this,"No user selected","Error Message",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_changeTypeBtnActionPerformed

    private void changePasswordBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePasswordBtnActionPerformed
        // TODO add your handling code here:
        if(usersTable.getSelectedRow()>=0){
            changePassword();
        }else{
            JOptionPane.showMessageDialog(this,"No user selected","Error Message",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_changePasswordBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(manageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(manageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(manageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(manageUsers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new manageUsers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changePasswordBtn;
    private javax.swing.JButton changeTypeBtn;
    private javax.swing.JButton createUserBtn;
    private javax.swing.JButton exitBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables
}

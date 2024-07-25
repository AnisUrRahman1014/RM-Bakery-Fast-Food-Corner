/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Classes.DBConnection;
import Classes.Item;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author M AYAN LAPTOP
 */
public class CategoryManagement extends javax.swing.JFrame {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    ItemManagement itemManagementRef;
    JPopupMenu contextMenu = new JPopupMenu();
    /**
     * Creates new form CategoryManagement
     * @param itemManagement
     */
    public CategoryManagement(ItemManagement itemManagement) {
        FlatDarkLaf.setup();
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);        
        listCategories();
        this.itemManagementRef=itemManagement;
        setContextBox();
        categoryTable.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = categoryTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < categoryTable.getRowCount()) {
                    categoryTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = categoryTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < categoryTable.getRowCount()) {
                    categoryTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    });
    }
    
    private void setContextBox(){        
        JMenuItem updateImage = new JMenuItem("Update Image");    
        updateImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try {
                        // Specify the destination directory
                        File destinationDir = new File(Item.IMAGEPATH);
                        
                        // Check if the destination directory exists, create if not
                        if (!destinationDir.exists()) {
                            destinationDir.mkdirs();
                        }

                        // Copy the file to the destination directory
                        Files.copy(selectedFile.toPath(), new File(destinationDir, selectedFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

                        JOptionPane.showMessageDialog(null, "File copied successfully!");
                        DefaultTableModel model=(DefaultTableModel) categoryTable.getModel();
                        String ctgName=model.getValueAt(categoryTable.getSelectedRow(), 1).toString();
                        con=DBConnection.connectDB();
                        pst=con.prepareStatement("update categories set image='"+selectedFile.getName()+"' where categoryName='"+ctgName+"'");
                        pst.execute();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error copying file!");
                    } catch (SQLException ex) {
                        Logger.getLogger(ItemManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        contextMenu.add(updateImage);
    }
    
    private void clearTable(){
        DefaultTableModel model=(DefaultTableModel) categoryTable.getModel();
        model.setRowCount(0);
    }
    
    private void listCategories(){
        clearTable();
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from categories");
            rs=pst.executeQuery();
            int count=0;
            while(rs.next()){
                Object row[]={++count,rs.getString("categoryName")};
                DefaultTableModel model=(DefaultTableModel)categoryTable.getModel();
                model.addRow(row);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void closeConnection(){
        try{
            if(con!=null){
                con.close();
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(this,"Failed to close connection with Database."+e,"Error Message",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addCategory(){
        String image=null;
        if(categoryNameField.getText().equals("All")){
            image="---";
        }else{        
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try {
                    // Specify the destination directory
                    File destinationDir = new File(Item.IMAGEPATH);

                    // Check if the destination directory exists, create if not
                    if (!destinationDir.exists()) {
                        destinationDir.mkdirs();
                    }

                    // Copy the file to the destination directory
                    Files.copy(selectedFile.toPath(), new File(destinationDir, selectedFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    image=selectedFile.getName();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error copying file!");
                }
            }
        }
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("insert into categories values('"+categoryNameField.getText().trim()+"','"+image+"')");
            pst.execute();
            JOptionPane.showMessageDialog(this,"Category added successfully.","Confirmation Message",JOptionPane.INFORMATION_MESSAGE);
            listCategories();            
        }catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(this,"Failed to add category."+e,"Error Message",JOptionPane.ERROR_MESSAGE);
        }finally{
            closeConnection();
        }
        categoryNameField.setText("");       
    }
    
    private void deleteItem(){
        DefaultTableModel model=(DefaultTableModel) categoryTable.getModel();
        String selectedName=model.getValueAt(categoryTable.getSelectedRow(), 1).toString();
        int decision=JOptionPane.showConfirmDialog(this, "Are you sure you want to delete \""+selectedName+"\" from the database?\nAll related record would be deleted.","Confirmation Message",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(decision == 0){
            try{
                con=DBConnection.connectDB();
                // DELETE ALL ITEMS OF THIS CATEGORY FROM ORDER ITEMS
                pst=con.prepareStatement("delete from ORDERITEMS where itemCtg='"+selectedName+"'");
                pst.executeUpdate();
                // DELETE ALL ITEMS OF THIS CATEGORY FROM MENU ITEMS
                pst=con.prepareStatement("delete from menuItems where itemCtg='"+selectedName+"'");
                pst.executeUpdate();
                // DELETE FROM CATEGROIES
                pst=con.prepareStatement("delete from categories where categoryName='"+selectedName+"'");
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this,"Category \""+selectedName+"\" was deleted successfully.","Category Deleted",JOptionPane.INFORMATION_MESSAGE);
                listCategories();
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,"Failed to delete category."+e,"Error Message",JOptionPane.ERROR_MESSAGE);
            }finally{
                closeConnection();
            }
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
        categoryTable = new javax.swing.JTable();
        addBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        categoryNameField = new javax.swing.JTextField();
        exitBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Category Menu");
        setType(java.awt.Window.Type.UTILITY);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        categoryTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        categoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S/No", "Category Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        categoryTable.getTableHeader().setReorderingAllowed(false);
        categoryTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                categoryTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(categoryTable);
        if (categoryTable.getColumnModel().getColumnCount() > 0) {
            categoryTable.getColumnModel().getColumn(0).setResizable(false);
            categoryTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            categoryTable.getColumnModel().getColumn(1).setResizable(false);
        }

        addBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("New Category Name:");

        categoryNameField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        categoryNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                categoryNameFieldKeyPressed(evt);
            }
        });

        exitBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(exitBtn)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .addComponent(categoryNameField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(categoryNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn)
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        // TODO add your handling code here:
        itemManagementRef.listCategories();
        dispose();
    }//GEN-LAST:event_exitBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        // TODO add your handling code here:
        addCategory();
    }//GEN-LAST:event_addBtnActionPerformed

    private void categoryTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_categoryTableKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DELETE){
            deleteItem();
        }
    }//GEN-LAST:event_categoryTableKeyPressed

    private void categoryNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_categoryNameFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            addCategory();
        }
    }//GEN-LAST:event_categoryNameFieldKeyPressed

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
            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CategoryManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new CategoryManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JTextField categoryNameField;
    private javax.swing.JTable categoryTable;
    private javax.swing.JButton exitBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}

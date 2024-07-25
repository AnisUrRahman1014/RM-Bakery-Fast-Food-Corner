/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Classes.DBConnection;
import Classes.DealItem;
import Classes.Item;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author M AYAN LAPTOP
 */
public class ManageDeals extends javax.swing.JFrame {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    ArrayList<String> categoryList=new ArrayList<>();
    static ArrayList<DealItem> dealItems=new ArrayList<>();
    /**
     * Creates new form ManageDeals
     */
    public ManageDeals() {
        FlatDarkLaf.setup();
        setTitle("Manage Deals");
        initComponents();
        listCategories();
        setLocationRelativeTo(null);
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
    
    private void listCategories(){
        categoryList.removeAll(categoryList);
        categoryContainer.removeAll();
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from categories");
            rs=pst.executeQuery();
            while(rs.next()){
                categoryList.add(rs.getString("categoryName"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        for(String s:categoryList){
            categoryContainer.add(s, new Category(this,s));
        }
    }
    
    private void removeItem(){
        DefaultTableModel model=(DefaultTableModel) dealItemsTable.getModel();
        String selectedName=model.getValueAt(dealItemsTable.getSelectedRow(), 1).toString();
        int decision=JOptionPane.showConfirmDialog(this, "Are you sure you want to remove \""+selectedName+"\" from the Deal?","Confirmation Message",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(decision == 0){
            for(DealItem i:dealItems){
                if(i.getName().equals(selectedName)){
                    dealItems.remove(i);
                    break;
                }
            }
            updateDealTable();
        }       
    }
    
    private void updateDealItem(){
        DefaultTableModel model=(DefaultTableModel) dealItemsTable.getModel();
        String itemName=model.getValueAt(dealItemsTable.getSelectedRow(), 1).toString();
        int newQuantity=Integer.valueOf(model.getValueAt(dealItemsTable.getSelectedRow(), 2).toString());
        int costPrice=Integer.valueOf(model.getValueAt(dealItemsTable.getSelectedRow(),4).toString());
        for(DealItem i:dealItems){
            if(i.toString().equals(itemName)){
                i.updateQuantity(newQuantity);
                model.setValueAt(i.getPrice(), dealItemsTable.getSelectedRow(),3);
                model.setValueAt(costPrice*newQuantity,dealItemsTable.getSelectedRow(),4);
                break;
            }
        }
        updateFields();
    }
    
    private void updateFields(){
        int totalCostPrice=0;
        int totalSalePrice=0;
        DefaultTableModel model=(DefaultTableModel) dealItemsTable.getModel();
        for(int i=0;i<model.getRowCount();i++){
            totalCostPrice+=Integer.valueOf(model.getValueAt(i, 4).toString());
            totalSalePrice+=Integer.valueOf(model.getValueAt(i,3).toString());
        }
        costPriceField.setText(String.valueOf(totalCostPrice));
        saleTotalField.setText(String.valueOf(totalSalePrice));
    }
    
    public void updateDealTable(){
        DefaultTableModel model=(DefaultTableModel) dealItemsTable.getModel();
        model.setRowCount(0);
        int count=1;
        for(DealItem item: dealItems){
            String itemName=item.toString();           
            int quantity=item.getQuantity();
            int totalPrice=item.getPrice();
            int costPrice=0;
            try{
                con=DBConnection.connectDB();
                pst=con.prepareStatement("select costprice from menuItems where itemName='"+item.getName()+"'");
                rs=pst.executeQuery();
                if(rs.next()){
                    costPrice=rs.getInt("costPrice");
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
            costPrice=costPrice*quantity;
            Object row[]={count,itemName,quantity,totalPrice,costPrice};
            model.addRow(row);
            count++;
        }
        updateFields();
    }
    
    private void resetDealPackage(){
        dealItems.removeAll(dealItems);
        DefaultTableModel model=(DefaultTableModel) dealItemsTable.getModel();
        model.setRowCount(0);
        costPriceField.setText("");
        salePriceField.setText("");
    }
    
    private void addDealPackage(){
        String dealName=JOptionPane.showInputDialog(this, "Please enter a name for the deal:", "Deal Name", JOptionPane.QUESTION_MESSAGE);
        if(dealName!=null){
            int costPrice=Integer.valueOf(costPriceField.getText());
            int salePrice=Integer.valueOf(salePriceField.getText());
            try{
                con=DBConnection.connectDB();
                // INSERTING VALUES INTO DEALS TABLE
                pst=con.prepareStatement("insert into deals values(?,?,?)");
                pst.setString(1,dealName);
                pst.setInt(2,costPrice);
                pst.setInt(3,salePrice);
                pst.execute();
                // INSERTING ITEMS INTO DEAL_ITEMS TABLE
                for(DealItem i:dealItems){
                    pst=con.prepareStatement("insert into dealItems values(?,?,?,?,?,?)");
                    pst.setString(1,dealName);
                    pst.setString(2,i.getId());
                    pst.setString(3,i.getCategory());
                    pst.setString(4, i.toString());
                    pst.setString(5,i.getSize());
                    pst.setInt(6, i.getQuantity());
                    pst.execute();
                }
                String dealId=dealName.replace("\\s", "");
                pst=con.prepareStatement("insert into menuItems values(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1,dealId);
                pst.setString(2,dealName);
                pst.setString(3,"Deals");
                pst.setInt(4,costPrice);
                pst.setBoolean(5, false);
                pst.setInt(6, salePrice);
                pst.setInt(7,0);
                pst.setInt(8,0);
                pst.setInt(9,0);
                pst.setInt(10, 0);
                pst.setString(11,"deal.png");
                pst.execute();
                JOptionPane.showMessageDialog(this, "Deal \""+dealName+"\" stored","Added Successfully",JOptionPane.INFORMATION_MESSAGE);
                resetDealPackage();
            }catch(Exception e){
                e.printStackTrace();
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

        rootPanel = new javax.swing.JPanel();
        orderPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dealItemsTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        proceedBtn = new javax.swing.JButton();
        dropBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        returnBtn = new javax.swing.JButton();
        costPriceField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        salePriceField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        saleTotalField = new javax.swing.JTextField();
        categoryContainer = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        orderPanel.setBackground(new java.awt.Color(61, 91, 116));
        orderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 0, true));
        orderPanel.setForeground(new java.awt.Color(88, 115, 141));

        dealItemsTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        dealItemsTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        dealItemsTable.setForeground(new java.awt.Color(255, 255, 255));
        dealItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S/No", "Item", "Quantity", "Sale", "Cost"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dealItemsTable.setColumnSelectionAllowed(true);
        dealItemsTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        dealItemsTable.setRowHeight(25);
        dealItemsTable.getTableHeader().setReorderingAllowed(false);
        dealItemsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dealItemsTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(dealItemsTable);
        dealItemsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (dealItemsTable.getColumnModel().getColumnCount() > 0) {
            dealItemsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
            dealItemsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            dealItemsTable.getColumnModel().getColumn(3).setResizable(false);
            dealItemsTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Manage Deals");

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        proceedBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        proceedBtn.setForeground(new java.awt.Color(0, 204, 255));
        proceedBtn.setText("Proceed");
        proceedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proceedBtnActionPerformed(evt);
            }
        });

        dropBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        dropBtn.setForeground(new java.awt.Color(255, 0, 51));
        dropBtn.setText("Drop");
        dropBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropBtnActionPerformed(evt);
            }
        });

        removeBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        removeBtn.setForeground(new java.awt.Color(255, 204, 0));
        removeBtn.setText("Remove");
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });

        returnBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        returnBtn.setText("Return");
        returnBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnBtnActionPerformed(evt);
            }
        });

        costPriceField.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        costPriceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                costPriceFieldActionPerformed(evt);
            }
        });
        costPriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                costPriceFieldKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Cost Price :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 255, 255));
        jLabel3.setText("Sale Price :");

        salePriceField.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        salePriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salePriceFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                salePriceFieldKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 255, 255));
        jLabel4.setText("Sale Total:");

        saleTotalField.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        saleTotalField.setEnabled(false);
        saleTotalField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saleTotalFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleTotalFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(39, 39, 39)
                        .addComponent(returnBtn))
                    .addComponent(jSeparator1)
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(orderPanelLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dropBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(proceedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(orderPanelLayout.createSequentialGroup()
                                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(costPriceField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(salePriceField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(saleTotalField))))
                        .addGap(0, 5, Short.MAX_VALUE)))
                .addContainerGap())
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(returnBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(costPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saleTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proceedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dropBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        categoryContainer.setBackground(new java.awt.Color(61, 91, 116));
        categoryContainer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 0, true));
        categoryContainer.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        categoryContainer.setOpaque(true);

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoryContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 901, Short.MAX_VALUE)
                .addContainerGap())
        );
        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categoryContainer))
                .addContainerGap())
        );

        getContentPane().add(rootPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dealItemsTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dealItemsTableKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DELETE){
            removeItem();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            updateDealItem();
            categoryContainer.requestFocus();
        }
    }//GEN-LAST:event_dealItemsTableKeyPressed

    private void proceedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedBtnActionPerformed
        // TODO add your handling code here:
        addDealPackage();
    }//GEN-LAST:event_proceedBtnActionPerformed

    private void dropBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropBtnActionPerformed
        // TODO add your handling code here:
        resetDealPackage();
    }//GEN-LAST:event_dropBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        // TODO add your handling code here:
        if(dealItemsTable.getSelectedRow()>=0){
            removeItem();
        }else{
            JOptionPane.showMessageDialog(this,"No item selected","Error Message",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_removeBtnActionPerformed

    private void returnBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnBtnActionPerformed
        // TODO add your handling code here:
        new ItemManagement().setVisible(true);
        dispose();
    }//GEN-LAST:event_returnBtnActionPerformed

    private void salePriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salePriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            salePriceField.setEditable(true);
            //            if(!cashPaidField.getText().equals("")){
                //                setCashBackField();
                //            }
        }else{
            if(evt.getExtendedKeyCode() == KeyEvent.VK_LEFT || evt.getExtendedKeyCode() == KeyEvent.VK_RIGHT || evt.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode() == KeyEvent.VK_DELETE || evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
                if(evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
                    proceedBtn.requestFocus();
                }else
                salePriceField.setEditable(true);
            }else{
                salePriceField.setEditable(false);
            }
        }

    }//GEN-LAST:event_salePriceFieldKeyPressed

    private void salePriceFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salePriceFieldKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_salePriceFieldKeyReleased

    private void costPriceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_costPriceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_costPriceFieldActionPerformed

    private void costPriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costPriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            costPriceField.setEditable(true);
            //            if(!cashPaidField.getText().equals("")){
                //                setCashBackField();
                //            }
        }else{
            if(evt.getExtendedKeyCode() == KeyEvent.VK_LEFT || evt.getExtendedKeyCode() == KeyEvent.VK_RIGHT || evt.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode() == KeyEvent.VK_DELETE || evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
                if(evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
                    salePriceField.requestFocus();
                }else
                costPriceField.setEditable(true);
            }else{
                costPriceField.setEditable(false);
            }
        }
    }//GEN-LAST:event_costPriceFieldKeyPressed

    private void saleTotalFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleTotalFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_saleTotalFieldKeyPressed

    private void saleTotalFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleTotalFieldKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_saleTotalFieldKeyReleased

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
            java.util.logging.Logger.getLogger(ManageDeals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageDeals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageDeals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageDeals.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageDeals().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane categoryContainer;
    private javax.swing.JTextField costPriceField;
    private javax.swing.JTable dealItemsTable;
    private javax.swing.JButton dropBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JButton proceedBtn;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton returnBtn;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JTextField salePriceField;
    private javax.swing.JTextField saleTotalField;
    // End of variables declaration//GEN-END:variables
}

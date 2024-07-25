/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Classes.DBConnection;
import Classes.OrderItem;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 *
 * @author M AYAN LAPTOP
 */
public class orderItemsView extends javax.swing.JPanel {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    /**
     * Creates new form orderItemsView
     */
    ArrayList<OrderItem> orderItems;
    Object parentFrame;
    String orderID;
    public orderItemsView(Object parent,String orderID,ArrayList<OrderItem> orderItems) {
        this.orderItems=orderItems;
        this.orderID=orderID;
        this.parentFrame=parent;
        initComponents();
        listOrderItems();
    }
    
    private void listOrderItems(){
        DefaultTableModel model=(DefaultTableModel) itemTable.getModel();
        model.setRowCount(0);
        int count=1;
        for(OrderItem i:orderItems){
            Object[] row={count,i.toString(),i.getCategory(),i.getQuantity(),i.getUnitPrice(),i.getTotalPrice()};
            model.addRow(row);
            count++;
        }
    }
    
    
    
    private void updateOrderItem(){
        DefaultTableModel model=(DefaultTableModel) itemTable.getModel();
        int selectedRow=itemTable.getSelectedRow();
        String selectedItemName=model.getValueAt(selectedRow,1).toString();
        int newQuantity=Integer.valueOf(model.getValueAt(selectedRow,3).toString());
        int decision=JOptionPane.showConfirmDialog(this, "Are you sure you want to commit changes to Item Name: \""+selectedItemName+"\"","Confirmation Message",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(decision==JOptionPane.YES_OPTION){
            OrderItem item=null;
            int oldPrice=0;
            for(OrderItem i:orderItems){
                if(selectedItemName.equals(i.toString())){
                    item=i;
                    oldPrice=item.getTotalPrice();
                    item.updateQuantity(newQuantity);
                    break;
                }
            }     
            int newPrice=item.getTotalPrice();
            try{
                con=DBConnection.connectDB();
                pst=con.prepareStatement("update orderItems set quantity=? , TotalPrice=? where orderId='"+orderID+"' and itemID='"+item.getId()+"' and itemCtg='"+item.getCategory()+"'");
                pst.setInt(1,item.getQuantity());
                pst.setInt(2,newPrice);
                pst.executeUpdate();
                
                int oldTotal=0;
                pst=con.prepareStatement("select orderTotal from orderRecords where orderId='"+orderID+"'");
                rs=pst.executeQuery();
                if(rs.next()){
                    oldTotal=rs.getInt("orderTotal");
                }
                
                int newTotal=(oldTotal-oldPrice)+newPrice;           
                
                pst=con.prepareStatement("update orderRecords set orderTotal=? where orderId='"+orderID+"'");
                pst.setInt(1,newTotal);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, selectedItemName+" was updated successfully","Item Updated",JOptionPane.INFORMATION_MESSAGE);
                listOrderItems();
                ((orderRecordView)parentFrame).listOrderRecords();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
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
    
    private void deleteOrderItem(){
        DefaultTableModel model=(DefaultTableModel) itemTable.getModel();
        int selectedRow=itemTable.getSelectedRow();
        String selectedItemName=model.getValueAt(selectedRow,1).toString();
        int decision=JOptionPane.showConfirmDialog(this, "Are you sure you want to delete \""+selectedItemName+"\" ?","Confirmation Message",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(decision==JOptionPane.YES_OPTION){
            OrderItem item=null;
            int oldPrice=0;
            for(OrderItem i:orderItems){
                if(selectedItemName.equals(i.toString())){
                    item=i;
                    orderItems.remove(i);
                    break;
                }
            }     
            try{
                con=DBConnection.connectDB();
                pst=con.prepareStatement("delete from orderItems where orderId='"+orderID+"' and itemID='"+item.getId()+"'");
                pst.executeUpdate();
                
                int oldTotal=0;
                pst=con.prepareStatement("select orderTotal from orderRecords where orderId='"+orderID+"'");
                rs=pst.executeQuery();
                if(rs.next()){
                    oldTotal=rs.getInt("orderTotal");
                }
                
                int newTotal=oldTotal-item.getTotalPrice();           
                
                pst=con.prepareStatement("update orderRecords set orderTotal=? where orderId='"+orderID+"'");
                pst.setInt(1,newTotal);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, selectedItemName+" was updated successfully","Item Updated",JOptionPane.INFORMATION_MESSAGE);
                listOrderItems();
                ((orderRecordView)parentFrame).listOrderRecords();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();

        itemTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "S/No", "Item Name", "Category", "Quantity", "Unit Price", "Total Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemTable.setIntercellSpacing(new java.awt.Dimension(7, 7));
        itemTable.setRowHeight(30);
        itemTable.getTableHeader().setReorderingAllowed(false);
        itemTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(itemTable);
        if (itemTable.getColumnModel().getColumnCount() > 0) {
            itemTable.getColumnModel().getColumn(0).setResizable(false);
            itemTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            itemTable.getColumnModel().getColumn(1).setResizable(false);
            itemTable.getColumnModel().getColumn(1).setPreferredWidth(280);
            itemTable.getColumnModel().getColumn(2).setResizable(false);
            itemTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            itemTable.getColumnModel().getColumn(3).setResizable(false);
            itemTable.getColumnModel().getColumn(4).setResizable(false);
            itemTable.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 904, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void itemTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemTableKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
            updateOrderItem();
        }
        if(evt.getExtendedKeyCode() == KeyEvent.VK_DELETE){
            deleteOrderItem();
        }
    }//GEN-LAST:event_itemTableKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable itemTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}

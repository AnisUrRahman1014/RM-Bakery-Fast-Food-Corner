/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;
import Classes.DBConnection;
import Classes.Item;
import Classes.OrderItem;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author M AYAN LAPTOP
 */
public class orderRecordView extends javax.swing.JPanel {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    PrinterJob job=null;
    
    public static final int EMPLOYEE=0;
    public static final int ADMIN=1;
    
    private int accessType=EMPLOYEE;
    private final Object parentFrame;
    
    /**
     * Creates new form orderRecordView
     * @param parent
     * @param access
     */
    public orderRecordView(Object parent,int access) {
        this.parentFrame=parent;
        this.accessType=access;
        initComponents();
        listOrderRecords();
        manageAccess();        
    }
    
    private void manageAccess(){
        if(accessType == EMPLOYEE){
            returnBtn.setVisible(false);
        }else{
            returnBtn.setVisible(true);
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
    
    public void listOrderRecords(){
        DefaultTableModel model=(DefaultTableModel) orderRecordTable.getModel();
        model.setRowCount(0);
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from orderRecords order by orderDate DESC");
            rs=pst.executeQuery();
            while(rs.next()){
                java.sql.Date date=rs.getDate("OrderDate");
                String orderId=rs.getString("orderID");
                int orderTotal=rs.getInt("orderTotal");
                String orderType=rs.getString("ordertype");
                Object row[]={date,orderId,orderTotal,orderType};
                model.addRow(row);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void displayBill(){
        ArrayList<OrderItem> orderItems=new ArrayList<>();
        DefaultTableModel model=(DefaultTableModel) orderRecordTable.getModel();
        String orderID=model.getValueAt(orderRecordTable.getSelectedRow(), 1).toString();
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from orderItems where orderID='"+orderID+"'");
            rs=pst.executeQuery();
            while(rs.next()){
                String itemID=rs.getString("itemID");
                String itemCtg=rs.getString("itemCtg");
                int price=rs.getInt("itemUnitPrice");
                int quantity=rs.getInt("quantity");
                String size=rs.getString("itemSize");
                Item i=getItem(itemID,itemCtg);
                if(i.getName().equals("Extras")){
                    i.setSingleSalePrice(price);
                }
                orderItems.add(new OrderItem(i,quantity,size));
            }
            for(OrderItem i:orderItems){
                System.out.println(i.toString() + " " + i.getCategory() + " " + i.getTotalPrice() + " " + i.getQuantity());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        if(accessType==EMPLOYEE){
            JOptionPane.showOptionDialog(this,new orderItemsView(this, orderID,orderItems), orderID, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,null);
        }else 
            JOptionPane.showOptionDialog((Component)parentFrame,new orderItemsView(this, orderID,orderItems), orderID, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null,null);
    }
    
    private void print(){
        ArrayList<OrderItem> orderItems=new ArrayList<>();
        DefaultTableModel model=(DefaultTableModel) orderRecordTable.getModel();
        String orderId=model.getValueAt(orderRecordTable.getSelectedRow(), 1).toString();
        String orderType=model.getValueAt(orderRecordTable.getSelectedRow(), 3).toString();
        String contact="---";
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from orderRecords inner join orderItems on orderRecords.orderID=orderItems.orderID where orderRecords.orderId='"+orderId+"'");
            rs=pst.executeQuery();
            while(rs.next()){
                String itemID=rs.getString("itemID");
                String itemCtg=rs.getString("itemCtg");
                int price=rs.getInt("itemUnitPrice");
                int quantity=rs.getInt("quantity");
                String size=rs.getString("itemSize");
                Item i=getItem(itemID,itemCtg);
                if(i.getName().equals("Extras")){
                    i.setSingleSalePrice(price);
                }
                orderItems.add(new OrderItem(i,quantity,size));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        printPanel(orderId, orderType, contact,orderItems);
    }
    
    private Item getItem(String itemID,String itemCtg){
        Item item=null;
        try{
            pst=con.prepareStatement("select * from menuItems where itemID='"+itemID+"' and itemCtg='"+itemCtg+"'");
            ResultSet rs2=pst.executeQuery();
            if(rs2.next()){
                String itemName=rs2.getString("itemName");
                boolean sizeFlag=rs2.getBoolean("sizeVarFlag");
                int singleCostPrice=rs2.getInt("CostPrice");
                int singleSalePrice=rs2.getInt("salePrice");
                int smallCostPrice=rs2.getInt("smallCost");
                int smallSalePrice=rs2.getInt("smallPrice");
                int mediumCostPrice=rs2.getInt("mediumCost");
                int mediumSalePrice=rs2.getInt("mediumPrice");
                int largeCostPrice=rs2.getInt("largeCost");
                int largeSalePrice=rs2.getInt("largePrice");
                int xLargeCostPrice=rs2.getInt("xLargeCost");
                int xLargeSalePrice=rs2.getInt("xLargePrice");
                item=new Item(itemID,itemName,itemCtg,sizeFlag,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singleCostPrice,singleSalePrice);
            }
        }catch(Exception e){
            e.printStackTrace();
        }        
        return item;
    }
    
    private String[] splitString(String input, int maxLength) {
        List<String> lines = new ArrayList<>();
        String[] words = input.split("\\s+");
        StringBuilder currentLine = new StringBuilder(words[0]);

        for (int i = 1; i < words.length; i++) {
            if (currentLine.length() + words[i].length() + 1 <= maxLength) {
                currentLine.append(" ").append(words[i]);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(words[i]);
            }
        }

        lines.add(currentLine.toString());

        return lines.toArray(new String[0]);
    }
    
    private void printPanel(String orderID, String billType, String contact, ArrayList<OrderItem> orderItems){
        job = PrinterJob.getPrinterJob();
        java.sql.Date sqlDate= new java.sql.Date(new java.util.Date().getTime());
        job.setJobName(sqlDate+" _"+String.valueOf(new java.util.Date().getTime())); 
        PageFormat page=job.defaultPage();
        Paper paper = new Paper();
        paper.setImageableArea(0, 0, 200.0, orderItems.size() * 500.0);
        page.setPaper(paper);
        job.setPrintable((Graphics pg, PageFormat pf, int pageNum) -> {
            pf.setOrientation(PageFormat.PORTRAIT);
            if(pageNum > 0){
                return Printable.NO_SUCH_PAGE;
            }
            Graphics2D g2 = (Graphics2D)pg;
            g2.translate(pf.getImageableX(), pf.getImageableY());
            g2.scale(0.9,0.9);
            int y1 = 20;
            int yShift=10;
            int headerRectHeight=15;
            g2.setFont(new Font("Century Gothic",Font.BOLD,12));
            g2.drawString("INVOICE", 0, y1);
            y1 += yShift;
            g2.setFont(new Font("Century Gothic",Font.BOLD,10));
            g2.drawString("RM Fast Food Corner", 0, y1);
            y1 += yShift;
            g2.setFont(new Font("Century Gothic",Font.PLAIN,9));
            g2.drawString("JalalPur Road, Gujrat", 0, y1);
            y1 += yShift;
            g2.drawString("+92 300 627 9757", 0, y1);
            y1 += yShift;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawString("Order ID: "+orderID, 0, y1); y1 += yShift;
            g2.drawString("Order Type: "+billType, 0, y1); y1 += yShift;
            g2.drawString("Customer: "+contact, 0, y1);           
            y1 += headerRectHeight;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawString("Name", 0, y1);
            g2.drawString("Quantity", 100, y1);
            g2.drawString("Price", 165, y1);
            y1 += yShift;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
//            for (OrderItem i : orderItems) {
//                g2.drawString(i.toString(), 12, y1);           
//                g2.drawString(String.valueOf(i.getQuantity()), 125, y1);
//                g2.drawString("x "+String.valueOf(i.getUnitPrice()), 135, y1);
//                g2.drawString(String.valueOf(i.getTotalPrice()), 200, y1);
//                y1 = y1 + yShift;
//            }
            int totalPriceField=0;
            for (OrderItem i : orderItems) {
                String itemString = i.toString();
                int itemStringStart = 0;
                int quantityStringStart = 100;

                // Split the itemString into lines if it's too long
                String[] lines = splitString(itemString, 20);
                int indexCount=0;
                for (String line : lines) {
                    g2.drawString(line, itemStringStart, y1);
                    indexCount++;
                    if(indexCount<lines.length){
                        y1 += yShift;
                    }                    
                }
                g2.drawString(String.valueOf(i.getQuantity()), quantityStringStart, y1);
                g2.drawString("x " + String.valueOf(i.getUnitPrice()), quantityStringStart+10, y1);
                g2.drawString(String.valueOf(i.getTotalPrice()), 165, y1);
                y1 += yShift+10;
                totalPriceField+=i.getTotalPrice();
            }
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawString("Total Amount", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(totalPriceField), 165, y1);
            y1 += yShift;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
//            g2.drawString("Paid", 10, y1);
//            g2.drawString(":", 75, y1);
//            g2.drawString(cashPaidField.getText(), 250, y1);
//            y1 += yShift;
//            g2.drawString("Change", 10, y1);
//            g2.drawString(":", 75, y1);
//            g2.drawString(cashBackField.getText(), 250, y1);
//            y1 += yShift;
//            g2.drawLine(12, y1, 280, y1);
//            y1 += headerRectHeight;
            g2.drawString("Date", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(LocalDate.now()), 145, y1);
            y1 += yShift;
            g2.drawString("*******************************************************************", 0, y1);
            y1 += yShift;
            g2.drawString("THANK YOU SO MUCH", 45, y1);
            return Printable.PAGE_EXISTS;
        },page);
        try{
                PrintService printService=job.getPrintService();
                if(printService==null)
                {
                    job.printDialog();
                    printService = job.getPrintService();
                }
                if(printService!=null) {
                    job.defaultPage();
                    job.print();
                }
        } catch (PrinterException ex) {
            ex.printStackTrace();
            Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getAccessType() {
        return accessType;
    }   
    
    private void deleteItem(){
        DefaultTableModel model=(DefaultTableModel) orderRecordTable.getModel();
        String orderID=model.getValueAt(orderRecordTable.getSelectedRow(), 1).toString();
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("Delete from orderItems where orderId='"+orderID+"'");
            pst.executeUpdate();
            
            pst=con.prepareStatement("Delete from orderRecords where orderId='"+orderID+"'");
            pst.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Record for order \""+orderID+"\" has been deleted successfully","Operation successful.",JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        listOrderRecords();
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
        orderRecordTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        printBtn = new javax.swing.JButton();
        returnBtn = new javax.swing.JButton();

        orderRecordTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        orderRecordTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Order ID", "Order Total", "Order Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderRecordTable.setIntercellSpacing(new java.awt.Dimension(7, 7));
        orderRecordTable.setRowHeight(35);
        orderRecordTable.getTableHeader().setReorderingAllowed(false);
        orderRecordTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderRecordTableMouseClicked(evt);
            }
        });
        orderRecordTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                orderRecordTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(orderRecordTable);
        if (orderRecordTable.getColumnModel().getColumnCount() > 0) {
            orderRecordTable.getColumnModel().getColumn(0).setResizable(false);
            orderRecordTable.getColumnModel().getColumn(1).setResizable(false);
            orderRecordTable.getColumnModel().getColumn(2).setResizable(false);
            orderRecordTable.getColumnModel().getColumn(3).setResizable(false);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        jLabel1.setText("Order Record");

        printBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        printBtn.setText("Print");
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });

        returnBtn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        returnBtn.setText("Return");
        returnBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 327, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(261, 261, 261)
                                .addComponent(returnBtn))
                            .addComponent(printBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        // TODO add your handling code here:
        print();
//        if(accessType==orderRecordView.EMPLOYEE){
//            this.setVisible(false);
//        }
    }//GEN-LAST:event_printBtnActionPerformed

    private void returnBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnBtnActionPerformed
        // TODO add your handling code here:
        ((ViewOrderRecords) parentFrame).dispose();
        new OptionMenu().setVisible(true);
    }//GEN-LAST:event_returnBtnActionPerformed

    private void orderRecordTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderRecordTableKeyPressed
        // TODO add your handling code here:
        if(accessType==ADMIN){
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DELETE){
                int choice=JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?","Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(choice==JOptionPane.YES_OPTION){
                    deleteItem();
                }                
            }
        }
    }//GEN-LAST:event_orderRecordTableKeyPressed

    private void orderRecordTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderRecordTableMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()== 2){
            displayBill();
        }
    }//GEN-LAST:event_orderRecordTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable orderRecordTable;
    private javax.swing.JButton printBtn;
    private javax.swing.JButton returnBtn;
    // End of variables declaration//GEN-END:variables
}

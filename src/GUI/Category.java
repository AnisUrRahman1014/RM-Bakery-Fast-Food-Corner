/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Classes.DBConnection;
import Classes.DealItem;
import Classes.Item;
import Classes.OrderItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.sql.*;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author M AYAN LAPTOP
 */
public class Category extends javax.swing.JPanel {

    String categoryName;
    ArrayList<Item> items=new ArrayList<>();
    ArrayList<String> categoryList=new ArrayList<>();
    Object parentFrame;
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    /**
     * Creates new form Category
     * @param parent
     * @param name
     */
    public Category(mainMenu parent,String name) {
        this.parentFrame=parent;
        this.categoryName=name;
        initComponents();
        listCtgItems();
        prepareItemButtons();
        setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    ((mainMenu)parentFrame).proceedTransaction();
                }
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    ((mainMenu)parentFrame).resetReceipt();
                }else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog((Component)parentFrame,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
    }
    
    public Category(ManageDeals parent,String name) {
        this.categoryName=name;
        this.parentFrame=parent;
        initComponents();
        listCtgItems();
        prepareItemButtons();
        setFocusable(true);
        
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
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from categories");
            rs=pst.executeQuery();
            while(rs.next()){
                if(!rs.getString("categoryName").equals("Deals")){
                    categoryList.add(rs.getString("categoryName"));
                }                
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void listCtgItems(){
        items.removeAll(items);
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from menuItems where itemCtg='"+categoryName+"'");
            rs=pst.executeQuery();
            Item item;
            while(rs.next()){
                String id=rs.getString("itemID");
                String name=rs.getString("itemName");
                String ctg=rs.getString("itemCtg");
                boolean sizeFlag=rs.getBoolean("sizeVarFlag");
                int singelCostPrice=rs.getInt("CostPrice");
                int singleSalePrice=rs.getInt("salePrice");
                int smallCostPrice=rs.getInt("smallCost");
                int smallSalePrice=rs.getInt("smallPrice");
                int mediumCostPrice=rs.getInt("mediumCost");
                int mediumSalePrice=rs.getInt("mediumPrice");
                int largeCostPrice=rs.getInt("largeCost");
                int largeSalePrice=rs.getInt("largePrice");
                int xLargeCostPrice=rs.getInt("xLargeCost");
                int xLargeSalePrice=rs.getInt("xLargePrice");
                String image=rs.getString("imageName");
                item=new Item(id,name,ctg,sizeFlag,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singelCostPrice,singleSalePrice,image);
                items.add(item);
            }
            
            pst=con.prepareStatement("select * from menuItems where itemCtg='All'");
            rs=pst.executeQuery();
            while(rs.next()){
                String id=rs.getString("itemID");
                String name=rs.getString("itemName");
                String ctg=rs.getString("itemCtg");
                boolean sizeFlag=rs.getBoolean("sizeVarFlag");
                int singelCostPrice=rs.getInt("CostPrice");
                int singleSalePrice=rs.getInt("salePrice");
                int smallCostPrice=rs.getInt("smallCost");
                int smallSalePrice=rs.getInt("smallPrice");
                int mediumCostPrice=rs.getInt("mediumCost");
                int mediumSalePrice=rs.getInt("mediumPrice");
                int largeCostPrice=rs.getInt("largeCost");
                int largeSalePrice=rs.getInt("largePrice");
                int xLargeCostPrice=rs.getInt("xLargeCost");
                int xLargeSalePrice=rs.getInt("xLargePrice");
                String image=rs.getString("imageName");
                item=new Item(id,name,ctg,sizeFlag,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singelCostPrice,singleSalePrice,image);
                items.add(item);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void prepareItemButtons(){        
        for(Item i:items){
            if(parentFrame instanceof mainMenu && i.getId().equals(DealItem.UNIVERSAL)){
                continue;
            }  
            JButton button=new JButton(i.getName());
            button.setHorizontalAlignment(JButton.TRAILING);
            button.setVerticalAlignment(JButton.BOTTOM);
            button.setFont(new java.awt.Font("Segoe UI", 1, 20));
            button.setPreferredSize(new Dimension(200,200));
            button.setMinimumSize(new Dimension(200,200));

            ImageIcon icon=new ImageIcon(System.getProperty("user.dir")+"\\Pictures\\"+i.getImageName());
            Image image = icon.getImage();
            Image resizedImage = image.getScaledInstance(160, 100, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon=new ImageIcon(resizedImage);

            JLabel iconLabel = new JLabel(resizedIcon);

            button.add(iconLabel);
//            button.setText("");
            button.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
//            
//            // Use HTML to allow line breaks
//            JLabel name = new JLabel("<html><div style='text-align: center;'>" + i.getName() + "</div></html>");
//            name.setFont(new java.awt.Font("Segoe UI", 1, 20));
//
//            // Set a fixed width to wrap text within the button
//            name.setPreferredSize(new Dimension(180, 50)); // Adjust dimensions as needed
//
//            button.add("nameLabel", name);

            button.addActionListener((ActionEvent e) -> {
                itemButtonActionPerformed(button);
            });
            
            button.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                        ((mainMenu)parentFrame).proceedTransaction();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                        ((mainMenu)parentFrame).resetReceipt();
                    }else
                    if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                        JOptionPane.showOptionDialog((Component)parentFrame,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                    }
                }
            });
            
            button.setFocusable(true);
            this.add(button);                    
        }
        
//        if(parentFrame instanceof ManageDeals){
//            System.out.println("THIS");
//            JButton button=new JButton("Universal Item");
//            button.setHorizontalAlignment(JButton.TRAILING);
//            button.setVerticalAlignment(JButton.BOTTOM);
//            button.setFont(new java.awt.Font("Segoe UI", 1, 16));
//            button.setPreferredSize(new Dimension(200,200));
//            button.setMinimumSize(new Dimension(200,200));
//            button.setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
//            
//            JLabel universalDeal=new JLabel("<html><center>Universal Item from this category</center></html>");
//            universalDeal.setFont(new java.awt.Font("Segoe UI", 1, 20));
//            universalDeal.setPreferredSize(new Dimension(170,120));
////            universalDeal.setMaximumSize(new Dimension(190,190));
//            button.add(universalDeal);
//            
//            button.addActionListener((ActionEvent e) -> {
//                universalDealActionPerformed(button);
//            });
//            this.add(button);
//        }        
    }
    
    private ArrayList<OrderItem> createItemsForDeal(ResultSet rs){
        ArrayList<OrderItem> dealItems=new ArrayList<>();
        try{
            while(rs.next()){
                String itemId=rs.getString("itemId");
                String itemName=rs.getString("itemName");
                String itemCtg=rs.getString("itemCtg");
                String size=rs.getString("itemSize");
                int costPrice=rs.getInt("dealCost");
                int salePrice=rs.getInt("dealSalePrice");
                int quantity=rs.getInt("itemQuantity");
                
                if(itemId.equals(DealItem.UNIVERSAL)){
                    ArrayList<Item> itemsList=new ArrayList<>();
                    try{
                        con=DBConnection.connectDB();
                        pst=con.prepareStatement("select * from menuItems where itemctg='"+itemCtg+"'");
                        rs=pst.executeQuery();
                        while(rs.next()){
                            String id=rs.getString("itemID");
                            String name=rs.getString("itemname");
                            String category=rs.getString("itemCtg");
                            boolean sizeVar=rs.getBoolean("SizeVarFlag");
                            int smallPrice=rs.getInt("smallPrice");
                            int mediumPrice=rs.getInt("mediumPrice");
                            int largePrice=rs.getInt("largePrice");
                            int xLargePrice=rs.getInt("xLargePrice");
//                            itemsList.add(new Item(id,name,category,costPrice,sizeVar,smallPrice,mediumPrice,largePrice,xLargePrice,salePrice));
                        }                    
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        closeConnection();
                    }

                    ArrayList<String> itemNamesList=new ArrayList<>();
                    for(Item i:itemsList){
                        itemNamesList.add(i.getName());
                    }
                    Object selectedItemName=JOptionPane.showInputDialog(this, "Please choose an item from this category: ", "Choose Item", JOptionPane.QUESTION_MESSAGE, null,itemNamesList.toArray(), null);
                    if(selectedItemName!=null){
                        String choosenItemName=(String)selectedItemName;
                        for(Item i:itemsList){
                            if(i.getName().equals(choosenItemName)){
//                                OrderItem item=new OrderItem(i,quantity,i.getSingleSalePrice(),size);   
//                                dealItems.add(item);
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return dealItems;
    }

    private void itemButtonActionPerformed(JButton btn){
        Item selectedItem=null;
        String itemName=btn.getText();
        if(itemName.isBlank()){
            JLabel label=(JLabel) btn.getComponents()[1];
            itemName=label.getText();
        }
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from menuItems where itemName='"+itemName+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                String itemId=rs.getString("itemID");
                String itemCtg=rs.getString("itemCtg");
                boolean sizeFlag=rs.getBoolean("sizeVarFlag");
                int singelCostPrice=rs.getInt("CostPrice");
                int singleSalePrice=rs.getInt("salePrice");
                int smallCostPrice=rs.getInt("smallCost");
                int smallSalePrice=rs.getInt("smallPrice");
                int mediumCostPrice=rs.getInt("mediumCost");
                int mediumSalePrice=rs.getInt("mediumPrice");
                int largeCostPrice=rs.getInt("largeCost");
                int largeSalePrice=rs.getInt("largePrice");
                int xLargeCostPrice=rs.getInt("xLargeCost");
                int xLargeSalePrice=rs.getInt("xLargePrice");
                selectedItem=new Item(itemId,itemName,itemCtg,sizeFlag,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singelCostPrice,singleSalePrice);
            }else{
                JOptionPane.showMessageDialog(this, "Item not found", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
            boolean abortCheck=false;
            if(selectedItem!=null){
                String choosenSize="---";
                int price=selectedItem.getSingleSalePrice();
                if(selectedItem.isSizeFlag()){                    
                    ArrayList<String> options=new ArrayList<>();
                    if(selectedItem.issFlag()){
                        options.add("Small");
                    }
                    if(selectedItem.ismFlag()){
                        options.add("Medium");
                    }
                    if(selectedItem.islFlag()){
                        options.add("Large");
                    }
                    if(selectedItem.isXlFlag()){
                        options.add("Extra Large");
                    }
                    Object output=JOptionPane.showInputDialog(this,"Please choose the size", "Size Chooser", JOptionPane.QUESTION_MESSAGE, null, options.toArray(), "Small");
                    if(output!=null){
                        choosenSize=output.toString();
                        System.out.println(choosenSize);
                    }else{
                        abortCheck=true;
                    }                    
                }else{
                    price=selectedItem.getSingleSalePrice();
                    choosenSize="---";
                }
                if(!abortCheck){
                    String output;
                    if(selectedItem.getName().equals("Extras")){
                        output=String.valueOf(1);
                        Object outputForExtraCharges=JOptionPane.showInputDialog(this, "Please enter the extra charges: ", "Extra Charges", JOptionPane.INFORMATION_MESSAGE, null, null, null);
                            if(outputForExtraCharges!=null){
                                try{
                                    int extraCharges=Integer.valueOf(outputForExtraCharges.toString());
                                    selectedItem.setSingleSalePrice(extraCharges);                                    
                                }catch(NumberFormatException e){
                                    JOptionPane.showMessageDialog(this, "Invalid Input, try again.","Invalid input",JOptionPane.ERROR_MESSAGE);
                                }
                            }
                    }else{
                        output=JOptionPane.showInputDialog(this,"Please enter quantity: ", "Item Quantity",JOptionPane.NO_OPTION);                    
                    }
                    if(!output.isBlank()){
                        try{                       
                            int quantity=Integer.valueOf(output);
                            if(quantity<=0){
                                JOptionPane.showMessageDialog(this, "Invalid Quantity","Quantity Invalid",JOptionPane.ERROR_MESSAGE);
                            }else{
                                boolean alreadyExists=false;
                                if(parentFrame instanceof mainMenu frame){
                                    // FOR DEAL ITEMS
//                                    if(selectedItem.getCategory().equals("Deals")){
//                                        ArrayList<OrderItems> dealItems=new ArrayList<>();
//                                        String dealName=selectedItem.getName();
//                                        pst=con.prepareStatement("select * from deals inner join dealItems on deals.dealName=dealItems.dealName where deals.dealName='"+dealName+"'");
//                                        rs=pst.executeQuery();
//                                        if(rs.next()){
//                                            dealItems=createItemsForDeal(rs);
//                                        }
//                                    }
                                    OrderItem i=new OrderItem(selectedItem,quantity,choosenSize);
                                    for(OrderItem x: mainMenu.orderItems){
                                        if(i.toString().equals(x.toString())){
                                            JOptionPane.showMessageDialog(this, "Item already exists.\nPlease update the quantity of this item in the table.","Invalid choice",JOptionPane.ERROR_MESSAGE);
                                            alreadyExists=true;
                                            break;
                                        }
                                    }
                                    if(!alreadyExists){
                                        mainMenu.orderItems.add(i);
                                        frame.updateOrderReceipt();
                                    }                                    
                                }
//                                else if(parentFrame instanceof ManageDeals frame){
//                                    if(!selectedItem.getCategory().equals("Deals")){
//                                        DealItem i=new DealItem(selectedItem,quantity,choosenSize,price,DealItem.NORMAL);
//                                        for(DealItem x: ManageDeals.dealItems){
//                                            if(i.toString().equals(x.toString())){
//                                                JOptionPane.showMessageDialog(this, "Item already exists.\nPlease update the quantity of this item in the table.","Invalid choice",JOptionPane.ERROR_MESSAGE);
//                                                alreadyExists=true;
//                                                break;
//                                            }
//                                        }
//                                        if(!alreadyExists){
//                                            ManageDeals.dealItems.add(i);
//                                            frame.updateDealTable();
//                                        }                                        
//                                    }else{
//                                        JOptionPane.showMessageDialog(this, "Cannot add a deal inside a deal.","Invalid choice",JOptionPane.ERROR_MESSAGE);
//                                    }                                  
//                                }
                            }
                        }catch(NumberFormatException e){
                            JOptionPane.showMessageDialog(this, "Invalid Input, try again.","Invalid input",JOptionPane.ERROR_MESSAGE);
                        } 
                    }                                       
                }                
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    private void universalDealActionPerformed(JButton btn){
        String output1=JOptionPane.showInputDialog(this, "Enter Quantity", "Enter Quantity", JOptionPane.QUESTION_MESSAGE);
        String output2=JOptionPane.showInputDialog(this, "Enter Price", "Enter Price", JOptionPane.QUESTION_MESSAGE);
        if(output1.isBlank() || output2.isBlank()){
            JOptionPane.showMessageDialog(this, "Invalid input","Error Message",JOptionPane.ERROR_MESSAGE);
        }else{
            try{
                boolean abortCheck=false;
                int quantity=Integer.valueOf(output1);
                int price=Integer.valueOf(output2);
                String choosenSize=null;
                Object[] options={"---","Small","Medium","Large","Extra Large"};
                Object output=JOptionPane.showInputDialog(this,"Please choose the size", "Size Chooser", JOptionPane.QUESTION_MESSAGE, null, options, "---");
                if(output!=null){
                    choosenSize=output.toString();
                }else{
                    abortCheck=true;
                }
                if(!abortCheck){
                    DealItem dealItem=new DealItem(categoryName,quantity,choosenSize,price,DealItem.UNIVERSAL);
                    ManageDeals.dealItems.add(dealItem);
                    ((ManageDeals) parentFrame).updateDealTable(); 
                }
                               
            }catch(NumberFormatException e){
                e.printStackTrace();
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
        java.awt.GridBagConstraints gridBagConstraints;

        setMaximumSize(new java.awt.Dimension(896, 32767));
        setMinimumSize(new java.awt.Dimension(896, 10));
        setPreferredSize(new java.awt.Dimension(896, 644));
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

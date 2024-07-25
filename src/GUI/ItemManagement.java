/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Classes.DBConnection;
import Classes.Item;
import com.formdev.flatlaf.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author M AYAN LAPTOP
 */
public class ItemManagement extends javax.swing.JFrame {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    JPopupMenu contextMenu = new JPopupMenu();
    ArrayList<String> categoryList=new ArrayList<>();
    String imageName;
    /**
     * Creates new form ItemManagement
     */
    public ItemManagement() {
        setTitle("Item Management");
        FlatDarkLaf.setup();
        initComponents();        
        itemTable.getColumnModel().getColumn(0).setPreferredWidth(40); // S/No
        itemTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Item Name
        itemTable.getColumnModel().getColumn(3).setPreferredWidth(115); // Item Category  
        itemTable.getColumnModel().getColumn(4).setPreferredWidth(30); 
        itemTable.getColumnModel().getColumn(5).setPreferredWidth(50); 
        itemTable.getColumnModel().getColumn(6).setPreferredWidth(50); 
        itemTable.getColumnModel().getColumn(7).setPreferredWidth(50);
        itemTable.getColumnModel().getColumn(8).setPreferredWidth(50);
        itemTable.getColumnModel().getColumn(9).setPreferredWidth(50);
        itemTable.getColumnModel().getColumn(10).setPreferredWidth(50);
        itemTable.getColumnModel().getColumn(11).setPreferredWidth(50);
        itemTable.getColumnModel().getColumn(12).setPreferredWidth(50);
        itemTable.getColumnModel().getColumn(13).setPreferredWidth(50);
        itemTable.getColumnModel().getColumn(14).setPreferredWidth(50);
        defaultSetting();
        setContextBox();
        itemTable.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = itemTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < itemTable.getRowCount()) {
                    itemTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = itemTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row < itemTable.getRowCount()) {
                    itemTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    });        
        //FRAME SETTING
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        setMaximumSize(screenSize);
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
                        DefaultTableModel model=(DefaultTableModel) itemTable.getModel();
                        String itemID=model.getValueAt(itemTable.getSelectedRow(), 1).toString();
                        String itemCtg=model.getValueAt(itemTable.getSelectedRow(), 3).toString();
                        con=DBConnection.connectDB();
                        pst=con.prepareStatement("update menuItems set imageName='"+selectedFile.getName()+"' where itemID='"+itemID+"' and itemCtg='"+itemCtg+"'");
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
        DefaultTableModel model=(DefaultTableModel) itemTable.getModel();
        model.setRowCount(0);
    }
    
    private void listItemsInTable(String s){
        String query="select * from menuItems";
        if(s!=null){
            query=s;
        }
        clearTable();
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement(query);
            rs=pst.executeQuery();
            int count=0;
            while(rs.next()){
                String id=rs.getString("itemID");
                String name=rs.getString("itemname");
                String category=rs.getString("itemCtg");
                boolean sizeVar=rs.getBoolean("SizeVarFlag");
                int singleCostPrice=rs.getInt("CostPrice");
                int singleSalePrice=rs.getInt("salePrice");
                int smallCostPrice=rs.getInt("smallCost");
                int smallSalePrice=rs.getInt("smallPrice");
                int mediumCostPrice=rs.getInt("mediumCost");
                int mediumSalePrice=rs.getInt("mediumPrice");
                int largeCostPrice=rs.getInt("largeCost");
                int largeSalePrice=rs.getInt("largePrice");
                int xLargeCostPrice=rs.getInt("xLargeCost");
                int xLargeSalePrice=rs.getInt("xLargePrice");
                Object row[]={++count,id,name,category,sizeVar,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singleCostPrice,singleSalePrice};
                DefaultTableModel model=(DefaultTableModel)itemTable.getModel();
                model.addRow(row);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    public void listCategories(){
        categoryList.removeAll(categoryList);
        categoryCB.removeAllItems();
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
//            if(!s.equals("Deals"))
                categoryCB.addItem(s);
        }
        categoryCB.setSelectedIndex(-1);
    }
    
    private void updateItemInDB(){
        DefaultTableModel model=(DefaultTableModel) itemTable.getModel();        
        int decision=JOptionPane.showConfirmDialog(this, "Are you sure you want to commit changes?","Confirmation Message",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(decision==JOptionPane.YES_OPTION){
            for(int i=0;i<model.getRowCount();i++){
                String itemID=model.getValueAt(i,1).toString();
                String itemCtg=model.getValueAt(i,3).toString();
                int singleCostPrice=Integer.valueOf(model.getValueAt(i, 13).toString());
                boolean sizeFlag=(boolean)model.getValueAt(i, 4);
                int singleSalePrice=Integer.valueOf(model.getValueAt(i, 14).toString());
                int smallCostPrice=Integer.valueOf(model.getValueAt(i, 5).toString());
                int smallSalePrice=Integer.valueOf(model.getValueAt(i, 6).toString());
                int mediumCostPrice=Integer.valueOf(model.getValueAt(i, 7).toString());
                int mediumSalePrice=Integer.valueOf(model.getValueAt(i, 8).toString());
                int largeCostPrice=Integer.valueOf(model.getValueAt(i, 9).toString());
                int largeSalePrice=Integer.valueOf(model.getValueAt(i, 10).toString());
                int xLargeCostPrice=Integer.valueOf(model.getValueAt(i, 11).toString());
                int xLargeSalePrice=Integer.valueOf(model.getValueAt(i, 12).toString());
                try{
                    con=DBConnection.connectDB();
                    pst=con.prepareStatement("update menuItems set costPrice=? , salePrice=? , sizeVarFlag=? , smallPrice=? , mediumPrice=? , largePrice=? , xLargePrice=? , smallCost=?, mediumCost=?, largeCost=?, xLargeCost=? where itemID='"+itemID+"' and itemCtg='"+itemCtg+"'");
                    pst.setBoolean(3,sizeFlag);
                    if(!sizeFlag){
                        smallCostPrice=0;
                        smallSalePrice=0;
                        mediumCostPrice=0;
                        mediumSalePrice=0;
                        largeCostPrice=0;
                        largeSalePrice=0;
                        xLargeCostPrice=0;
                        xLargeSalePrice=0;
                    }else{
                        singleCostPrice=0;
                        singleSalePrice=0;
                    }
                    pst.setInt(1,singleCostPrice);            
                    pst.setInt(2, singleSalePrice);
                    pst.setInt(4,smallSalePrice);
                    pst.setInt(5,mediumSalePrice);
                    pst.setInt(6,largeSalePrice);
                    pst.setInt(7,xLargeSalePrice);
                    pst.setInt(8, smallCostPrice);
                    pst.setInt(9, mediumCostPrice);
                    pst.setInt(10,largeCostPrice);
                    pst.setInt(11, xLargeCostPrice);
                    pst.executeUpdate();
//                    JOptionPane.showMessageDialog(this, itemID+" was updated successfully","Item Updated",JOptionPane.INFORMATION_MESSAGE);
                    
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    closeConnection();
                }                
            }
            listItemsInTable(null);            
        }
        
    }
    
    private void defaultSetting(){
        // LISTING CATEGORIES
        listCategories();
        
        // CLEARING FIELDS
        itemIdField.setText("");
        itemNameField.setText("");
        costPriceField.setText("");        
        salePriceField.setText("");
        
        smallPriceField.setText("");
        sCostField.setText("");
        
        mediumPriceField.setText("");
        mCostField.setText("");
        
        largePriceField.setText("");
        lCostField.setText("");
        
        xLargePriceField.setText("");
        xlCostField.setText("");
        
        categoryCB.setSelectedIndex(-1);
        sizeVarCB.setSelected(false);
        smallRB.setSelected(false);
        mediumRB.setSelected(false);
        largeRB.setSelected(false);
        xLargeRB.setSelected(false);
        // DISABLING SIZE VAIRATION ATTRIBUTES
        sizeVarDescLabel.setEnabled(false);
            
        smallRB.setEnabled(false);
        sCostField.setEnabled(false);
        smallPriceField.setEnabled(false);

        mediumRB.setEnabled(false);
        mCostField.setEnabled(false);
        mediumPriceField.setEnabled(false);

        largeRB.setEnabled(false);
        lCostField.setEnabled(false);
        largePriceField.setEnabled(false);

        xLargeRB.setEnabled(false);
        xlCostField.setEnabled(false);
        xLargePriceField.setEnabled(false);
        
        // ENABLING SINGLE PRICE ATTRIBUTES
        singlePriceDesc.setEnabled(true);
        salePriceLabel.setEnabled(true);
        salePriceField.setEnabled(true);
        costPriceLabel.setEnabled(true);
        costPriceField.setEnabled(true);
        
        // LISTING ITEMS IN TABLE
        listItemsInTable(null);
    }
    
    private void sizeVarFieldsManagement(){
        if(sizeVarCB.isSelected()){
            sizeVarDescLabel.setEnabled(true);
            
            smallRB.setEnabled(true);
//            smallPriceField.setEnabled(true);
            
            mediumRB.setEnabled(true);
//            mediumPriceField.setEnabled(true);
            
            largeRB.setEnabled(true);
//            largePriceField.setEnabled(true);
            
            xLargeRB.setEnabled(true);
//            xLargePriceField.setEnabled(true);
            
            singlePriceDesc.setEnabled(false);
            salePriceLabel.setEnabled(false);
            salePriceField.setEnabled(false);
            
            costPriceLabel.setEnabled(false);
            costPriceField.setEnabled(false);
        }else{
            sizeVarDescLabel.setEnabled(false);
            
            smallRB.setEnabled(false);
            smallPriceField.setEnabled(false);
            
            mediumRB.setEnabled(false);
            mediumPriceField.setEnabled(false);
            
            largeRB.setEnabled(false);
            largePriceField.setEnabled(false);
            
            xLargeRB.setEnabled(false);
            xLargePriceField.setEnabled(false);
            
            singlePriceDesc.setEnabled(true);
            salePriceLabel.setEnabled(true);
            salePriceField.setEnabled(true);
            
            costPriceLabel.setEnabled(true);
            costPriceField.setEnabled(true);
        }
    }
    
    private boolean fieldCheck(){
        if(itemIdField.getText().isEmpty() || itemNameField.getText().isEmpty() || categoryCB.getSelectedIndex()==-1){
            return false;
        }
        if(sizeVarCB.isSelected()){
            if(smallRB.isSelected()){
                if(smallPriceField.getText().isEmpty()){
                    return false;
                }
            }
            if(mediumRB.isSelected()){
                if(mediumPriceField.getText().isEmpty()){
                    return false;
                }
            }
            if(largeRB.isSelected()){
                if(largePriceField.getText().isEmpty()){
                    return false;
                }
            }
            if(xLargeRB.isSelected()){
                if(xLargePriceField.getText().isEmpty()){
                    return false;
                }
            }
        }else{
            if(salePriceField.getText().isEmpty()){
                return false;
            }
        }
        return true;
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
    
    private void addItem(){
        if(fieldCheck()){
            String id=itemIdField.getText();
            String name=itemNameField.getText();
            String ctg=categoryCB.getSelectedItem().toString();
            int singleCostPrice=0;
            int smallSalePrice;
            int smallCostPrice;
            int mediumSalePrice;
            int mediumCostPrice;
            int largeSalePrice;
            int largeCostPrice;
            int xLargeSalePrice;
            int xLargeCostPrice;
            int simpleSalePrice;
            Item item=new Item(id,name,ctg,singleCostPrice,sizeVarCB.isSelected());
            if(sizeVarCB.isSelected()){
                if(smallRB.isSelected()){
                    smallSalePrice=Integer.valueOf(smallPriceField.getText());
                    smallCostPrice=Integer.valueOf(sCostField.getText());
                    item.setsFlag(true);
                    item.setSmallSalePrice(smallSalePrice);
                    item.setSmallCost(smallCostPrice);
                }
                if(mediumRB.isSelected()){
                    mediumSalePrice=Integer.valueOf(mediumPriceField.getText());
                    mediumCostPrice=Integer.valueOf(mCostField.getText());
                    item.setmFlag(true);
                    item.setMediumSalePrice(mediumSalePrice);
                    item.setMediumCost(mediumCostPrice);
                }
                if(largeRB.isSelected()){
                    largeSalePrice=Integer.valueOf(largePriceField.getText());
                    largeCostPrice=Integer.valueOf(lCostField.getText());
                    item.setlFlag(true);
                    item.setLargeSalePrice(largeSalePrice);
                    item.setLargeCost(largeCostPrice);
                }
                if(xLargeRB.isSelected()){
                    xLargeSalePrice=Integer.valueOf(xLargePriceField.getText());
                    xLargeCostPrice=Integer.valueOf(xlCostField.getText());
                    item.setXlFlag(true);
                    item.setXLargeSalePrice(xLargeSalePrice);
                    item.setXLargeCost(xLargeCostPrice);
                }
            }else{
                singleCostPrice=Integer.valueOf(costPriceField.getText());
                simpleSalePrice=Integer.valueOf(salePriceField.getText());
                item.setSingleSalePrice(simpleSalePrice);
                item.setSingleCostPrice(singleCostPrice);
            }
            if(imageCB.isSelected()){
                item.setImageName(imageName);
            }
            try{
                con=DBConnection.connectDB();
                pst=con.prepareStatement("insert into menuItems values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1,item.getId());
                pst.setString(2,item.getName());
                pst.setString(3,item.getCategory());
                pst.setInt(4,item.getSingleCostPrice());
                pst.setBoolean(5, item.isSizeFlag());
                pst.setInt(6, item.getSingleSalePrice());
                pst.setInt(7,item.getSmallSalePrice());
                pst.setInt(8,item.getMediumSalePrice());
                pst.setInt(9,item.getLargeSalePrice());
                pst.setInt(10, item.getXLargeSalePrice());
                pst.setString(11,item.getImageName());
                pst.setInt(12,item.getSmallCost());
                pst.setInt(13,item.getMediumCost());
                pst.setInt(14,item.getLargeCost());
                pst.setInt(15,item.getXLargeCost());
                pst.execute();
                JOptionPane.showMessageDialog(this, "Item added successfully.","Confirmation Message",JOptionPane.INFORMATION_MESSAGE);
                defaultSetting();
            }catch(SQLException e){
                e.printStackTrace();
                 JOptionPane.showMessageDialog(this, "Failed to add item due to key duplicatoin.\nPlease recheck the ID field and try again.","Error Message",JOptionPane.ERROR_MESSAGE);
            }finally{
                closeConnection();
            }
            
        }else{
            JOptionPane.showMessageDialog(this, "Failed to add item.\nPlease recheck the fields and try again.","Error Message",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteItem(){
        DefaultTableModel model=(DefaultTableModel) itemTable.getModel();
        String selectedId=model.getValueAt(itemTable.getSelectedRow(), 1).toString();
        String selectedName=model.getValueAt(itemTable.getSelectedRow(), 2).toString();
        int decision=JOptionPane.showConfirmDialog(this, "Are you sure you want to delete \""+selectedName+"\" from the database?\nAll related record would be deleted.","Confirmation Message",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(decision == 0){
            boolean validate=false;
            String password=JOptionPane.showInputDialog(this, "Please enter an Admin Password:","Verification",JOptionPane.OK_OPTION);
            try{
                con=DBConnection.connectDB();
                pst=con.prepareStatement("select * from users where userType='Admin'");
                rs=pst.executeQuery();
                while(rs.next()){
                    if(password.equals(rs.getString("password"))){
                        validate=true;
                        break;
                    }
                }    
                if(validate){
                    pst=con.prepareStatement("delete from orderItems where itemId='"+selectedId+"'");
                    pst.executeUpdate();
                    pst=con.prepareStatement("delete from menuItems where itemId='"+selectedId+"'");
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this,"Item \""+selectedId+"\" was deleted successfully.","Item Deleted",JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(this,"Validation failed.\nItem \""+selectedId+"\" was not deleted.","Validation Failed",JOptionPane.ERROR_MESSAGE);
                }
                defaultSetting();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }        
    }
    
    private void searchTable(){
        String query="select * from menuItems where itemName like '%"+searchBar.getText()+"%'";
        listItemsInTable(query);
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
        rootPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        costPriceLabel = new javax.swing.JLabel();
        itemIdField = new javax.swing.JTextField();
        itemNameField = new javax.swing.JTextField();
        costPriceField = new javax.swing.JTextField();
        categoryCB = new javax.swing.JComboBox<>();
        sizeVarCB = new javax.swing.JCheckBox();
        sizeVarDescLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        smallRB = new javax.swing.JRadioButton();
        mediumRB = new javax.swing.JRadioButton();
        largeRB = new javax.swing.JRadioButton();
        xLargeRB = new javax.swing.JRadioButton();
        smallPriceField = new javax.swing.JTextField();
        mediumPriceField = new javax.swing.JTextField();
        largePriceField = new javax.swing.JTextField();
        xLargePriceField = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        salePriceLabel = new javax.swing.JLabel();
        salePriceField = new javax.swing.JTextField();
        addBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        manageCtgBtn = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        exitBtn = new javax.swing.JButton();
        singlePriceDesc = new javax.swing.JLabel();
        manageDealsBtn = new javax.swing.JButton();
        imageCB = new javax.swing.JCheckBox();
        sCostField = new javax.swing.JTextField();
        mCostField = new javax.swing.JTextField();
        lCostField = new javax.swing.JTextField();
        xlCostField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        searchBar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        rootPanel.setBackground(new java.awt.Color(61, 91, 116));
        rootPanel.setForeground(new java.awt.Color(61, 91, 116));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Item Details");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Item ID: ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Item Name:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Category:");

        costPriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        costPriceLabel.setText("Cost Price:");

        itemIdField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemIdField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemIdFieldKeyPressed(evt);
            }
        });

        itemNameField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemNameFieldKeyPressed(evt);
            }
        });

        costPriceField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        costPriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                costPriceFieldKeyPressed(evt);
            }
        });

        categoryCB.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        categoryCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryCBActionPerformed(evt);
            }
        });
        categoryCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                categoryCBKeyPressed(evt);
            }
        });

        sizeVarCB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sizeVarCB.setText("Size-Variation");
        sizeVarCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sizeVarCBActionPerformed(evt);
            }
        });
        sizeVarCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sizeVarCBKeyPressed(evt);
            }
        });

        sizeVarDescLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        sizeVarDescLabel.setText("Please choose the available sizes and set their prices:");

        smallRB.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        smallRB.setText("Small");
        smallRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smallRBActionPerformed(evt);
            }
        });
        smallRB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                smallRBKeyPressed(evt);
            }
        });

        mediumRB.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        mediumRB.setText("Medium");
        mediumRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mediumRBActionPerformed(evt);
            }
        });
        mediumRB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mediumRBKeyPressed(evt);
            }
        });

        largeRB.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        largeRB.setText("Large");
        largeRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                largeRBActionPerformed(evt);
            }
        });
        largeRB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                largeRBKeyPressed(evt);
            }
        });

        xLargeRB.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        xLargeRB.setText("Extra Large");
        xLargeRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xLargeRBActionPerformed(evt);
            }
        });
        xLargeRB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                xLargeRBKeyPressed(evt);
            }
        });

        smallPriceField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        smallPriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                smallPriceFieldKeyPressed(evt);
            }
        });

        mediumPriceField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        mediumPriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mediumPriceFieldKeyPressed(evt);
            }
        });

        largePriceField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        largePriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                largePriceFieldKeyPressed(evt);
            }
        });

        xLargePriceField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        xLargePriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                xLargePriceFieldKeyPressed(evt);
            }
        });

        salePriceLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        salePriceLabel.setText("Sale Price");

        salePriceField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        salePriceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                salePriceFieldKeyPressed(evt);
            }
        });

        addBtn.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        addBtn.setText("Add Item");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });
        addBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addBtnKeyPressed(evt);
            }
        });

        cancelBtn.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        cancelBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cancelBtnKeyPressed(evt);
            }
        });

        manageCtgBtn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        manageCtgBtn.setText("Manage Categories");
        manageCtgBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageCtgBtnActionPerformed(evt);
            }
        });
        manageCtgBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                manageCtgBtnKeyPressed(evt);
            }
        });

        exitBtn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        exitBtn.setText("Save & Exit");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });
        exitBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                exitBtnKeyPressed(evt);
            }
        });

        singlePriceDesc.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        singlePriceDesc.setText("Only applies to items with no size variation.");

        manageDealsBtn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        manageDealsBtn.setText("Manage Deals");
        manageDealsBtn.setEnabled(false);
        manageDealsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageDealsBtnActionPerformed(evt);
            }
        });
        manageDealsBtn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                manageDealsBtnKeyPressed(evt);
            }
        });

        imageCB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        imageCB.setText("Image ");
        imageCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageCBActionPerformed(evt);
            }
        });
        imageCB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                imageCBKeyPressed(evt);
            }
        });

        sCostField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        sCostField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sCostFieldKeyPressed(evt);
            }
        });

        mCostField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        mCostField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mCostFieldKeyPressed(evt);
            }
        });

        lCostField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lCostField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lCostFieldActionPerformed(evt);
            }
        });
        lCostField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lCostFieldKeyPressed(evt);
            }
        });

        xlCostField.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        xlCostField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                xlCostFieldKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Cost Price:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Sale Price");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(manageCtgBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(manageDealsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator3)
                    .addComponent(singlePriceDesc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator2)
                        .addComponent(sizeVarDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(xLargeRB, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(largeRB, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mediumRB, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(smallRB, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(xlCostField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lCostField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mCostField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sCostField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(xLargePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(largePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mediumPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(smallPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(91, 91, 91))
                    .addComponent(imageCB)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(costPriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(costPriceField))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(salePriceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(salePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(296, 296, 296)
                                .addComponent(sizeVarCB))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(categoryCB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(itemNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(itemIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(itemIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(itemNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(categoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sizeVarCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sizeVarDescLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(smallPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(smallRB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mediumPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mediumRB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(largePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(largeRB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(xLargePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(xLargeRB)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(sCostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mCostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lCostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(xlCostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singlePriceDesc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(costPriceLabel)
                    .addComponent(costPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salePriceLabel)
                    .addComponent(salePriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageCB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageCtgBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(manageDealsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61))
        );

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        itemTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S/No", "Item ID", "Item Name", "Category", "Size Flag", "S- Cost", "S- Sale", "M- Cost", "M- Sale", "L- Cost", "L- Sale", "Xl- Cost", "Xl- Sale", "Single Cost", "Single Sale"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 5 || columnIndex == 6 || columnIndex == 7 || columnIndex == 8 || columnIndex == 9 || columnIndex == 10 || columnIndex == 11 || columnIndex == 12) {
                    // Check if sizeFlag is true (editable) or false (not editable)
                    Boolean sizeFlag = (Boolean)getValueAt(rowIndex, 4);
                    return sizeFlag;
                }

                if(columnIndex == 13 || columnIndex == 14){
                    Boolean sizeFlag = (Boolean)getValueAt(rowIndex, 4);
                    return !sizeFlag;
                }
                return canEdit [columnIndex];
            }
        });
        itemTable.setColumnSelectionAllowed(true);
        itemTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        itemTable.setRowHeight(30);
        itemTable.getTableHeader().setReorderingAllowed(false);
        itemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemTableMouseClicked(evt);
            }
        });
        itemTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(itemTable);
        itemTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel6.setText("Search: ");

        searchBar.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchBarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 956, Short.MAX_VALUE)
                    .addComponent(jSeparator5)
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(62, 62, 62)
                        .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(rootPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 702, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
        );

        jScrollPane1.setViewportView(rootPanel);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void largeRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_largeRBActionPerformed
        // TODO add your handling code here:
        if(largeRB.isSelected()){
            largePriceField.setEnabled(true);
            lCostField.setEnabled(true);
        }else{
            largePriceField.setEnabled(false);
            lCostField.setEnabled(false);
        }
    }//GEN-LAST:event_largeRBActionPerformed

    private void sizeVarCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sizeVarCBActionPerformed
        // TODO add your handling code here:
        sizeVarFieldsManagement();
    }//GEN-LAST:event_sizeVarCBActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        // TODO add your handling code here:
        new OptionMenu().setVisible(true);
        dispose();
    }//GEN-LAST:event_exitBtnActionPerformed

    private void itemIdFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemIdFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_SPACE){
            itemIdField.setEditable(false);
        }else{
            itemIdField.setEditable(true);
            if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                itemNameField.requestFocus();
            }
            if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                if(sizeVarCB.isSelected()){
                    xLargeRB.requestFocus();
                }else{
                    salePriceField.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_itemIdFieldKeyPressed

    private void costPriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_costPriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            costPriceField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                costPriceField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    if(sizeVarCB.isSelected()){
                        smallRB.requestFocus();
                    }else{
                        salePriceField.requestFocus();
                    }                
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    categoryCB.requestFocus();
                }
            }else{
                costPriceField.setEditable(false);
            }
        }
    }//GEN-LAST:event_costPriceFieldKeyPressed

    private void smallRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_smallRBActionPerformed
        // TODO add your handling code here:
        if(smallRB.isSelected()){
            smallPriceField.setEnabled(true);
            sCostField.setEnabled(true);
        }else{
            smallPriceField.setEnabled(false);
            sCostField.setEnabled(false);
        }
    }//GEN-LAST:event_smallRBActionPerformed

    private void mediumRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mediumRBActionPerformed
        // TODO add your handling code here:
        if(mediumRB.isSelected()){
            mediumPriceField.setEnabled(true);
            mCostField.setEnabled(true);
        }else{
            mediumPriceField.setEnabled(false);
            mCostField.setEnabled(false);
        }
    }//GEN-LAST:event_mediumRBActionPerformed

    private void xLargeRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xLargeRBActionPerformed
        // TODO add your handling code here:
        if(xLargeRB.isSelected()){
            xLargePriceField.setEnabled(true);
            xlCostField.setEnabled(true);
        }else{
            xLargePriceField.setEnabled(false);
            xlCostField.setEnabled(false);
        }
    }//GEN-LAST:event_xLargeRBActionPerformed

    private void itemNameFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemNameFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
            categoryCB.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            itemIdField.requestFocus();
        }
    }//GEN-LAST:event_itemNameFieldKeyPressed

    private void categoryCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_categoryCBKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
            costPriceField.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            itemNameField.requestFocus();
        }
    }//GEN-LAST:event_categoryCBKeyPressed

    private void salePriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salePriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            salePriceField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                salePriceField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    addBtn.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    costPriceField.requestFocus();
                }
            }else{
                salePriceField.setEditable(false);
            }
        }
    }//GEN-LAST:event_salePriceFieldKeyPressed

    private void smallRBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_smallRBKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
            mediumRB.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            costPriceField.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            smallRB.setSelected(true);
            smallPriceField.setEnabled(true);
            sCostField.setEnabled(true);
            sCostField.requestFocus();
        }
    }//GEN-LAST:event_smallRBKeyPressed

    private void mediumRBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mediumRBKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
            largeRB.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            smallRB.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            mediumRB.setSelected(true);
            mediumPriceField.setEnabled(true);
            mCostField.setEnabled(true);
            mCostField.requestFocus();            
        }
    }//GEN-LAST:event_mediumRBKeyPressed

    private void largeRBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_largeRBKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
            xLargeRB.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            mediumRB.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            largeRB.setSelected(true);
            largePriceField.setEnabled(true);
            lCostField.setEnabled(true);
            lCostField.requestFocus();
        }
    }//GEN-LAST:event_largeRBKeyPressed

    private void xLargeRBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xLargeRBKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
            addBtn.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            largeRB.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            xLargeRB.setSelected(true);
            xLargePriceField.setEnabled(true);
            xlCostField.setEnabled(true);
            xlCostField.requestFocus();
        }
    }//GEN-LAST:event_xLargeRBKeyPressed

    private void smallPriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_smallPriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            smallPriceField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                smallPriceField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    mediumRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    costPriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    sCostField.requestFocus();
                }
            }else{
                smallPriceField.setEditable(false);
            }
        }
    }//GEN-LAST:event_smallPriceFieldKeyPressed

    private void mediumPriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mediumPriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            mediumPriceField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                mediumPriceField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    largeRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    mediumRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    mCostField.requestFocus();
                }
            }else{
                mediumPriceField.setEditable(false);
            }
        }
    }//GEN-LAST:event_mediumPriceFieldKeyPressed

    private void largePriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_largePriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            largePriceField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                largePriceField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    xLargeRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    lCostField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    mediumRB.requestFocus();
                }
            }else{
                largePriceField.setEditable(false);
            }
        }
    }//GEN-LAST:event_largePriceFieldKeyPressed

    private void xLargePriceFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xLargePriceFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            xLargePriceField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                xLargePriceField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    addBtn.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    xlCostField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    largeRB.requestFocus();
                }
            }else{
                xLargePriceField.setEditable(false);
            }
        }
    }//GEN-LAST:event_xLargePriceFieldKeyPressed

    private void addBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addBtnKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            if(sizeVarCB.isSelected()){
                xLargeRB.requestFocus();
            }else{
                salePriceField.requestFocus();
            }
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
            exitBtn.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
            cancelBtn.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT){
            manageCtgBtn.requestFocus();
        }
//        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
//            addItem();
//        }
    }//GEN-LAST:event_addBtnKeyPressed

    private void manageCtgBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_manageCtgBtnKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            if(sizeVarCB.isSelected()){
                xLargeRB.requestFocus();
            }else{
                salePriceField.requestFocus();
            }
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
            addBtn.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT){
            cancelBtn.requestFocus();
        }
    }//GEN-LAST:event_manageCtgBtnKeyPressed

    private void cancelBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cancelBtnKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            if(sizeVarCB.isSelected()){
                xLargeRB.requestFocus();
            }else{
                salePriceField.requestFocus();
            }
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
            manageCtgBtn.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT){
            addBtn.requestFocus();
        }
    }//GEN-LAST:event_cancelBtnKeyPressed

    private void exitBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_exitBtnKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
            addBtn.requestFocus();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            dispose();
        }
    }//GEN-LAST:event_exitBtnKeyPressed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        // TODO add your handling code here:
        addItem();
    }//GEN-LAST:event_addBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        defaultSetting();
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void itemTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemTableKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DELETE){
            deleteItem();
        }
        if(evt.getExtendedKeyCode() ==  KeyEvent.VK_ENTER){
            updateItemInDB();
        }
    }//GEN-LAST:event_itemTableKeyPressed

    private void sizeVarCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sizeVarCBKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            if(!sizeVarCB.isSelected()){                
                sizeVarCB.setSelected(true);
                sizeVarFieldsManagement();
                smallRB.requestFocus();
            }else{
                sizeVarCB.setSelected(false);
                sizeVarFieldsManagement();
                salePriceField.requestFocus();
            }
        }
    }//GEN-LAST:event_sizeVarCBKeyPressed

    private void manageCtgBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageCtgBtnActionPerformed
        // TODO add your handling code here:
        CategoryManagement obj=new CategoryManagement(this);
        obj.setVisible(true);
    }//GEN-LAST:event_manageCtgBtnActionPerformed

    private void manageDealsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageDealsBtnActionPerformed
        // TODO add your handling code here:
        new ManageDeals().setVisible(true);
        dispose();
    }//GEN-LAST:event_manageDealsBtnActionPerformed

    private void manageDealsBtnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_manageDealsBtnKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_manageDealsBtnKeyPressed

    private void imageCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageCBActionPerformed
        // TODO add your handling code here:
        if(imageCB.isSelected()){
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
                    imageName=selectedFile.getName();
                    JOptionPane.showMessageDialog(this, "Image \""+imageName+"\" selected.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error copying file!");
                }
            }
//            imageName=JOptionPane.showInputDialog(this,"Please paste the image in the given folder: \n"+System.getProperty("user.dir")+"\nNow provide the name of the image along with extension: \nEmample: ZingerBurger.png");
        }else{
            imageName=null;
        }
        
    }//GEN-LAST:event_imageCBActionPerformed

    private void imageCBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imageCBKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_imageCBKeyPressed

    private void searchBarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarKeyReleased
        // TODO add your handling code here:
        searchTable();
    }//GEN-LAST:event_searchBarKeyReleased

    private void categoryCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryCBActionPerformed

    private void itemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTableMouseClicked
        // TODO add your handling code here:
        if(evt.getButton() == MouseEvent.BUTTON2){
            
        }
    }//GEN-LAST:event_itemTableMouseClicked

    private void sCostFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sCostFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            sCostField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                sCostField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    smallPriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    costPriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    smallRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT){
                    smallPriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode() == KeyEvent.VK_DOWN){
                    mediumRB.requestFocus();
                }
            }else{
                sCostField.setEditable(false);
            }
        }
    }//GEN-LAST:event_sCostFieldKeyPressed

    private void mCostFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mCostFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            mCostField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                mCostField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    mediumPriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    smallRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    mediumRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT){
                    mediumPriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode() == KeyEvent.VK_DOWN){
                    largeRB.requestFocus();
                }
            }else{
                mCostField.setEditable(false);
            }
        }
    }//GEN-LAST:event_mCostFieldKeyPressed

    private void lCostFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lCostFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            lCostField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                lCostField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER || evt.getExtendedKeyCode()==KeyEvent.VK_DOWN){
                    largePriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    mediumRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    largeRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT){
                    largePriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode() == KeyEvent.VK_DOWN){
                    xLargeRB.requestFocus();
                }
            }else{
                lCostField.setEditable(false);
            }
        }
    }//GEN-LAST:event_lCostFieldKeyPressed

    private void xlCostFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xlCostFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            xlCostField.setEditable(true);
        }else{
            if(evt.getExtendedKeyCode()==KeyEvent.VK_DECIMAL||evt.getExtendedKeyCode()==KeyEvent.VK_UP||evt.getExtendedKeyCode()==KeyEvent.VK_DOWN||evt.getExtendedKeyCode()==KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode()==KeyEvent.VK_DELETE|| evt.getExtendedKeyCode()==KeyEvent.VK_LEFT || evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT || evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
                xlCostField.setEditable(true);
                if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                    xLargePriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_UP){
                    largeRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_LEFT){
                    xLargeRB.requestFocus();
                }
                if(evt.getExtendedKeyCode()==KeyEvent.VK_RIGHT){
                    xLargePriceField.requestFocus();
                }
                if(evt.getExtendedKeyCode() == KeyEvent.VK_DOWN){
                    addBtn.requestFocus();
                }
            }else{
                xlCostField.setEditable(false);
            }
        }
    }//GEN-LAST:event_xlCostFieldKeyPressed

    private void lCostFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lCostFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lCostFieldActionPerformed

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
            java.util.logging.Logger.getLogger(ItemManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItemManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox<String> categoryCB;
    private javax.swing.JTextField costPriceField;
    private javax.swing.JLabel costPriceLabel;
    private javax.swing.JButton exitBtn;
    private javax.swing.JCheckBox imageCB;
    private javax.swing.JTextField itemIdField;
    private javax.swing.JTextField itemNameField;
    private javax.swing.JTable itemTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JTextField lCostField;
    private javax.swing.JTextField largePriceField;
    private javax.swing.JRadioButton largeRB;
    private javax.swing.JTextField mCostField;
    private javax.swing.JButton manageCtgBtn;
    private javax.swing.JButton manageDealsBtn;
    private javax.swing.JTextField mediumPriceField;
    private javax.swing.JRadioButton mediumRB;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JTextField sCostField;
    private javax.swing.JTextField salePriceField;
    private javax.swing.JLabel salePriceLabel;
    private javax.swing.JTextField searchBar;
    private javax.swing.JLabel singlePriceDesc;
    private javax.swing.JCheckBox sizeVarCB;
    private javax.swing.JLabel sizeVarDescLabel;
    private javax.swing.JTextField smallPriceField;
    private javax.swing.JRadioButton smallRB;
    private javax.swing.JTextField xLargePriceField;
    private javax.swing.JRadioButton xLargeRB;
    private javax.swing.JTextField xlCostField;
    // End of variables declaration//GEN-END:variables
}

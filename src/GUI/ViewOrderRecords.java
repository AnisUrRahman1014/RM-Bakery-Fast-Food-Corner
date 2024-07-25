/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Classes.DBConnection;
import Classes.Item;
import Classes.OrderItem;
import Classes.Report;
import Classes.ReportItem;
import static GUI.mainMenu.orderItems;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
/**
 *
 * @author M AYAN LAPTOP
 */
public class ViewOrderRecords extends javax.swing.JFrame {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    
    ArrayList<String> categoryList=new ArrayList<>();
    ArrayList<Item> ctgItems=new ArrayList<>();
    
    String reportType;
    PrinterJob job=null;
    /**
     * Creates new form ViewOrderRecords
     */
    public ViewOrderRecords() {
        FlatDarkLaf.setup();
        initComponents();
        setTitle("Record View & Management");
        setLocationRelativeTo(null);
        setResizable(false);
        noneRB.setSelected(true);
        orderRecordContainer.add(new orderRecordView(this,orderRecordView.ADMIN));
        listCategories();
    }
    
    private void listCategories(){
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
                categoryCB.addItem(s);
        }
//        categoryCB.addItem("All");
        categoryCB.setSelectedIndex(-1);
    }
    
    private void listCtgItems(String category){
        ctgItems.removeAll(ctgItems);
        itemCB.removeAllItems();
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from menuItems where itemCtg='"+category+"'");
            rs=pst.executeQuery();
            while(rs.next()){
                String itemId=rs.getString("itemID");
                String itemCtg=rs.getString("itemCtg");
                String itemName=rs.getString("itemName");
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
                Item i=new Item(itemId,itemName,itemCtg,sizeFlag,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singelCostPrice,singleSalePrice);
                ctgItems.add(i);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        
        for(Item i:ctgItems){
                itemCB.addItem(i.getName());
        }
        itemCB.addItem("All");
        itemCB.setSelectedIndex(-1);
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
    
    private void generateReport(){
        Report report;
        SimpleDateFormat dateFormater=new SimpleDateFormat("dd-MM-YYYY");
        if(categoryCB.getSelectedItem().equals("All") && noneRB.isSelected()){
            report=new Report(Report.ALL_CATEGORY_REPORT,null,null,Report.NO_RANGE_REPORT,null,null);
            reportType="ALL_CATEGORY REPORT - NO_RANGE_REPORT";
        }else if(categoryCB.getSelectedItem().equals("All") && !noneRB.isSelected()){
            report=new Report(Report.ALL_CATEGORY_REPORT,null,null,Report.RANGE_REPORT,fromDateChooser.getDate(),toDateChooser.getDate());
            reportType="ALL_CATEGORY_REPORT - RANGE_REPORT".concat(" | From:"+dateFormater.format(fromDateChooser.getDate())+" To: "+dateFormater.format(toDateChooser.getDate()));
        }else if(itemCB.getSelectedItem().equals("All") && noneRB.isSelected()){
            String itemCtg=categoryCB.getSelectedItem().toString();
            report=new Report(Report.ALL_ITEM_REPORT,itemCtg,null,Report.NO_RANGE_REPORT,null,null);
            reportType="ALL_ITEM_REPORT - NO_RANGE_REPORT";
        }else if(itemCB.getSelectedItem().equals("All") && !noneRB.isSelected()){
            String itemCtg=categoryCB.getSelectedItem().toString();
            report=new Report(Report.ALL_ITEM_REPORT,itemCtg,null,Report.RANGE_REPORT,fromDateChooser.getDate(),toDateChooser.getDate());
            reportType="ALL_ITEM_REPORT - RANGE_REPORT".concat(" | From:"+dateFormater.format(fromDateChooser.getDate())+" To: "+dateFormater.format(toDateChooser.getDate()));
        }
        else{
            Item item=null;
            for(Item i:ctgItems){
                if(i.getName().equals(itemCB.getSelectedItem().toString())){
                    item=i;
                    break;
                }
            }
            if(!noneRB.isSelected()){
                report=new Report(Report.RANGE_REPORT,item.getId(),item.getCategory(),fromDateChooser.getDate(),toDateChooser.getDate());
                reportType=item.getCategory().toUpperCase().concat(" - "+item.getName().toUpperCase()).concat(" - RANGE_REPORT").concat(" | From:"+dateFormater.format(fromDateChooser.getDate())+" To: "+dateFormater.format(toDateChooser.getDate()));
            }else{
                report=new Report(Report.NO_RANGE_REPORT,item.getId(),item.getCategory(),null,null);
                reportType=item.getCategory().toUpperCase().concat(" - "+item.getName().toUpperCase()).concat(" - NO_RANGE_REPORT");
            }            
        }
        
        
        itemIDLabel.setText(report.getItemID());
        ItemNameLabel.setText(report.getItemName());
        itemCtgLabel.setText(report.getItemCtg());
        quantitySoldLabel.setText(String.valueOf(report.getSoldQuantity()));
        avgCostPerUnitLabel.setText(String.valueOf(report.getAvgUnitCost()));
        avgSalePerUnitLabel.setText(String.valueOf(report.getAvgUnitSale()));
        totalCostLabel.setText(String.valueOf(report.getTotalCost()));
        totalSaleLabel.setText(String.valueOf(report.getTotalSale()));
        profitLabel.setText(String.valueOf(report.getProfit()+" %"));
        profitValLabel.setText("| Rs."+String.valueOf(report.getProfitValue()));
        
        printPanel(report);
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

    private void printPanel(Report report){
        job = PrinterJob.getPrinterJob();
        java.sql.Date sqlDate= new java.sql.Date(new java.util.Date().getTime());
        job.setJobName(sqlDate+" _"+String.valueOf(new java.util.Date().getTime())); 
        PageFormat page=job.defaultPage();
        Paper paper = new Paper();
        paper.setImageableArea(0, 0, 200.0, report.getReportItems().size()*1000.0);
        page.setPaper(paper);
        job.setPrintable((Graphics pg, PageFormat pf, int pageNum) -> {  
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
            g2.drawString("REPORT", 0, y1);
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
            g2.drawString("Report Type: ", 0, y1);
            String[] lines = splitString(reportType, 25);
            int indexCount=0;
            for (String line : lines) {
                g2.drawString(line, 60, y1);
                indexCount++;
                y1 += yShift;                   
            }
//            g2.drawLine(0, y1, 240, y1);
//            y1 += headerRectHeight;
//            g2.drawString("Item Details", 0, y1); y1 += yShift;
//            g2.drawLine(0, y1, 240, y1);
//            y1 += headerRectHeight;            
//            g2.drawString("Name", 0, y1);
//            g2.drawString("Quantity", 100, y1);
//            g2.drawString("Profit %", 165, y1);
//            y1 += yShift;
//            g2.drawLine(0, y1, 240, y1);
//            y1 += headerRectHeight;
//            for (ReportItem i : report.getReportItems()) {
//                String itemString = i.toString();
//                int itemStringStart = 0;
//                int quantityStringStart = 115;
//
//                // Split the itemString into lines if it's too long
//                lines = splitString(itemString, 20);
//                indexCount=0;
//                for (String line : lines) {
//                    g2.drawString(line, itemStringStart, y1);
//                    indexCount++;
//                    if(indexCount<lines.length){
//                        y1 += yShift;
//                    }                    
//                }
//                g2.drawString(String.valueOf(i.getItemQuantity()), quantityStringStart, y1);
//                g2.drawString(String.valueOf(i.getProfit()), 165, y1);
//                y1 += yShift+10;
//            }
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawString("Report Summary",0,y1); y1+=yShift;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawString("Total Quantity Sold", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(report.getSoldQuantity()), 165, y1);
            y1 += yShift;
            
            g2.drawString("Avg Cost / Unit", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(report.getAvgUnitCost()), 165, y1);
            y1 += yShift;
            
            g2.drawString("Avg Sale / Unit", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(report.getAvgUnitSale()), 165, y1);
            y1 += yShift;
            
            g2.drawString("Total Cost", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(report.getTotalCost()), 165, y1);
            y1 += yShift;
            
            g2.drawString("Total Sale", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(report.getTotalSale()), 165, y1);
            y1 += yShift;
            
            g2.drawString("Profit Amount", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(report.getProfitValue()), 165, y1);
            y1 += yShift;
            
            g2.drawString("Profit %", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(String.valueOf(report.getProfit()), 165, y1);
            y1 += yShift;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
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
                    
                    job.print();
                }
        } catch (PrinterException ex) {
            ex.printStackTrace();
            Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
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

        dateFilterGroup = new javax.swing.ButtonGroup();
        rootPanel = new javax.swing.JPanel();
        orderRecordContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        categoryCB = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        itemCB = new javax.swing.JComboBox<>();
        reportResultPanel = new javax.swing.JPanel();
        itemIDLabel = new javax.swing.JLabel();
        ItemNameLabel = new javax.swing.JLabel();
        itemCtgLabel = new javax.swing.JLabel();
        quantitySoldLabel = new javax.swing.JLabel();
        avgCostPerUnitLabel = new javax.swing.JLabel();
        avgSalePerUnitLabel = new javax.swing.JLabel();
        totalCostLabel = new javax.swing.JLabel();
        totalSaleLabel = new javax.swing.JLabel();
        profitLabel = new javax.swing.JLabel();
        profitPerLabel1 = new javax.swing.JLabel();
        itemIDLabel1 = new javax.swing.JLabel();
        ItemNameLabel1 = new javax.swing.JLabel();
        itemCtgLabel1 = new javax.swing.JLabel();
        quantitySoldLabel1 = new javax.swing.JLabel();
        avgCostPerUnitLabel1 = new javax.swing.JLabel();
        avgSalePerUnitLabel1 = new javax.swing.JLabel();
        totalCostLabel1 = new javax.swing.JLabel();
        totalSaleLabel1 = new javax.swing.JLabel();
        profitValLabel = new javax.swing.JLabel();
        generateReportBtn = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fromDateChooser = new com.toedter.calendar.JDateChooser();
        toDateChooser = new com.toedter.calendar.JDateChooser();
        jSeparator3 = new javax.swing.JSeparator();
        todayRB = new javax.swing.JRadioButton();
        lastWeekRB = new javax.swing.JRadioButton();
        lastMonthRB = new javax.swing.JRadioButton();
        noneRB = new javax.swing.JRadioButton();
        rangeRB = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        orderRecordContainer.setBackground(new java.awt.Color(61, 92, 116));
        orderRecordContainer.setPreferredSize(new java.awt.Dimension(815, 610));
        orderRecordContainer.setLayout(new javax.swing.BoxLayout(orderRecordContainer, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(61, 91, 116));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setText("Report Generation");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Choose category :");

        categoryCB.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        categoryCB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                categoryCBMouseClicked(evt);
            }
        });
        categoryCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryCBActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Choose item :");

        itemCB.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCBActionPerformed(evt);
            }
        });

        itemIDLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        ItemNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        itemCtgLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        quantitySoldLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        avgCostPerUnitLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        avgSalePerUnitLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        totalCostLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        totalSaleLabel.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        profitLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N

        profitPerLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        profitPerLabel1.setText("Profit :");

        itemIDLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemIDLabel1.setText("Item ID:");

        ItemNameLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        ItemNameLabel1.setText("Item Name:");

        itemCtgLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        itemCtgLabel1.setText("Category:");

        quantitySoldLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        quantitySoldLabel1.setText("Total Quantity Sold:");

        avgCostPerUnitLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        avgCostPerUnitLabel1.setText("Avg Cost / Unit :");

        avgSalePerUnitLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        avgSalePerUnitLabel1.setText("Avg Sale / Unit :");

        totalCostLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        totalCostLabel1.setText("Total Cost :");

        totalSaleLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        totalSaleLabel1.setText("Total Sale :");

        profitValLabel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N

        javax.swing.GroupLayout reportResultPanelLayout = new javax.swing.GroupLayout(reportResultPanel);
        reportResultPanel.setLayout(reportResultPanelLayout);
        reportResultPanelLayout.setHorizontalGroup(
            reportResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportResultPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(reportResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalSaleLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalCostLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(avgSalePerUnitLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(avgCostPerUnitLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemIDLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemNameLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemCtgLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(quantitySoldLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(profitPerLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(reportResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalSaleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(totalCostLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(avgSalePerUnitLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(avgCostPerUnitLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(quantitySoldLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ItemNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemCtgLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(reportResultPanelLayout.createSequentialGroup()
                        .addComponent(profitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profitValLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(23, 23, 23))
        );
        reportResultPanelLayout.setVerticalGroup(
            reportResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportResultPanelLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(reportResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportResultPanelLayout.createSequentialGroup()
                        .addComponent(itemIDLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ItemNameLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemCtgLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantitySoldLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(avgCostPerUnitLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(avgSalePerUnitLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalCostLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalSaleLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profitPerLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(reportResultPanelLayout.createSequentialGroup()
                        .addComponent(itemIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ItemNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemCtgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(quantitySoldLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(avgCostPerUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(avgSalePerUnitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalCostLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalSaleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(reportResultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(profitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(profitValLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );

        generateReportBtn.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        generateReportBtn.setText("Generate");
        generateReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateReportBtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("From : ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("To :");

        fromDateChooser.setForeground(new java.awt.Color(255, 255, 255));
        fromDateChooser.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        toDateChooser.setForeground(new java.awt.Color(255, 255, 255));
        toDateChooser.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        dateFilterGroup.add(todayRB);
        todayRB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        todayRB.setText("Today");
        todayRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todayRBActionPerformed(evt);
            }
        });

        dateFilterGroup.add(lastWeekRB);
        lastWeekRB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lastWeekRB.setText("Last Week");
        lastWeekRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastWeekRBActionPerformed(evt);
            }
        });

        dateFilterGroup.add(lastMonthRB);
        lastMonthRB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lastMonthRB.setText("Last Month");
        lastMonthRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastMonthRBActionPerformed(evt);
            }
        });

        dateFilterGroup.add(noneRB);
        noneRB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        noneRB.setText("None");
        noneRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noneRBActionPerformed(evt);
            }
        });

        dateFilterGroup.add(rangeRB);
        rangeRB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rangeRB.setText("Range");
        rangeRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rangeRBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(categoryCB, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemCB, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(49, 49, 49)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fromDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                    .addComponent(toDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(todayRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lastWeekRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lastMonthRB))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(noneRB)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rangeRB)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(generateReportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(reportResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categoryCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(itemCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generateReportBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(todayRB)
                            .addComponent(lastWeekRB)
                            .addComponent(lastMonthRB))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(noneRB)
                            .addComponent(rangeRB))))
                .addGap(18, 18, 18)
                .addComponent(reportResultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderRecordContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orderRecordContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(rootPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generateReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateReportBtnActionPerformed
        // TODO add your handling code here:
        generateReport();
    }//GEN-LAST:event_generateReportBtnActionPerformed

    private void categoryCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryCBActionPerformed
        // TODO add your handling code here:
        Object selected=categoryCB.getSelectedItem();
        if(selected!=null){
            String s=selected.toString();
            if(s.equals("All")){
                itemCB.setSelectedIndex(-1);
                itemCB.setEnabled(false);
            }else{
                itemCB.setEnabled(true);
                listCtgItems(s);
            }            
        }    
    }//GEN-LAST:event_categoryCBActionPerformed

    private void categoryCBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoryCBMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryCBMouseClicked

    private void itemCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCBActionPerformed

    private void lastMonthRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastMonthRBActionPerformed
        // TODO add your handling code here:
        toDateChooser.setDate(new java.util.Date());
        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set the calendar time to the "to" date
        calendar.setTime(toDateChooser.getDate());

        // Subtract one month from the "to" date
        calendar.add(Calendar.MONTH, -1);

        // Set the "from" date to the same day of the previous month
        fromDateChooser.setDate(calendar.getTime());
    }//GEN-LAST:event_lastMonthRBActionPerformed

    private void todayRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todayRBActionPerformed
        // TODO add your handling code here:
        fromDateChooser.setDate(new java.util.Date());
        toDateChooser.setDate(new java.util.Date());
    }//GEN-LAST:event_todayRBActionPerformed

    private void lastWeekRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastWeekRBActionPerformed
        // TODO add your handling code here:
        toDateChooser.setDate(new java.util.Date());
        // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();

        // Set the calendar time to the "to" date
        calendar.setTime(toDateChooser.getDate());

        // Subtract 7 days from the "to" date
        calendar.add(Calendar.DAY_OF_MONTH, -7);

        // Set the "from" date to 7 days before the "to" date
        fromDateChooser.setDate(calendar.getTime());
    }//GEN-LAST:event_lastWeekRBActionPerformed

    private void noneRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noneRBActionPerformed
        // TODO add your handling code here:
        fromDateChooser.setDate(null);
        toDateChooser.setDate(null);
    }//GEN-LAST:event_noneRBActionPerformed

    private void rangeRBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rangeRBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rangeRBActionPerformed

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
            java.util.logging.Logger.getLogger(ViewOrderRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewOrderRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewOrderRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewOrderRecords.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewOrderRecords().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ItemNameLabel;
    private javax.swing.JLabel ItemNameLabel1;
    private javax.swing.JLabel avgCostPerUnitLabel;
    private javax.swing.JLabel avgCostPerUnitLabel1;
    private javax.swing.JLabel avgSalePerUnitLabel;
    private javax.swing.JLabel avgSalePerUnitLabel1;
    private javax.swing.JComboBox<String> categoryCB;
    private javax.swing.ButtonGroup dateFilterGroup;
    private com.toedter.calendar.JDateChooser fromDateChooser;
    private javax.swing.JButton generateReportBtn;
    private javax.swing.JComboBox<String> itemCB;
    private javax.swing.JLabel itemCtgLabel;
    private javax.swing.JLabel itemCtgLabel1;
    private javax.swing.JLabel itemIDLabel;
    private javax.swing.JLabel itemIDLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JRadioButton lastMonthRB;
    private javax.swing.JRadioButton lastWeekRB;
    private javax.swing.JRadioButton noneRB;
    private javax.swing.JPanel orderRecordContainer;
    private javax.swing.JLabel profitLabel;
    private javax.swing.JLabel profitPerLabel1;
    private javax.swing.JLabel profitValLabel;
    private javax.swing.JLabel quantitySoldLabel;
    private javax.swing.JLabel quantitySoldLabel1;
    private javax.swing.JRadioButton rangeRB;
    private javax.swing.JPanel reportResultPanel;
    private javax.swing.JPanel rootPanel;
    private com.toedter.calendar.JDateChooser toDateChooser;
    private javax.swing.JRadioButton todayRB;
    private javax.swing.JLabel totalCostLabel;
    private javax.swing.JLabel totalCostLabel1;
    private javax.swing.JLabel totalSaleLabel;
    private javax.swing.JLabel totalSaleLabel1;
    // End of variables declaration//GEN-END:variables
}

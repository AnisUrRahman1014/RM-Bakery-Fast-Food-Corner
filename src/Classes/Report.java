/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import GUI.ReportItemsTable;
import java.util.Date;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Report {
    private Connection con=null;
    private PreparedStatement pst=null;
    private ResultSet rs=null;
    
    // REPORT TYPES
    public static final int ALL_CATEGORY_REPORT=0;
    public static final int ALL_ITEM_REPORT=1;
    public static final int SINGLE_ITEM_REPORT=2;
    
    // RANGE TYPES
    public static final int NO_RANGE_REPORT=0;
    public static final int RANGE_REPORT=1;
    
    private ArrayList<ReportItem> reportItems=new ArrayList<>();
    ArrayList<String> categoryList=new ArrayList<>();
    
    private String itemID;
    private String itemName;
    private String itemCtg;
    private int soldQuantity =0;
    private int avgUnitCost=0;
    private int avgUnitSale=0;    
    private int totalSale=0;
    private int totalCost=0;
    private double profit=0;
    private int profitValue=0;
    private Date fromDate;
    private Date toDate;
    
    public Report(int orderType, String itemCtg, String itemID,int dateFilterType, Date fromDate,Date toDate){
        listCategories();
        switch(orderType){
            case ALL_CATEGORY_REPORT -> {
                this.itemID="All";
                this.itemCtg="All";
                itemName="N/A";
                avgUnitCost=0;
                avgUnitSale=0;
//                For each category, list all items... for each item, access orderItems and generate ReportItems
                try{
                    con=DBConnection.connectDB();
                    String queryy;
                    queryy="SELECT itemID, itemCtg, itemSize, SUM(quantity) as totalQuantitySold FROM orderItems GROUP BY itemID, itemCtg, itemSize";
                    pst=con.prepareStatement(queryy);
                    if(dateFilterType == RANGE_REPORT){
                        queryy="SELECT oi.itemCtg, oi.itemSize, oi.itemID, SUM(oi.quantity) as totalQuantitySold FROM orderItems oi INNER JOIN orderRecords o ON oi.orderID = o.orderID WHERE o.orderDate BETWEEN ? AND ? GROUP BY oi.itemCtg, oi.itemSize, oi.itemID";
                        pst=con.prepareStatement(queryy);
                        java.sql.Date fDate=new java.sql.Date(fromDate.getTime());
                        java.sql.Date tDate=new java.sql.Date(toDate.getTime());
                        pst.setDate(1,fDate);
                        pst.setDate(2,tDate);
                    }
                    rs=pst.executeQuery();
                    while(rs.next()){
                        // SELECTING ITEMS FOR THE CURRENT CATEGORY                        
                        pst=con.prepareStatement("select * from menuItems where itemCtg='"+rs.getString("itemCtg")+"' and itemID='"+rs.getString("itemID")+"'");
                        ResultSet rs2=pst.executeQuery();
                        while(rs2.next()){
                            String id=rs2.getString("itemID");
                            String name=rs2.getString("itemname");
                            String category=rs2.getString("itemCtg");
                            boolean sizeVar=rs2.getBoolean("SizeVarFlag");
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
                            Item i=new Item(id,name,category,sizeVar,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singleCostPrice,singleSalePrice);
                            String itemSize=rs.getString("itemSize");
                            int costPrice=0;
                            int salePrice=0;
                            switch(itemSize){
                                case "---" ->{
                                    costPrice=i.getSingleCostPrice();
                                    salePrice=i.getSingleSalePrice();
                                }
                                case "Small" -> {
                                    costPrice=i.getSmallCost();
                                    salePrice=i.getSmallSalePrice();
                                }
                                case "Medium" -> {
                                    costPrice=i.getMediumCost();
                                    salePrice=i.getMediumSalePrice();
                                }
                                case "Large" -> {
                                    costPrice=i.getLargeCost();
                                    salePrice=i.getLargeSalePrice();
                                }
                                case "Extra Large" -> {
                                    costPrice=i.getXLargeCost();
                                    salePrice=i.getXLargeSalePrice();
                                }
                            }
                            int itemTotalCost=costPrice*rs.getInt("totalQuantitySold");
                            int itemTotalSale=salePrice*rs.getInt("totalQuantitySold");
                            double itemProfit=profitCal(itemTotalCost,itemTotalSale);
                            System.out.println("THIS");
                            reportItems.add(new ReportItem(i, rs.getString("itemSize"), rs.getInt("totalQuantitySold"), itemTotalCost, itemTotalSale, itemProfit));
                        }                        
                    }
                    
//                    for(String ctg:categoryList){
//                        ArrayList<Item> ctgItems=new ArrayList<>();
//                        // SELECTING ITEMS FOR THE CURRENT CATEGORY                        
//                        pst=con.prepareStatement("select * from menuItems where itemCtg='"+ctg+"'");
//                        rs=pst.executeQuery();
//                        while(rs.next()){
//                            String id=rs.getString("itemID");
//                            String name=rs.getString("itemname");
//                            String category=rs.getString("itemCtg");
//                            boolean sizeVar=rs.getBoolean("SizeVarFlag");
//                            int singleCostPrice=rs.getInt("CostPrice");
//                            int singleSalePrice=rs.getInt("salePrice");
//                            int smallCostPrice=rs.getInt("smallCost");
//                            int smallSalePrice=rs.getInt("smallPrice");
//                            int mediumCostPrice=rs.getInt("mediumCost");
//                            int mediumSalePrice=rs.getInt("mediumPrice");
//                            int largeCostPrice=rs.getInt("largeCost");
//                            int largeSalePrice=rs.getInt("largePrice");
//                            int xLargeCostPrice=rs.getInt("xLargeCost");
//                            int xLargeSalePrice=rs.getInt("xLargePrice");
//                            Item i=new Item(id,name,category,sizeVar,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singleCostPrice,singleSalePrice);
//                            ctgItems.add(i);
//                        }
//                        // SELECTING RECORD DETAILS FOR EACH ITEM                        
//                        for(Item i:ctgItems){  
//                            ArrayList<OrderItem> orderItems=new ArrayList<>();
//                            String query="select * from orderItems where itemID='"+i.getId()+"' and itemCtg='"+i.getCategory()+"'";                            
//                            pst=con.prepareStatement(query);
//                            if(dateFilterType == RANGE_REPORT){
//                                query="select * from orderRecords inner join orderItems on orderRecords.orderID=orderItems.orderId where itemID='"+i.getId()+"' and itemCtg='"+i.getCategory()+"' and orderDate between ? and ?";
//                                pst=con.prepareStatement(query);
//                                java.sql.Date fDate=new java.sql.Date(fromDate.getTime());
//                                java.sql.Date tDate=new java.sql.Date(toDate.getTime());
//                                pst.setDate(1,fDate);
//                                pst.setDate(2,tDate);
//                            }                            
//                            rs=pst.executeQuery();
//                            while(rs.next()){
//                                int quantity=rs.getInt("quantity");
//                                String size=rs.getString("itemSize");
//                                orderItems.add(new OrderItem(i, quantity, size));
//                            }
//                            if(orderItems.isEmpty()){
//                                continue;
//                            }
//                            // SEPARATING ITEM BASED ON SIZES:
//                            ArrayList<OrderItem> small=new ArrayList<>();
//                            ArrayList<OrderItem> medium=new ArrayList<>();
//                            ArrayList<OrderItem> large=new ArrayList<>();
//                            ArrayList<OrderItem> xLarge=new ArrayList<>();
//                            int itemSoldQuantity=0;
//                            int itemTotalCost=0;
//                            int itemTotalSale=0;
//                            double itemProfit=0;
//                            if(i.isSizeFlag()){ 
//                                ArrayList<OrderItem> temp=orderItems;  
//                                for (Iterator<OrderItem> iterator = temp.iterator(); iterator.hasNext();) {
//                                    OrderItem x = iterator.next();
//                                    String size = x.getSize();
//
//                                    switch (size) {
//                                        case "Small" -> {
//                                            small.add(x);
//                                            iterator.remove(); // Use iterator.remove() instead of orderItems.remove(x)
//                                        }
//                                        case "Medium" -> {
//                                            medium.add(x);
//                                            iterator.remove();
//                                        }
//                                        case "Large" -> {
//                                            large.add(x);
//                                            iterator.remove();
//                                        }
//                                        case "Extra Large" -> {
//                                            xLarge.add(x);
//                                            iterator.remove();
//                                        }
//                                    }
//                                    if(!small.isEmpty()){
//                                        itemSoldQuantity=0;
//                                        for(OrderItem s:small){
//                                            itemSoldQuantity+=s.getQuantity();
//                                        }
//                                        itemTotalCost=i.getSmallCost()*itemSoldQuantity;
//                                        itemTotalSale=i.getSmallSalePrice()*itemSoldQuantity;
//                                        itemProfit=profitCal(itemTotalCost,itemTotalSale);
//                                        reportItems.add(new ReportItem(i, small.get(0).getSize(), itemSoldQuantity, itemTotalCost, itemTotalSale, itemProfit));
//                                    }
//                                    
//                                    if(!medium.isEmpty()){
//                                        itemSoldQuantity=0;
//                                        for(OrderItem s:medium){
//                                            itemSoldQuantity+=s.getQuantity();
//                                        }
//                                        itemTotalCost=i.getSmallCost()*itemSoldQuantity;
//                                        itemTotalSale=i.getSmallSalePrice()*itemSoldQuantity;
//                                        itemProfit=profitCal(itemTotalCost,itemTotalSale);
//                                        reportItems.add(new ReportItem(i, medium.get(0).getSize(), itemSoldQuantity, itemTotalCost, itemTotalSale, itemProfit));
//                                    }
//                                    
//                                    if(!large.isEmpty()){
//                                        itemSoldQuantity=0;
//                                        for(OrderItem s:large){
//                                            itemSoldQuantity+=s.getQuantity();
//                                        }
//                                        itemTotalCost=i.getSmallCost()*itemSoldQuantity;
//                                        itemTotalSale=i.getSmallSalePrice()*itemSoldQuantity;
//                                        itemProfit=profitCal(itemTotalCost,itemTotalSale);
//                                        reportItems.add(new ReportItem(i, large.get(0).getSize(), itemSoldQuantity, itemTotalCost, itemTotalSale, itemProfit));
//                                    }
//                                    
//                                    if(!xLarge.isEmpty()){
//                                        itemSoldQuantity=0;
//                                        for(OrderItem s:xLarge){
//                                            itemSoldQuantity+=s.getQuantity();
//                                        }
//                                        itemTotalCost=i.getSmallCost()*itemSoldQuantity;
//                                        itemTotalSale=i.getSmallSalePrice()*itemSoldQuantity;
//                                        itemProfit=profitCal(itemTotalCost,itemTotalSale);
//                                        reportItems.add(new ReportItem(i, xLarge.get(0).getSize(), itemSoldQuantity, itemTotalCost, itemTotalSale, itemProfit));
//                                    }                                    
//                                }
//                            }else{
//                            // CREATING REPORT ITEMS  
//                                System.out.println(i.getName());
//                                itemSoldQuantity=0;
//                                for(OrderItem x:orderItems){
//                                    itemSoldQuantity+=x.getQuantity();
//                                }
//                                itemTotalCost=i.getSingleCostPrice()*itemSoldQuantity;
//                                itemTotalSale=i.getSingleSalePrice()*itemSoldQuantity;
//                                itemProfit=profitCal(itemTotalCost,itemTotalSale);
//                                System.out.println("THIS");
//                                reportItems.add(new ReportItem(i, orderItems.get(0).getSize(), itemSoldQuantity, itemTotalCost, itemTotalSale, itemProfit));
//                            }
//                        }
//                    }
                    for(ReportItem r:reportItems){
                        totalCost+=r.getTotalCost();
                        totalSale+=r.getTotalSale();
                        soldQuantity+=r.getItemQuantity();
                        profit=profitCal();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    closeConnection();
                }
                break;
            }
            
            case ALL_ITEM_REPORT -> {
                this.itemID="All";
                this.itemCtg=itemCtg;
                itemName="N/A";
                avgUnitCost=0;
                avgUnitSale=0;
//                For each category, list all items... for each item, access orderItems and generate ReportItems
                try{
                    con=DBConnection.connectDB();
                        String queryy;
                    queryy="SELECT itemID, itemSize, SUM(quantity) as totalQuantitySold FROM orderItems where itemCtg='"+itemCtg+"' GROUP BY itemID, itemSize";
                    pst=con.prepareStatement(queryy);
                    if(dateFilterType == RANGE_REPORT){
                        queryy="SELECT oi.itemSize, oi.itemID, SUM(oi.quantity) as totalQuantitySold FROM orderItems oi INNER JOIN orderRecords o ON oi.orderID = o.orderID WHERE oi.itemCtg='"+itemCtg+"' and o.orderDate BETWEEN ? AND ? GROUP BY oi.itemSize, oi.itemID";
                        pst=con.prepareStatement(queryy);
                        java.sql.Date fDate=new java.sql.Date(fromDate.getTime());
                        java.sql.Date tDate=new java.sql.Date(toDate.getTime());
                        pst.setDate(1,fDate);
                        pst.setDate(2,tDate);
                    }
                    rs=pst.executeQuery();
                    while(rs.next()){
                        // SELECTING ITEMS FOR THE CURRENT CATEGORY                        
                        pst=con.prepareStatement("select * from menuItems where itemCtg='"+itemCtg+"' and itemID='"+rs.getString("itemID")+"'");
                        ResultSet rs2=pst.executeQuery();
                        while(rs2.next()){
                            String id=rs2.getString("itemID");
                            String name=rs2.getString("itemname");
                            String category=rs2.getString("itemCtg");
                            boolean sizeVar=rs2.getBoolean("SizeVarFlag");
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
                            Item i=new Item(id,name,category,sizeVar,smallCostPrice,smallSalePrice,mediumCostPrice,mediumSalePrice,largeCostPrice,largeSalePrice,xLargeCostPrice,xLargeSalePrice,singleCostPrice,singleSalePrice);
                            String itemSize=rs.getString("itemSize");
                            int costPrice=0;
                            int salePrice=0;
                            switch(itemSize){
                                case "---" ->{
                                    costPrice=i.getSingleCostPrice();
                                    salePrice=i.getSingleSalePrice();
                                }
                                case "Small" -> {
                                    costPrice=i.getSmallCost();
                                    salePrice=i.getSmallSalePrice();
                                }
                                case "Medium" -> {
                                    costPrice=i.getMediumCost();
                                    salePrice=i.getMediumSalePrice();
                                }
                                case "Large" -> {
                                    costPrice=i.getLargeCost();
                                    salePrice=i.getLargeSalePrice();
                                }
                                case "Extra Large" -> {
                                    costPrice=i.getXLargeCost();
                                    salePrice=i.getXLargeSalePrice();
                                }
                            }
                            int itemTotalCost=costPrice*rs.getInt("totalQuantitySold");
                            int itemTotalSale=salePrice*rs.getInt("totalQuantitySold");
                            double itemProfit=profitCal(itemTotalCost,itemTotalSale);
                            System.out.println("THIS");
                            reportItems.add(new ReportItem(i, rs.getString("itemSize"), rs.getInt("totalQuantitySold"), itemTotalCost, itemTotalSale, itemProfit));
                        }                        
                    }
                    for(ReportItem r:reportItems){
                        totalCost+=r.getTotalCost();
                        totalSale+=r.getTotalSale();
                        soldQuantity+=r.getItemQuantity();
                        profit=profitCal();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    closeConnection();
                }
                break;
            }
        }
        
        JOptionPane.showOptionDialog(null, new ReportItemsTable(null,reportItems), "Report Table", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
    }
    
    public final void listCategories(){
        categoryList.removeAll(categoryList);
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

    public Report(int dateFilterType,String itemID, String itemCtg, Date fromDate, Date toDate){
        java.sql.Date fDate=null;
        java.sql.Date tDate=null;
        if(fromDate !=null && toDate!=null){
            fDate=new java.sql.Date(fromDate.getTime());
            tDate=new java.sql.Date(toDate.getTime());
        }        
        this.itemID = itemID;
        this.itemCtg = itemCtg;
        this.fromDate = fromDate;
        this.toDate = toDate;
        int quantity=0;
        int totalSalePrice=0;
        int avgSalePrice=0;
        
        try{            
            con=DBConnection.connectDB();
            // GETTING TOTAL SOLD QUANTITY AND TOTAL SALE PRICE FROM ORDER ITEMS TABLE
            if(dateFilterType == NO_RANGE_REPORT){
                System.out.println("HELLO");
                pst = con.prepareStatement("SELECT SUM(quantity) as totalQuantity, SUM(totalPrice) as totalSalePrice FROM orderRecords " +
                           "INNER JOIN orderItems ON orderRecords.orderID = orderItems.orderID " +
                           "WHERE itemId = ? AND itemCtg = ?");
                pst.setString(1, itemID);
                pst.setString(2, itemCtg);
            }else{
                System.out.println("HELLOOEOEOE");
                pst = con.prepareStatement("SELECT SUM(quantity) as totalQuantity, SUM(totalPrice) as totalSalePrice FROM orderRecords " +
                           "INNER JOIN orderItems ON orderRecords.orderID = orderItems.orderID " +
                           "WHERE itemId = ? AND itemCtg = ? AND orderRecords.orderDate BETWEEN ? AND ?");
                pst.setString(1, itemID);
                pst.setString(2, itemCtg);
                pst.setDate(3, fDate);
                pst.setDate(4, tDate);
            }           
            
            rs=pst.executeQuery();
            if(rs.next()){
                quantity=rs.getInt("totalQuantity");
                totalSalePrice=rs.getInt("totalSalePrice");
            }
            if(dateFilterType == NO_RANGE_REPORT){
                pst = con.prepareStatement("SELECT AVG(itemUnitPrice) as avgSalePrice from orderRecords " +
                       "INNER JOIN orderItems ON orderRecords.orderID = orderItems.orderID " +
                       "WHERE itemId = ? AND itemCtg = ?");

                pst.setString(1, itemID);
                pst.setString(2, itemCtg); 
            }else{
                pst = con.prepareStatement("SELECT AVG(itemUnitPrice) as avgSalePrice from orderRecords " +
                           "INNER JOIN orderItems ON orderRecords.orderID = orderItems.orderID " +
                           "WHERE itemId = ? AND itemCtg = ? AND orderRecords.orderDate BETWEEN ? AND ?");

                pst.setString(1, itemID);
                pst.setString(2, itemCtg);
                pst.setDate(3, fDate);
                pst.setDate(4, tDate); 
            }           
            rs=pst.executeQuery();
            if(rs.next()){
                avgSalePrice=rs.getInt("avgSalePrice");
            }

            // GETTING ITEM COST
            pst=con.prepareStatement("select * from menuItems where itemId='"+itemID+"' and itemCtg='"+itemCtg+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                boolean sizeFlag=rs.getBoolean("sizeVarFlag");
                if(!sizeFlag){
                    avgUnitCost=rs.getInt("costPrice");
                }else{
                    int smallCost=rs.getInt("smallCost");
                    int mediumCost=rs.getInt("mediumCost");
                    int largeCost=rs.getInt("largeCost");
                    int xLargeCost=rs.getInt("xLargeCost");
                    avgUnitCost=(smallCost+mediumCost+largeCost+xLargeCost)/4;
                }                 
                itemName=rs.getString("itemName");
            }
                       
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(con!=null){
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        soldQuantity=quantity;
        totalSale=totalSalePrice;
        totalCost=(avgUnitCost*soldQuantity);
        avgUnitSale=avgSalePrice;
        profit=profitCal();
    }
    
    private double profitCal() {
        profitValue=(totalSale - totalCost);
        double c = (double) profitValue;
        double profitt = (double) (c / totalSale) * 100;

        // Create a DecimalFormat object with 3 decimal places
        DecimalFormat df = new DecimalFormat("#.###");

        // Round the profit value to 3 decimal places
        return Double.parseDouble(df.format(profitt));
    }
    
    private double profitCal(int cost,int sale) {
        int diff=(sale - cost);
        double c = (double) diff;
        double profitt = (double) (c / sale) * 100;

        // Create a DecimalFormat object with 3 decimal places
        DecimalFormat df = new DecimalFormat("#.###");

        // Round the profit value to 3 decimal places
        return Double.parseDouble(df.format(profitt));
    }

    public double getProfit() {
        return profit;
    }

    public int getProfitValue() {
        return profitValue;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCtg() {
        return itemCtg;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public int getAvgUnitCost() {
        return avgUnitCost;
    }

    public int getAvgUnitSale() {
        return avgUnitSale;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public ArrayList<ReportItem> getReportItems() {
        return reportItems;
    }
    
    
    
}

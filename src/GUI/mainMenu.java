package GUI;

import Classes.DBConnection;
import Classes.Item;
import Classes.OrderItem;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class mainMenu extends javax.swing.JFrame {
    Connection con=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    ArrayList<Classes.Category> categoryList=new ArrayList<>();
    public static ArrayList<OrderItem> orderItems=new ArrayList<>();
    PrinterJob job=null;
    /**
     * Creates new form mainMenu
     */
    String userType;
    private int pcID;
    public mainMenu(String userType) { 
        getPCId();
        setTitle("Main Menu");
        FlatDarkLaf.setup();
        initComponents();
        setLocationRelativeTo(null);
        listCategories(1);
        this.userType=userType;   
        setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                }else
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                } else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
        categoryContainer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                } else
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                } else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
        orderTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                } else
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                } else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
        orderPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                }else 
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                }else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
    }
    public mainMenu(String userType, int z) { 
        getPCId();
        setTitle("Main Menu");
        FlatDarkLaf.setup();
        initComponents();
        setLocationRelativeTo(null);
        listCategories(z);
        this.userType=userType;   
        setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                }else
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                } else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
        categoryContainer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                } else
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                } else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
        orderTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                } else
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                } else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
        orderPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proceedTransaction();
                }else 
                if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                    resetReceipt();
                }else
                if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                    JOptionPane.showOptionDialog(categoryContainer,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                }
            }
        });
    }
    
    
    private void getPCId(){
        String filePath=System.getProperty("user.dir").concat("\\ipconfig.txt");
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;                
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("pcID:")) {
                        pcID = Integer.valueOf(line.substring(line.indexOf(":") + 2, line.indexOf(";")).trim());
                        System.out.println("pcID: " + pcID);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    
    private void listCategories(int z){
        categoryList.removeAll(categoryList);
        categoryContainer.removeAll();
        try{
            con=DBConnection.connectDB();
            if(z==1){
                pst=con.prepareStatement("select * from categories");
            }else{
                pst=con.prepareStatement("select * from categories where categoryName='Dairy'");
            }                
            rs=pst.executeQuery();
            while(rs.next()){
                if(!rs.getString("categoryName").equals("All")){
                    Classes.Category ctg=new Classes.Category(rs.getString("categoryName"), rs.getString("image"));
                    categoryList.add(ctg);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
        createCategoryButtons();
    }
    
    private void createCategoryButtons(){
        for(Classes.Category ctg:categoryList){
            JButton button=new JButton(ctg.getCategoryName());
            button.setHorizontalAlignment(JButton.TRAILING);
            button.setVerticalAlignment(JButton.BOTTOM);
            button.setFont(new java.awt.Font("Segoe UI", 1, 20));
            button.setPreferredSize(new Dimension(200,200));
            button.setMinimumSize(new Dimension(200,200));

            ImageIcon icon=new ImageIcon(System.getProperty("user.dir")+"\\Pictures\\"+ctg.getImage());
            Image image = icon.getImage();
            Image resizedImage = image.getScaledInstance(160, 100, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon=new ImageIcon(resizedImage);

            JLabel iconLabel = new JLabel(resizedIcon);

            button.add(iconLabel);

            button.addActionListener((ActionEvent e) -> {
                itemButtonActionPerformed(button);
            });
            
            button.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                        proceedTransaction();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
                        resetReceipt();
                    }else
                    if (e.getKeyCode() == KeyEvent.VK_A && e.isControlDown()) {
                        JOptionPane.showOptionDialog(null,new orderRecordView(this, orderRecordView.EMPLOYEE), "Orders", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,null,null);
                    }
                }
            });
            
            button.setFocusable(true);
            categoryContainer.add(button);
        }
    }
    
    private void itemButtonActionPerformed(JButton btn){
        Classes.Category selectedCtg=null;
        String ctgName=btn.getText();
        if(ctgName.isBlank()){
            JLabel label=(JLabel) btn.getComponents()[1];
            ctgName=label.getText();
        }
        try{
            con=DBConnection.connectDB();
            pst=con.prepareStatement("select * from categories where categoryName='"+ctgName+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                selectedCtg=new Classes.Category(rs.getString("categoryName"),rs.getString("image"));
            }else{
                JOptionPane.showMessageDialog(this, "Item not found", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showOptionDialog(this,new Category(this, selectedCtg.getCategoryName()),selectedCtg.getCategoryName(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            closeConnection();
        }
    }
    
    public void updateOrderReceipt(){
        DefaultTableModel model=(DefaultTableModel) orderTable.getModel();
        model.setRowCount(0);
        int count=1;
        int totalPrice=0;
        for(OrderItem item: orderItems){
            String itemName=item.toString();           
            int quantity=item.getQuantity();
            int price=item.getTotalPrice();
            totalPrice+=price;
            Object row[]={count,itemName,quantity,price};
            model.addRow(row);
            count++;
        }
        totalPriceField.setText(String.valueOf(totalPrice));
        cashPaidField.setText(String.valueOf(totalPrice));
        setCashBackField();
    }
    
    private void removeItem(){
        DefaultTableModel model=(DefaultTableModel) orderTable.getModel();
        String selectedName=model.getValueAt(orderTable.getSelectedRow(), 1).toString();
        int decision=JOptionPane.showConfirmDialog(this, "Are you sure you want to remove \""+selectedName+"\" from the order?","Confirmation Message",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(decision == 0){
            for(OrderItem i:orderItems){
                if(i.getName().equals(selectedName)){
                    orderItems.remove(i);
                }
            }
            updateOrderReceipt();
        }        
    }
    
    private boolean validateCash(){
        if(cashPaidField.getText()==null || "".equals(cashPaidField.getText())){
            return false;
        }
        int cashPaid=Integer.valueOf(cashPaidField.getText());
        int totalPrice=Integer.valueOf(totalPriceField.getText());
        return cashPaid >= totalPrice;
    }
    
    public void proceedTransaction(){
        if(validateCash()){
            String contactNumber="---";
            String choosenType;
            Object options[]={"Take-Away","Delivery"};
            Object output=JOptionPane.showInputDialog(this, "Please choose the order type: ", "Order type", JOptionPane.QUESTION_MESSAGE, null, options, "Take-Away");
            if(output!=null){
                boolean abort=false;
                choosenType=output.toString();
                if(choosenType.equals("Delivery")){
    //                contactNumber=JOptionPane.showInputDialog(this,"Please enter customer phone number: ", "Delivery Details", JOptionPane.INFORMATION_MESSAGE);
                    JTextField inputField = new JTextField(11);
                    inputField.addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {
                        }

                        @Override
                        public void keyPressed(KeyEvent evt) {
                            if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
                                inputField.setEditable(true);
                                if(inputField.getText().length()>10){
                                    inputField.setEditable(false);
                                }else{
                                    inputField.setEditable(true);
                                }
                            }else{
                                if(evt.getExtendedKeyCode() == KeyEvent.VK_LEFT || evt.getExtendedKeyCode() == KeyEvent.VK_RIGHT || evt.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode() == KeyEvent.VK_DELETE || evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
                                    inputField.setEditable(true);
                                }else{
                                    inputField.setEditable(false);
                                }
                            }
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
    //                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        }
                    }); // Restrict input to integers only

                    Object[] message = {"Please enter your contact number:",inputField};

                    int option = JOptionPane.showOptionDialog(this,message,"Delivery Details",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,null,null);

                    if (option == JOptionPane.OK_OPTION) {
                        contactNumber = inputField.getText();
                    }else{
                        JOptionPane.showMessageDialog(this, "Transaction Failed","Delivery Details not entered.",JOptionPane.ERROR_MESSAGE);
                        abort=true;
                    }
                }
                if(!abort){
                    String orderID=recordTransaction(choosenType);
                    printPanel(orderID,choosenType, contactNumber);
                    printPanel(orderID,choosenType, contactNumber);
                    resetReceipt();
                }
            }else{
                JOptionPane.showMessageDialog(this, "Transaction Failed","Order Type Not Selected",JOptionPane.ERROR_MESSAGE);
            } 
        }else{
            JOptionPane.showMessageDialog(this, "Please check the \"Cash Paid\" field to proceed.","Not Acceptable Cash Paid",JOptionPane.ERROR_MESSAGE);
        }        
    }

    private String recordTransaction(String type){
        int orderCount;
        String orderID;
        int orderTotal=Integer.valueOf(totalPriceField.getText());
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yy");
        orderID=dateFormat.format(new java.util.Date());
        orderID=orderID.replace("-", "");
        orderID=orderID.concat(String.valueOf(pcID));        
        if(type!=null){
            try{
                con=DBConnection.connectDB();
                pst=con.prepareStatement("SELECT * FROM orderRecords WHERE DATE(orderDate) = CURRENT_DATE ORDER BY orderDate DESC FETCH FIRST 1 ROW ONLY");
                rs=pst.executeQuery();
                if(rs.next()){
                    String lastOrder=rs.getString("orderID");
                    String sub[]=lastOrder.split("-");
                    System.out.println("THIS" + sub[0] + sub[1]);
                    orderCount=Integer.valueOf(sub[1]);
                    System.out.println(orderCount);
                    orderCount++;
                }else{
                    orderCount=1;
                }
                orderID=orderID.concat("-"+String.valueOf(orderCount));
                System.out.println(orderID);
                pst=con.prepareStatement("insert into orderRecords values(?,?,?,?)");
                pst.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
                pst.setString(2,orderID);
                pst.setInt(3,orderTotal);
                pst.setString(4,type);
                pst.execute();
                
                pst=con.prepareStatement("insert into orderItems values(?,?,?,?,?,?,?,?)");
                for(OrderItem i:orderItems){
                    pst.setString(1,orderID);                    
                    pst.setString(2, i.getId());
                    pst.setString(3, i.toString());
                    pst.setString(4,i.getCategory());
                    pst.setInt(5,i.getQuantity());
                    pst.setInt(6, i.getUnitPrice());                    
                    pst.setInt(7, i.getTotalPrice()); 
                    pst.setString(8, i.getSize());
                    pst.execute();
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return orderID;
    }
    
    private void setCashBackField(){
        int cashPaid;
        if(cashPaidField.getText().equals("")){
            cashPaid=0;
        }else{
            cashPaid=Integer.valueOf(cashPaidField.getText());
        }        
        int totalPrice=Integer.valueOf(totalPriceField.getText());
        int cashBack=cashPaid-totalPrice;
        cashBackField.setText(String.valueOf(cashBack));
    }
    
    private void updateOrderItem(){
        DefaultTableModel model=(DefaultTableModel) orderTable.getModel();
        String itemName=model.getValueAt(orderTable.getSelectedRow(), 1).toString();
        int newQuantity=Integer.valueOf(model.getValueAt(orderTable.getSelectedRow(), 2).toString());
        for(OrderItem i:orderItems){
            if(i.toString().equals(itemName)){
                i.updateQuantity(newQuantity);
                model.setValueAt(i.getTotalPrice(), orderTable.getSelectedRow(), 3);
                break;
            }
        }
    }
    
    public void resetReceipt(){
        orderItems.removeAll(orderItems);
        DefaultTableModel model=(DefaultTableModel) orderTable.getModel();
        model.setRowCount(0);
        totalPriceField.setText("");
        cashPaidField.setText("");
        cashBackField.setText("");
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

    private void printPanel(String orderID, String billType, String contact){
        job = PrinterJob.getPrinterJob();
        java.sql.Date sqlDate= new java.sql.Date(new java.util.Date().getTime());
        job.setJobName(sqlDate+" _"+String.valueOf(new java.util.Date().getTime())); 
        PageFormat page=job.defaultPage();
        Paper paper = new Paper();
        paper.setImageableArea(0, 0, 200.0, orderItems.size()*500.0);
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
            }
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawLine(0, y1, 240, y1);
            y1 += headerRectHeight;
            g2.drawString("Total Amount", 0, y1);
            g2.drawString(":", 75, y1);
            g2.drawString(totalPriceField.getText(), 165, y1);
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
            g2.drawString(":", 65, y1);
            g2.drawString(String.valueOf(LocalDate.now()), 135, y1);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        rootPanel = new javax.swing.JPanel();
        orderPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        proceedBtn = new javax.swing.JButton();
        dropBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        returnBtn = new javax.swing.JButton();
        totalPriceField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cashPaidField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cashBackField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        categoryContainer = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        orderPanel.setBackground(new java.awt.Color(61, 91, 116));
        orderPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 0, true));
        orderPanel.setForeground(new java.awt.Color(88, 115, 141));

        orderTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        orderTable.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        orderTable.setForeground(new java.awt.Color(255, 255, 255));
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S/No", "Item", "Quantity", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderTable.setColumnSelectionAllowed(true);
        orderTable.setIntercellSpacing(new java.awt.Dimension(5, 5));
        orderTable.setRowHeight(25);
        orderTable.getTableHeader().setReorderingAllowed(false);
        orderTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                orderTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(orderTable);
        orderTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (orderTable.getColumnModel().getColumnCount() > 0) {
            orderTable.getColumnModel().getColumn(0).setResizable(false);
            orderTable.getColumnModel().getColumn(0).setPreferredWidth(5);
            orderTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            orderTable.getColumnModel().getColumn(2).setResizable(false);
            orderTable.getColumnModel().getColumn(2).setPreferredWidth(5);
            orderTable.getColumnModel().getColumn(3).setResizable(false);
            orderTable.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Order Receipt");

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

        totalPriceField.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        totalPriceField.setForeground(new java.awt.Color(102, 255, 102));
        totalPriceField.setEnabled(false);
        totalPriceField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalPriceFieldActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Total Price :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 255, 255));
        jLabel3.setText("Cash Paid :");

        cashPaidField.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        cashPaidField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cashPaidFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cashPaidFieldKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setText("Change:");

        cashBackField.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        cashBackField.setForeground(new java.awt.Color(102, 255, 102));
        cashBackField.setEnabled(false);

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
                                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(totalPriceField, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                    .addComponent(cashPaidField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(cashBackField, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(14, 14, 14)))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(orderPanelLayout.createSequentialGroup()
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cashPaidField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cashBackField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29)
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proceedBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dropBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        categoryContainer.setBackground(new java.awt.Color(61, 91, 116));
        categoryContainer.setAutoscrolls(true);
        categoryContainer.setMaximumSize(new java.awt.Dimension(1100, 32767));
        categoryContainer.setMinimumSize(new java.awt.Dimension(850, 10));
        categoryContainer.setPreferredSize(new java.awt.Dimension(850, 10));
        jPanel1.add(categoryContainer);

        javax.swing.GroupLayout rootPanelLayout = new javax.swing.GroupLayout(rootPanel);
        rootPanel.setLayout(rootPanelLayout);
        rootPanelLayout.setHorizontalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1098, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rootPanelLayout.setVerticalGroup(
            rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rootPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rootPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(orderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane2.setViewportView(rootPanel);

        getContentPane().add(jScrollPane2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void returnBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnBtnActionPerformed
        // TODO add your handling code here:
        if(userType.equals("Admin")){
            new OptionMenu().setVisible(true);
        }else{
            new LoginPage().setVisible(true);
        }        
        dispose();
    }//GEN-LAST:event_returnBtnActionPerformed

    private void orderTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_orderTableKeyPressed
        // TODO add your handling code here:
        if(evt.getExtendedKeyCode()==KeyEvent.VK_DELETE){
            removeItem();
        }
        if(evt.getExtendedKeyCode()==KeyEvent.VK_ENTER){
            updateOrderItem();
            updateOrderReceipt();
            categoryContainer.requestFocus();
        }
    }//GEN-LAST:event_orderTableKeyPressed

    private void cashPaidFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashPaidFieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyChar()>='0' && evt.getKeyChar()<='9'){
            cashPaidField.setEditable(true);
//            if(!cashPaidField.getText().equals("")){
//                setCashBackField();
//            }                
        }else{
            if(evt.getExtendedKeyCode() == KeyEvent.VK_LEFT || evt.getExtendedKeyCode() == KeyEvent.VK_RIGHT || evt.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE || evt.getExtendedKeyCode() == KeyEvent.VK_DELETE || evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
                if(evt.getExtendedKeyCode() == KeyEvent.VK_ENTER){
                    proceedBtn.requestFocus();
                }else
                    cashPaidField.setEditable(true);
            }else{
                cashPaidField.setEditable(false);
            }            
        }
            
    }//GEN-LAST:event_cashPaidFieldKeyPressed

    private void proceedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedBtnActionPerformed
        // TODO add your handling code here:
        if(((DefaultTableModel) orderTable.getModel()).getRowCount()>0){
            proceedTransaction();
        }            
    }//GEN-LAST:event_proceedBtnActionPerformed

    private void cashPaidFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashPaidFieldKeyReleased
        // TODO add your handling code here:
        setCashBackField();
    }//GEN-LAST:event_cashPaidFieldKeyReleased

    private void dropBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropBtnActionPerformed
        // TODO add your handling code here:
        resetReceipt();
    }//GEN-LAST:event_dropBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        // TODO add your handling code here:
        if(orderTable.getSelectedRow()>=0){
            removeItem();
        }else{
            JOptionPane.showMessageDialog(this,"No item selected","Error Message",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_removeBtnActionPerformed

    private void totalPriceFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalPriceFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalPriceFieldActionPerformed

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
            java.util.logging.Logger.getLogger(mainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainMenu("Emp").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cashBackField;
    private javax.swing.JTextField cashPaidField;
    private javax.swing.JPanel categoryContainer;
    private javax.swing.JButton dropBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JTable orderTable;
    private javax.swing.JButton proceedBtn;
    private javax.swing.JButton removeBtn;
    private javax.swing.JButton returnBtn;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JTextField totalPriceField;
    // End of variables declaration//GEN-END:variables
}

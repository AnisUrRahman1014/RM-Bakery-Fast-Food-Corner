/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author M AYAN LAPTOP
 */
public class DBConnection {
    private static String ipAddress;
    private static int port;
    
    private static void fetchIpConfig(){
        String filePath=System.getProperty("user.dir").concat("\\ipconfig.txt");
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("ipAddress:")) {
                        ipAddress = line.substring(line.indexOf(":") + 2, line.indexOf(";")).trim();
                        System.out.println("IP Address: " + ipAddress);
                    } else if (line.startsWith("port:")) {
                        String portString = line.substring(line.indexOf(":") + 2, line.indexOf(";")).trim();
                        port = Integer.parseInt(portString);
                        System.out.println("Port: " + port);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection connectDB(){
        fetchIpConfig();
        Connection con=null;
        Statement st=null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            con=DriverManager.getConnection("jdbc:derby://"+ipAddress+":"+port+"/RM_FFC","RMBakery","zawsar123456");
//            con=DriverManager.getConnection("jdbc:derby://"+"192.168.0.1"+":"+"2002"+"/RM_FFC","RMBakery","zawsar123456");
//            con=DriverManager.getConnection("jdbc:derby://localhost:2002/RM_FFC","RMBakery","zawsar123456");
            st=con.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error connecting to database", "Connection failed",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
    public static void main(String[] args) {
       connectDB();
    }
}

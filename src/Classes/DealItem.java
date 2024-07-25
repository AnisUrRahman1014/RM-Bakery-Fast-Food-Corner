/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author M AYAN LAPTOP
 */
public class DealItem extends Item{
    private int quantity;
    private final String size;
    private int price;
    private final int unitPrice;
    public static final String NORMAL="Normal";
    public static final String UNIVERSAL="Universal";
    public static final String SIZE_UNKNOWN="Unknown";
    private final String type;
    
    public DealItem(Item item,int quantity,String size,int price, String type){
        super(item);
        this.quantity=quantity;
        this.size=size;
        this.unitPrice=price;
        setPrice(quantity, price);
        this.type=type;
    }
    
    public DealItem(String category,int quantity,String size,int price, String type){
        super();
        setCategory(category);
        this.quantity=quantity;
        this.size=size;
        this.unitPrice=price;
        setPrice(quantity, price);
        this.type=type;
    }

    public String getType() {
        return type;
    }
    
    

    public int getPrice() {
        return price;
    }

    private void setPrice(int quantity,int price) {
        this.price = price*quantity;
    }
    

    public int getQuantity() {
        return quantity;
    }

    public String getSize() {
        return size;
    }
    
    public String toString(){
        if(!size.equals("---")){
           return getName().concat(" ").concat(size); 
        }else{
            return getName();
        }        
    }
    
    public void updateQuantity(int quantity){
        this.quantity=quantity;
        setPrice(quantity, unitPrice);
    }
    
    
}

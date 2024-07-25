package Classes;

import java.util.Iterator;

public class OrderItem extends Item implements Iterable<OrderItem>{
    private int quantity;
    private int unitPrice;
    private int totalPrice;
    private final String size;
    public OrderItem(Item item,int quantity,String size){
        super(item);
        this.quantity=quantity;
        this.size=size;
        setTotalPrice(quantity, size);        
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    private void setTotalPrice(int quantity,String size) {
        switch (size) {
            case "Small" -> {
                unitPrice=getSmallSalePrice();
            }
            case "Medium" -> {
                unitPrice = getMediumSalePrice();
            }
            case "Large" -> {
                unitPrice = getLargeSalePrice();
            }
            case "Extra Large" -> {
                unitPrice = getXLargeSalePrice();
            }
            default -> {
                unitPrice = getSingleSalePrice();
            }
        }
        this.totalPrice = unitPrice*quantity;
    }
    
    private void setTotalPrice(int quantity,int price) {
        this.totalPrice = price*quantity;
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
        setTotalPrice(quantity, unitPrice);
    }
    
    public int getUnitPrice(){
        return unitPrice;
    }

    @Override
    public Iterator<OrderItem> iterator() {
        return new Iterator<OrderItem>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < quantity;
            }

            @Override
            public OrderItem next() {
                currentIndex++;
                return OrderItem.this;
            }
        };
    }
}

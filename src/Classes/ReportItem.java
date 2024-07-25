package Classes;
public class ReportItem extends Item{
    private String itemSize;
    private int itemQuantity;
    private int totalCost;
    private int totalSale;
    private double profit;

    public ReportItem(Item i,String itemSize, int itemQuantity, int totalCost, int totalSale, double profit) {
        super(i);
        this.itemSize = itemSize;
        this.itemQuantity = itemQuantity;
        this.totalCost = totalCost;
        this.totalSale = totalSale;
        this.profit = profit;
    }

    public String getItemSize() {
        return itemSize;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public double getProfit() {
        return profit;
    }
    
    public String toString(){
        if(!itemSize.equals("---")){
           return getName().concat(" ").concat(itemSize); 
        }else{
            return getName();
        }        
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.ArrayList;

/**
 *
 * @author M AYAN LAPTOP
 */
public class Deal {
    private  ArrayList<Item> items=new ArrayList<>();
    private int dealCost;
    private int dealSalePrice;
    
    Deal(ArrayList<Item> items, int cost, int salePrice){
        this.items=items;
        this.dealCost=cost;
        this.dealSalePrice=salePrice;
    }
    
    public void updateItems(ArrayList<Item> items){
        this.items=items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int getDealCost() {
        return dealCost;
    }

    public void setDealCost(int dealCost) {
        this.dealCost = dealCost;
    }

    public int getDealSalePrice() {
        return dealSalePrice;
    }

    public void setDealSalePrice(int dealSalePrice) {
        this.dealSalePrice = dealSalePrice;
    }
    
    
    
}

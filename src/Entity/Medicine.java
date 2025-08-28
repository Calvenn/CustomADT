package Entity;

/**
 * 
 * @author Cheang Wei Ting
 */

public class Medicine implements Comparable<Medicine>{
    private String medID;
    private String name;
    private String desc;
    private int stock; 
    private double price; 
    private int totalDispensed = 0;
    
    public Medicine(){
        medID = "";
        name = "";
        desc = "";
        stock = 0;
        price = 0.00;
    }
    
    public Medicine(String medID, String name, String desc,int stock, double price){
        this.medID = medID;
        this.name = name;
        this.desc = desc;
        this.stock = stock;
        this.price = price;
    }
    
    @Override
    public int compareTo(Medicine other){
        return Integer.compare(this.stock,other.stock);
    }
    
    public int compareToBySales(Medicine other) {
        return Integer.compare(this.totalDispensed, other.totalDispensed);
    }
    
    public void incrementDispensed(int quantity) {
        this.totalDispensed += quantity;
    }
    
    public int getTotalDispensed() {
        return totalDispensed;
    }
    
    public String getMedID(){
        return medID;
    }
    public String getName(){
        return name;
    }
    public String getDesc(){
        return desc;
    }
    public int getStock(){
        return stock;
    }
    public double getPrice(){
        return price;
    }
    
    public void setMedID(String medID){
        this.medID = medID;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    public void setPrice(double price){
        this.price = price;
    }
}

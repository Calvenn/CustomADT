package Entity;

public class Medicine {
    private String medID;
    private String name;
    private String desc;
    private int stock; 
    private double price; 
    
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

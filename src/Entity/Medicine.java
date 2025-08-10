package Entity;

public class Medicine implements Comparable<Medicine>{
    private String medID;
    private String name;
    private String desc;
    private int stock; 
    
    public Medicine(){
        medID = "";
        name = "";
        desc = "";
        stock = 0;
    }
    
    public Medicine(String medID, String name, String desc,int stock){
        this.medID = medID;
        this.name = name;
        this.desc = desc;
        this.stock = stock;
    }
    
    public Medicine(Medicine original){
        this.medID = original.medID;
        this.name = original.name;
        this.desc = original.desc;
        this.stock = original.stock;
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
    
    @Override
    public int compareTo(Medicine other) {
        return Integer.compare(this.stock, other.stock);  // lower stock = higher priority
    }
}

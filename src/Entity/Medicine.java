package Entity;

public class Medicine implements Comparable<Medicine>{
    private String medID;
    private String batchID;
    private String name;
    private String desc;
    private String expiryDate; //format:"yyyy-MM-dd"
    private int stock; 
    
    public Medicine(){
        medID = "";
        name = "";
        desc = "";
        expiryDate = "";
        stock = 0;
    }
    
    public Medicine(String medID, String name, String desc, String expiryDate,int stock){
        this.medID = medID;
        this.batchID = medID + "-" + expiryDate.replaceAll("-","");
        this.name = name;
        this.desc = desc;
        this.expiryDate = expiryDate;
        this.stock = stock;
    }
    
    public Medicine(Medicine original){
        this.medID = original.medID;
        this.batchID = original.batchID;
        this.name = original.name;
        this.desc = original.desc;
        this.expiryDate = original.expiryDate;
        this.stock = original.stock;
    }
    
    public String getMedID(){
        return medID;
    }
    public String getBatchID(){
        return batchID;
    }
    public String getName(){
        return name;
    }
    public String getDesc(){
        return desc;
    }
    public String getExpiryDate(){
        return expiryDate;
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
    public void setExpiryDate(String expiryDate){
        this.expiryDate = expiryDate;
    }
    public void setStock(int stock){
        this.stock = stock;
    }
    
    @Override
    public int compareTo(Medicine other) {
        return Integer.compare(this.stock, other.stock);  // lower stock = higher priority
    }
}

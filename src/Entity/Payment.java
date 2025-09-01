/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.time.LocalDateTime;

/**
 *
 * @author calve
 */
public class Payment {
    public static final double consultPrice = 20.00;
    private static int idNo = 0; 
    private static int receiptIdNo = 0; 
    
    private String paymentId;
    private String receiptId;
    private Patient patient;
    private Consultation consult;
    private TreatmentAppointment trtAppt;
    private MedRecord medRec;
    private double price;
    private LocalDateTime paymentAt;
    private boolean isPay;
    
    public Payment(Patient patient, Consultation consult, double price, boolean isPay, TreatmentAppointment trtAppt, MedRecord medRec){
        this.paymentId = "P" + String.format("%04d", generateId()); 
        this.patient = patient;
        this.consult = consult;
        this.price = price;
        this.paymentAt = LocalDateTime.now();
        this.isPay = isPay;
        this.trtAppt = trtAppt;
        this.medRec = medRec;
    }
    
    private static int generateId() {
        idNo += 1; 
        return idNo; 
    }
    
    public String getPaymentId(){
        return paymentId;
    }
    
    public Patient getPatient(){
        return patient;
    }
    
    public Consultation getConsult(){
        return consult;
    }
    
    public TreatmentAppointment getTrtAppt(){
        return trtAppt;
    }
    
    public MedRecord getMedCollect(){
        return medRec;
    }
    
    public double getPrice(){
        return price;
    }
    
    public LocalDateTime getPaymentAt(){
        return paymentAt;
    }
    
    public boolean getIsPay(){
        return isPay;
    }
    
    private static int generateReceiptIdNo(){
        receiptIdNo += 1; 
        return receiptIdNo; 
    }
    
    public void generateReceiptId(){
        this.receiptId = "R" + String.format("%04d", generateReceiptIdNo());
    }
    
    public void setIsPay(boolean isPay){
        this.isPay = isPay;
    }
    
    public void setPaymentAt(LocalDateTime paymentAt){
        this.paymentAt = paymentAt;
    }
    
    public static String getHeader() {
        return String.format(
            "\n" + "-".repeat(150) + 
            "\n%-10s %-10s %-20s %-12s %-20s %-15s %-12s %-20s %-8s" + 
            "\n" + "-".repeat(150),
            "PaymentID","ReceiptID","Patient Name","ConsultID","Treatment","MedRecord","Price(RM)","Created At","Paid"
        );
    }
    
    @Override
    public String toString() {
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return String.format(
            "%-10s %-10s %-20s %-12s %-20s %-15s RM %-10.2f %-20s %-8s",
            paymentId,
            (receiptId != null ? receiptId : "-"),
            patient.getPatientName(),
            consult.getID(),
            (trtAppt != null ? trtAppt.getTreatment().getName() : "-"),
            (medRec != null ? medRec.getMed().getMedID() : "-"),
            price,
            paymentAt.format(dtf),
            (isPay ? "Yes" : "No")
        );
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Consultation;
import Entity.MedRecord;
import Entity.Payment;
import adt.List;
import adt.Queue;

/**
 *
 * @author CalvenPhnuahKahHong
 */
public class PaymentManager {
    private final Queue<MedRecord> medCollectQueue;
    public static List<Payment> paymentRec;
    
    public PaymentManager(Queue<MedRecord> medCollectQueue){
        this.medCollectQueue = medCollectQueue;
        this.paymentRec = new List<>();
    }
    
    public List<Payment> findPendingPayment(boolean isPay){
        Payment payment = null;
        List<Payment> pendingPayment = new List<>();
        for(int i = 1; i <= paymentRec.size(); i++){
            payment = paymentRec.get(i);
            if(payment.getIsPay() == isPay){
                pendingPayment.add(payment);
            }
        }
        return pendingPayment;
    }
    
    public Payment getPaymentInfo(String ic){
        Payment payment = null;
        for(int i = 1; i <= paymentRec.size(); i++){
            payment = paymentRec.get(i);
            if(payment.getPatient().getPatientIC().equals(ic)){
               return payment;
            }
        }
        return null;
    }
    
    public boolean isTrtAppt(Payment payment){
        if(payment.getTrtAppt() == null){
            return false;
        }
        Consultation.numOfTreatment++;
        return true;
    }
    
    public boolean isMedCollect(Payment payment){
        if(payment.getMedCollect() == null){
            return false;
        }
        medCollectQueue.enqueue(payment.getMedCollect());
        Consultation.numOfPharmacy++;
        return true;
    }   
    
    public void sortByDate(List<Payment> list, boolean ascending) {
        if (list.isEmpty()) return;

        // Bubble sort using ADT 
        for (int i = 1; i <= list.size(); i++) {
            for (int j = 1; j <= list.size() - i; j++) {
                Payment p1 = list.get(j);
                Payment p2 = list.get(j + 1);

                boolean needSwap = false;
                if (ascending && p1.getPaymentAt().isAfter(p2.getPaymentAt())) {
                    needSwap = true;
                } else if (!ascending && p1.getPaymentAt().isBefore(p2.getPaymentAt())) {
                    needSwap = true;
                }

                if (needSwap) {
                    list.replace(j, p2);
                    list.replace(j + 1, p1);
                }
            }
        }
    }
}

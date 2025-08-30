/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Consultation;
import Entity.MedRecord;
import Entity.Patient;
import Entity.Payment;
import Entity.TreatmentAppointment;
import adt.List;
import adt.Queue;

/**
 *
 * @author CalvenPhnuahKahHong
 */
public class PaymentManager {
    private final Queue<TreatmentAppointment> treatmentQueue;
    private final Queue<MedRecord> medCollectQueue;
    public static List<Payment> paymentRec = new List<>();
    
    public PaymentManager(Queue<TreatmentAppointment> treatmentQueue, Queue<MedRecord> medCollectQueue){
        this.treatmentQueue = treatmentQueue;
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
        treatmentQueue.enqueue(payment.getTrtAppt());
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
}

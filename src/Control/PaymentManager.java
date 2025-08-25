/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Consultation;
import Entity.MedRecord;
import Entity.Payment;
import Entity.TreatmentAppointment;
import adt.List;
import adt.Queue;

/**
 *
 * @author calve
 */
public class PaymentManager {
    private final List<Payment> paymentRec;
    private final Queue<TreatmentAppointment> treatmentQueue;
    private final Queue<MedRecord> medCollectQueue;
    
    public PaymentManager(List<Payment> paymentRec, Queue<TreatmentAppointment> treatmentQueue, Queue<MedRecord> medCollectQueue){
        this.paymentRec = paymentRec;
        this.treatmentQueue = treatmentQueue;
        this.medCollectQueue = medCollectQueue;
    }
    
    public List<Payment> findPendingPayment(){
        Payment payment = null;
        List<Payment> pendingPayment = new List<>();
        for(int i = 1; i <= paymentRec.size(); i++){
            payment = paymentRec.get(i);
            if(!payment.getIsPay()){
                pendingPayment.add(payment);
            }
        }
        return pendingPayment;
    }
    
    public Payment getPaymentInfo(String ic){
        Payment payment = null;
        for(int i = 1; i <= paymentRec.size(); i++){
            payment = paymentRec.get(i);
            if(!payment.getPatient().getPatientIC().equals(ic)){
                continue;
            }
        }
        return payment;
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

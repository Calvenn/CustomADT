/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Control.PaymentManager;
import Entity.Payment;
import adt.List;
import exception.ValidationHelper;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author calve
 */
public class PaymentUI {  
    private final PaymentManager paymentManager;
    
     public PaymentUI(PaymentManager paymentManager){
         this.paymentManager = paymentManager;
     }
     public void paymentMenu(){
        displayPayment(false); //pending payment
        int choice;
        while (true){ // Repeat the step if the user input invalid choice
            System.out.println("\n" + "=".repeat(35));
            System.out.println("        Payment");
            System.out.println("=".repeat(35));
            System.out.println("1. Make Payment"); //read only
            System.out.println("2. View Payment History"); //read only??
            System.out.println("0. Back");   
            System.out.println("===============================");
            
            choice = ValidationHelper.inputValidatedChoice(0, 5, "your choice");

            switch(choice){
                case 1 -> payment();
                case 2 -> displayPayment(true); //payment history
                case 0 -> {return;}
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
        }
     }
     
     public void payment() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String ic = ValidationHelper.inputValidatedIC("Enter ic number");
        Payment payment = paymentManager.getPaymentInfo(ic);

        if (payment != null) {
            System.out.println("\n" + "=".repeat(35));
            System.out.println("        Payment");
            System.out.println("=".repeat(35));
            System.out.println("Payment ID          : " + payment.getPaymentId());
            System.out.println("Patient             : " + payment.getPatient().getPatientName());
            System.out.println("Consultation        : " + payment.getConsult().getID());
            System.out.println("Doctor in charge    : " + payment.getConsult().getDoctor().getName());
            System.out.println("Price (RM)          : " + payment.getPrice());
            if (payment.getMedCollect() != null) {
                System.out.println("Medicine            : " + payment.getMedCollect().getMed().getName() + "(" + payment.getMedCollect().getMed().getMedID() +")");
            } 
            if(payment.getTrtAppt() != null) {
                System.out.println("Treatment           : " + payment.getTrtAppt().getTreatment());
            } 
            System.out.println("Created At          : " + payment.getCreatedAt().format(dtf));
            System.out.println("=".repeat(35));  
            System.out.println("\n[1] Mark as Paid");
            System.out.println("[0] Exit");

            int choice = ValidationHelper.inputValidatedChoice(0, 1, "your choice");

            switch (choice) {
                case 1:
                    payment.setIsPay(true);
                    payment.setPaymentAt(java.time.LocalDateTime.now());
                    if(paymentManager.isTrtAppt(payment)){
                        System.out.println("Payment done. Please ask patient to wait for treatment time.");
                    } else if(paymentManager.isMedCollect(payment)){
                        System.out.println("Payment done. Please ask patient go collect medicine.");
                    } else {                                       
                        System.out.println("Payment done");
                    }
                    payment.generateReceiptId();
                    break;
                case 0:
                    System.out.println("Exiting Payment Menu...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
     
     public void displayPayment(boolean isPay){
        List<Payment> pending = paymentManager.findPendingPayment(isPay);
        if(pending.isEmpty()){
            System.out.println(isPay == true? "No payment record found" : "No pending payment found");
            return;
        } 
        System.out.println(Payment.getHeader());
        for(int i = 1; i <= pending.size(); i++){
            Payment info = pending.get(i);
            System.out.println(info);
        }
     }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Boundary;

import Control.PaymentManager;
import Entity.Consultation;
import Entity.Payment;
import adt.List;
import exception.ValidationHelper;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author CalvenPhnuahKahHong
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
                case 2 -> {
                    displayPayment(true);
                    sortAndDisplayMenu();
                } //payment history
                case 0 -> {return;}
                default -> System.out.printf("\nInvalid choice entered. Please choose again.");
            }
        }
     }
     
     private Payment searchByIC(){
        String ic = ValidationHelper.inputValidatedIC("Enter ic number");
        Payment payment = paymentManager.getPaymentInfo(ic);
        if(payment == null){
            System.out.println("Pending payment for patient " + ic + " not found");
            return null;
        } else {
            return payment;
        }
     }
     
     public void payment() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Payment payment = searchByIC();

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
                System.out.println("Treatment           : " + payment.getTrtAppt().getTreatment().getName());
            } 
            System.out.println("Created At          : " + payment.getPaymentAt().format(dtf));
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
                        Consultation.numOfTreatment++;
                    } else if(paymentManager.isMedCollect(payment)){
                        System.out.println("Payment done. Please ask patient go collect medicine.");
                        Consultation.numOfPharmacy++;
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
     
     private void sortAndDisplayMenu() {
        List<Payment> sortPayment = PaymentManager.paymentRec;
        while (true) {
            System.out.println("\nDo you want to sort records by consultation date?");
            System.out.println("[1] Ascending (oldest first)");
            System.out.println("[2] Descending (latest first)");
            System.out.println("[3] Search by patient IC");
            System.out.println("[0] Back");

            int choice = ValidationHelper.inputValidatedChoice(0, 3, "sort option");

            switch (choice) {
                case 1 -> {
                    paymentManager.sortByDate(sortPayment,true);
                    printRecords(sortPayment);
                }
                case 2 -> {
                    paymentManager.sortByDate(sortPayment, false);
                    printRecords(sortPayment);
                }
                case 3 -> {
                    Payment payment = searchByIC();
                    Payment rec = paymentManager.getPaymentInfo(payment.getPatient().getPatientIC());
                    System.out.println(Payment.getHeader());
                    System.out.println(rec);
                }
                case 0 -> {
                    return;
                }
                default -> {
                }
            }
        }
    }
     
     private void printRecords(List<Payment> records) {
        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        System.out.println(Payment.getHeader());
        for (int i = 1; i <= records.size(); i++) {   
            Payment p = records.get(i);
            System.out.println(p);
        }
    }
}


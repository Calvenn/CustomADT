/*
 * PharmacyReportControl.java
 */
package Control;

import Entity.MedRecord;
import Entity.Medicine;
import adt.List;
import adt.LinkedHashMap;
import java.time.format.DateTimeFormatter;

public class PharmacyReport {
    private final List<MedRecord> medRecList;
    private final LinkedHashMap<String,Medicine> medMap;
    
    public PharmacyReport(List<MedRecord> medRecList,LinkedHashMap<String,Medicine> medMap){
        this.medRecList = medRecList;
        this.medMap = medMap;
    }
    
    public void generateInactiveMedicinesReport() {
        System.out.println("\n=== Inactive Medicines ===");

        if (medMap.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }

        // Step 1: Mark all medicines as "not dispensed"
        LinkedHashMap<String, Boolean> dispensedMap = new LinkedHashMap<>();
        Object[] medKeys = medMap.getKeys();
        for (Object obj : medKeys) {
            String medID = (String) obj;
            dispensedMap.put(medID, false);
        }

        // Step 2: Mark medicines that were actually dispensed
        for (int i = 1; i <= medRecList.size(); i++) {
            MedRecord rec = medRecList.get(i);
            dispensedMap.put(rec.getMed().getMedID(), true);
        }

        // Step 3: Print inactive medicines from medMap
        boolean foundInactive = false;
        for (Object obj : medKeys) {
            String medID = (String) obj;
            if (!dispensedMap.get(medID)) {
                Medicine med = medMap.get(medID);
                System.out.printf("MedID: %-5s -> %s (0 dispense records)%n", 
                                   med.getMedID(), med.getName());
                foundInactive = true;
            }
        }

        if (!foundInactive) {
            System.out.println("All medicines have been dispensed at least once.");
        }
    }


    public void generateMonthlyTrendsReport() {
        System.out.println("\n=== Monthly Medicine Trends ===");

        if (medRecList.isEmpty()) {
            System.out.println("No medicine records available.");
            return;
        }

        // Month -> (Medicine -> total dispensed)
        LinkedHashMap<String, LinkedHashMap<String, Integer>> monthMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");

        // Step 1: Aggregate by month and medicine
        for (int i = 1; i <= medRecList.size(); i++) {
            MedRecord rec = medRecList.get(i);
            String month = rec.getTimestamp().format(formatter);
            String medName = rec.getMed().getName();

            if (!monthMap.containsKey(month)) {
                monthMap.put(month, new LinkedHashMap<>());
            }

            LinkedHashMap<String, Integer> medMap = monthMap.get(month);
            int current = medMap.containsKey(medName) ? medMap.get(medName) : 0;
            medMap.put(medName, current + rec.getQuantityTaken());
        }

        // Step 2: Print monthly top medicine
        for (Object obj : monthMap.getKeys()) {
            String month = (String) obj;
            LinkedHashMap<String, Integer> medMap = monthMap.get(month);

            String topMed = "";
            int max = 0;

            for (Object medObj : medMap.getKeys()) {
                String medName = (String) medObj;
                int qty = medMap.get(medName);

                if (qty > max) {
                    max = qty;
                    topMed = medName;
                }
            }

            System.out.printf("%-10s -> %-20s (%d)%n", month, topMed, max);
        }
    }

   
    public void generateMedicineDispenseSummary() {
        System.out.println("\n=== Medicine Dispense Summary ===");

        if (medRecList.isEmpty()) {
            System.out.println("No medicine has been dispensed yet.");
            return;
        }

        // Step 1: Aggregate totals
        LinkedHashMap<String, Integer> summaryMap = new LinkedHashMap<>();
        int maxDispensed = 0;

        for (int i = 1; i <= medRecList.size(); i++) {
            MedRecord rec = medRecList.get(i);
            Medicine med = rec.getMed();

            String medID = med.getMedID();
            int currentTotal = summaryMap.containsKey(medID) ? summaryMap.get(medID) : 0;
            int newTotal = currentTotal + rec.getQuantityTaken();
            summaryMap.put(medID, newTotal);

            if (newTotal > maxDispensed) {
                maxDispensed = newTotal;
            }
        }

        Object[] keys = summaryMap.getKeys();


        // Scale: print ruler in steps of 5
        System.out.print("       ");
        for (int i = 0; i <= maxDispensed; i += 5) {
            System.out.printf("%-5d", i);
        }
        System.out.println();

        // Bars
        for (Object obj : keys) {
            String medID = (String) obj;
            int totalDispensed = summaryMap.get(medID);

            StringBuilder bar = new StringBuilder();
            for (int j = 0; j < totalDispensed; j++) {
                bar.append("#");
            }

            System.out.printf("%-6s | %-25s (%d)%n", medID, bar.toString(), totalDispensed);
        }

        // Step 3: Display Table Summary
        System.out.println("===========================================================");
        System.out.println("                     Tabular Summary                       ");
        System.out.println("===========================================================");
        System.out.printf("%-15s %-25s %-15s%n", "Medicine ID", "Medicine Name", "Total Dispensed");
        System.out.println("===========================================================");

        for (Object obj : keys) {
            String medID = (String) obj;
            int totalDispensed = summaryMap.get(medID);

            // find medicine name
            String medName = "";
            for (int i = 1; i <= medRecList.size(); i++) {
                if (medRecList.get(i).getMed().getMedID().equals(medID)) {
                    medName = medRecList.get(i).getMed().getName();
                    break;
                }
            }

            System.out.printf("%-15s %-25s %-15d%n", medID, medName, totalDispensed);
        }
    }
}

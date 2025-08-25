/*
 * PharmacyReportControl.java
 */
package Control;

import Entity.MedRecord;
import Entity.Medicine;
import adt.List;
import adt.LinkedHashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PharmacyReport {
    private final List<MedRecord> medRecList;
    private final LinkedHashMap<String,Medicine> medMap;
    
    public PharmacyReport(List<MedRecord> medRecList,LinkedHashMap<String,Medicine> medMap){
        this.medRecList = medRecList;
        this.medMap = medMap;
    }
    
    public void generateLowUsageMedicinesReport(int threshold, int days) {
        System.out.println("\n=== Low Usage Medicines (Usage < " + threshold +
                           " in last " + days + " days) ===");

        if (medMap.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }

        LinkedHashMap<String, Integer> usageMap = new LinkedHashMap<>();
        for (Object obj : medMap.getKeys()) {
            usageMap.put((String) obj, 0);
        }
        
        LocalDateTime now = LocalDateTime.now();
        for (int i = 1; i <= medRecList.size(); i++) {
            MedRecord rec = medRecList.get(i);
            String medID = rec.getMed().getMedID();

            // convert difference in seconds, then to days
            long secondsDiff = java.time.Duration.between(rec.getTimestamp(), now).getSeconds();
            long daysAgo = secondsDiff / (60 * 60 * 24);

            if (daysAgo <= days) {
                usageMap.put(medID, usageMap.get(medID) + rec.getQuantityTaken());
            }
        }

        boolean foundLowUsage = false;
        for (Object obj : medMap.getKeys()) {
            String medID = (String) obj;
            int totalDispensed = usageMap.get(medID);

            if (totalDispensed < threshold) {
                Medicine med = medMap.get(medID);
                System.out.printf("MedID: %-5s -> %-20s | Dispensed: %d in last %d days%n",
                                   med.getMedID(), med.getName(), totalDispensed, days);
                foundLowUsage = true;
            }
        }

        if (!foundLowUsage) {
            System.out.println("All medicines meet the usage threshold in the last " + days + " days.");
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
        int grandTotal = 0; // to calculate percentages

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

            grandTotal += rec.getQuantityTaken();
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

            // Calculate percentage
            double percentage = (totalDispensed * 100.0) / grandTotal;

            System.out.printf("%-6s | %-40s (%d) %.2f%%%n",
                    medID, bar.toString(), totalDispensed, percentage);
        }

        // Step 3: Display Table Summary
        System.out.println("================================================================================");
        System.out.println("                     Tabular Summary                       ");
        System.out.println("================================================================================");
        System.out.printf("%-15s %-25s %-20s %-15s%n", "Medicine ID", "Medicine Name", "Total Dispensed", "Percentage");
        System.out.println("================================================================================");

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

            double percentage = (totalDispensed * 100.0) / grandTotal;

            System.out.printf("%-15s %-25s %-20d %.2f%%%n",
                    medID, medName, totalDispensed, percentage);
        }
    }

    
}

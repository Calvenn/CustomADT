/*
 * PharmacyReportControl.java
 */
package Control;

import Entity.MedRecord;
import Entity.Medicine;
import adt.List;
import adt.LinkedHashMap;
import java.time.LocalDateTime;

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



    public void generateRevenueReport() {
        System.out.println("\n=== Medicine Revenue Report ===");

        if (medRecList.isEmpty()) {
            System.out.println("No medicine records available.");
            return;
        }

        double totalRevenue = 0;
        LinkedHashMap<String, Double> medRevenue = new LinkedHashMap<>();

        for (int i = 1; i <= medRecList.size(); i++) {
            MedRecord rec = medRecList.get(i);
            Medicine med = rec.getMed();
            double revenue = rec.getQuantityTaken() * med.getPrice();

            double current = medRevenue.containsKey(med.getName()) ? medRevenue.get(med.getName()) : 0;
            medRevenue.put(med.getName(), current + revenue);
            totalRevenue += revenue;
        }

        System.out.println("Per-Medicine Revenue:");
        for (Object obj : medRevenue.getKeys()) {
            String medName = (String) obj;
            double revenue = medRevenue.get(medName);
            System.out.printf(" - %-20s : RM %.2f%n", medName, revenue);
        }

        System.out.printf("\nTotal Revenue: RM %.2f%n", totalRevenue);
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

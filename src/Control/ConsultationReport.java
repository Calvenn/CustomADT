/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Consultation;
import adt.LinkedHashMap;
import adt.List;

/**
 *
 * @author calve
 */
public class ConsultationReport {
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    
    public ConsultationReport(LinkedHashMap<String, List<Consultation>> consultLog){
        this.consultLog = consultLog;
    }
    
    public void consultationOutcomeTrends(){
        System.out.println("===========================================");
        System.out.println("      Consultation Outcome Trends Report    ");
        System.out.println("===========================================");

        generateOutcomeCounts();
        generateDiagnosisTrends();

        System.out.println("===========================================\n");
    }

    private void generateOutcomeCounts() {
        int followUp = Consultation.numOfFollowUp;
        int pharmacy = Consultation.numOfPharmacy;
        int treatment = Consultation.numOfTreatment;
        int totalOutcome = followUp + pharmacy + treatment;

        if (totalOutcome == 0) {
            System.out.println("\nNo consultation outcomes recorded yet.");
            return;
        }

        System.out.println("\n--- Consultation Outcome Distribution ---");
        System.out.printf("| %-11s | %-5s | %-9s |\n", "Outcome", "Count", "Percentage");
        System.out.println("|-------------|-------|------------|");
        System.out.printf("| %-11s | %5d | %8.2f %% |\n", "Follow-up", followUp, followUp * 100.0 / totalOutcome);
        System.out.printf("| %-11s | %5d | %8.2f %% |\n", "Pharmacy", pharmacy, pharmacy * 100.0 / totalOutcome);
        System.out.printf("| %-11s | %5d | %8.2f %% |\n", "Treatment", treatment, treatment * 100.0 / totalOutcome);
        System.out.println("|-------------|-------|------------|");
        System.out.printf("| %-11s | %5d | %8.2f %% |\n", "Total", totalOutcome, 100.0);
    }

    
    private void generateDiagnosisTrends() {
        LinkedHashMap<String, Integer> diagnosisCount = new LinkedHashMap<>();
        Consultation c = null;

        if (consultLog == null || consultLog.isEmpty()) {
            System.out.println("No consultation records available.");
            return;
        }

        Object[] lists = consultLog.getValues(); // Each is a List<Consultation>

        for (Object obj : lists) {
            List<Consultation> consultationList = (List<Consultation>) obj;

            for (int i = 1; i <= consultationList.size(); i++) {
                c = consultationList.getEntry(i);
                if (c == null || c.getNotes() == null || c.getNotes().trim().isEmpty()) continue;

                String diagnosis = c.getNotes().trim().toLowerCase();

                if (diagnosisCount.containsKey(diagnosis)) {
                    diagnosisCount.put(diagnosis, diagnosisCount.get(diagnosis) + 1);
                } else {
                    diagnosisCount.put(diagnosis, 1);
                }
            }
        }

        if (diagnosisCount.isEmpty()) {
            System.out.println("\nNo diagnoses found in consultation notes.");
            return;
        }

        System.out.println("\n--- Diagnosis Frequency ---");
        System.out.printf("| %-18s | %-5s |\n", "Diagnosis", "Count");
        System.out.println("|--------------------|-------|");

        Object[] keys = diagnosisCount.getKeys();
        for (Object keyObj : keys) {
            String key = (String) keyObj;
            int count = diagnosisCount.get(key);
            System.out.printf("| %-18s | %5d |\n", key, count);
        }
    }
}

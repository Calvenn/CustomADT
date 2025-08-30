/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Entity.Consultation;
import adt.LinkedHashMap;
import adt.List;
import adt.Heap;

/**
 *
 * @author CalvenPhnuahKahHong
 */
public class ConsultationReport {
    private final LinkedHashMap<String, List<Consultation>> consultLog;
    
    public ConsultationReport(LinkedHashMap<String, List<Consultation>> consultLog){
        this.consultLog = consultLog;
    }

    public int[] generateOutcomeCounts() {
        int followUp = Consultation.numOfFollowUp;
        int pharmacy = Consultation.numOfPharmacy;
        int treatment = Consultation.numOfTreatment;
        int totalOutcome = followUp + pharmacy + treatment; //counut all

        if (totalOutcome == 0) return null;

        return new int[]{followUp, pharmacy, treatment, totalOutcome};
    }

    public LinkedHashMap<String, Integer> generateDiagnosisTrends() {
        LinkedHashMap<String, Integer> diagnosisCount = new LinkedHashMap<>();
        Consultation c = null;

        if (consultLog.isEmpty()) return null; 
   
        Object[] lists = consultLog.getValues(); //get consult list rec

        for (Object obj : lists) {
            List<Consultation> consultationList = (List<Consultation>) obj;

            for (int i = 1; i <= consultationList.size(); i++) {
                c = consultationList.get(i);
                if (c == null || c.getDisease() == null || c.getDisease().trim().isEmpty()) continue; //continue if no rec

                String diagnosis = c.getDisease().trim().toLowerCase(); //assign diagnosis

                if (diagnosisCount.containsKey(diagnosis)) {
                    diagnosisCount.put(diagnosis, diagnosisCount.get(diagnosis) + 1); // count num of diagnosis occur
                } else {
                    diagnosisCount.put(diagnosis, 1); //other wise 1
                }
            }
        }
        return diagnosisCount.isEmpty() ? null : diagnosisCount;
    }
    
    public LinkedHashMap<String, Object[]> generateTimeToConsultReport() {
        LinkedHashMap<String, Object[]> report = new LinkedHashMap<>();

        if (consultLog.isEmpty()) return report;
        
        // Stats for appointment
        long totalAppt = 0, apptCount = 0;
        Heap<Long> apptMaxHeap = new Heap<>(true);  //max-heap(store longest wait)
        Heap<Long> apptMinHeap = new Heap<>(false); //min-heap(store shortest wait)

        // Stats for walk-in
        long totalWalk = 0, walkCount = 0;
        Heap<Long> walkMaxHeap = new Heap<>(true); //max-heap(store longest wait)
        Heap<Long> walkMinHeap = new Heap<>(false); //min-heap(store shortest wait)

        Object[] lists = consultLog.getValues();
        for (Object obj : lists) {
            List<Consultation> consultations = (List<Consultation>) obj;

            for (int i = 1; i <= consultations.size(); i++) {
                Consultation c = consultations.get(i);
                if (c == null) continue;

                //convert to minutes
                long wait = java.time.Duration.between(c.getConsultTime(), c.getCreatedAt()).toMinutes();

                if (c.getDateTime() != null) { //appt
                    totalAppt += wait;
                    apptCount++;
                    apptMaxHeap.insert(wait);
                    apptMinHeap.insert(wait);
                } else { //walk in
                    totalWalk += wait;
                    walkCount++;
                    walkMaxHeap.insert(wait);
                    walkMinHeap.insert(wait);
                }
            }
        }

        long avgAppt = (apptCount == 0 ? 0 : totalAppt / apptCount); //count avg wait time for appt
        long avgWalk = (walkCount == 0 ? 0 : totalWalk / walkCount); //count walk in wait time for appt
        long avgOverall = ((apptCount + walkCount) == 0 ? 0 : (totalAppt + totalWalk) / (apptCount + walkCount)); //count overall wait time

        //put into LinkedHashMap: category → {avg, max, min}
        report.put("Appointments", new Object[]{
                avgAppt,
                (apptCount == 0 ? "-" : apptMaxHeap.peekRoot() + " mins"), //show longest wait
                (apptCount == 0 ? "-" : apptMinHeap.peekRoot() + " mins") //show shortest wait
        });
        report.put("Walk-ins", new Object[]{
                avgWalk,
                (walkCount == 0 ? "-" : walkMaxHeap.peekRoot() + " mins"), //show longest wait
                (walkCount == 0 ? "-" : walkMinHeap.peekRoot() + " mins") //show shortest wait
        });
        report.put("Overall", new Object[]{
                avgOverall,
                (apptCount + walkCount == 0 ? "-" :
                        Math.max(apptMaxHeap.isEmpty() ? Long.MIN_VALUE : apptMaxHeap.peekRoot(), 
                                walkMaxHeap.isEmpty() ? Long.MIN_VALUE : walkMaxHeap.peekRoot()) + " mins"), //f
                (apptCount + walkCount == 0 ? "-" :
                        Math.min(apptMinHeap.isEmpty() ? Long.MAX_VALUE : apptMinHeap.peekRoot(),
                                walkMinHeap.isEmpty() ? Long.MAX_VALUE : walkMinHeap.peekRoot()) + " mins")
        });
        return report;
    }
    
    // ===== Helper method Sorting =====
    private void swap(Object[] arr, int i, int j) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public void bubbleSortByCountDesc(Object[] keys, LinkedHashMap<String, Integer> trends) {
        for (int i = 0; i < keys.length - 1; i++) {
            for (int j = 0; j < keys.length - i - 1; j++) {
                String key1 = (String) keys[j];
                String key2 = (String) keys[j + 1];
                int count1 = trends.get(key1);
                int count2 = trends.get(key2);

                if (count1 < count2) { // descending order
                    swap(keys, j, j + 1);
                }
            }
        }
    }
}

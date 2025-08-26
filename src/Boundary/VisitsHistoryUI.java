package Boundary;

import Control.VisitHistoryManager;
import Entity.Patient;
import Entity.Visit;
import adt.List;
import exception.ValidationHelper;

import java.util.Scanner;

import java.time.LocalDate;

public class VisitsHistoryUI {
    private Scanner scanner;
    private final VisitHistoryManager historyManager;

    public VisitsHistoryUI(VisitHistoryManager historyManager) {
        this.historyManager = historyManager;
        this.scanner = new Scanner(System.in);
    }

    private void visitsTableHeader() {
        System.out.println("-".repeat(107));
        System.out.println(String.format("| %-10s | %-14s | %-20s | %-17s | %-30s |",
                "Visit ID", "Severity", "Doctor", "Register Time", "Symptoms"));
        System.out.println("-".repeat(107));
    }

    //show 10 records per page
    //if more than 10 records, prompt user to press enter to see next page
    private void displayVisitsPaginated(List<Visit> visits) {
        int pageSize = 10;
        int totalVisits = visits.size();
        int page = 0;

        while (page * pageSize < totalVisits) {
            int start = page * pageSize;
            int end = Math.min(start + pageSize, totalVisits);

            visitsTableHeader();
            for (int i = start + 1; i <= end; i++) {
                System.out.println(visits.get(i));
            }

            page++;
            if (end < totalVisits) {
                System.out.print("\nPress Enter to see next page...");
                scanner.nextLine();
            }
        }
    }

    public void displayHistoricalVisits() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    Patient Visits History");
        System.out.println("=".repeat(35));

        int month = ValidationHelper.inputValidatedChoice(1, 12, "month (1-12)");
        int year = ValidationHelper.inputValidatedChoice(2020, LocalDate.now().getYear(), "year");

        List<Visit> filteredVisits = historyManager.getVisitsByMonthYear(month, year);
        System.out.println("Total visits in selected period: " + filteredVisits.size());
        System.out.println(("-".repeat(107)));
        int width = 106; 
        String title = "VISIT HISTORY FOR " + month + "/" + year;
        System.out.printf("| %-"+(width-2)+"s |%n", title);
        if (filteredVisits.size() == 0) {
            System.out.println("No visits found for the selected period.");
            System.out.println(("-".repeat(101)));
            return;
        }
        displayVisitsPaginated(filteredVisits);
    }

    public void displayPatientHistory(Patient patient) {
        if (patient == null) return; 
        System.out.println(("-".repeat(101)));
        int width = 99; 
        String title = "VISIT HISTORY FOR PATIENT: " + patient.getPatientIC();
        System.out.printf("| %-"+(width-2)+"s |%n", title);
        List<Visit> patientVisits = historyManager.getVisitsByPatient(patient.getPatientIC());
        if (patientVisits.size() == 0) {
            System.out.println("No visit records found for this patient.");
            return;
        }
        visitsTableHeader();
        for (int i = 1; i <= patientVisits.size(); i++) {  // keep 1-based if your List ADT is 1-based
            System.out.println(patientVisits.get(i));
        }
    }

    // Helper method: bar chart + table
    private void printBarChartWithTable(String title, String[] labels, int[] counts, boolean showPercentage) {
        int total = 0;
        for (int c : counts) total += c;

        System.out.println("\n" + "=".repeat(46));
        System.out.println("       " + title + " CHART");
        System.out.println("=".repeat(46));

        int maxBarLength = 25; // max number of stars
        int maxCount = 0;
        for (int c : counts) if (c > maxCount) maxCount = c;

        for (int i = 0; i < labels.length; i++) {
            int barLength = (maxCount == 0) ? 0 : (int)((counts[i] * maxBarLength * 1.0) / maxCount);
            System.out.printf("%-15s | %s%n", labels[i], "*".repeat(barLength));
        }
        System.out.println("-".repeat(46));

        System.out.println("       " + title + " TABLE");
        System.out.println("-".repeat(46));
        if (showPercentage) {
            System.out.printf("%-15s %-10s %-10s%n", "Label", "Count", "Percentage");
            System.out.println("-".repeat(46));
            for (int i = 0; i < labels.length; i++) {
                double percent = (total > 0) ? counts[i] * 100.0 / total : 0;
                System.out.printf("%-15s %-10d %-9.2f%%%n", labels[i], counts[i], percent);
            }
        } else {
            System.out.printf("%-15s %-10s%n", "Label", "Count");
            System.out.println("-".repeat(46));
            for (int i = 0; i < labels.length; i++) {
                System.out.printf("%-15s %-10d%n", labels[i], counts[i]);
            }
        }

        System.out.println("-".repeat(46));
        System.out.println("Total: " + total);
    }

    public void displayYearlyReport() {
        int year = ValidationHelper.inputValidatedChoice(2025, LocalDate.now().getYear(), "year");
        int[] monthCounts = historyManager.getMonthlyVisitCounts(year);
        String[] monthNames = {"January","February","March","April","May","June", "July","August","September","October","November","December"};
        printBarChartWithTable("YEAR " + year + " VISITS PER MONTH", monthNames, monthCounts, true);
    }

    public void displaySeverityReport() {
        int month = ValidationHelper.inputValidatedChoice(1, 12, "month (1-12)");
        int year = ValidationHelper.inputValidatedChoice(2025, LocalDate.now().getYear(), "year");

        int[] severityCounts = historyManager.getSeverityCounts(month, year);
        String[] labels = {"Mild", "Urgent", "Emergency"};

        printBarChartWithTable("SEVERITY REPORT " + month + "/" + year, labels, severityCounts, true);
    }

}
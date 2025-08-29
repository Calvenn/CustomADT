package Boundary;

import Control.VisitHistoryManager;
import Entity.Patient;
import Entity.Visit;
import adt.List;
import exception.ValidationHelper;

import java.time.LocalDate;

public class VisitsHistoryUI {
    private final VisitHistoryManager historyManager;

    public VisitsHistoryUI(VisitHistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private void visitsTableHeader() {
        System.out.println("-".repeat(107));
        System.out.println(String.format("| %-10s | %-14s | %-20s | %-17s | %-30s |",
                "Visit ID", "Severity", "Doctor", "Register Time", "Symptoms"));
        System.out.println("-".repeat(107));
    }

    public void displayHistoricalVisits() {
        System.out.println("\n" + "=".repeat(35));
        System.out.println("    Patient Visits History");
        System.out.println("=".repeat(35));

        int month = ValidationHelper.inputValidatedChoice(1, 12, "month (1-12)");
        int year = ValidationHelper.inputValidatedChoice(2020, LocalDate.now().getYear(), "year");

        // Ask user sorting order
        System.out.println("\nChoose sorting order:");
        System.out.println("1. Oldest date first");
        System.out.println("2. Latest date first");
        int choice = ValidationHelper.inputValidatedChoice(1, 2, "choice");
        boolean ascending = (choice == 1);

        // Now get visits with bubble sort applied
        List<Visit> filteredVisits = historyManager.getVisitsByMonthYear(month, year, ascending);

        System.out.println("\nTotal visits in selected period: " + filteredVisits.size());
        System.out.println("-".repeat(107));
        System.out.println("| VISIT HISTORY FOR " + month + "/" + year + " ".repeat(76) + " |");

        if (filteredVisits.size() == 0) {
            System.out.println("No visits found for the selected period.");
            System.out.println("-".repeat(101));
            return;
        }

        // Display visits in chosen order
        visitsTableHeader();
        for (int i = 1; i <= filteredVisits.size(); i++) {
            System.out.println(filteredVisits.get(i));
        }
    }

    public void displayPatientHistory(Patient patient) {
        if (patient == null) return; 
        System.out.println(("-".repeat(107)));
        System.out.println("| VISIT HISTORY FOR PATIENT " + patient.getPatientIC() + " ".repeat(63) + " |");
        List<Visit> patientVisits = historyManager.getVisitsByPatient(patient.getPatientIC());
        if (patientVisits.size() == 0) {
            System.out.println("No visit records found for this patient.");
            return;
        }
        visitsTableHeader();
        for (int i = 1; i <= patientVisits.size(); i++) { 
            System.out.println(patientVisits.get(i));
        }
    }

    // bar chart + table
    private void printBarChartWithTable(String title, String[] labels, int[] counts, boolean showPercentage) {
        // Calculate the total count
        int total = 0;
        for (int i = 0; i < counts.length; i++) {
            total = total + counts[i];
        }

        // Print the header for chart
        System.out.println("\n" + "=".repeat(46));
        System.out.println("       " + title + " CHART");
        System.out.println("=".repeat(46));

        int maxBarLength = 25; // maximum number of stars
        int maxCount = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
            }
        }

        // Print the bar chart
        for (int i = 0; i < labels.length; i++) {
            // Calculate how many stars to print
            int barLength = 0;
            if (maxCount > 0) {
                barLength = (int)((counts[i] * maxBarLength) / maxCount);
            }
            
            // Print label and stars
            System.out.printf("%-15s | %s\n", labels[i], "*".repeat(barLength));
        }
        
        // Print separator and table header
        System.out.println("-".repeat(46));
        System.out.println("       " + title + " TABLE");
        System.out.println("-".repeat(46));
        
        // Print table with or without percentage
        if (showPercentage == true) {
            System.out.printf("%-15s %-10s %-10s\n", "Label", "Count", "Percentage");
            System.out.println("-".repeat(46));
            
            for (int i = 0; i < labels.length; i++) {
                double percent = 0.0;
                if (total > 0) {
                    percent = (counts[i] * 100.0) / total;
                }
                System.out.printf("%-15s %-10d %.2f%%\n", labels[i], counts[i], percent);
            }
        } else {
            System.out.printf("%-15s %-10s\n", "Label", "Count");
            System.out.println("-".repeat(46));
            
            for (int i = 0; i < labels.length; i++) {
                System.out.printf("%-15s %-10d\n", labels[i], counts[i]);
            }
        }

        System.out.println("-".repeat(46));
        System.out.println("Total: " + total);
    }

    public void displayYearlyReport() {
        int year = ValidationHelper.inputValidatedChoice(2025, LocalDate.now().getYear(), "year");
        int[] monthCounts = historyManager.getMonthlyVisitCounts(year);
        
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                            "July", "August", "September", "October", "November", "December"};
        
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
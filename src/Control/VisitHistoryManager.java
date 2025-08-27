package Control;

import Entity.Visit;
import adt.LinkedHashMap;
import adt.List;
import java.time.LocalDate;

/*  manage patient visit history
    It keeps track of all visits and organizes them by date
    each date has a list of visits that happened on that day
*/

public class VisitHistoryManager {
    // Store visits grouped by date (each date can have multiple visits)
    private final LinkedHashMap<LocalDate, List<Visit>> historicalVisits;
    // Cache all dates in sorted order
    private Object[] allDates;

    public VisitHistoryManager() {
        this.historicalVisits = new LinkedHashMap<>();
        this.allDates = new Object[0];
    }

    public void addHistoricalVisit(Visit visit) {
        // Get the date of the visit, not extract the time part
        LocalDate date = visit.getRegistrationTime().toLocalDate();

        // Retrieve or create the list of visits for that date
        List<Visit> visitsOnDate = historicalVisits.get(date);
        // If no list exists for that date, create a new one
        if (visitsOnDate == null) {
            visitsOnDate = new List<>();
            historicalVisits.put(date, visitsOnDate);
        }
        // Add the visit to the list for that date
        visitsOnDate.add(visit);
        
        // Update and sort the dates
        updateSortedDates();
    }
    
    // Update the allDates array and sort it
    private void updateSortedDates() {
        allDates = historicalVisits.getKeys();
        
        // Sort the dates using Insertion Sort
        for (int i = 1; i < allDates.length; i++) {
            LocalDate key = (LocalDate) allDates[i];
            int j = i - 1;

            // Shift elements that are later than key
            // Move dates that come after our current date to the right
            while (j >= 0 && ((LocalDate) allDates[j]).isAfter(key)) {
                allDates[j + 1] = allDates[j];
                j--;
            }
            allDates[j + 1] = key;
        }
    }

    // Retrieve all visits for a specific patient by their IC
    public List<Visit> getVisitsByPatient(String patientIC) {
        List<Visit> patientVisits = new List<>();
        
        for (Object obj : allDates) {
            LocalDate date = (LocalDate) obj;
            List<Visit> visitsOnDate = historicalVisits.get(date);
            // Check each visit on this date
            for (int i = 1; i <= visitsOnDate.size(); i++) {
                Visit v = visitsOnDate.get(i);
                // If this visit belongs to our patient, add to our list for patient history
                if (v.getPatient().getPatientIC().equals(patientIC)) {
                    patientVisits.add(v);
                }
            }
        }
        return patientVisits;
    }

    // Retrieve all visits in a specific month and year
    // Enter 0 for month or year if want to include all months/years
    public List<Visit> getVisitsByMonthYear(int month, int year) {
        List<Visit> result = new List<>();
        
        for (Object obj : allDates) {
            LocalDate date = (LocalDate) obj;
            boolean monthMatches = (date.getMonthValue() == month);
            boolean yearMatches =  (date.getYear() == year);
            if (monthMatches && yearMatches) {
                List<Visit> visitsOnDate = historicalVisits.get(date);
                for (int i = 1; i <= visitsOnDate.size(); i++) {
                    result.add(visitsOnDate.get(i));
                }
            }
        }
        return result;
    }

    // Count how many visits happened in each month of a specific year
    // Returns an array where index 0 = January, index 1 = February
    public int[] getMonthlyVisitCounts(int year) {
        int[] monthCounts = new int[12];
        
        // allDates is already sorted
        for (Object obj : allDates) {
            LocalDate date = (LocalDate) obj;

            // Only count visits from the year we want
            if (date.getYear() == year) {
                List<Visit> visitsOnDate = historicalVisits.get(date);
                monthCounts[date.getMonthValue() - 1] += visitsOnDate.size();
            }
        }
        return monthCounts;
    }

    // Count how many visits of each severity level happened in a specific month and year
    // Enter 0 for month or year if want to include all months/years
    public int[] getSeverityCounts(int month, int year) {
        int[] severityCounts = new int[3];
        
        // allDates is already sorted
        for (Object obj : allDates) {
            LocalDate date = (LocalDate) obj;
            // Check if this date matches the month and year we want
            boolean monthMatches = (date.getMonthValue() == month);
            boolean yearMatches = (date.getYear() == year);
            // Only process visits on this date if it matches our criteria
            if (monthMatches && yearMatches) {
                List<Visit> visitsOnDate = historicalVisits.get(date);

                // Count each visit based on its severity
                for (int i = 1; i <= visitsOnDate.size(); i++) {
                    Visit v = visitsOnDate.get(i);
                    // Increment the appropriate severity count
                    switch (v.getSeverityLevel()) {
                        case MILD -> severityCounts[0]++;
                        case URGENT -> severityCounts[1]++;
                        case EMERGENCY -> severityCounts[2]++;
                    }
                }
            }
        }
        return severityCounts;
    }
}
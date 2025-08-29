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

    public VisitHistoryManager() {
        this.historicalVisits = new LinkedHashMap<>();
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
    }

    // Retrieve all visits for a specific patient by their IC
    public List<Visit> getVisitsByPatient(String patientIC) {
        List<Visit> patientVisits = new List<>();
        
        // Get all dates from the HashMap
        Object[] dates = historicalVisits.getKeys();
        
        // Sort the dates using Insertion Sort
        for (int i = 1; i < dates.length; i++) {
            LocalDate key = (LocalDate) dates[i];
            int j = i - 1;

            // Shift elements that are later than key
            // Move dates that come after our current date to the right
            while (j >= 0 && ((LocalDate) dates[j]).isAfter(key)) {
                dates[j + 1] = dates[j];
                j--;
            }
            dates[j + 1] = key;
        }
        
        // Now iterate through sorted dates
        for (Object obj : dates) {
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
    public List<Visit> getVisitsByMonthYear(int month, int year, boolean ascending) {
        List<Visit> result = new List<>();
        Object[] dates = historicalVisits.getKeys();

        // Collect all visits for the month & year
        for (Object obj : dates) {
            LocalDate date = (LocalDate) obj;
            boolean monthMatches = (date.getMonthValue() == month);
            boolean yearMatches = (date.getYear() == year);
            if (monthMatches && yearMatches) {
                List<Visit> visitsOnDate = historicalVisits.get(date);
                for (int i = 1; i <= visitsOnDate.size(); i++) {
                    result.add(visitsOnDate.get(i));
                }
            }
        }

        // Bubble Sort by registrationTime
        for (int i = 1; i <= result.size(); i++) {
            for (int j = 1; j <= result.size() - i; j++) {
                Visit v1 = result.get(j);
                Visit v2 = result.get(j + 1);

                boolean shouldSwap = false;
                
                if (ascending) {
                    // For ascending order (oldest first)
                    if (v1.getRegistrationTime().isAfter(v2.getRegistrationTime())) {
                        shouldSwap = true;
                    }
                } else {
                    // For descending order (latest first)
                    if (v1.getRegistrationTime().isBefore(v2.getRegistrationTime())) {
                        shouldSwap = true;
                    }
                }

                if (shouldSwap) {
                    // Swap visits
                    result.replace(j, v2);
                    result.replace(j + 1, v1);
                }
            }
        }

        return result;
    }

    // Count how many visits happened in each month of a specific year
    // Returns an array where index 0 = January, index 1 = February
    public int[] getMonthlyVisitCounts(int year) {
        // Create an array with 12 slots (Jan to Dec)
        int[] monthCounts = new int[12];
        Object[] dates = historicalVisits.getKeys();
        
        // Go through every date we have
        for (Object obj : dates) {
            LocalDate date = (LocalDate) obj;

            // Check if the date is in the same year
            if (date.getYear() == year) {
                // Get all visits for that date
                List<Visit> visitsOnDate = historicalVisits.get(date);
                // Find which month (1-12) → subtract 1 to fit array index (0-11)
                int monthIndex = date.getMonthValue() - 1;

                monthCounts[monthIndex] = monthCounts[monthIndex] + visitsOnDate.size();
            }
        }
        return monthCounts;
    }

    // Count how many visits of each severity level happened in a specific month and year
    // Enter 0 for month or year if want to include all months/years
    public int[] getSeverityCounts(int month, int year) {
        int[] severityCounts = new int[3];
        Object[] dates = historicalVisits.getKeys();

        for (Object obj : dates) {
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
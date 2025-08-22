package Control;

import Entity.Visit;
import adt.LinkedHashMap;
import adt.List;
import java.time.LocalDate;

public class VisitHistoryManager {
    // Key = date of visit, Value = all visits on that date
    private final LinkedHashMap<LocalDate, List<Visit>> historicalVisits;

    public VisitHistoryManager() {
        this.historicalVisits = new LinkedHashMap<>();
    }

    // Add a visit into history based on its date
    public void addHistoricalVisit(Visit visit) {
        LocalDate date = visit.getRegistrationTime().toLocalDate();
        List<Visit> visitsOnDate = historicalVisits.get(date);

        if (visitsOnDate == null) {
            visitsOnDate = new List<>();
            historicalVisits.put(date, visitsOnDate);
        }
        visitsOnDate.add(visit);
    }

    // Get visits for a specific date
    public List<Visit> getVisitsByDate(LocalDate date) {
        return historicalVisits.get(date);
    }

    // Get all stored history
    public LinkedHashMap<LocalDate, List<Visit>> getAllHistoricalVisits() {
        return historicalVisits;
    }

    // Reporting helper
    public int getVisitCountByDate(LocalDate date) {
        List<Visit> visits = historicalVisits.get(date);
        return (visits != null) ? visits.size() : 0;
    }
}


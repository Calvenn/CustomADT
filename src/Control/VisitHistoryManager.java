package Control;

import Entity.Visit;
import adt.LinkedHashMap;
import adt.List;
import java.time.LocalDate;

public class VisitHistoryManager {
    private final LinkedHashMap<LocalDate, List<Visit>> historicalVisits;

    public VisitHistoryManager() {
        this.historicalVisits = new LinkedHashMap<>();
    }

    public void addHistoricalVisit(Visit visit) {
        LocalDate date = visit.getRegistrationTime().toLocalDate();
        List<Visit> visitsOnDate = historicalVisits.get(date);
        if (visitsOnDate == null) {
            visitsOnDate = new List<>();
            historicalVisits.put(date, visitsOnDate);
        }
        visitsOnDate.add(visit);
    }

    public List<Visit> getVisitsByDate(LocalDate date) {
        return historicalVisits.get(date);
    }

    public LinkedHashMap<LocalDate, List<Visit>> getAllHistoricalVisits() {
        return historicalVisits;
    }

    public Object[] getAllDates() {
        return historicalVisits.getKeys();
    }

    public List<Visit> getAllVisits() {
        List<Visit> allVisits = new List<>();
        Object[] dates = getAllDates();
        for (Object obj : dates) {
            LocalDate date = (LocalDate) obj;
            List<Visit> visitsOnDate = historicalVisits.get(date);
            for (int i = 1; i <= visitsOnDate.size(); i++) {
                allVisits.add(visitsOnDate.get(i));
            }
        }
        return allVisits;
    }

    public List<Visit> getVisitsByPatient(String patientIC) {
        List<Visit> patientVisits = new List<>();
        Object[] dates = getAllDates();
        for (Object obj : dates) {
            LocalDate date = (LocalDate) obj;
            List<Visit> visitsOnDate = historicalVisits.get(date);
            for (int i = 1; i <= visitsOnDate.size(); i++) {
                Visit v = visitsOnDate.get(i);
                if (v.getPatient().getPatientIC().equals(patientIC)) {
                    patientVisits.add(v);
                }
            }
        }
        return patientVisits;
    }

    public int getVisitCountByDate(LocalDate date) {
        List<Visit> visits = historicalVisits.get(date);
        return (visits != null) ? visits.size() : 0;
    }

    public List<Visit> getVisitsByMonthYear(int month, int year) {
        List<Visit> result = new List<>();
        Object[] dates = getAllDates();
        for (Object obj : dates) {
            LocalDate date = (LocalDate) obj;
            boolean monthMatches = (month == 0) || (date.getMonthValue() == month);
            boolean yearMatches = (year == 0) || (date.getYear() == year);
            if (monthMatches && yearMatches) {
                List<Visit> visitsOnDate = historicalVisits.get(date);
                for (int i = 1; i <= visitsOnDate.size(); i++) {
                    result.add(visitsOnDate.get(i));
                }
            }
        }
        return result;
    }

    public int[] getMonthlyVisitCounts(int year) {
        int[] monthCounts = new int[12];
        Object[] dates = getAllDates();
        for (Object obj : dates) {
            LocalDate date = (LocalDate) obj;
            if (date.getYear() == year) {
                List<Visit> visitsOnDate = historicalVisits.get(date);
                monthCounts[date.getMonthValue() - 1] += visitsOnDate.size();
            }
        }
        return monthCounts;
    }

    public int[] getSeverityCounts(int month, int year) {
        int[] severityCounts = new int[3];
        Object[] dates = getAllDates();
        for (Object obj : dates) {
            LocalDate date = (LocalDate) obj;
            boolean monthMatches = (month == 0) || (date.getMonthValue() == month);
            boolean yearMatches = (year == 0) || (date.getYear() == year);
            if (monthMatches && yearMatches) {
                List<Visit> visitsOnDate = historicalVisits.get(date);
                for (int i = 1; i <= visitsOnDate.size(); i++) {
                    Visit v = visitsOnDate.get(i);
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
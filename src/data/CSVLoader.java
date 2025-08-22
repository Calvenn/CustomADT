/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import Control.ConsultationManager;
import Control.DoctorManager;
import Control.PatientManager;
import Control.VisitHistoryManager;
import Entity.Consultation;
import Entity.Doctor;
import Entity.Patient;
import Entity.Severity;
import Entity.Visit;
import adt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author calve
 */
public class CSVLoader {
    
    public static void loadPatientFromCSV(String filePath, PatientManager patientManager) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                // Skip header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // Split CSV values
                String[] values = line.split(",", 6); 
                if (values.length < 6) {
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                String patientIc = values[0].trim();
                String name = values[1].trim();
                String phone = values[2].trim();
                int age = Integer.parseInt(values[3].trim());
                char gender = values[4].trim().charAt(0);
                String address = values[5].trim().replaceAll("^\"|\"$", "");

                // Register into patient manager
                patientManager.registerNewPatient(patientIc, name, phone, age, gender, address);
            }

            System.out.println("Patients loaded successfully from " + filePath);

        } catch (IOException e) {
            System.err.println("Error reading patient CSV file: " + e.getMessage());
        }
    }
    
    public static void loadConsultRecFromCSV(String filePath, DoctorManager docManager, LinkedHashMap<String, List<Consultation>> consultLog) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { // skip header
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");

                // ===== Extract Patient =====
                Patient p = new Patient(
                        values[1], // ic
                        values[2], // name
                        values[3], // phone
                        Integer.parseInt(values[4]), // age
                        values[5].charAt(0),         // gender
                        values[6]                    // address
                );

                String doctorId = values[7];
                Doctor doc = docManager.findDoctor(doctorId);

                // ===== Extract Consultation =====
                int severity = Integer.parseInt(values[8]);
                String disease = values[9];
                String notes = values[10];

                LocalDateTime consultTime = LocalDateTime.parse(values[11]);
                LocalDateTime apptDateTime = values[12].equals("null") ? null : LocalDateTime.parse(values[12]);
                LocalDateTime createdAt = LocalDateTime.parse(values[13]);

                Consultation c = new Consultation(severity, p, disease, notes, doc,
                                                  consultTime, apptDateTime, createdAt);

                List<Consultation> doctorConsults = consultLog.get(doctorId);
                if (doctorConsults == null) {
                    doctorConsults = new List<>(); 
                    consultLog.put(doctorId, doctorConsults);
                }
                doctorConsults.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadVisitHistoryFromCSV(String filePath, PatientManager patientManager, DoctorManager docManager, VisitHistoryManager historyManager) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { // skip header
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",", 6); // 6 columns: VisitID, IC, Symptoms, Severity, DoctorID, RegistrationTime

                if (values.length < 6) {
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                String visitId = values[0].trim();
                String patientIc = values[1].trim();
                String symptoms = values[2].trim();
                String severityStr = values[3].trim();
                String doctorId = values[4].trim();
                LocalDateTime registrationTime = LocalDateTime.parse(values[5].trim(), formatter);

                // Retrieve patient and doctor
                Patient patient = patientManager.findPatientByIC(patientIc);
                if (patient == null) {
                    System.err.println("Patient not found: " + patientIc + " (Skipping visit " + visitId + ")");
                    continue;
                }

                Doctor doctor = docManager.findDoctor(doctorId);
                if (doctor == null) {
                    System.err.println("Doctor not found: " + doctorId + " (Skipping visit " + visitId + ")");
                    continue;
                }

                // Convert string to Severity enum
                Severity severity;
                try {
                    severity = Severity.valueOf(severityStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid severity: " + severityStr + " (defaulting to MILD)");
                    severity = Severity.MILD;
                }

                Visit visit = new Visit(visitId, patient, symptoms, severity, doctor, registrationTime);
                historyManager.addHistoricalVisit(visit);
            }

            System.out.println("Visit history loaded successfully from " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

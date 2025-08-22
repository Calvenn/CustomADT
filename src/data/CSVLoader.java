/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import Control.DoctorManager;
import Control.MedicineControl;
import Control.PatientManager;
import Control.VisitHistoryManager;
import Control.TreatmentManager;
import Entity.Consultation;
import Entity.Doctor;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Patient;
import Entity.Severity;
import Entity.Visit;
import adt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
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

        } catch (IOException e) {
            System.err.println("Error reading patient CSV file: " + e.getMessage());
        }
    }
    
    public static void loadDoctorsFromCSV(String filePath, DoctorManager docManager) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { // Skip header line
                    isHeader = false;
                    continue;
                }

                // Split by comma, handle quotes if needed
                String[] values = line.split(",");

                String doctorId = values[0].trim();
                String name = values[1].trim();
                int age = Integer.parseInt(values[2].trim());
                String phone = values[3].trim();
                String gender = values[4].trim();
                String position = values[5].trim();
                LocalDate hireDate = LocalDate.parse(values[6].trim(), formatter);

                docManager.addNewDoctor(doctorId, name, age, phone, gender, position, hireDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadConsultRecFromCSV(String filePath, PatientManager patientManager, DoctorManager docManager, LinkedHashMap<String, List<Consultation>> consultLog) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { // skip header
                    isHeader = false;
                    continue;
                }

                String[] values = line.split(",");   
                Patient patient = patientManager.findPatientByIC(values[1]);

                String doctorId = values[3];
                Doctor doc = docManager.findDoctor(doctorId);

                // ===== Extract Consultation =====
                int severity = Integer.parseInt(values[4]);
                String disease = values[5];
                String notes = values[6];

                LocalDateTime consultTime = LocalDateTime.parse(values[7], formatter);
                LocalDateTime apptDateTime = values[8].equals("null") ? null : LocalDateTime.parse(values[8], formatter);
                LocalDateTime createdAt = LocalDateTime.parse(values[9], formatter);

                Consultation c = new Consultation(severity, patient, disease, notes, doc,
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
          } catch (IOException e) {
            System.err.println("Error reading visit CSV file: " + e.getMessage());
        }
    }

     public static void loadMedicineFromCSV(String filePath, MedicineControl medControl) {    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // skip header
                    continue;
                }

                String[] values = line.split(",", -1);
                if (values.length < 4) {
  
                String id = values[0].trim();
                String name = values[1].trim();
                String desc = values[2].trim();
                int stock = Integer.parseInt(values[3].trim());

                medControl.addMedicine(new Medicine(id, name, desc, stock));
            }

        } catch (IOException e) {
            System.err.println("Error reading medicine CSV file: " + e.getMessage());
        }
    }
    
     public static void loadMedRecordFromCSV(String filePath, PatientManager patientManager, DoctorManager docManager, MedicineControl medControl, List<MedRecord> medRecList) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // skip header
                    continue;
                }

                String[] values = line.split(",");

                try {
                    // Patient info
                    Patient patient = patientManager.findPatientByIC(values[0].trim());
                    // Doctor
                    Doctor doctor = docManager.findDoctor(values[1].trim());
                    if (doctor == null) {
                        System.err.println("Doctor not found: " + values[1]);
                        continue;
                    }

                    // Medicine
                    Medicine medicine = medControl.findMedicine(values[2].trim());
                    if (medicine == null) {
                        System.err.println("Medicine not found: " + values[2]);
                        continue;
                    }

                    // Quantity
                    int quantity = Integer.parseInt(values[3].trim());

                    // DateTime (ISO format: 2025-08-05T08:30)
                    LocalDateTime dateTime = LocalDateTime.parse(values[4].trim(), formatter);

                    // Collected
                    boolean collected = Boolean.parseBoolean(values[5].trim());

                    // Create MedRecord and add to list
                    MedRecord record = new MedRecord(patient, doctor, medicine, quantity, dateTime, collected);
                    medRecList.add(record);

                } catch (Exception e) {
                    System.err.println("Skipping invalid row: " + line + " (" + e.getMessage() + ")");
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading medical record CSV file: " + e.getMessage());
        }
    }
     
     public static void loadTreatmentFromCSV(String filePath, TreatmentManager trtManager) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { // Skip header
                    isHeader = false;
                    continue;
                }

                // Split by comma, handle quoted description with commas
                String[] values = line.split(",");

                String treatmentName = values[0].trim();
                String description = values[1].trim();
                String durationStr = values[2].trim(); // e.g., PT30M
                //int frequency = Integer.parseInt(values[3].trim());

                // Convert ISO-8601 duration string to Duration
                Duration duration = Duration.parse(durationStr);

                trtManager.newTreatment(treatmentName, description, duration);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

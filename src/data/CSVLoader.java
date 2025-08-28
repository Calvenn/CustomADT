/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import Control.ConsultationManager;
import Control.DoctorManager;
import Control.StaffManager;
import Control.MedicineControl;
import Control.PatientManager;
import Control.TreatmentManager;
import Control.VisitHistoryManager;
import Control.TreatmentApptManager;
import Entity.Consultation;
import Entity.Doctor;
import Entity.Staff;
import Entity.Staff.Position;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Patient;
import Entity.Treatment;
import Entity.Visit;
import Entity.Severity;
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
                String[] values = line.split(",", 7); 
                if (values.length < 7) {
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                String patientIc = values[0].trim();
                String studentId = values[1].trim();
                String name = values[2].trim();
                String phone = values[3].trim();
                int age = Integer.parseInt(values[4].trim());
                char gender = values[5].trim().charAt(0);
                String address = values[6].trim().replaceAll("^\"|\"$", "");

                // Register into patient manager
                patientManager.registerNewPatient(patientIc, studentId, name, phone, age, gender, address);
            }

        } catch (IOException e) {
            System.err.println("Error reading patient CSV file: " + e.getMessage());
        }
    }
    
    public static void loadDoctorsFromCSV(String filePath, DoctorManager docManager, StaffManager staffManager) {
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

                String Id = values[0].trim();
                String name = values[1].trim();
                int age = Integer.parseInt(values[2].trim());
                String phone = values[3].trim();
                String gender = values[4].trim();
                Position position = Position.valueOf(values[5].trim().toUpperCase());
                String department = values[6].trim();
                LocalDateTime hireDate = LocalDate.parse(values[7].trim(), formatter).atStartOfDay();
                String password = values[8].trim();

                Doctor s = new Doctor(Id, name, age, phone, gender, position, department, hireDate, password);
                docManager.addNewDoctor(s);
                staffManager.addNewStaff(s);
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadStaffFromCSV(String filePath, StaffManager staffManager) {
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

                String Id = values[0].trim();
                String name = values[1].trim();
                int age = Integer.parseInt(values[2].trim());
                String phone = values[3].trim();
                String gender = values[4].trim();
                Position position = Position.valueOf(values[5].trim().toUpperCase());
                LocalDateTime hireDate = LocalDate.parse(values[6].trim(), formatter).atStartOfDay();
                String password = values[7].trim();

                Staff s = new Staff(Id, name, age, phone, gender, position, hireDate, password);
                staffManager.addNewStaff(s);
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
                Patient patient = patientManager.findPatientByIC(values[0]);

                String doctorId = values[1];
                Doctor doc = docManager.findDoctor(doctorId);

                // ===== Extract Consultation =====
                int severity = Integer.parseInt(values[2]);
                String disease = values[3];
                String notes = values[4];

                LocalDateTime consultTime = LocalDateTime.parse(values[5], formatter);
                LocalDateTime apptDateTime = values[6].equals("null") ? null : LocalDateTime.parse(values[6], formatter);
                LocalDateTime createdAt = LocalDateTime.parse(values[7], formatter);

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
                    System.err.println("Skipping invalid line: " + line);
                    continue;
                }

                String id = values[0].trim();
                String name = values[1].trim();
                String desc = values[2].trim();
                int stock = Integer.parseInt(values[3].trim());
                double price = Double.parseDouble(values[4].trim());

                medControl.addMedicine(new Medicine(id, name.toLowerCase(), desc, stock, price));
            }

        } catch (IOException e) {
            System.err.println("Error reading medicine CSV file: " + e.getMessage());
        }
    }
    
     public static void loadMedRecordFromCSV(String filePath, PatientManager patientManager, DoctorManager docManager, MedicineControl medControl, List<MedRecord> medRecList, ConsultationManager consultManager) {
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
                    Medicine medicine = medControl.findMedicineById(values[2].trim());
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
                    
                    Consultation consult = consultManager.getConsultRec(values[6], doctor.getID());
                    
                    if(consult == null){
                        System.err.println("Consult Record not found: " + values[6]);
                        continue;
                    }

                    // Create MedRecord and add to list
                    MedRecord record = new MedRecord(patient, doctor, medicine, quantity, dateTime, collected, consult);
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
                double price = Double.parseDouble(values[3].trim());
                int frequency = Integer.parseInt(values[4].trim());

                // Convert ISO-8601 duration string to Duration
                Duration duration = Duration.parse(durationStr);

                trtManager.newTreatment(treatmentName, description, duration, price, frequency);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public static void loadTreatmentApptFromCSV(String filePath, TreatmentApptManager trtApptManager, DoctorManager docManager, TreatmentManager trtManager, ConsultationManager consultManager) {
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

                Doctor doctor = docManager.findDoctor(values[0].trim());
                String consultId = values[1].trim();
                Treatment treatment = trtManager.findTreatmentName(values[2].trim());
                String room = values[3].trim();

                LocalDateTime apptTime = LocalDateTime.parse(values[4].trim(), formatter);
                LocalDateTime createdAt = LocalDateTime.parse(values[5].trim(), formatter);

                Consultation consultRec = consultManager.getConsultRec(consultId, docManager);
                if (consultRec == null) {
                    System.out.println(consultId + " not found");
                } else {
                    System.out.println("Found consult: " + consultRec.getID());
                    trtApptManager.newTreatmentApptHist(doctor, consultRec, treatment, room, apptTime, createdAt);
                }

                
                //need find consult 
                trtApptManager.newTreatmentApptHist(doctor, consultRec, treatment, room, apptTime, createdAt);
                
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
}
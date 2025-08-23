/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data;

import Control.DoctorManager;
import Control.StaffManager;
import Control.MedicineControl;
import Control.PatientManager;
import Control.TreatmentManager;
import Entity.Consultation;
import Entity.Doctor;
import Entity.Staff;
import Entity.Staff.Position;
import Entity.MedRecord;
import Entity.Medicine;
import Entity.Patient;
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
                String address = values[5].trim();

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

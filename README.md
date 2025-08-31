# Clinic Management System  

This project is a **Clinic Management System** developed in Java.  
It follows the **Entity–Control–Boundary (ECB)** design pattern, and uses various ADTs such as Heap, LinkedHashMap, List, and Queue to manage different aspects of the clinic (patients, staff, appointments, treatments, and pharmacy).  

---

# Project Overview  

The system provides an integrated solution for:  
- Managing patients and their visit histories  
- Handling staff information (doctors, nurses, admins)  
- Managing consultations & appointments  
- Managing treatments and treatment appointments  
- Managing pharmacy operations and medicine records
- Managing Payment functoin  
- Generating reports for decision-making  

---

# Features by Module  

## 1. Patient Management Module  

Manages patient records, walk-in visits, and statistics.  

### Features  
- **CRUD Patient Records** (IC, Name, Age, Gender, Phone, Address)  
- **Walk-In Visit Management** with severity levels (MILD, URGENT, EMERGENCY)  
- **Queue Management** with emergency override  
- **Visit History** and **Reports** (gender distribution, severity, yearly trends)  

### ADT Used  
- Heap  
- LinkedHashMap  
- List  

---

## 2. Staff Management Module  

Handles doctor, nurse, and admin staff details.  

### Features  
- **CRUD Staff Records** (Doctor, Nurse, Admin)  
- **Doctor Availability Management** using Heap  
- **Reports**: Tenure Report, Staff Hiring Report  
- **Role-based Access Control** (admins manage staff, doctors/nurses restricted)  

### ADT Used  
- Heap  
- LinkedHashMap  

---

## 3. Consultation & Appointment Management Module  

Handles consultations, appointments, and reporting.  

### Features  
- **Appointment Scheduling** (create, reschedule, cancel)  
- **Consultation Records** (diagnosis, severity, notes, timestamps)  
- **Consultation Reports** (efficiency, diagnosis trends)  
- **Auto-suggest earliest available timeslot**  
- **Track missed/rescheduled appointments**  

### ADT Used  
- Heap  
- LinkedHashMap  
- List  
- Queue  

---

## 4. Treatment Management Module  

Manages treatments, treatment appointments, and reporting.  

### Features  
- **CRUD Treatments** (name, description, duration, price)  
- **Treatment Appointments** (book, cancel, complete, search)  
- **Treatment Reports**: frequency, time allocation, revenue  
- **Integration with Consultation Module**: consult doctors can direct patients to treatment  

### ADT Used  
- Heap  
- LinkedHashMap  
- List  

---

## 5. Pharmacy Management Module  

Manages medicines, stock, dispensing, and revenue analysis.  

### Features  
- **CRUD Medicine Records** (ID, name, description, price, stock)  
- **Stock Management** (add/remove stock, low-stock alerts)  
- **Dispense Medicine** (with quantity tracking)  
- **Medicine Records** (search by patient, doctor, medicine)  
- **Reports**: Low Usage Medicine, Revenue, Dispense Summary  

### ADT Used
- Heap
- LinkedHashMap  
- List

---

#  Module Design (ECB Pattern)  

Each module follows the **Entity – Control – Boundary** structure:  

- **Entity** → Core data objects (Patient, Staff, Appointment, Consultation, Treatment, Medicine, etc.)  
- **Control** → Handles business logic (Managers for CRUD, Queue, Reports)  
- **Boundary** → User Interfaces (console-based menus for interaction)  

---

# Reports Provided  

- Patient gender distribution  
- Visit severity distribution & yearly visit counts  
- Staff tenure and hiring trend reports  
- Consultation efficiency report  
- Diagnosis trends  
- Treatment frequency, time allocation, and revenue  
- Pharmacy revenue and low-usage medicine  

---

# How to Run  

1. Clone or download the project.  
2. Open in any Java IDE (e.g., IntelliJ, Eclipse, NetBeans).  
3. Run the `Main.java` file.  
4. Navigate through the **Main Menu** to access modules:  

### Main Menu Options  (LogIn as Doctor)
- **Option 1** → Consultatoin & Appointment Management System
- **Option 2** → Treatment Management System
- **Option 3** → Treatment Appointment System
- **Option 4** → Pharmacy System (Read Only)
- **Option 5** → Staff Management System

### Main Menu Options  (LogIn as Admin)
- **Option 1** → Consultatoin (Read Only) & Appointment Management System
- **Option 2** → Treatment Management System
- **Option 3** → Treatment Appointment System
- **Option 4** → Pharmacy System (Read Only)
- **Option 5** → Patient Registration System
- **Option 6** → Payment System
- **Option 7** → Staff Management System

### Main Menu Options  (LogIn as Nurse)
- **Option 1** → Consultatoin (Read Only) & Appointment Management System
- **Option 2** → Pharmacy Control System
- **Option 3** → Patient Registration System

---

# Common Input Formats  

| Field              | Format Example           |  
|--------------------|--------------------------|  
| IC                 | `xxxxxx-xx-xxxx` (050205-07-0228) |  
| Student ID         | `xxABCxxxxx` (22PMR12345) |  
| Phone Number       | `xxx-xxxxxxx` (012-3456789) |  
| Staff ID           | `Axxxx / Dxxxx / Nxxxx` (A0001, D0001, N0001) |  
| Visit ID           | `Vxxxx` (V1000) |  
| Consultation ID    | `Cxxxx` (C0001) |  
| Treatment ID       | `Txxxx` (T0001) |  
| Treatment Appt ID  | `Rxxxx` (R0001) |  
| Medicine ID        | `Mxxxx` (M0001) |  
| Date               | `yyyy-MM-dd` (2025-08-29) |  
| Date-Time          | `yyyy-MM-dd HH:mm` (2025-08-29 11:15) |  

---

# Notes  

- IDs are **auto-generated** with prefixes depending on entity (V, C, T, R, M, A, D, N).  
- **Custom Exception Handling** is used for input validation (date-time, working hours, past time).  
- **Role-based Access** ensures that only authorized users can manage staff or treatments.  
- **Missed Appointments** are automatically tracked if patients fail to arrive within 15 minutes.  

---

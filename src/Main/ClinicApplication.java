/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Boundary.*;
import Control.*;
import Entity.*;
import adt.*;
import data.CSVLoader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**f
 *
 * @author calve
 */
public class ClinicApplication {
    // Shared ADTs
    private final Heap<Doctor> sharedDoc = new Heap<>(false);
    private final Heap<Visit> sharedVisitQueue = new Heap<>(true);
    private final Heap<Appointment> apptQueue = new Heap<>(false);
    private final LinkedHashMap<String,Treatment> providedTreatments = new LinkedHashMap<>();
    private final LinkedHashMap<String,Medicine> medMap = new LinkedHashMap<>();
    private final LinkedHashMap<String,Doctor> doctorLookup = new LinkedHashMap<>();
    private final LinkedHashMap<String,Staff> staffLookup = new LinkedHashMap<>();
    private final List<MedRecord> medRecList = new List<>(); 
    private final LinkedHashMap<String, List<TreatmentAppointment>> trtApptHistory = new LinkedHashMap<>();
    private final Queue<TreatmentAppointment> treatmentQueue = new Queue<>();
    private final Queue<MedRecord> medCollectQueue = new Queue<>(); 
    private final LinkedHashMap<String, Queue<Appointment>> missAppt = new LinkedHashMap<>();  
    private final LinkedHashMap<String, List<Consultation>> consultLog = new LinkedHashMap<>();

    // Control Layer
    private final PatientManager patientManager;
    private final DoctorManager docManager;
    private final StaffManager staffManager;
    private final QueueManager queueManager;
    private final AppointmentManager apptManager;
    private final ConsultationManager consultManager;
    private final ConsultationReport consultReport;
    private final TreatmentManager trtManager;
    private final TreatmentApptManager treatmentApptManager;
    private final MedicineControl medControl;
    private final MedRecordControl medRecControl;
    private final PharmacyReport pharReport;
    private final VisitHistoryManager historyManager;
    private final PaymentManager paymentManager;

    // UI Layer
    private final ConsultationUI consultUI;
    private final PatientManagementUI patientUI;
    private final StaffManagementUI staffUI;
    private final StaffLogin staffLogin; 
    private final TreatmentUI treatmentUI;
    private final TreatmentApptUI treatmentApptUI;
    private final PharmacyUI pharUI;
    private final PaymentUI payUI;

    public ClinicApplication() {
        patientManager = new PatientManager();
        docManager = new DoctorManager(doctorLookup, sharedDoc);
        staffManager = new StaffManager(staffLookup);
        historyManager = new VisitHistoryManager();
        queueManager = new QueueManager(sharedVisitQueue, apptQueue, docManager, consultLog, historyManager);
        apptManager = new AppointmentManager(missAppt, consultLog, apptQueue,docManager);
        consultManager = new ConsultationManager(sharedVisitQueue,apptManager.getAppointmentHeap(),
        consultLog, trtApptHistory, medRecList,docManager,apptManager);
        consultReport = new ConsultationReport(consultLog, apptManager);
        trtManager = new TreatmentManager(providedTreatments);
        treatmentApptManager = new TreatmentApptManager(trtApptHistory);
        medControl = new MedicineControl(medMap);
        medRecControl = new MedRecordControl(medRecList);
        paymentManager = new PaymentManager(treatmentQueue, medCollectQueue);
    
        patientUI = new PatientManagementUI(queueManager, patientManager, historyManager);
        treatmentUI = new TreatmentUI(trtManager);
        treatmentApptUI = new TreatmentApptUI(treatmentApptManager, docManager, treatmentUI);
        consultUI = new ConsultationUI(docManager, apptManager, consultManager, trtManager, medControl, consultReport, treatmentApptUI);
        pharReport = new PharmacyReport(medRecList,medMap);
        pharUI = new PharmacyUI(medRecControl, medControl, medCollectQueue, pharReport);
        staffUI = new StaffManagementUI(staffManager, docManager);
        payUI = new PaymentUI(paymentManager);
        staffLogin = new StaffLogin(queueManager, staffManager, consultUI, treatmentUI, treatmentApptUI, pharUI, patientUI, staffUI, payUI);

    }
    
    public void run(){
        CSVLoader.loadPatientFromCSV("src/data/patients.csv", patientManager);
        CSVLoader.loadDoctorsFromCSV("src/data/doctor.csv", docManager, staffManager);
        CSVLoader.loadVisitHistoryFromCSV("src/data/visits.csv", patientManager, docManager, historyManager);
        CSVLoader.loadStaffFromCSV("src/data/staff.csv", staffManager);
        CSVLoader.loadConsultRecFromCSV("src/data/consultation.csv", patientManager, docManager, consultLog);
        CSVLoader.loadTreatmentFromCSV("src/data/treatment.csv", trtManager);
        CSVLoader.loadTreatmentApptFromCSV("src/data/treatmentAppt.csv", treatmentApptManager, docManager, trtManager, consultManager);
        CSVLoader.loadMedicineFromCSV("src/data/medicine.csv", medControl);
        CSVLoader.loadMedRecordFromCSV("src/data/medicineRec.csv", patientManager, docManager, medControl, medRecList, consultManager);
        staffLogin.login();

    }
}
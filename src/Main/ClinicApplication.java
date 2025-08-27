/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import Boundary.StaffLogin;
import Boundary.*;
import Control.*;
import Entity.*;
import adt.*;
import data.CSVLoader;

/**f
 *
 * @author calve
 */
public class ClinicApplication {
    // Shared ADTs
    private final Heap<Doctor> sharedDoc = new Heap<>(false);
    private final Heap<Visit> sharedVisitQueue = new Heap<>(true);
    private final LinkedHashMap<String,Treatment> providedTreatments = new LinkedHashMap<>();
    private final LinkedHashMap<String,Medicine> medMap = new LinkedHashMap<>();
    private final LinkedHashMap<String,Doctor> doctorLookup = new LinkedHashMap<>();
    private final LinkedHashMap<String,Staff> staffLookup = new LinkedHashMap<>();
    private final List<MedRecord> medRecList = new List<>(); 
    private final LinkedHashMap<String, TreatmentAppointment> trtApptHistory = new LinkedHashMap<>();
    private final Queue<TreatmentAppointment> treatmentQueue = new Queue<>();
    private final Queue<MedRecord> medCollectQueue = new Queue<>(); 
    private final LinkedHashMap<String, Queue<Appointment>> missAppt = new LinkedHashMap<>();  
    private final LinkedHashMap<String, List<Consultation>> consultLog = new LinkedHashMap<>();
    private final List<Payment> paymentRec = new List<>();

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
        queueManager = new QueueManager(sharedVisitQueue, docManager, consultLog, historyManager);
        apptManager = new AppointmentManager(missAppt, consultLog, docManager, queueManager);
        consultManager = new ConsultationManager(sharedVisitQueue,apptManager.getAppointmentHeap(),
        consultLog, trtApptHistory, medRecList,docManager,apptManager, paymentRec);
        consultReport = new ConsultationReport(consultLog, apptManager);
        trtManager = new TreatmentManager(providedTreatments);
        treatmentApptManager = new TreatmentApptManager(trtApptHistory);
        medControl = new MedicineControl(medMap);
        medRecControl = new MedRecordControl(medRecList);
        paymentManager = new PaymentManager(paymentRec, treatmentQueue, medCollectQueue);

        consultUI = new ConsultationUI(docManager, apptManager, consultManager, trtManager, medControl, consultReport);
        patientUI = new PatientManagementUI(queueManager, patientManager, historyManager);
        treatmentUI = new TreatmentUI(trtManager);
        treatmentApptUI = new TreatmentApptUI(treatmentApptManager);
        pharReport = new PharmacyReport(medRecList,medMap);
        pharUI = new PharmacyUI(medRecControl, medControl, medCollectQueue, pharReport);
        staffUI = new StaffManagementUI(staffManager, docManager);
        payUI = new PaymentUI(paymentManager);
        staffLogin = new StaffLogin(queueManager, staffManager, consultUI, treatmentUI, pharUI, patientUI, staffUI, payUI); 
    }
    
    public void run(){
        CSVLoader.loadPatientFromCSV("src/data/patients.csv", patientManager);
        CSVLoader.loadDoctorsFromCSV("src/data/doctor.csv", docManager, staffManager);
        CSVLoader.loadStaffFromCSV("src/data/staff.csv", staffManager);
        CSVLoader.loadConsultRecFromCSV("src/data/consultation.csv", patientManager, docManager, consultLog);
        CSVLoader.loadTreatmentFromCSV("src/data/treatment.csv", trtManager);
        CSVLoader.loadMedicineFromCSV("src/data/medicine.csv", medControl);
        CSVLoader.loadMedRecordFromCSV("src/data/medicineRec.csv", patientManager, docManager, medControl, medRecList, consultManager);
        CSVLoader.loadTreatmentApptFromCSV("src/data/treatmentAppt.csv", treatmentApptManager, docManager, trtManager, consultManager);
        staffLogin.login();
    }
}
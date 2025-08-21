package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author calve
 */
public class Main {
        
    public static void main(String[] args) {
<<<<<<< HEAD
        ClinicApplication app = new ClinicApplication(); //WHY USE THIS: BECAUSE HERE I CANNOT DIRECT CALL FUNCTION IT IS STATIC, BUT OTHER FUNC NOT STATIC
        app.run();
=======
        Scanner scanner = new Scanner(System.in);
        Heap<Doctor> sharedDoc = new Heap<>(false);
        Heap<Visit> sharedVisitQueue = new Heap<>(true);
        //Heap<Appointment> consultAppt = new Heap<>(false);
        Heap<Treatment> providedTreatments = new Heap<>(true);
        LinkedHashMap<String,Medicine> medMap = new LinkedHashMap<>();
        List<MedRecord> medRecList = new List<>();
        
        Queue<TreatmentAppointment> treatmentQueue = new Queue<>();
        Queue<MedRecord> medCollectQueue = new Queue<>();
        
        LinkedHashMap<String, Queue<Appointment>> missAppt = new LinkedHashMap<>();  
        LinkedHashMap<String, List<Consultation>> consultLog = new LinkedHashMap<>();
           
        PatientManager patientManager = new PatientManager();
        DoctorManager docManager = new DoctorManager(sharedDoc);
        QueueManager queueManager = new QueueManager(sharedVisitQueue, docManager, consultLog);
        AppointmentManager apptManager = new AppointmentManager(missAppt, consultLog, docManager, queueManager);
        ConsultationManager consultManager = new ConsultationManager(sharedVisitQueue, apptManager.getAppointmentHeap(), docManager, consultLog, treatmentQueue, medCollectQueue, apptManager);
        TreatmentManager trtManager = new TreatmentManager(providedTreatments); 
        MedicineControl medControl = new MedicineControl(medMap);
        MedRecordControl medRecControl = new MedRecordControl(medRecList);
        
        loadDummyDoctors(docManager);
        loadDummyTreatment(trtManager);
        loadDummyMed(medControl);
        loadDummyMedRec(medRecControl, docManager, medControl);
        //loadDummyAppt(apptManager, docManager);
        loadDummyConsult(consultManager, docManager, consultLog);
        
        ConsultationReport consultReport = new ConsultationReport(consultLog);

        ConsultationUI consultUI = new ConsultationUI(docManager, apptManager, consultManager, trtManager, medControl);
        PatientManagementUI patientUI = new PatientManagementUI(queueManager);
        ReportUI reportUI = new ReportUI(consultReport);
        PharmacyUI pharUI = new PharmacyUI(medRecControl, medControl);

        int choice;
        do {
            while (true) {
            queueManager.loadVisit();
            displayMainMenu();
                try {
                    System.out.print("Enter your choice: ");
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 1 || choice > 8) {
                        System.out.println("Invalid choice. Please enter 1 to 3.");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            switch (choice) {
                case 1 -> patientUI.patientMenu();
                //case 2 -> consultUI.consultMainMenu();
                case 3 -> {
                    System.out.println("Treatment queue: " + treatmentQueue.size());
                    System.out.println("Med Collection queue: " + medCollectQueue.size());
                    consultUI.consultMainMenu();
                }
                //case 4 -> consultUI.consultMainMenu();
                case 5 -> pharUI.pharmacyMenu();
                case 6 -> reportUI.reportMainMenu();
                case 7 -> {
                    System.out.println("Thank you for using the Clinic System!");
                    System.exit(0);
                }
            }
        } while (choice != 6);
>>>>>>> 64392b2379cc37b93157dff6075452b4084e9649
    }
    
    

}



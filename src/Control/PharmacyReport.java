/*
 * PharmacyReportControl.java
 */
package Control;

import Entity.MedRecord;
import Entity.Patient;
import Entity.Medicine;
import adt.List;
import adt.LinkedHashMap;
import java.util.Scanner;

public class PharmacyReport {
    private final List<MedRecord> medRecList;
    
    public PharmacyReport(List<MedRecord> medRecList){
        this.medRecList = medRecList;
    }
}


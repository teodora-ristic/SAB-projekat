package domaci_zadatak;


import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestRunner;
import rs.etf.sab.tests.TestHandler;
import student.rt200436_CityOperations;
import student.rt200436_CourierOperations;
import student.rt200436_CourierRequestOperation;
import student.rt200436_DistrictOperations;
import student.rt200436_GeneralOperations;
import student.rt200436_PackageOperations;
import student.rt200436_UserOperations;
import student.rt200436_VehicleOperations;


public class StudentMain {

    public static void main(String[] args) {
        
        CityOperations cityOperations = new rt200436_CityOperations(); 
        DistrictOperations districtOperations = new rt200436_DistrictOperations(); 
        CourierOperations courierOperations = new rt200436_CourierOperations(); 
        CourierRequestOperation courierRequestOperation = new rt200436_CourierRequestOperation();
        GeneralOperations generalOperations = new rt200436_GeneralOperations();
        UserOperations userOperations = new rt200436_UserOperations();
        VehicleOperations vehicleOperations = new rt200436_VehicleOperations();
        PackageOperations packageOperations = new rt200436_PackageOperations();
       
        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();  

  }
}
    
    


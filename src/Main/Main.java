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
        ClinicApplication app = new ClinicApplication(); //WHY USE THIS: BECAUSE HERE I CANNOT DIRECT CALL FUNCTION IT IS STATIC, BUT OTHER FUNC NOT STATIC
        app.run();
    }
    
    public static void clearScr(){
        System.out.println("\033c");
    }
}



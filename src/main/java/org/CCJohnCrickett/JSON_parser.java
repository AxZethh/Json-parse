package org.CCJohnCrickett;


import java.io.File;
import java.util.Scanner;



public class JSON_parser {
    public static void main(String[] args) {

        /// STEP 1 AND 2 ARE DONE!
        JSONCheck jsonCheck = new JSONCheck();
        System.out.println("Please enter your file Location: ");
        Scanner scanner = new Scanner(System.in);
        File inputFile;
        inputFile = new File(scanner.nextLine());
        scanner.close();

        if(inputFile.isFile()  && jsonCheck.valueOfJSON(inputFile)) {
            System.out.println(jsonCheck.getJSONKeyValue());
            System.out.println("JSON parsing Complete!");
            System.exit(0);
        } else {
            System.out.println(jsonCheck.getJSONKeyValue());
            System.out.println("No objects found or Invalid JSON provided!");
            System.exit(1);
        }

    }

    //C:\Users\SSink\Desktop\Ideas\tests\step3/valid.json

}
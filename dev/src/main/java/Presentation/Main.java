package Presentation;

import Data.SQLDB;
import Domain.ReadData2System;

import java.io.*;

public class Main {

    public static void main(String[] args) {
    try {
//        SQLDB.deleteAllData2();
////        SQLDB.dropAllTables2();
//       boolean b = createDB();
//            SQLDB.createTables2();
//            //add data
//            ReadData2System.readConfigurtionFile(null);
//            ReadData2System.readWorkersData(null);
//            ReadData2System.readWorkersRolesData(null);
//            ReadData2System.readPrevWorkersData();
//            ReadData2System.readShifts(null);
//            ReadData2System.readAvailability(null);

        Presentation.Login();
    }
    catch(Exception e){
        System.out.println(e.getMessage());

    }
    }

        private static boolean createDB() {
        // if directory does not exist, create it
        File directory = new File("src/");
        if (!directory.exists())
            directory.mkdir();
        directory = new File("src/main/");
        if (!directory.exists())
            directory.mkdir();
        directory = new File("src/main/resources/");
        if (!directory.exists())
            directory.mkdir();

        String dbPath = "src/main/resources/MYDB.db";
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try (InputStream in = Main.class.getResourceAsStream("/MYDB.db");
                 OutputStream out = new FileOutputStream(dbPath)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}

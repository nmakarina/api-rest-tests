package utils;

import enums.Server;
import enums.User;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utility {

    public static Server getServer() {
        Server server = Server.DEMO;
        //TODO: раскомментировать для CI/CD
        /**String name = System.getProperty("AT_SERVER");
        if (name == null || name.isEmpty()) {
            return server;
        }
        return Server.valueOf(name);**/
        return server;
    }

    public static User getUser() {
        User user = User.USER;
        //TODO: раскомментировать для CI/CD
        /**String name = System.getProperty("AT_USER");
        if (name == null || name.isEmpty()) {
            return user;
        }
        return User.valueOf(name);**/
        return user;
    }


    public static String randomName(String prefix){
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        for(int i = 0; i < 6; ++i){
            prefix += abc.charAt(rand.nextInt(25));
        }
        return prefix;
    }
    public static String newExcelFile() throws IOException {
        String filePath = "src/test/resources/userlist.xlsx";
        InputStream excelFileToRead = new FileInputStream(filePath);
        XSSFWorkbook wb = new XSSFWorkbook(excelFileToRead);
        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
        XSSFSheet sheet = wb.getSheetAt(0);

        String newValue = (System.currentTimeMillis() + "").substring(2);
        System.setProperty("newValue", newValue);

        sheet
                .getRow(1)
                .getCell(0)
                .setCellValue(newValue);

        FileOutputStream out = new FileOutputStream(new File(filePath));
        wb.write(out);
        out.close();

        return filePath;
    }

}

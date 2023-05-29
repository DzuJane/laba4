/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.mavenproject2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
/**
 *
 * @author Дзюба
 */
public class ReadFile{
    public void ReadXLSX(Storage s, String fileName, int variant) throws FileNotFoundException, IOException, InvalidFormatException{
        ArrayList<ArrayList<Double>> samples = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        File file = new File(fileName);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet worksheet = workbook.getSheetAt(variant-1);
        int cols = worksheet.getRow(0).getLastCellNum();
        for (int i = 0; i < cols; ++i){
            samples.add(new ArrayList<>());
        }
        Iterator<Row> ri = worksheet.rowIterator();
        XSSFRow row1 = (XSSFRow) ri.next();
        Iterator<Cell> ci1 = row1.cellIterator();
        while(ci1.hasNext()) {
            XSSFCell cell = (XSSFCell) ci1.next();
            name.add(cell.getStringCellValue());
        }
        
        while(ri.hasNext()) {
            XSSFRow row = (XSSFRow) ri.next();
            Iterator<Cell> ci = row.cellIterator();
            int i = 0;
            while(ci.hasNext()) {
                XSSFCell cell = (XSSFCell) ci.next();
                System.out.println(cell.getNumericCellValue());
                samples.get(i).add(cell.getNumericCellValue());
                i++;
            }                
        }        
        s.setName(name);
        s.setSamples(samples);       
    }
    
    private void AddNamesRow(Row row, ArrayList<String> nameSample){
        row.createCell(0).setCellValue("Параметры");
        int numbCell = 1;
        for (String s : nameSample){
            Cell nameTemp = row.createCell(numbCell++);
            nameTemp.setCellValue(s);        
        }
        
        
        
    }
 
  /* public static void writeResultsToExcel(Storage s, String fileName) throws IOException {
       ArrayList<ArrayList<Object>> results = s.getResult();
       XSSFWorkbook workbook = new XSSFWorkbook(); // создаем новую книгу Excel
      XSSFSheet sheet = workbook.createSheet("Результаты"); // создаем новую страницу в книге

      int rowNum = 0;
      for (ArrayList<Object> rowData : results) {
         XSSFRow row = sheet.createRow(rowNum++); // создаем новую строку для каждой записи данных
        
         int colNum = 1;
         for (Object field : rowData) {
            Cell cell = row.createCell(colNum++); // создаем ячейку для каждого поля
            if (field instanceof String) {
               cell.setCellValue((String) field);
            } else if (field instanceof Integer) {
               cell.setCellValue((Integer) field);
            } else if (field instanceof Double) {
               cell.setCellValue((Double) field);
            }
         } 
        //sheet.getRow(1).createCell(1).setCellValue(String.valueOf("X:")); // записываем значение в ячейку
        //sheet.getRow(2).createCell(1).setCellValue(String.valueOf("Y:"));
        //sheet.getRow(3).createCell(1).setCellValue(String.valueOf("Z:"));
      }

      // настраиваем ширину столбцов, чтобы данные в них помещались полностью
      for (int i = 0; i < results.get(0).size(); i++) {
         sheet.autoSizeColumn(i);
      }

      // записываем книгу Excel в файл
      FileOutputStream outputStream = new FileOutputStream(fileName);
      workbook.write(outputStream);
      workbook.close();
   }
 }
*/
    public static void writeResultsToExcel(Storage s, String fileNames) throws IOException {
         ArrayList<ArrayList<Object>> results = s.getResult();
      XSSFWorkbook workbook = new XSSFWorkbook(); // создаем новую книгу Excel
      XSSFSheet sheet = workbook.createSheet("Результаты"); // создаем новую страницу в книге

      XSSFRow headerRow = sheet.createRow(0); // создаем заголовок в первой строке
      /*headerRow.createCell(0).setCellValue("X");
      headerRow.createCell(1).setCellValue("Y");
      headerRow.createCell(2).setCellValue("Z");
      
*/ 
      
      int r = 0;
      for(int i = 1; i<(s.getNameParam()).length+1;i++)
      {
           String [] m = s.getNameParam();
           headerRow.createCell(i).setCellValue(m[i-1]);
           r = i;
      }
      for(int i = 1; i<(s.getNameParam2()).length+1;i++)
      {
           String [] a = s.getNameParam2();
           headerRow.createCell(r+1).setCellValue(a[i-1]);
           r++;
      }
      
      
      int rowNum = 1;
      /*XSSFRow row2 = sheet.createRow(1);
      row2.createCell(0).setCellValue("X");
      XSSFRow row3 = sheet.createRow(2);
      row3.createCell(0).setCellValue("Y");
      XSSFRow row4 = sheet.createRow(3);
      row4.createCell(0).setCellValue("Z");*/
      for (ArrayList<Object> rowData : results) {
         XSSFRow row = sheet.createRow(rowNum++); // создаем новую строку для каждой записи данных
         int colNum = 1; 
         if (rowNum == 2)  
           {
          row.createCell(0).setCellValue("X");
           }
            if (rowNum == 3)  
           {
          row.createCell(0).setCellValue("Y");
           }
             if (rowNum == 4)  
           {
                row.createCell(0).setCellValue("Z");
           }
         for (Object field : rowData) {
           
          Cell cell = row.createCell(colNum++); // создаем ячейку для каждого поля
          
          cell.setCellValue(String.valueOf(field)); // записываем значение в ячейку
            
         }
            
      }
       
         
         
      // настраиваем ширину столбцов, чтобы данные в них помещались полностью
      for (int i = 0; i < results.get(0).size(); i++) {
         sheet.autoSizeColumn(i);
      }

      // записываем книгу Excel в файл
      FileOutputStream outputStream = new FileOutputStream(fileNames);
      workbook.write(outputStream);
      workbook.close();
   }
}

package com.dmss.dmssadminmaintanance.xcelsheet;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


import com.dmss.dmssadminmaintanance.R;
import com.dmss.dmssadminmaintanance.db.FemaleRestRoomTasks;
import com.dmss.dmssadminmaintanance.db.PantryTasks;
import com.dmss.dmssadminmaintanance.db.RestRoomTasks;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Excel Worksheet Utility Methods
 * <p>
 * Created by: Ranit Raj Ganguly on 16/04/21.
 */
public class ExcelUtils {
    public static final String TAG = "ExcelUtil";
    private static Cell cell;
    private static Sheet sheet;
    private static Sheet sheet1;

    private static Workbook workbook;
    private static CellStyle headerCellStyle;
    private static CellStyle CellsCellStyle;
    private static CellStyle headerAssignedCellStyle;
    private static String name="Name";
    private static String created_date="Date";
    private static String pantry="Pantry";
    private static String restRoom="Rest Room";
    private static String mCategory="";
    private static String male="Male";
    private static String female="Female";

    private static List<PantryTasks> importedExcelData;
    private static Row pantry_headerRow;
    /**
     * Import data from Excel Workbook
     *
     * @param context - Application Context
     * @param fileName - Name of the excel file
     * @return importedExcelData
     */
    public static List<PantryTasks> readFromExcelWorkbook(Context context, String fileName) {
        return retrieveExcelFromStorage(context, fileName);
    }


    /**
     * Export Data into Excel Workbook
     *
     * @param context  - Pass the application context
     * @param fileName - Pass the desired fileName for the output excel Workbook
     * @param dataList - Contains the actual data to be displayed in excel
     */
    public static boolean exportPantryDataIntoWorkbook(Context context, String fileName,String selectedDate,
                                                       Map<String,Object> dataList, Map<String,Object> femaledataList,ArrayList<String> columns,
                                                       ArrayList<String> timings,String category) {
        boolean isWorkbookWrittenIntoStorage;
        mCategory =category;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();
        setCellsCellStyle();
        setHeaderAssignedCellStyle();

        // Creating a New Sheet and Setting width for each column
        if(category.equalsIgnoreCase(context.getString(R.string.rest_rooms))) {
            sheet = workbook.createSheet("Male");

            sheet.setColumnWidth(0, (15 * 400));
            sheet.setColumnWidth(1, (15 * 400));

            sheet1 = workbook.createSheet("Female");
            sheet1.setColumnWidth(0, (15 * 400));
            sheet1.setColumnWidth(1, (15 * 400));

            if(!dataList.isEmpty()) {
                fillPantryDataIntoExcel(dataList, timings, columns, sheet, male);
            }
            if(!femaledataList.isEmpty()) {
                fillPantryDataIntoExcel(femaledataList, timings, columns, sheet1, female);
            }

        }else {
            sheet = workbook.createSheet(selectedDate);
            sheet.setColumnWidth(0, (15 * 400));
            sheet.setColumnWidth(1, (15 * 400));
            fillPantryDataIntoExcel(dataList,timings,columns,sheet,pantry);

        }



      /*  ArrayList<String> cellRowNames= new ArrayList<>();
        for(int i =0;i<dataList.get("10:00").size();i++){
            if(!cellRowNames.contains(dataList.get("10:00").get(i).getTask_name())) {
                cellRowNames.add(dataList.get("10:00").get(i).getTask_name());
            }
        }
*/

        isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);

        return isWorkbookWrittenIntoStorage;
    }
    public static boolean exportRestRoomDataIntoWorkbook(Context context, String fileName, String selectedDate,
                                                         Map<String,List<RestRoomTasks>> dataList, Map<String,List<FemaleRestRoomTasks>> femaledataList, ArrayList<String> columns,
                                                         ArrayList<String> timings) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();
       setCellsCellStyle();
        setHeaderAssignedCellStyle();

        // Creating a New Sheet and Setting width for each column
        sheet = workbook.createSheet("Male");
        sheet.setColumnWidth(0, (15 * 400));
        sheet.setColumnWidth(1, (15 * 400));

        sheet1 = workbook.createSheet("Female");
        sheet1.setColumnWidth(0, (15 * 400));
        sheet1.setColumnWidth(1, (15 * 400));

        setRestRoomHeader(columns,timings,sheet);
        setRestRoomHeader(columns,timings,sheet1);

        fillRestRoomDataIntoExcel(dataList,timings,columns,sheet);
        fillFemaleRestRoomDataIntoExcel(femaledataList,timings,columns,sheet1);

        isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);

        return isWorkbookWrittenIntoStorage;
    }
    /**
     * Checks if Storage is READ-ONLY
     *
     * @return boolean
     */
    private static boolean isExternalStorageReadOnly() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(externalStorageState);
    }

    /**
     * Checks if Storage is Available
     *
     * @return boolean
     */
    private static boolean isExternalStorageAvailable() {
        String externalStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(externalStorageState);
    }

    /**
     * Setup header cell style
     */
    private static void setHeaderCellStyle() {
        headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }
    private static void setCellsCellStyle() {
        CellsCellStyle = workbook.createCellStyle();
//        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index);
//        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        CellsCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }
    private static void setHeaderAssignedCellStyle() {
        headerAssignedCellStyle = workbook.createCellStyle();
        headerAssignedCellStyle.setFillForegroundColor(HSSFColor.RED.index);
        headerAssignedCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerAssignedCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
    }
    /**
     * Setup Header Row
     */
    private static void setRestRoomHeader(ArrayList<String> columsList,ArrayList<String> timingsList,Sheet sheet){

        pantry_headerRow = sheet.createRow(0);
        cell = pantry_headerRow.createCell(0);
        cell.setCellValue("Time");
        cell.setCellStyle(headerCellStyle);
        for (int i=0;i<columsList.size();i++) {
            sheet.setColumnWidth(i+1, (10 * 400));
//            Row headerRow = sheet.createRow(i+1);
            cell = pantry_headerRow.createCell(i+1);
            cell.setCellValue(columsList.get(i));
            cell.setCellStyle(headerCellStyle);
        }
      /*  cell = pantry_headerRow.createCell(columsList.size()+1);
        cell.setCellValue("Assign To");*/
//        cell.setCellStyle(headerAssignedCellStyle);


    }
    private static void setPantryHeaderRow(String fromTable,String column0,String column1) {
        Row headerRow = sheet.createRow(0);
        cell = headerRow.createCell(0);
        cell.setCellValue(column0);
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue(column1);
        cell.setCellStyle(headerCellStyle);
            cell = headerRow.createCell(2);
            cell.setCellValue("Assigned");
            cell.setCellStyle(headerCellStyle);

            cell = headerRow.createCell(3);
            cell.setCellValue("Completed");
            cell.setCellStyle(headerCellStyle);

    }
   /* private static void fillPantryDataIntoExcel(List<PantryTasks> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + 1);

            // Create Cells for each row
            cell = rowData.createCell(0);
            cell.setCellValue(dataList.get(i).getTask_name());

            cell = rowData.createCell(1);
            cell.setCellValue(dataList.get(i).getCreated_date());

            cell = rowData.createCell(2);
            if(dataList.get(i).isAssigned()) {
                cell.setCellValue("Yes");
            }else{
                cell.setCellValue("No");

            }

            cell = rowData.createCell(3);
            if(dataList.get(i).isCompleted()) {
                cell.setCellValue("Yes");
            }else{
                cell.setCellValue("No");

            }

        }
    }*/
    private static void fillRestRoomDataIntoExcel(Map<String,List<RestRoomTasks>> dataList,ArrayList<String> timingsList,ArrayList<String> columns,Sheet sheet) {

        for ( int i=0;i<timingsList.size();i++ ) {
            System.out.println( timingsList.get(i) );
            Row rowData1 = sheet.createRow(i+1);

            cell = rowData1.createCell(0);
            cell.setCellValue(timingsList.get(i));
            cell.setCellStyle(CellsCellStyle);

            cell = rowData1.createCell(columns.size()+1);


            for ( String key : dataList.keySet() ) {

                if(key.equalsIgnoreCase(timingsList.get(i))) {
                    cell.setCellValue(dataList.get(key).get(0).getAssignedTo());
                    cell.setCellStyle(CellsCellStyle);
                    List<RestRoomTasks> listData = dataList.get(key);
                    insertData(listData, rowData1);
                }
            }


        }

    }
    private static void fillFemaleRestRoomDataIntoExcel(Map<String,List<FemaleRestRoomTasks>> dataList,ArrayList<String> timingsList,ArrayList<String> columns,Sheet sheet) {

        for ( int i=0;i<timingsList.size();i++ ) {
            System.out.println( timingsList.get(i) );
            Row rowData1 = sheet.createRow(i+1);

            cell = rowData1.createCell(0);
            cell.setCellValue(timingsList.get(i));
            cell.setCellStyle(CellsCellStyle);

            cell = rowData1.createCell(columns.size()+1);


            for ( String key : dataList.keySet() ) {

                if(key.equalsIgnoreCase(timingsList.get(i))) {
                    cell.setCellValue(dataList.get(key).get(0).getAssignedTo());
                    cell.setCellStyle(CellsCellStyle);
                    List<FemaleRestRoomTasks> listData = dataList.get(key);
                    insertData1(listData, rowData1);
                }
            }


        }

    }
    private static void fillPantryDataIntoExcel(Map<String,Object> dataList,ArrayList<String> timingsList,ArrayList<String> columns,Sheet sheet,String category) {
        setRestRoomHeader(columns,timingsList,sheet);

        for ( int i=0;i<timingsList.size();i++ ) {
            System.out.println( timingsList.get(i) );
            Row rowData1 = sheet.createRow(i+1);

            cell = rowData1.createCell(0);
            cell.setCellValue(timingsList.get(i));
            cell.setCellStyle(CellsCellStyle);

            cell = rowData1.createCell(columns.size()+1);


            for ( String key : dataList.keySet() ) {

                if(key.equalsIgnoreCase(timingsList.get(i))) {
              /*      cell.setCellValue(dataList.get(key).get(0).getAssignedTo());
                    cell.setCellStyle(CellsCellStyle);*/
                    List<Object> listData = (List<Object>) dataList.get(key);
                    if(category.equalsIgnoreCase(pantry)) {
                        insertPantryTasksData(listData, rowData1, columns);
                    }else if(category.equalsIgnoreCase(male)){
                        insertMaleRestRoomTasksData(listData, rowData1, columns);

                    }else if(category.equalsIgnoreCase(female)){
                        insertFeMaleRestRoomTasksData(listData, rowData1, columns);

                    }
                }
            }


        }

    }
    private static void insertData(List<RestRoomTasks> listData,Row rowData){
        if (listData!=null && listData.size()>0){
            System.out.println("sevenList:: "+listData);
            for (int j = 0; j < listData.size(); j++) {
                cell = rowData.createCell(j + 1);
                if(listData.get(j).isCompleted()==true) {
                    cell.setCellValue("Done");
                }else{
                    cell.setCellValue(" ");

                }

                cell.setCellStyle(CellsCellStyle);

            }
        }
    }
    private static void insertData1(List<FemaleRestRoomTasks> listData,Row rowData){
        if (listData!=null && listData.size()>0){
            System.out.println("sevenList:: "+listData);
            for (int j = 0; j < listData.size(); j++) {
                cell = rowData.createCell(j + 1);
                if(listData.get(j).isCompleted()==true) {
                    cell.setCellValue("Done");
                }else{
                    cell.setCellValue(" ");

                }

                cell.setCellStyle(CellsCellStyle);

            }
        }
    }
    private static void insertPantryTasksData(List<Object> listData,Row rowData,ArrayList<String> columns){
        if (listData!=null && listData.size()>0){
            System.out.println("sevenList:: "+listData);
            System.out.println("columns:: "+columns);
            cell = rowData.createCell(1);
            PantryTasks pantryTasks1=(PantryTasks) listData.get(0);
            cell.setCellValue(pantryTasks1.getAssignedTo());
            cell.setCellStyle(CellsCellStyle);
            int cellNumber =2;
            for (int j = 0; j < listData.size(); j++) {
//                if(!listData.get(j).getTask_name().equalsIgnoreCase("id")) {
                    PantryTasks pantryTasks = (PantryTasks) listData.get(j);
                    String cellValue = "";
                    cell.getStringCellValue();
//                    String cellExistingValue = pantry_headerRow.getCell(j + 1).getStringCellValue();
//                    System.out.println("pantry_headerRow:: " + cellExistingValue);
                    int newcellNumber = cellNumber + j;
                    System.out.println("newcellNumber:: " + newcellNumber);
                    cell = rowData.createCell(newcellNumber);
                    if (pantryTasks.isCompleted() == true) {
                        System.out.println("pantry_headerRow123:: " + pantryTasks.getTask_name() + " cellNumber:: " + newcellNumber);

//                    System.out.println("Cell Name:: "+listData.get(j).getTask_name()+" columns:: "+columns.get(j));
//                    if(listData.get(j).getTask_name().equalsIgnoreCase(columns.get(j))){
                        cellValue = "Done";
//                    }


                    } else {
                        cellValue = "";
//                    cell.setCellValue(cellValue);

                    }
                    cell.setCellValue(cellValue);
                    cell.setCellStyle(CellsCellStyle);

//                }
            }
        }
    }
    private static void insertMaleRestRoomTasksData(List<Object> listData,Row rowData,ArrayList<String> columns){
        if (listData!=null && listData.size()>0){
            System.out.println("sevenList:: "+listData);
            System.out.println("columns:: "+columns);
            cell = rowData.createCell(1);
            RestRoomTasks pantryTasks1=(RestRoomTasks) listData.get(0);
            cell.setCellValue(pantryTasks1.getAssignedTo());
            cell.setCellStyle(CellsCellStyle);
            int cellNumber =2;
            for (int j = 0; j < listData.size(); j++) {
//                if(!listData.get(j).getTask_name().equalsIgnoreCase("id")) {
                RestRoomTasks pantryTasks = (RestRoomTasks) listData.get(j);
                String cellValue = "";
                cell.getStringCellValue();
//                    String cellExistingValue = pantry_headerRow.getCell(j + 1).getStringCellValue();
//                    System.out.println("pantry_headerRow:: " + cellExistingValue);
                int newcellNumber = cellNumber + j;
                System.out.println("newcellNumber:: " + newcellNumber);
                cell = rowData.createCell(newcellNumber);
                if (pantryTasks.isCompleted() == true) {
                    System.out.println("pantry_headerRow123:: " + pantryTasks.getTask_name() + " cellNumber:: " + newcellNumber);

//                    System.out.println("Cell Name:: "+listData.get(j).getTask_name()+" columns:: "+columns.get(j));
//                    if(listData.get(j).getTask_name().equalsIgnoreCase(columns.get(j))){
                    cellValue = "Done";
//                    }


                } else {
                    cellValue = "";
//                    cell.setCellValue(cellValue);

                }
                cell.setCellValue(cellValue);
                cell.setCellStyle(CellsCellStyle);

//                }
            }
        }
    }
    private static void insertFeMaleRestRoomTasksData(List<Object> listData,Row rowData,ArrayList<String> columns){
        if (listData!=null && listData.size()>0){
            System.out.println("sevenList:: "+listData);
            System.out.println("columns:: "+columns);
            cell = rowData.createCell(1);
            FemaleRestRoomTasks pantryTasks1=(FemaleRestRoomTasks) listData.get(0);
            cell.setCellValue(pantryTasks1.getAssignedTo());
            cell.setCellStyle(CellsCellStyle);
            int cellNumber =2;
            for (int j = 0; j < listData.size(); j++) {
//                if(!listData.get(j).getTask_name().equalsIgnoreCase("id")) {
                FemaleRestRoomTasks pantryTasks = (FemaleRestRoomTasks) listData.get(j);
                String cellValue = "";
                cell.getStringCellValue();
//                    String cellExistingValue = pantry_headerRow.getCell(j + 1).getStringCellValue();
//                    System.out.println("pantry_headerRow:: " + cellExistingValue);
                int newcellNumber = cellNumber + j;
                System.out.println("newcellNumber:: " + newcellNumber);
                cell = rowData.createCell(newcellNumber);
                if (pantryTasks.isCompleted() == true) {
                    System.out.println("pantry_headerRow123:: " + pantryTasks.getTask_name() + " cellNumber:: " + newcellNumber);

//                    System.out.println("Cell Name:: "+listData.get(j).getTask_name()+" columns:: "+columns.get(j));
//                    if(listData.get(j).getTask_name().equalsIgnoreCase(columns.get(j))){
                    cellValue = "Done";
//                    }


                } else {
                    cellValue = "";
//                    cell.setCellValue(cellValue);

                }
                cell.setCellValue(cellValue);
                cell.setCellStyle(CellsCellStyle);

//                }
            }
        }
    }
    /**
     * Store Excel Workbook in external storage
     *
     * @param context  - application context
     * @param fileName - name of workbook which will be stored in device
     * @return boolean - returns state whether workbook is written into storage or not
     */
    private static boolean storeExcelInStorage(Context context, String fileName) {
        boolean isSuccess;
//        File file = new File(context.getExternalFilesDir(null), fileName);
//        File file = new File(Environment.getExternalStorageDirectory(), "DMSS_EXCEL");
//        File mydir = new File(context.getExternalFilesDir("DMSS_EXCEL").getAbsolutePath());
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File myDir = new File(root + "/DMSS");
        try {
            String filePath = "/storage/emulated/0/Documents/DMSS" + "/" + fileName;

//            Boolean isFile = new File(filePath).isFile();
//            if (isFile) {
//                File fdelete = new File(filePath);
             /*   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Path fileToDeletePath = Paths.get(filePath);
                    Files.delete(fileToDeletePath);

                    System.out.println("fileToDeletePath:: "+fileToDeletePath);

                }*/

//                Boolean isDeleted=fdelete.delete();
//                System.out.println("isDeleted:: "+isDeleted+"  fdelete:: "+fdelete);

            //}
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!myDir.exists())
        {
            myDir.mkdirs();
//            Toast.makeText(getApplicationContext(),"Directory Created",Toast.LENGTH_LONG).show();
        }
                File file = new File(myDir, fileName);

//        File file = new File(file1, fileName);
        FileOutputStream fileOutputStream = null;

        try {


            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            Log.e(TAG, "Writing file" + file);
            isSuccess = true;
        } catch (IOException e) {
            Log.e(TAG, "Error writing Exception: ", e);
            isSuccess = false;
        } catch (Exception e) {
            Log.e(TAG, "Failed to save file due to Exception: ", e);
            isSuccess = false;
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return isSuccess;
    }

    /**
     * Retrieve excel from External Storage
     *
     * @param context  - application context
     * @param fileName - name of workbook to be read
     * @return importedExcelData
     */
    private static List<PantryTasks> retrieveExcelFromStorage(Context context, String fileName) {
        importedExcelData = new ArrayList<>();

        File file = new File(context.getExternalFilesDir(null), fileName);
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(file);
            Log.e(TAG, "Reading from Excel" + file);

            // Create instance having reference to .xls file
            workbook = new HSSFWorkbook(fileInputStream);

            // Fetch sheet at position 'i' from the workbook
            sheet = workbook.getSheetAt(0);

            // Iterate through each row
            for (Row row : sheet) {
                int index = 0;
                List<String> rowDataList = new ArrayList<>();
//                List<ContactResponse.PhoneNumber> phoneNumberList= new ArrayList<>();

                if (row.getRowNum() > 0) {
                    // Iterate through all the columns in a row (Excluding header row)
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        // Check cell type and format accordingly
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:

                                break;
                            case Cell.CELL_TYPE_STRING:
                                rowDataList.add(index, cell.getStringCellValue());
                                index++;
                                break;
                        }
                    }

                    // Adding cells with phone numbers to phoneNumberList
                  /*  for (int i = 1; i < rowDataList.size(); i++) {
                        phoneNumberList.add(new ContactResponse.PhoneNumber(rowDataList.get(i)));
                    }*/

                    /**
                     * Index 0 of rowDataList will Always have name.
                     * So, passing it as 'name' in ContactResponse
                     *
                     * Index 1 onwards of rowDataList will have phone numbers (if >1 numbers)
                     * So, adding them to phoneNumberList
                     *
                     * Thus, importedExcelData list has appropriately mapped data
                     */
                    System.out.println("rowDataList:: "+rowDataList);

                    Boolean isAssigned= false;
                    if(rowDataList.get(2)=="true"){
                        isAssigned = true;
                    }
                    Boolean isCompleted= false;
                    if(rowDataList.get(3)=="true"){
                        isCompleted = true;
                    }

//                    importedExcelData.add(new PantryTasks(rowDataList.get(0),rowDataList.get(1), isAssigned,isCompleted,1));
                }

            }

        } catch (IOException e) {
            Log.e(TAG, "Error Reading Exception: ", e);

        } catch (Exception e) {
            Log.e(TAG, "Failed to read file due to Exception: ", e);

        } finally {
            try {
                if (null != fileInputStream) {
                    fileInputStream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return importedExcelData;
    }

}

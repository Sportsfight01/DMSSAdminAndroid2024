package com.dmss.dmssadminmaintanance.xcelsheet;

import android.content.Context;
import android.os.Environment;
import android.util.Log;


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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private static List<PantryTasks> importedExcelData;

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
                                                       Map<String,List<PantryTasks>> dataList,ArrayList<String> columns,
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
        sheet = workbook.createSheet(selectedDate);
        sheet.setColumnWidth(0, (15 * 400));
        sheet.setColumnWidth(1, (15 * 400));

        /*setPantryHeaderRow(pantry,name,created_date);
        fillPantryDataIntoExcel(dataList);
        isWorkbookWrittenIntoStorage = storeExcelInStorage(context, fileName);*/
        setRestRoomHeader(columns,timings,sheet);
        fillPantryDataIntoExcel(dataList,timings,columns);
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
      /*      Row headerRow = sheet.createRow(0);
            Row headerRow1 = sheet.createRow(1);
            Row headerRow2 = sheet.createRow(2);
            Row headerRow3 = sheet.createRow(3);
            Row headerRow4 = sheet.createRow(4);
            Row headerRow5 = sheet.createRow(5);
            Row headerRow6 = sheet.createRow(6);
            Row headerRow7 = sheet.createRow(7);*/
        Row headerRow1 = sheet.createRow(0);
        cell = headerRow1.createCell(0);
        cell.setCellValue("Time");
        cell.setCellStyle(headerCellStyle);
        for (int i=0;i<columsList.size();i++) {
            sheet.setColumnWidth(i+1, (10 * 400));
//            Row headerRow = sheet.createRow(i+1);
            cell = headerRow1.createCell(i+1);
            cell.setCellValue(columsList.get(i));
            cell.setCellStyle(headerCellStyle);
        }
        cell = headerRow1.createCell(columsList.size()+1);
        cell.setCellValue("Assign To");
        cell.setCellStyle(headerAssignedCellStyle);


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
    private static void fillPantryDataIntoExcel(List<PantryTasks> dataList) {
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
    }
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
    private static void fillPantryDataIntoExcel(Map<String,List<PantryTasks>> dataList,ArrayList<String> timingsList,ArrayList<String> columns) {

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
                    List<PantryTasks> listData = dataList.get(key);
                    insertPantryTasksData(listData, rowData1);
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
    private static void insertPantryTasksData(List<PantryTasks> listData,Row rowData){
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

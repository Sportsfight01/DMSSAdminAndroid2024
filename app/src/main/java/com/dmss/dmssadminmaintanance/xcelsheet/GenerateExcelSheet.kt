package com.dmss.dmssadminmaintanance.xcelsheet

import android.content.Context
import android.os.Environment
import android.util.Log
import com.dmss.dmssadminmaintanance.db.PantryTasks
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object GenerateExcelSheet {

    private var TAG ="GenerateExcelSheet:: "
    fun exportDataIntoExcel(dataList: List<PantryTasks>) {
         var sheet: Sheet?  =null
        var cell : Cell ?  =null
        val workbook: Workbook = HSSFWorkbook()

        sheet = workbook.createSheet("EXCEL_SHEET_NAME");

        for (i in dataList.indices) {
            // Create a New Row for every new entry in list
            val rowData: Row? = sheet?.createRow(i + 1)
            cell = rowData?.createCell(0);

            // Create Cells for each row
            cell = rowData?.createCell(0)
            cell?.setCellValue(dataList[i].task_name)

            cell = rowData?.createCell(1)
            cell?.setCellValue(dataList[i].created_date)

            cell = rowData?.createCell(2)
            cell?.setCellValue(dataList[i].isCompleted)

            cell = rowData?.createCell(4)
            cell?.setCellValue(dataList[i].isAssigned)
        }
    }
    private fun isExternalStorageReadOnly(): Boolean {
        val externalStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == externalStorageState
    }
    private fun isExternalStorageAvailable(): Boolean {
        val externalStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == externalStorageState
    }
    private fun storeExcelInStorage(context: Context, fileName: String): Boolean {
        val workbook: Workbook = HSSFWorkbook()

        var isSuccess: Boolean
        val file = File(context.getExternalFilesDir(null), "$fileName.xlsx")
        var fileOutputStream: FileOutputStream? = null

        try {
            fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            Log.e(TAG, "Writing file$file")
            isSuccess = true
        } catch (e: IOException) {
            Log.e(TAG, "Error writing Exception: ", e)
            isSuccess = false
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save file due to Exception: ", e)
            isSuccess = false
        } finally {
            try {
                fileOutputStream?.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return isSuccess
    }
    private fun fillDataIntoExcel(dataList: List<PantryTasks?>?) {
        var sheet: Sheet?  =null
        var cell : Cell ?  =null
        val workbook: Workbook = HSSFWorkbook()
        sheet = workbook.createSheet("EXCEL_SHEET_NAME");

        for (i in dataList!!.indices) {
            // Create a New Row for every new entry in list
            val rowData: Row = sheet.createRow(i + 1)

            // Create Cells for each row
            cell = rowData.createCell(0)
            cell.setCellValue(dataList.get(i)?.task_name)

            cell = rowData.createCell(1)
            cell.setCellValue(dataList.get(i)?.created_date)

            cell = rowData.createCell(2)
            cell.setCellValue(dataList.get(i)!!.isAssigned)

            cell = rowData.createCell(4)
            cell.setCellValue(dataList.get(i)!!.isCompleted)
        }
    }
    private fun setHeaderCellStyle() {
        val workbook: Workbook = HSSFWorkbook()

        var headerCellStyle = workbook.createCellStyle()
        headerCellStyle.setFillForegroundColor(HSSFColor.AQUA.index)
        headerCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND)
        headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER)
    }
    private fun setHeaderRow() {
        val workbook: Workbook = HSSFWorkbook()

        var sheet = workbook.createSheet("EXCEL_SHEET_NAME");
        var cellStyle = workbook.createCellStyle()

        val headerRow: Row = sheet.createRow(0)

        var cell = headerRow.createCell(0)
        cell.setCellValue("Task Name")
        cell.setCellStyle(cellStyle)

        cell = headerRow.createCell(1)
        cell.setCellValue("Create Date")
        cell.setCellStyle(cellStyle)

        cell = headerRow.createCell(2)
        cell.setCellValue("Is Assigned")
        cell.setCellStyle(cellStyle)

        cell = headerRow.createCell(3)
        cell.setCellValue("Is Completed")
        println("Cell :: "+cell)
    }
    fun exportDataIntoWorkbook(
        context: Context?, fileName: String?,
        dataList: List<PantryTasks?>?
    ): Boolean {
        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only")
            return false
        }

        // Creating a New HSSF Workbook (.xls format)
       var workbook = HSSFWorkbook()

        setHeaderCellStyle()

        // Creating a New Sheet and Setting width for each column
        var sheet = workbook.createSheet("EXCEL_SHEET_NAME")
        sheet.setColumnWidth(0, (15 * 400))
        sheet.setColumnWidth(1, (15 * 400))
        sheet.setColumnWidth(2, (15 * 400))
        sheet.setColumnWidth(3, (15 * 400))

        setHeaderRow()
        fillDataIntoExcel(dataList)
        val isWorkbookWrittenIntoStorage = storeExcelInStorage(context!!, fileName!!)

        return isWorkbookWrittenIntoStorage
    }
}
package uz.univer.argis.common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
import java.util.Iterator;
import java.util.List;

import uz.univer.argis.models.EstateData;
import uz.univer.argis.models.LandPlotData;
import uz.univer.argis.models.PlaceDate;
import uz.univer.argis.models.export_data.StaticValue;

/**
 * Excel Worksheet Utility Methods
 * <p>
 * Created by: Ranit Raj Ganguly on 16/04/21.
 */
public class ExcelUtils {
    public static final String TAG = "ExcelUtil";
    private static Cell cell;
    private static Sheet sheet;
    private static Workbook workbook;
    private static CellStyle headerCellStyle;

    private static List<String> importedExcelData;

    /**
     * Import data from Excel Workbook
     *
     * @param context  - Application Context
     * @param fileName - Name of the excel file
     * @return importedExcelData
     */
    public static List<String> readFromExcelWorkbook(Context context, String fileName) {
        return retrieveExcelFromStorage(context, fileName);
    }


    /**
     * Export Data into Excel Workbook
     *
     * @param context  - Pass the application context
     * @param fileName - Pass the desired fileName for the output excel Workbook
     *                 //     * @param dataList - Contains the actual data to be displayed in excel
     */
    public static boolean exportDataIntoWorkbook(Context context, String fileName, EstateData estateData, LandPlotData landPlotData, PlaceDate placeDate) {
        boolean isWorkbookWrittenIntoStorage;

        // Check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        // Creating a New HSSF Workbook (.xls format)
        workbook = new HSSFWorkbook();

        setHeaderCellStyle();

        // Creating a New Sheet and Setting width for each column
        sheet = workbook.createSheet(Constants.EXCEL_SHEET_NAME);
        sheet.setColumnWidth(0, (15 * 400));
        sheet.setColumnWidth(1, (15 * 400));

        setHeaderRow();
        fillDataIntoExcel(estateData, landPlotData, placeDate);
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

    /**
     * Setup Header Row
     */
    private static void setHeaderRow() {
        Row headerRow = sheet.createRow(0);

        cell = headerRow.createCell(0);
        cell.setCellValue("Malumotlar nomi");
        cell.setCellStyle(headerCellStyle);

        cell = headerRow.createCell(1);
        cell.setCellValue("Yer malumotlari");
        cell.setCellStyle(headerCellStyle);
    }

    /**
     * Fills Data into Excel Sheet
     * <p>
     * NOTE: Set row index as i+1 since 0th index belongs to header row
     * <p>
     * //     * @param dataList - List containing data to be filled into excel
     */

//    ArrayList aataFildsName = ArrayList<String>(
//            "Kadstr raqami",
//    "Obyektning nomi",
//            "Obyektning manzili",
//            "Xuquq turi",
//            "Davlat ruyxatiga olingan sana",
//            "Maydon",
//            "Maqsad vazifasi",
//            "Soliq zonasi",
//            "Qiymati (so'm)"
//            )
    private static void fillDataIntoExcel(EstateData estateData, LandPlotData landPlotData, PlaceDate placeDate) {
        for (int i = 0; i < StaticValue.INSTANCE.getPlaceDataFildsName().length; i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + 1);
            cell = rowData.createCell(0);
            cell.setCellValue(StaticValue.INSTANCE.getPlaceDataFildsName()[i]);
            cell = rowData.createCell(1);
            switch (i) {
                case 0: {
                    cell.setCellValue(placeDate.getViloyat());
                }
                break;
                case 1: {
                    cell.setCellValue(placeDate.getShaxar());
                }
                break;
                case 2: {
                    cell.setCellValue(placeDate.getTuman());
                }
                break;
                case 3: {
                    cell.setCellValue(placeDate.getQishloq());
                }
                break;
                case 4: {
                    cell.setCellValue(placeDate.getMFY());
                }
                break;
                case 5: {
                    cell.setCellValue(placeDate.getYerMulkFoydalanuvchisi());
                }
                break;
                case 6: {
                    cell.setCellValue(placeDate.getYerToifasi());
                }
                break;
            }
        }
        for (int i = 0; i < StaticValue.INSTANCE.getDataFildsName().length; i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + 9);
            cell = rowData.createCell(0);
            cell.setCellValue(StaticValue.INSTANCE.getDataFildsName()[i]);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(placeDate.getViloyat()).append("\n").append(placeDate.getShaxar()).append("\n").append(placeDate.getTuman()).append("\n").append(placeDate.getQishloq()).append("\n").append(placeDate.getMFY()).append("\n").append(placeDate.getYerMulkFoydalanuvchisi()).append("\n").append(placeDate.getYerToifasi()).append("\n");
            cell = rowData.createCell(1);
            switch (i) {
                case 0: {
                    cell.setCellValue(landPlotData.getKadastr());
                }break;
                case 1: {
                    cell.setCellValue(landPlotData.getObyektNomi());
                }break;
                case 2: {
                    cell.setCellValue(landPlotData.getObyektManzili());
                }break;
                case 3: {
                    cell.setCellValue(landPlotData.getXuquqTuri());
                }break;
                case 4: {
                    cell.setCellValue(landPlotData.getRuyxatSanasi());
                }break;
                case 5: {
                    cell.setCellValue(landPlotData.getMaydon());
                }break;
                case 6: {
                    cell.setCellValue(landPlotData.getMaqsad());
                }break;
                case 7: {
                    cell.setCellValue(landPlotData.getSoliq());
                }break;
                case 8: {
                    cell.setCellValue(landPlotData.getQiymati());
                }break;
            }
        }
        for (int i = 0; i < StaticValue.INSTANCE.getDataFildsName().length; i++) {
            // Create a New Row for every new entry in list
            Row rowData = sheet.createRow(i + 19);
            cell = rowData.createCell(0);
            cell.setCellValue(StaticValue.INSTANCE.getDataFildsName()[i]);
            cell = rowData.createCell(1);
            switch (i) {
                case 0: {
                    cell.setCellValue(estateData.getKadastr());
                }break;
                case 1: {
                    cell.setCellValue(estateData.getObyektNomi());
                }break;
                case 2: {
                    cell.setCellValue(estateData.getObyektManzili());
                }break;
                case 3: {
                    cell.setCellValue(estateData.getXuquqTuri());
                }break;
                case 4: {
                    cell.setCellValue(estateData.getRuyxatSanasi());
                }break;
                case 5: {
                    cell.setCellValue(estateData.getMaydon());
                }break;
                case 6: {
                    cell.setCellValue(estateData.getMaqsad());
                }break;
                case 7: {
                    cell.setCellValue(estateData.getSoliq());
                }break;
                case 8: {
                    cell.setCellValue(estateData.getQiymati());
                }break;
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
        File file = new File(context.getExternalFilesDir(null), fileName);
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
    private static List<String> retrieveExcelFromStorage(Context context, String fileName) {
        importedExcelData = new ArrayList<>();
        ArrayList<String> listArgis = new ArrayList<>();
        listArgis.add("ARGIS");
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
//                List<String> phoneNumberList = new ArrayList<>();

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
                    for (int i = 1; i < rowDataList.size(); i++) {
//                        phoneNumberList.add(rowDataList.get(i));
                    }

                    /**
                     * Index 0 of rowDataList will Always have name.
                     * So, passing it as 'name' in String
                     *
                     * Index 1 onwards of rowDataList will have phone numbers (if >1 numbers)
                     * So, adding them to phoneNumberList
                     *
                     * Thus, importedExcelData list has appropriately mapped data
                     */

                    importedExcelData.add(rowDataList.get(0));
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

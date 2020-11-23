package dataUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.CellType;

public class Excel {

    public static void writeToNewExcel(Object[] inputData, String excelPath, String sheetName)
    {
        // Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Create a blank sheet
        XSSFSheet sheet = workbook.createSheet(sheetName);

        // This data needs to be written (Object[])
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        for (int i = 0; i < inputData.length; i++) {
            data.put(Integer.toString(i+1),new Object[]{inputData[i]});
        }
        // Iterate over data and write to sheet
        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            // this creates a new row in the sheet
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                // this line creates a cell in the next column of that row
                Cell cell = row.createCell(cellnum);
                String[] _temprow = (String[]) obj;
                for (String k: _temprow) {
                    cell.setCellValue(k);
                    cell = row.createCell(++cellnum);
                }
            }
        }
        try {
            // this Writes the workbook
            FileOutputStream out = new FileOutputStream(new File(excelPath));
            workbook.write(out);
            out.close();
            System.out.println("Data written successfully on "+excelPath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void appendToExcel(Object[] inputData, String excelPath,String sheetName) throws IOException, InvalidFormatException {
        InputStream inp = new FileInputStream(excelPath);
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheet(sheetName);
        int num = sheet.getLastRowNum();
        Row row = sheet.createRow(++num);
        int cellnum = 0;
        for (Object obj : inputData) {
            // this line creates a cell in the next column of that row
            Cell cell = row.createCell(cellnum);
            String[] _temprow = (String[]) obj;
            for (String k: _temprow) {
                cell.setCellValue(k);
                cell = row.createCell(++cellnum);
            }
            row = sheet.createRow(++num);
            cellnum = 0;
        }
        // Now this Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(excelPath);
        wb.write(fileOut);
        fileOut.close();
    }
    public static Map<Integer,String[]> readFromExcel(String excelPath, String sheetName)  {
        try {
            InputStream file = new FileInputStream(excelPath);
            Workbook wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheet(sheetName);

            // Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            Map<Integer,String[]> excelContents = new TreeMap<>();
            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                // For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                String[] _tempcolVals = new String[0];
                ArrayList<String> colValuesList = new ArrayList<String>();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    // Check the cell type and format accordingly
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            colValuesList.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        case STRING:
                            colValuesList.add(cell.getStringCellValue());
                            break;
                    }
                }
                _tempcolVals = GetStringArray(colValuesList);
                excelContents.put(rowIndex++,_tempcolVals);
            }
            file.close();
            return excelContents;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String[] GetStringArray(ArrayList<String> arr)
    {
        // declaration and initialise String Array
        String str[] = new String[arr.size()];
        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }
        return str;
    }

    public static Object[] convertMapToObject(Map<Integer,String[]> mapData)
    {
        Object[] obj = new Object[mapData.size()];
        for (int i = 0; i < mapData.size(); i++) {
            obj[i] = mapData.get(i);
        }
        return obj;
    }
    public static Map<Integer,String[]> convertObjectToMap(Object[] objData)
    {
        Map<Integer,String[]> out = new TreeMap<>();
        for (int i = 0; i < objData.length; i++) {
            out.put(i, (String[]) objData[i]);
        }
        return out;
    }
}

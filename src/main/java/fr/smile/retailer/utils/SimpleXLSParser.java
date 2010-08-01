package fr.smile.retailer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import fr.smile.retailer.dao.XLSLineModelFactory;
import fr.smile.retailer.model.XLSLineModel;

public class SimpleXLSParser {

	private final Logger logger = Logger.getLogger(this.getClass());
	private Map<Integer,Integer> columnsWithData;
	
	
	public List<? extends XLSLineModel> parse(InputStream in, XLSLineModelFactory modelService) throws IOException {
		List<XLSLineModel> result = new ArrayList<XLSLineModel>();
		List<String> columnNames = modelService.getListHeaderLineCells();
		columnsWithData = null;
		
		logger.debug("Creating model container for lines");

		HSSFWorkbook wb = new HSSFWorkbook(in);
		/**
		 * There are two kind of lines: headers and data. We check for header
		 * line first, then all the lines are data. 
		 * <br/>
		 * dataLineFlag=true indicates that header line has been already parsed
		 */
		boolean dataLineFlag = false;
		for (int k = 0; k < wb.getNumberOfSheets(); k++) {
			HSSFSheet sheet = wb.getSheetAt(k);
			int rows = sheet.getPhysicalNumberOfRows();
			logger.info("Sheet " + k + " \"" + wb.getSheetName(k) + "\" has " + rows + " row(s).");
			for (int r = 0; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {

					int cells = row.getPhysicalNumberOfCells();
					logger.info("ROW " + row.getRowNum() + " has " + cells + " cell(s).");
					List<String> values = parseCells(row, cells);
	
					if (!dataLineFlag) {
						logger.debug("Header line has not been found yet");
						columnsWithData = checkAndGetDataColumnsNumber(columnNames, values);
						if (columnsWithData != null && !columnsWithData.isEmpty()) {
							logger.info("Header line parsed and verified correctly");
							dataLineFlag = true;
						} else {
							logger.error("Header line parserd with errors - skipping this line");
						}
					} else if (columnNames.size() <= values.size()){
						logger.debug("Creating item from data line");
						XLSLineModel lineItem = modelService.createItem(values);
						result.add(lineItem);
					} else {
						logger.warn("Data line contains only " + values.size() + " cells, while expecting " + columnNames.size());
						logger.warn("Skipping line #" + r);
					}
				}
			}
		}
		return result;
	}

	/**
	 * @param row
	 * @param cells
	 * @return
	 */
	private List<String> parseCells(HSSFRow row, int cells) {
		if (columnsWithData != null && !columnsWithData.isEmpty()) {
			return parseOnlyDataColumns(row, cells);
		} else {
			return parseAllCells(row,cells);
		}
	}

	/**
	 * Parse only columns that contains data. Match is done during verification of header lines.
	 * <br/>
	 * Able to parse columns in random order. So that if for example it is necessary to take data from columns 1,5,10 and put
	 * in the following order: 10,1,5 - it can do it.
	 * 
	 * @param row
	 * @param cells
	 * @return
	 */
	private List<String> parseOnlyDataColumns(HSSFRow row, int cells) {
		String[] valuesTemp = new String[3];
		for (int c = 0; c < cells; c++) {
			if (!columnsWithData.containsKey(c)) { 
				logger.debug("Skiping cell col=" + c + " as we don't need it");
				continue; 
			}
			HSSFCell cell = row.getCell(c);
			if (cell == null) {
				logger.info("CELL col=" + c + " is empty");
				valuesTemp[columnsWithData.get(c)]= "";
				continue;
			}
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				valuesTemp[columnsWithData.get(c)] = new String(cell.getNumericCellValue() + "").trim();
				break;
			case HSSFCell.CELL_TYPE_STRING:
				valuesTemp[columnsWithData.get(c)] = cell.getStringCellValue().trim();
				break;
			default:
			}
			logger.info("CELL col=" + cell.getColumnIndex() + " VALUE=" + valuesTemp[columnsWithData.get(c)]);
		}
		return Arrays.asList(valuesTemp);
	}

	private List<String> parseAllCells(HSSFRow row, int cells) {
		List<String> values = new ArrayList<String>();
		for (int c = 0; c < cells; c++) {
			HSSFCell cell = row.getCell(c);
			if (cell == null) {
				logger.info("CELL col=" + c + " is empty");
				values.add("");
				continue;
			}
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				values.add(new String(cell.getNumericCellValue() + "").trim());
				break;
			case HSSFCell.CELL_TYPE_STRING:
				values.add(cell.getStringCellValue().trim());
				break;
			default:
			}
			logger.info("CELL col=" + cell.getColumnIndex() + " VALUE=" + values.get(values.size() - 1));
		}
		return values;
	}

	/**
	 * Identify if all necessary for current parsing configuratio columns are present and put them in Map<columnNumber
	 * 
	 * @param columnNames
	 * @param foundValues
	 */
	private Map<Integer,Integer> checkAndGetDataColumnsNumber(List<String> columnNames, List<String> foundValues) {
		int counter = 0;
		Map<Integer,Integer> result = new HashMap<Integer,Integer>();
		for (String columnName : columnNames) {
			if (foundValues.indexOf(columnName) == -1) {
				logger.error("Column with name " + columnName + " not found amoung cells in the first row");
				return null;
			} else {
				result.put(foundValues.indexOf(columnName), counter);
			}
			counter++;
		}
		return result;
	}
}
package fr.smile.retailer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import fr.smile.retailer.dao.XLSLineModelFactory;
import fr.smile.retailer.model.XLSLineModel;

public class SimpleXLSParser {

	private final Logger logger = Logger.getLogger(this.getClass());

	public List<? extends XLSLineModel> parse(InputStream in, XLSLineModelFactory modelService) throws IOException {
		List<XLSLineModel> result = new ArrayList<XLSLineModel>();
		List<String> columnNames = modelService.getListHeaderLineCells();

		
		logger.debug("Creating model container for lines");

		HSSFWorkbook wb = new HSSFWorkbook(in);
		/**
		 * There are two kind of lines: headers and data. We check for header
		 * line first, then all the lines are data
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
					List<String> values = new ArrayList<String>();
					for (int c = 0; c < cells; c++) {
						HSSFCell cell = row.getCell(c);
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
					if (!dataLineFlag) {
						if (checkAllNecessaryColumnsPresent(columnNames, values)) {
							logger.info("Header line parsed and verified correctly");
							dataLineFlag = true;
						} else {
							logger.error("Header line parserd with errors");
							return result;
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
	 * @param columnNames
	 * @param foundValues
	 */
	private boolean checkAllNecessaryColumnsPresent(List<String> columnNames, List<String> foundValues) {
		boolean atLeastOneColumnNotFound = false;
		for (String columnName : columnNames) {
			if (foundValues.indexOf(columnName) == -1) {
				atLeastOneColumnNotFound = true;
				logger.error("Column with name " + columnName + " not found amoung cells in the first row");
			}
		}
		return !atLeastOneColumnNotFound;
	}
}
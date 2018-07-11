package ping.wg.apittest.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

	/**
	 * 获取excel表所有sheet数据
	 * @param clz
	 * @param path
	 * @return
	 */
	public static <T> List<T> readExcel(Class<T> clz, String path) {

		if (null == path || "".equals(path)) {
			return null;
		}
		InputStream inputStream;
		Workbook xssfWorkbook;
		try {
			inputStream = new FileInputStream(path);
			if (path.endsWith(".xls")) {
				xssfWorkbook = new HSSFWorkbook(inputStream);
			} else {
				xssfWorkbook = new XSSFWorkbook(inputStream);
			}
			inputStream.close();
			int sheetNumber = xssfWorkbook.getNumberOfSheets();
			List<T> allData = new ArrayList<T>();
			for (int i = 0; i < sheetNumber; i++) {
				allData.addAll(transToObject(clz, xssfWorkbook,
						xssfWorkbook.getSheetName(i)));
			}
			return allData;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("转换excel文件失败：" + e.getMessage());
		}
	}
	
	/**
	 * 获取excel表指定sheet表数据
	 * @param clz
	 * @param path
	 * @param sheetName
	 * @return
	 */
	public static <T> List<T> readExcel(Class<T> clz, String path,
			String sheetName) {
		if (null == path || "".equals(path)) {
			return null;
		}
		InputStream is;
		Workbook xssfWorkbook;
		try {
			is = new FileInputStream(path);
			if (path.endsWith(".xls")) {
				xssfWorkbook = new HSSFWorkbook(is);
			} else {
				xssfWorkbook = new XSSFWorkbook(is);
			}
			is.close();
			return transToObject(clz, xssfWorkbook, sheetName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("转换excel文件失败：" + e.getMessage());
		}

	}



	//转化对象
	private static <T> List<T> transToObject(Class<T> clz,
			Workbook xssfWorkbook, String sheetName)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException {

		List<T> list = new ArrayList<T>();
		Sheet xssfSheet = xssfWorkbook.getSheet(sheetName);
		Row firstRow = xssfSheet.getRow(0);

		if(firstRow!=null){
			//excel表格中第一行
			List<Object> heads = getRow(firstRow);
			//添加sheetName字段，用于封装至bean中，与bean中的字段相匹配。
			heads.add("sheetName");

			//得到excel表头字段和与之对应的方法集合
			Map<String, Method> headMethod = getSetMethod(clz, heads);

			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				try {
					Row xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow!=null){
						T t = clz.newInstance();

						//得到整行单元格数据的集合
						List<Object> data = getRow(xssfRow);
						//如果发现表数据的列数小于表头的列数，则自动填充为null，最后一位不动，用于添加sheetName数据
						while(data.size()<heads.size()-1){
							data.add("");
						}
						data.add(sheetName);

						//将excel中的数据设置到javaBean中
						setValue(t, data, heads, headMethod);
						list.add(t);
					}

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	//获取所有的excel表头字段的set方法
	private static Map<String, Method> getSetMethod(Class<?> clz,
			List<Object> heads) {
		Map<String, Method> map = new HashMap<String, Method>();
		Method[] methods = clz.getMethods();
		for (Object head : heads) {
			for (Method method : methods) {
				if (method.getName().toLowerCase()
						.equals("set" + head.toString().toLowerCase())
						&& method.getParameterTypes().length == 1) {
					map.put(head.toString(), method);
					break;
				}
			}

		}
		return map;
	}

	//将excel中的数据设置到javaBean中
	private static void setValue(Object obj, List<Object> data,
			List<Object> heads, Map<String, Method> methods)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		//遍历methods map集合
		for (Map.Entry<String, Method> entry : methods.entrySet()) {

			//data集合中的值，data集合为excel每行具体的值
			Object value = "";
			//方法中的键位于表头集合中的位置，heads集合的长度大于data集合的长度
			int dataIndex = heads.indexOf(entry.getKey());
			//如果当前位置小于数据长度
			if (dataIndex < data.size()) {
				value = data.get(dataIndex);
			}

			Method method = entry.getValue();

			//得到参数的类型
			Class<?> param = method.getParameterTypes()[0];
			if (String.class.equals(param)) {
				method.invoke(obj, value);
				//如果参数的类型为整型
			} else if (Integer.class.equals(param) || int.class.equals(param)) {
				//如果参数的值为空
				if(value.toString()==""){
					value=0;
				}
				method.invoke(obj, new BigDecimal(value.toString()).intValue());
			} else if (Long.class.equals(param) || long.class.equals(param)) {	
				if(value.toString()==""){
					value=0;
				}
				method.invoke(obj, new BigDecimal(value.toString()).longValue());
			} else if (Short.class.equals(param) || short.class.equals(param)) {
				if(value.toString()==""){
					value=0;
				}
				method.invoke(obj, new BigDecimal(value.toString()).shortValue());
			} else if (Boolean.class.equals(param)
					|| boolean.class.equals(param)) {
				
				method.invoke(obj, Boolean.valueOf(value.toString())
						|| value.toString().toLowerCase().equals("y"));

			} else if (JSONObject.class.equals(param)) {
				method.invoke(obj, JSONObject.parseObject(value.toString()));
			}else {
				// Date
				method.invoke(obj, value);
			}
		}
	}

	//得到整行单元格数据值的集合
	private static List<Object> getRow(Row xssfRow) {
		List<Object> cells = new ArrayList<Object>();
		if (xssfRow != null) {
			for (short cellNum = 0; cellNum < xssfRow.getLastCellNum(); cellNum++) {
				Cell xssfCell = xssfRow.getCell(cellNum);
				cells.add(getValue(xssfCell));
			}
		}
		return cells;
	}

	//得到每个单元格的值
	private static String getValue(Cell cell) {
		if (cell==null) {
			return "";			
		} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(cell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(cell.getStringCellValue());
		}
	}
}

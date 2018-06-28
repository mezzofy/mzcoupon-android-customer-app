package com.mezzofy.mzcoupon.mapper;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetMapper<T> {
	
	 private void setProperty(Object clazz, String fieldName, Object columnValue) {
		    try {
		      // get all fields of the class (including public/protected/private)
		      Field field = clazz.getClass().getDeclaredField(fieldName);
		      // this is necessary in case the field visibility is set at private
		      field.setAccessible(true);
		      field.set(clazz, columnValue);
		    } catch (NoSuchFieldException e) {
		      e.printStackTrace();
		    } catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		  }
	 
	public List<T> mapRersultSetToObject(ResultSet rs, Class<T> outputClass) {
		List<T> outputList = null;
		try {
			// make sure resultset is not null
			if (rs != null) {
				// check if outputClass has 'Entity' annotation
				if (outputClass.isAnnotationPresent(Entity.class)) {
					// get the resultset metadata
					ResultSetMetaData rsmd = rs.getMetaData();
					// get all the attributes of outputClass
					Field[] fields = outputClass.getDeclaredFields();
					while (rs.next()) {
						T bean = (T) outputClass.newInstance();
						for (int _iterator = 0; _iterator < rsmd
								.getColumnCount(); _iterator++) {
							// getting the SQL column name
							String columnName = rsmd
									.getColumnName(_iterator + 1);
							// reading the value of the SQL column
							Object columnValue = rs.getObject(_iterator + 1);
							// iterating over outputClass attributes to check if any attribute has 'Column' annotation with matching 'name' value
							for (Field field : fields) {
								if (field.isAnnotationPresent(Column.class)) {
									Column column = field.getAnnotation(Column.class);
									if (column.name().equalsIgnoreCase(columnName)&& columnValue != null) {
										this.setProperty(bean, field.getName(), columnValue);
										break;
									}
								}
							}
						}
						if (outputList == null) {
							outputList = new ArrayList<T>();
						}
						outputList.add(bean);
					}

				} else {
					// throw some error
				}
			} else {
				return null;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return outputList;
	}
}


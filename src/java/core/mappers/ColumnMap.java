/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.mappers;

import java.lang.reflect.Field;

/**
 *
 * @author aldo
 */
public class ColumnMap {
    private String columnName;
    private String fieldName;
    private Field field;
    private MetaDataMap dataMap;

    public ColumnMap(String columnName, String fieldName, MetaDataMap dataMap) {
        this.columnName = columnName;
        this.fieldName = fieldName;
        this.dataMap = dataMap;
        initField();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setField(Object subject, Object columnValue) {
        try {            
            field.set(subject, columnValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue(Object subject) {
        try {
            return field.get(subject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initField() {
        try {
            field = dataMap.getDomainClass().getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

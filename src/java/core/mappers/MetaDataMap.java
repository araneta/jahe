/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aldo
 */
public class MetaDataMap {
    private Class domainClass;
    private String tableName;
    private List<ColumnMap> columnMaps = new ArrayList<ColumnMap>();

    public MetaDataMap(Class domainClass, String tableName) {
        this.domainClass = domainClass;
        this.tableName = tableName;
    }
    public int count(){
        return columnMaps.size();
    }
    public void addColumn(String columnName, String type, String fieldName) {
        columnMaps.add(new ColumnMap(columnName, fieldName, this));
    }

    public Class getDomainClass() {
        return domainClass;
    }

    public String getTableName() {
        return tableName;
    }

    public /*List<ColumnMap>*/ Iterator getColumns() {
        return Collections.unmodifiableCollection(columnMaps).iterator();
        //return columnMaps;
    }

    public String columnList() {
        StringBuilder columnList = new StringBuilder(" id");
        for (ColumnMap colmunMap : columnMaps) {
            columnList.append(", ");
            columnList.append(colmunMap.getColumnName());
        }
        return columnList.toString();
    }

    public String getUpdateList() {
        StringBuilder columnList = new StringBuilder(" SET ");
        for (ColumnMap colmunMap : columnMaps) {
            columnList.append(colmunMap.getColumnName());
            columnList.append("=?,");
        }
        return core.helpers.StringUtils.chop(columnList.toString());
    }

    public String getInsertList() {
        StringBuilder columnList = new StringBuilder("");
        for (int i = 0; i < columnMaps.size(); i++) {
            columnList.append(",?");
        }
        return columnList.toString();
    }
}

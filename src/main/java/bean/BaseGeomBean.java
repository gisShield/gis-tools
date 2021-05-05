package bean;

import enums.DataTypeEnum;
import enums.GeomTypeEnum;

/**
 * @author gisShield
 * @title: BaseGeomBean
 * @projectName gis-tools
 * @description: 基本几何类型
 * @date 2021/5/4 11:12
 */
public class BaseGeomBean {
    /**
     * 几何对象类型枚举
     * 点(point) 线(line) 面(polygon) 多点(multipoint) 多线(multilinestring) 多面(multipolygon)
     */
    private GeomTypeEnum geomType;
    /**
     * 数据类型
     * 1.base 基础模式采用,和;拼接的字符串
     * 2.wkt WKT格式字符串
     * 3.geojson geoJson格式字符串
     */
    private DataTypeEnum dataType;
    /**
     * 坐标串
     */
    private String coordinates;


    public GeomTypeEnum getGeomType() {
        return geomType;
    }

    public void setGeomType(GeomTypeEnum geomType) {
        this.geomType = geomType;
    }

    public DataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(DataTypeEnum dataType) {
        this.dataType = dataType;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}

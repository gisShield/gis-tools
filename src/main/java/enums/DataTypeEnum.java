package enums;

/**
 * @author gisShield
 * @title: DataTypeEnum
 * @projectName gis-tools
 * @description: 数据类型枚举类
 * @date 2021/5/4 11:07
 */
public enum DataTypeEnum {

    /**
     * base 基础模式 即按照一定规则拼接坐标串
     */
    BASE("base"),
    /**
     * wkt WKT格式字符串
     */
    WKT("wkt"),
    /**
     * geoJson格式字符串
     */
    GEOJSON("geojson");

    // 类型
    private String type;

    DataTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 根据名称获取枚举类
     *
     * @param typeName
     * @return
     */
    public static DataTypeEnum fromTypeName(String typeName) {
        for (DataTypeEnum type : DataTypeEnum.values()) {
            if (type.getType().equals(typeName)) {
                return type;
            }
        }
        return null;
    }
}

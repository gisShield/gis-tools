package enums;

/**
 * @author gisShield
 * @title: GeomTypeEnum
 * @projectName gis-tools
 * @description: 几何类型枚举
 * @date 2021/5/4 11:10
 */
public enum GeomTypeEnum {
    /**
     * 点类型
     */
    POINT("point"),
    /**
     * 线类型
     */
    LINE("line"),
    /**
     * 面类型
     */
    POLYGON("polygon"),
    /**
     * 多点类型
     */
    MULTIPOINT("multipoint"),
    /**
     * 多线类型
     */
    MULTILINESTRING("multilinestring"),
    /**
     * 多面类型
     */
    MULTIPOLYGON("multipolygon");

    private String type;

    GeomTypeEnum(String type) {
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
    public static GeomTypeEnum fromTypeName(String typeName) {
        for (GeomTypeEnum type : GeomTypeEnum.values()) {
            if (type.getType().equals(typeName)) {
                return type;
            }
        }
        return null;
    }
}

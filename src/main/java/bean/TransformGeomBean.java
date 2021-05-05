package bean;

/**
 * @author gisShield
 * @title: TransformGeomBean
 * @projectName gis-tools
 * @description: 坐标转换基础类
 * @date 2021/5/4 11:14
 */
public class TransformGeomBean extends BaseGeomBean {
    /**
     * 几何对象源坐标系
     */
    private String fromCrsCode;
    /**
     * 几何目标坐标系
     */
    private String toCrsCode;

    public TransformGeomBean() {
    }

    public String getFromCrsCode() {
        return fromCrsCode;
    }

    public void setFromCrsCode(String fromCrsCode) {
        this.fromCrsCode = fromCrsCode;
    }

    public String getToCrsCode() {
        return toCrsCode;
    }

    public void setToCrsCode(String toCrsCode) {
        this.toCrsCode = toCrsCode;
    }
}

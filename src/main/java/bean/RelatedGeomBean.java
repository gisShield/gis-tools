package bean;

/**
 * @author gisShield
 * @title: RelatedGeomBean
 * @projectName gis-tools
 * @description: 空间关系基础类
 * @date 2021/5/4 11:14
 */
public class RelatedGeomBean extends BaseGeomBean {
    /**
     * 坐标参考系 EPSG代码
     */
    private int srid = 4326;

    public RelatedGeomBean() {
        super();
    }

    public RelatedGeomBean(int srid) {
        super();
        this.srid = srid;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }
}

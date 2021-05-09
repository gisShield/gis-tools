package utils;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author gisShield
 * @title: GeometryRelatedUtil
 * @projectName gis-tools
 * @description: 比较两个Geometry对象关系包括：
 * 相离（disjoint）、相等（equal）、相接（touch）、相交（intersects）、内含（in）、包含（contain）、交叠(overlap)、
 * 覆盖（cover）、被覆盖（coveredBy）、交叉（cross）共12中基本空间关系 返回Boolean类型
 * @date 2021/5/4 11:22
 */
public class GeometryRelatedUtil {
    /**
     * 测试此几何体geom1是否与几何体geom2不相交。 (几何形状没有共有的点)
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean disjointGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.disjoint(geom2) : false;
    }

    /**
     * 测试此geom1在拓扑上是否等于geom2。
     *
     * @param geom1 几何对象
     * @param geom2 几何对象
     * @return true
     */
    public static Boolean equalsGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.equals(geom2) : false;

    }

    /**
     * 测试此几何geom1是否接触geom2几何。 (几何对象有至少一个公共的边界点，但是没有内部点。)
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean touchesGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.touches(geom2) : false;

    }

    /**
     * 测试此几何geom1是否穿过几何geom2。(存在共有的点，但并不全部共有)
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean crossesGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.crosses(geom2) : false;

    }

    /**
     * 测试几何体geom1是否在指定的几何geom2内。 (几何形状A的线都在几何形状B内部。)
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean withinGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.within(geom2) : false;

    }

    /**
     * 测试此几何geom1是否包含几何geom2。
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean containsGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.contains(geom2) : false;

    }

    /**
     * 判断两个几何对象是否空间重叠
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean overlapsGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.overlaps(geom2) : false;

    }

    /**
     * 测试几何geom1是否覆盖几何geom2。
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean coversGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.covers(geom2) : false;

    }

    /**
     * 测试几何geom2是否覆盖此几何geom1。
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean coveredByGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.coveredBy(geom2) : false;

    }

    /**
     * 测试此几何geom1是否与几何geom2相交。
     *
     * @param geom1
     * @param geom2
     * @return
     */
    public static Boolean intersectGeo(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.intersects(geom2) : false;

    }

    /**
     * 判断geom2 在geom1 的缓冲区内
     *
     * @param geom1 缓冲区几何对象
     * @param geom2 几何对象
     * @return
     */
    public static Boolean isInBuffer(Geometry geom1, Geometry geom2) {
        return containsGeo(geom1, geom2);
    }

    /**
     * 测试从此geom1 到另一个 geom2 的距离是否小于或等于指定distance。
     *
     * @param geom1    几何对象
     * @param geom2    用于检查距离的几何对象
     * @param distance 指定的距离值
     * @return
     */
    public static Boolean isWithinDistance(Geometry geom1, Geometry geom2, double distance) {
        return isNotNull(geom1, geom2) ? geom1.isWithinDistance(geom2, distance) : false;
    }

    /**
     * 差异分析
     *
     * @param geom1
     * @param geom2
     * @return 去除geom1 与geom2重叠部分剩下的geom1几何对象
     */
    public static Geometry geometryDifference(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.difference(geom2) : null;
    }

    /**
     * 合并分析
     *
     * @param geom1
     * @param geom2
     * @return 几何对象geom1 和 geom2几何对象 合并成一个几何对象
     */
    public static Geometry geometryUnion(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.union(geom2) : null;
    }

    /**
     * 叠加分析
     *
     * @param geom1
     * @param geom2
     * @return geom1几何对象和geom2几何对象叠加部分
     */
    public static Geometry geometryIntersection(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.intersection(geom2) : null;
    }

    /**
     * sym差异分析
     *
     * @param geom1
     * @param geom2
     * @return 去除geom1 与geom2重叠部分，剩下的geom1和geom2生成新的几何对象
     */
    public static Geometry geometrySymdifference(Geometry geom1, Geometry geom2) {
        return isNotNull(geom1, geom2) ? geom1.symDifference(geom2) : null;
    }


    /**
     * 判断几何对象是否为空
     *
     * @param geom1
     * @param geom2
     * @return
     */
    private static boolean isNotNull(Geometry geom1, Geometry geom2) {
        return (geom1 != null && geom2 != null);
    }
}

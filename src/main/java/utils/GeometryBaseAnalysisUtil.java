package utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * @author gisShield
 * @title: GeometryBaseAnalysisUtil
 * @projectName gis-tools
 * @description: 几何对象基本属性工具类
 * @date 2021/5/4 11:21
 */
public class GeometryBaseAnalysisUtil {
    /**
     * 给指定几何对象生成缓冲区
     *
     * @param geometry 几何对象
     * @param distance 缓冲区半径
     * @return
     */
    public static Geometry getBuffer(Geometry geometry, double distance) {
        return geometry.buffer(distance);
    }

    /**
     * 获取结合对象的外包矩形
     *
     * @param geometry
     * @return
     */
    public static Geometry getEnvelope(Geometry geometry) {
        return geometry.getEnvelope();
    }

    /**
     * 获取几何对象最小凸包多边形
     *
     * @param geometry
     * @return
     */
    public static Geometry convexHull(Geometry geometry) {
        return geometry.convexHull();
    }

    /**
     * 获取几何对象的质心点
     *
     * @param geometry
     * @return
     */
    public static Point getCentroid(Geometry geometry) {
        return geometry.getCentroid();
    }

    /**
     * 获取长度 线几何返回长度 面几何返回周长，其他返回0.0
     *
     * @param geometry
     * @return
     */
    public static double getLength(Geometry geometry) {
        return geometry.getLength();
    }

    /**
     * 获取几何对象面积 面几何返回面积，其他几何返回0.0
     *
     * @param geometry
     * @return
     */
    public static double getArea(Geometry geometry) {
        return geometry.getArea();
    }
}

package utils;

import com.vividsolutions.jts.geom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author gisShield
 * @title: GeometryUtil
 * @projectName gis-tools
 * @description: 几何工具类
 * @date 2021/5/4 11:20
 */
public class GeometryUtil {

    private static final String GEOM_TYPE_POINT = "point";
    private static final String GEOM_TYPE_LINESTRING = "linestring";
    private static final String GEOM_TYPE_POLYGON = "polygon";

    private static final String GEOM_COLLECTION_PREFIX = "multi";
    private static final String GEOM_TYPE_MULTIPOINT = "multipoint";
    private static final String GEOM_TYPE_MULTILINESTRING = "multilinestring";
    private static final String GEOM_TYPE_MULTIPOLYGON = "multipolygon";

    /**
     * 判断当前几何是否属于multi类型
     *
     * @param geometry
     * @return
     */
    public static boolean isGeometryCollection(Geometry geometry) {
        boolean flag = false;
        if (geometry instanceof GeometryCollection || geometry.getGeometryType().toLowerCase().startsWith(GEOM_COLLECTION_PREFIX)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 获取几何对象的坐标串 针对简单几何类型
     *
     * @param geometry
     * @return
     */
    public static List<Coordinate> getGeomCoordinates(Geometry geometry) {
        List<Coordinate> list = new ArrayList<>();
        if (geometry instanceof Point) {
            list.add(geometry.getCoordinate());
        } else if (geometry instanceof LineString) {
            Coordinate[] coordinates = geometry.getCoordinates();
            list.addAll(Arrays.asList(coordinates));
        } else if (geometry instanceof Polygon) {
            Coordinate[] coordinates = geometry.getCoordinates();
            list.addAll(Arrays.asList(coordinates));
        }
        return list;
    }

    /**
     * 获取几何对象的坐标串 针对简单几何类型
     *
     * @param geometry
     * @return
     */
    public static String getGeomCoordinatesString(Geometry geometry) {
        List<Coordinate> list = getGeomCoordinates(geometry);
        StringBuffer stringBuffer = new StringBuffer();
        list.forEach(item -> stringBuffer.append(item.x).append(",").append(item.y).append(","));
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    /**
     * 获取几何对象坐标 针对Multi类型
     *
     * @param geometry
     * @return
     */
    public static List<List<Coordinate>> getGeomCoordinates(GeometryCollection geometry) {
        List<List<Coordinate>> list = new ArrayList<>();
        int num = geometry.getNumGeometries();
        for (int i = 0; i < num; i++) {
            List<Coordinate> list1 = Arrays.asList(geometry.getCoordinates());
            list.add(list1);
        }
        return list;
    }

    /**
     * 获取几何对象坐标串 针对Multi类型
     *
     * @param geometry
     * @return
     */
    public static String getGeomCoordinatesString(GeometryCollection geometry) {
        List<List<Coordinate>> list = getGeomCoordinates(geometry);
        StringBuffer stringBuffer = new StringBuffer();
        list.forEach(item -> {
            StringBuffer stringBuffer1 = new StringBuffer();
            item.forEach(sub -> stringBuffer1.append(sub.x).append(",").append(sub.y).append(","));
            stringBuffer.append(stringBuffer1.substring(0, stringBuffer1.length() - 1)).append(";");
        });
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

    /**
     * 根据坐标集合生成几何对象
     *
     * @param coordinates 坐标集合
     * @param geomType    几何类型
     * @return
     */
    public static Geometry createGeomByCoordinates(List<Coordinate> coordinates, String geomType) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
        if (GEOM_TYPE_POINT.equals(geomType.toLowerCase())) {
            return geometryFactory.createPoint(coordinates.get(0));
        } else if (GEOM_TYPE_LINESTRING.equals(geomType.toLowerCase())) {
            return geometryFactory.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
        } else if (GEOM_TYPE_POLYGON.equals(geomType.toLowerCase())) {
            return geometryFactory.createPolygon(coordinates.toArray(new Coordinate[coordinates.size()]));
        }
        return null;
    }

    /**
     * 根据坐标集合生成几何对象 针对Multi类型
     *
     * @param coordinateList 坐标集合
     * @param geomType       几何类型
     * @return
     */
    public static Geometry createGeomCollByCoordinates(List<List<Coordinate>> coordinateList, String geomType) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel());
        if (GEOM_TYPE_MULTIPOINT.equals(geomType.toLowerCase())) {
            List<Point> points = new ArrayList<>();
            for (int i = 0, len = coordinateList.size(); i < len; i++) {
                Geometry geometry = createGeomByCoordinates(coordinateList.get(i), GEOM_TYPE_POINT);
                if (geometry != null) {
                    points.add((Point) geometry);
                }
            }
            return geometryFactory.createMultiPoint(points.toArray(new Point[points.size()]));
        } else if (GEOM_TYPE_MULTILINESTRING.equals(geomType.toLowerCase())) {
            List<LineString> lineStrings = new ArrayList<>();
            for (int i = 0, len = coordinateList.size(); i < len; i++) {
                Geometry geometry = createGeomByCoordinates(coordinateList.get(i), GEOM_TYPE_LINESTRING);
                if (geometry != null) {
                    lineStrings.add((LineString) geometry);
                }
            }
            return geometryFactory.createMultiLineString(lineStrings.toArray(new LineString[lineStrings.size()]));
        } else if (GEOM_TYPE_MULTIPOLYGON.equals(geomType.toLowerCase())) {
            List<Polygon> polygons = new ArrayList<>();
            for (int i = 0, len = coordinateList.size(); i < len; i++) {
                Geometry geometry = createGeomByCoordinates(coordinateList.get(i), GEOM_TYPE_POLYGON);
                if (geometry != null) {
                    polygons.add((Polygon) geometry);
                }
            }
            return geometryFactory.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        }
        return null;
    }
}

package utils;

import bean.TransformGeomBean;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.io.ParseException;
import factory.GeoFactory;
import org.apache.commons.lang3.StringUtils;
import org.osgeo.proj4j.*;
import org.osgeo.proj4j.io.Proj4FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gisShield
 * @title: CoordTransformUtil
 * @projectName gis-tools
 * @description: 坐标转换工具类
 * @date 2021/5/4 11:21
 */
public class CoordinateTransformUtil {
    /**
     * 百度坐标系定义
     */
    private final static String BD09 = "BD09";
    /**
     * 高德/谷歌中国坐标系
     */
    private static final String GCJ02 = "GCJ02";

    /**
     * wgs 84坐标系
     */
    private static final String WGS84 = "EPSG:4326";

    private final static double XPI = Math.PI * 3000.0 / 180.0;
    private final static double OFFSET = 0.00669342162296594323;
    private final static double AXIS = 6378245.0;

    private static GeoFactory myGeometryFactory = new GeoFactory();
    private static Proj4FileReader proj4FileReader = new Proj4FileReader();

    public static Geometry transform(TransformGeomBean transformGeomBean) throws IOException, ParseException {
        String source = transformGeomBean.getFromCrsCode();
        String target = transformGeomBean.getToCrsCode();

        if (BD09.equals(source.toUpperCase()) || GCJ02.equals(source.toUpperCase())) {
            Geometry geometry = myGeometryFactory.getGeometry(transformGeomBean);
            if (BD09.equals(target.toUpperCase()) || GCJ02.equals(target.toUpperCase())) {
                return transformForOther(geometry, source, target);
            } else {
                return transformFormOther2EPSG(geometry, source, target);
            }
        } else {
            Geometry geometry = myGeometryFactory.getGeometry(transformGeomBean);
            if (BD09.equals(target.toUpperCase()) || GCJ02.equals(target.toUpperCase())) {
                return transformFormEPSG2Other(geometry, source, target);
            } else {
                return transformForEPSG(geometry, source, target);
            }
        }
    }

    /**
     * 从自定义转EPSG标准
     */
    public static Geometry transformFormOther2EPSG(Geometry geometry, String source, String target) throws IOException {
        //先转84 再转EPSG
        Geometry point = geometry;
        if (BD09.equals(source.toUpperCase())) {
            if (GeometryUtil.isGeometryCollection(geometry)) {
                List<List<Coordinate>> list = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
                List<List<Coordinate>> newList = new ArrayList<>();
                list.forEach(list1 -> {
                    List<Coordinate> coordinateList = new ArrayList<>();
                    for (Coordinate coordinate : list1) {
                        Coordinate newCoord = bd09ToWgs84(coordinate);
                        coordinateList.add(newCoord);
                    }
                    newList.add(coordinateList);
                });
                point = GeometryUtil.createGeomCollByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            } else {
                List<Coordinate> list = GeometryUtil.getGeomCoordinates(geometry);
                List<Coordinate> newList = new ArrayList<>();
                list.forEach(item -> newList.add(bd09ToWgs84(item)));
                point = GeometryUtil.createGeomByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            }
        } else if (GCJ02.equals(source.toUpperCase())) {
            if (GeometryUtil.isGeometryCollection(geometry)) {
                List<List<Coordinate>> list = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
                List<List<Coordinate>> newList = new ArrayList<>();
                list.forEach(list1 -> {
                    List<Coordinate> coordinateList = new ArrayList<>();
                    for (Coordinate coordinate : list1) {
                        Coordinate newCoord = gcj02ToWgs84(coordinate);
                        coordinateList.add(newCoord);
                    }
                    newList.add(coordinateList);
                });
                point = GeometryUtil.createGeomCollByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            } else {
                List<Coordinate> list = GeometryUtil.getGeomCoordinates(geometry);
                List<Coordinate> newList = new ArrayList<>();
                list.forEach(item -> newList.add(gcj02ToWgs84(item)));
                point = GeometryUtil.createGeomByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            }
        }
        point.setSRID(4326);
        // 如果指定的转化坐标系为84坐标系则直接输出，否则转化指定坐标系
        if (target.contains(":4326")) {
            // 直接生成Geom 返回
            return point;
        } else {
            // 转化成指定坐标系
            point = transformForEPSG(point, WGS84, target);
            return point;
        }
    }

    /**
     * 从EPSG标准转自定义
     */
    public static Geometry transformFormEPSG2Other(Geometry geometry, String source, String target) throws IOException {
        // 先转84 再转Other
        geometry = transformForEPSG(geometry, source, WGS84);
        Geometry newGeom = null;
        if (BD09.equals(target.toUpperCase())) {
            if (GeometryUtil.isGeometryCollection(geometry)) {
                List<List<Coordinate>> list = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
                List<List<Coordinate>> newList = new ArrayList<>();
                list.forEach(list1 -> {
                    List<Coordinate> coordinateList = new ArrayList<>();
                    for (Coordinate coordinate : list1) {
                        Coordinate newCoord = wgs84ToBd09(coordinate);
                        coordinateList.add(newCoord);
                    }
                    newList.add(coordinateList);
                });
                newGeom = GeometryUtil.createGeomCollByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            } else {
                List<Coordinate> list = GeometryUtil.getGeomCoordinates(geometry);
                List<Coordinate> newList = new ArrayList<>();
                list.forEach(item -> newList.add(wgs84ToBd09(item)));
                newGeom = GeometryUtil.createGeomByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            }
        } else if (GCJ02.equals(target.toUpperCase())) {
            if (GeometryUtil.isGeometryCollection(geometry)) {
                List<List<Coordinate>> list = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
                List<List<Coordinate>> newList = new ArrayList<>();
                list.forEach(list1 -> {
                    List<Coordinate> coordinateList = new ArrayList<>();
                    for (Coordinate coordinate : list1) {
                        Coordinate newCoord = wgs84ToGcj02(coordinate);
                        coordinateList.add(newCoord);
                    }
                    newList.add(coordinateList);
                });
                newGeom = GeometryUtil.createGeomCollByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            } else {
                List<Coordinate> list = GeometryUtil.getGeomCoordinates(geometry);
                List<Coordinate> newList = new ArrayList<>();
                list.forEach(item -> newList.add(wgs84ToGcj02(item)));
                newGeom = GeometryUtil.createGeomByCoordinates(newList, geometry.getGeometryType().toLowerCase());
            }
        }
        return newGeom;
    }

    /**
     * EPSG标准相互转
     */
    public static Geometry transformForEPSG(Geometry geometry, String source, String target) throws IOException {
        if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(target)) {
            String[] sourceArray = source.toLowerCase().split(":");
            String[] targetArray = target.toLowerCase().split(":");
            // 验证参数
            if (sourceArray.length == 2 && targetArray.length == 2) {
                if (sourceArray[1].equals(targetArray[1])) {
                    return geometry;
                }
                String[] paramStrSrc = proj4FileReader.readParametersFromFile(sourceArray[0], sourceArray[1]);
                String[] paramStrTar = proj4FileReader.readParametersFromFile(targetArray[0], targetArray[1]);

                CRSFactory targetFactory = new CRSFactory();
                //目标坐标系统
                CoordinateReferenceSystem sourceCRS = targetFactory.createFromParameters(sourceArray[1], paramStrSrc);
                //源坐标系统
                CoordinateReferenceSystem targetCRS = targetFactory.createFromParameters(targetArray[1], paramStrTar);

                CoordinateTransformFactory ctf = new CoordinateTransformFactory();
                CoordinateTransform transform = ctf.createTransform(sourceCRS, targetCRS);

                if (GeometryUtil.isGeometryCollection(geometry)) {
                    List<List<Coordinate>> coordinateList = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
                    List<List<Coordinate>> newList = new ArrayList<>();
                    coordinateList.forEach(list1 -> {
                        List<Coordinate> coordinateList1 = new ArrayList<>();
                        for (Coordinate coordinate : list1) {
                            ProjCoordinate projCoordinate = new ProjCoordinate(coordinate.x, coordinate.y);
                            transform.transform(projCoordinate, projCoordinate);
                            Coordinate coordinate1 = new Coordinate(projCoordinate.x, projCoordinate.y);
                            coordinateList1.add(coordinate1);
                        }
                        newList.add(coordinateList1);
                    });
                    return GeometryUtil.createGeomCollByCoordinates(newList, geometry.getGeometryType().toLowerCase());
                } else {
                    List<Coordinate> coordinateList = GeometryUtil.getGeomCoordinates(geometry);
                    List<Coordinate> newList = new ArrayList<>();
                    coordinateList.forEach(item -> {
                        ProjCoordinate projCoordinate = new ProjCoordinate(item.x, item.y);
                        transform.transform(projCoordinate, projCoordinate);
                        Coordinate coordinate1 = new Coordinate(projCoordinate.x, projCoordinate.y);
                        newList.add(coordinate1);
                    });
                    return GeometryUtil.createGeomByCoordinates(newList, geometry.getGeometryType().toLowerCase());
                }
            }
        }
        return null;

    }

    /**
     * 自定义标准相互转
     */
    public static Geometry transformForOther(Geometry geometry, String source, String target) {
        Geometry newGeom = geometry;
        if (BD09.equals(source.toUpperCase())) {
            if (GCJ02.equals(target.toUpperCase())) {
                if (GeometryUtil.isGeometryCollection(geometry)) {
                    List<List<Coordinate>> list = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
                    List<List<Coordinate>> newList = new ArrayList<>();
                    list.forEach(list1 -> {
                        List<Coordinate> coordinateList = new ArrayList<>();
                        for (Coordinate coordinate : list1) {
                            Coordinate newCoord = bd09ToGcj02(coordinate);
                            coordinateList.add(newCoord);
                        }
                        newList.add(coordinateList);
                    });
                    newGeom = GeometryUtil.createGeomCollByCoordinates(newList, geometry.getGeometryType().toLowerCase());
                } else {
                    List<Coordinate> list = GeometryUtil.getGeomCoordinates(geometry);
                    List<Coordinate> newList = new ArrayList<>();
                    list.forEach(item -> newList.add(bd09ToGcj02(item)));
                    newGeom = GeometryUtil.createGeomByCoordinates(newList, geometry.getGeometryType().toLowerCase());
                }
            }
        } else if (GCJ02.equals(source.toUpperCase())) {
            if (BD09.equals(target.toUpperCase())) {
                if (GeometryUtil.isGeometryCollection(geometry)) {
                    List<List<Coordinate>> list = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
                    List<List<Coordinate>> newList = new ArrayList<>();
                    list.forEach(list1 -> {
                        List<Coordinate> coordinateList = new ArrayList<>();
                        for (Coordinate coordinate : list1) {
                            Coordinate newCoord = gcj02ToBd09(coordinate);
                            coordinateList.add(newCoord);
                        }
                        newList.add(coordinateList);
                    });
                    newGeom = GeometryUtil.createGeomCollByCoordinates(newList, geometry.getGeometryType().toLowerCase());
                } else {
                    List<Coordinate> list = GeometryUtil.getGeomCoordinates(geometry);
                    List<Coordinate> newList = new ArrayList<>();
                    list.forEach(item -> newList.add(gcj02ToBd09(item)));
                    newGeom = GeometryUtil.createGeomByCoordinates(newList, geometry.getGeometryType().toLowerCase());
                }
            }
        }
        return newGeom;
    }

    /**
     * 百度坐标系转84坐标系
     *
     * @param coordinate
     * @return
     */
    private static Coordinate bd09ToWgs84(Coordinate coordinate) {
        // 判断点是否在国内
        // 在国内执行 bd -> gc02 -> wgs84
        if (coordinate != null) {
            if (isInChina(coordinate)) {
                Coordinate coordinateGcj02 = bd09ToGcj02(coordinate);
                return gcj02ToWgs84(coordinateGcj02);
            } else {
                return coordinate;
            }
        }
        return null;
    }

    /**
     * 84坐标系转百度坐标系
     *
     * @param coordinate
     * @return
     */
    private static Coordinate wgs84ToBd09(Coordinate coordinate) {
        if (coordinate != null) {
            if (isInChina(coordinate)) {
                Coordinate coordinateGcj02 = wgs84ToGcj02(coordinate);
                return gcj02ToBd09(coordinateGcj02);
            } else {
                return coordinate;
            }
        }
        return null;
    }

    /**
     * 火星坐标系转84坐标系
     *
     * @param coordinate
     * @return
     */
    private static Coordinate gcj02ToWgs84(Coordinate coordinate) {
        if (coordinate != null) {
            if (isInChina(coordinate)) {
                Coordinate coordinateTemp = wgs84ToGcj02(coordinate);
                if (coordinateTemp != null) {
                    double x = coordinate.x * 2 - coordinateTemp.x;
                    double y = coordinate.y * 2 - coordinateTemp.y;
                    return new Coordinate(x, y);
                }
            } else {
                return coordinate;
            }
        }
        return null;
    }

    /**
     * 84坐标系转火星坐标系
     *
     * @param coordinate
     * @return
     */
    private static Coordinate wgs84ToGcj02(Coordinate coordinate) {
        if (coordinate != null) {
            if (isInChina(coordinate)) {
                Coordinate coordinate1 = getDelta(coordinate);
                if (coordinate1 != null) {
                    return new Coordinate(coordinate.x + coordinate1.x, coordinate.y + coordinate1.y);
                }
            } else {
                return coordinate;
            }
        }
        return null;
    }

    /**
     * 火星坐标系转百度坐标系
     *
     * @param coordinate
     * @return
     */
    private static Coordinate gcj02ToBd09(Coordinate coordinate) {
        if (coordinate != null) {
            if (isInChina(coordinate)) {
                double z = Math.sqrt(Math.pow(coordinate.x, 2) + Math.pow(coordinate.y, 2)) + 0.00002 * Math.sin(coordinate.y * XPI);
                double theta = Math.atan2(coordinate.y, coordinate.x) + 0.000003 * Math.cos(coordinate.x * XPI);
                double x = z * Math.cos(theta) + 0.0065;
                double y = z * Math.sin(theta) + 0.006;
                return new Coordinate(x, y);
            } else {
                return coordinate;
            }
        }
        return null;
    }

    /**
     * 百度坐标系转火星坐标系
     *
     * @param coordinate
     * @return
     */
    private static Coordinate bd09ToGcj02(Coordinate coordinate) {
        if (coordinate != null) {
            if (isInChina(coordinate)) {
                double x = coordinate.x - 0.0065;
                double y = coordinate.y - 0.006;
                double z = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) - 0.00002 * Math.sin(y * XPI);
                double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * XPI);
                return new Coordinate(z * Math.cos(theta), z * Math.sin(theta));
            } else {
                return coordinate;
            }
        }
        return null;
    }

    /**
     * 判断是否在国内
     *
     * @param coordinate
     * @return
     */
    private static boolean isInChina(Coordinate coordinate) {
        return coordinate.x >= 72.004 && coordinate.x <= 137.8347 && coordinate.y >= 0.8293 && coordinate.y <= 55.8271;
    }

    private static Coordinate getDelta(Coordinate coordinate) {
        if (coordinate != null) {
            double x = coordinate.x;
            double y = coordinate.y;
            double tempX = transformLon(x - 105, y - 35);
            double tempY = transformLat(x - 105, y - 35);
            double radLat = y / 180 * Math.PI;
            double magic = Math.sin(radLat);

            magic = 1 - OFFSET * magic * magic;

            double sqrtMagic = Math.sqrt(magic);
            double dLon = (tempX * 180) / (AXIS / sqrtMagic * Math.cos(radLat) * Math.PI);
            double dLat = (tempY * 180) / ((AXIS * (1 - OFFSET)) / (magic * sqrtMagic) * Math.PI);
            return new Coordinate(dLon, dLat);
        }
        return null;
    }

    private static double transformLon(double x, double y) {
        double ret = 300 + x + 2 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret = ret + (20 * Math.sin(6 * x * Math.PI) + 20 * Math.sin(2 * x * Math.PI)) * 2 / 3;
        ret = ret + (20 * Math.sin(x * Math.PI) + 40 * Math.sin(x / 3 * Math.PI)) * 2 / 3;
        ret = ret + (150 * Math.sin(x / 12 * Math.PI) + 300 * Math.sin(x / 30 * Math.PI)) * 2 / 3;

        return ret;
    }

    private static double transformLat(double x, double y) {
        double ret = -100 + 2 * x + 3 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret = ret + (20 * Math.sin(6 * x * Math.PI) + 20 * Math.sin(2 * x * Math.PI)) * 2 / 3;
        ret = ret + (20 * Math.sin(y * Math.PI) + 40 * Math.sin(y / 3 * Math.PI)) * 2 / 3;
        ret = ret + (160 * Math.sin(y / 12 * Math.PI) + 320 * Math.sin(y * Math.PI / 30)) * 2 / 3;
        return ret;
    }

}

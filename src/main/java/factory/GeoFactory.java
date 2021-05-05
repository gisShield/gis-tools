package factory;

import bean.BaseGeomBean;
import bean.RelatedGeomBean;
import bean.TransformGeomBean;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import enums.DataTypeEnum;
import enums.GeomTypeEnum;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gisShield
 * @title: GeoFactory
 * @projectName gis-tools
 * @description: 几何对象工厂类
 * @date 2021/5/4 11:18
 */
public class GeoFactory {
    /**
     * 根据参数生成几何对象
     *
     * @param baseGeomBean
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public Geometry getGeometry(BaseGeomBean baseGeomBean) throws IOException, ParseException {
        GeomTypeEnum geomType = baseGeomBean.getGeomType();
        GeometryFactory geometryFactory;
        if (baseGeomBean instanceof RelatedGeomBean) {
            RelatedGeomBean relatedGeomBean = (RelatedGeomBean) baseGeomBean;
            geometryFactory = new GeometryFactory(new PrecisionModel(), relatedGeomBean.getSrid());
        } else if (baseGeomBean instanceof TransformGeomBean) {
            TransformGeomBean transformGeomBean = (TransformGeomBean) baseGeomBean;
            int srid = 0;
            if (transformGeomBean.getFromCrsCode().toLowerCase().startsWith("epsg:")) {
                srid = Integer.valueOf(transformGeomBean.getFromCrsCode().split(":")[1]);
            }
            geometryFactory = new GeometryFactory(new PrecisionModel(), srid);
        } else {
            geometryFactory = new GeometryFactory(new PrecisionModel());
        }
        Geometry geometry;
        switch (geomType) {
            case POINT:
                geometry = createPointGeometry(baseGeomBean, geometryFactory);
                break;
            case LINE:
                geometry = createLineStringGeometry(baseGeomBean, geometryFactory);
                break;
            case POLYGON:
                geometry = createPolygonGeometry(baseGeomBean, geometryFactory);
                break;
            case MULTIPOINT:
                geometry = createMultipointGeometry(baseGeomBean, geometryFactory);
                break;
            case MULTILINESTRING:
                geometry = createMultilinestringGeometry(baseGeomBean, geometryFactory);
                break;
            case MULTIPOLYGON:
                geometry = createMultipolygonGeometry(baseGeomBean, geometryFactory);
                break;
            default:
                geometry = null;
                break;
        }
        return geometry;
    }

    /**
     * 创建点几何对象
     *
     * @param baseGeomBean
     * @param geometryFactory
     * @return
     * @throws ParseException
     * @throws IOException
     */
    private Geometry createPointGeometry(BaseGeomBean baseGeomBean, GeometryFactory geometryFactory) throws ParseException, IOException {
        DataTypeEnum dataTypeEnum = baseGeomBean.getDataType();
        Geometry geometry;
        switch (dataTypeEnum) {
            case BASE:
                geometry = processBaseGeometry(baseGeomBean, geometryFactory);
                break;
            case WKT:
                WKTReader wktReader = new WKTReader(geometryFactory);
                geometry = wktReader.read(baseGeomBean.getCoordinates());
                break;
            case GEOJSON:
                GeometryJSON gjson = new GeometryJSON();
                Reader reader = new StringReader(baseGeomBean.getCoordinates());
                geometry = gjson.readPoint(reader);
                break;
            default:
                geometry = null;
                break;
        }
        return geometry;
    }

    /**
     * 创建线几何对象
     *
     * @param baseGeomBean
     * @param geometryFactory
     * @return
     * @throws ParseException
     * @throws IOException
     */
    private Geometry createLineStringGeometry(BaseGeomBean baseGeomBean, GeometryFactory geometryFactory) throws ParseException, IOException {
        DataTypeEnum dataTypeEnum = baseGeomBean.getDataType();
        Geometry geometry = null;
        switch (dataTypeEnum) {
            case BASE:
                geometry = processBaseGeometry(baseGeomBean, geometryFactory);
                break;
            case WKT:
                WKTReader wktReader = new WKTReader(geometryFactory);
                geometry = wktReader.read(baseGeomBean.getCoordinates());
                break;
            case GEOJSON:
                GeometryJSON gjson = new GeometryJSON();
                Reader reader = new StringReader(baseGeomBean.getCoordinates());
                geometry = gjson.readLine(reader);
                break;
            default:
                geometry = null;
                break;
        }
        return geometry;

    }

    /**
     * 创建面几何对象
     *
     * @param baseGeomBean
     * @param geometryFactory
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private Geometry createPolygonGeometry(BaseGeomBean baseGeomBean, GeometryFactory geometryFactory) throws IOException, ParseException {
        DataTypeEnum dataTypeEnum = baseGeomBean.getDataType();
        Geometry geometry = null;
        switch (dataTypeEnum) {
            case BASE:
                geometry = processBaseGeometry(baseGeomBean, geometryFactory);
                break;
            case WKT:
                WKTReader wktReader = new WKTReader(geometryFactory);
                geometry = wktReader.read(baseGeomBean.getCoordinates());
                break;
            case GEOJSON:
                GeometryJSON gjson = new GeometryJSON();
                Reader reader = new StringReader(baseGeomBean.getCoordinates());
                geometry = gjson.readPolygon(reader);
                break;
            default:
                geometry = null;
                break;
        }
        return geometry;

    }

    /**
     * 创建多点几何对象
     *
     * @param baseGeomBean
     * @param geometryFactory
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private Geometry createMultipointGeometry(BaseGeomBean baseGeomBean, GeometryFactory geometryFactory) throws IOException, ParseException {
        DataTypeEnum dataTypeEnum = baseGeomBean.getDataType();
        Geometry geometry;
        switch (dataTypeEnum) {
            case BASE:
                geometry = processBaseGeometry(baseGeomBean, geometryFactory);
                break;
            case WKT:
                WKTReader wktReader = new WKTReader(geometryFactory);
                geometry = wktReader.read(baseGeomBean.getCoordinates());
                break;
            case GEOJSON:
                GeometryJSON gjson = new GeometryJSON();
                Reader reader = new StringReader(baseGeomBean.getCoordinates());
                geometry = gjson.readMultiPoint(reader);
                break;
            default:
                geometry = null;
                break;
        }
        return geometry;

    }

    /**
     * 创建多线几何对象
     *
     * @param baseGeomBean
     * @param geometryFactory
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private Geometry createMultilinestringGeometry(BaseGeomBean baseGeomBean, GeometryFactory geometryFactory) throws IOException, ParseException {
        DataTypeEnum dataTypeEnum = baseGeomBean.getDataType();
        Geometry geometry;
        switch (dataTypeEnum) {
            case BASE:
                geometry = processBaseGeometry(baseGeomBean, geometryFactory);
                break;
            case WKT:
                WKTReader wktReader = new WKTReader(geometryFactory);
                geometry = wktReader.read(baseGeomBean.getCoordinates());
                break;
            case GEOJSON:
                GeometryJSON gjson = new GeometryJSON();
                Reader reader = new StringReader(baseGeomBean.getCoordinates());
                geometry = gjson.readMultiLine(reader);
                break;
            default:
                geometry = null;
                break;
        }
        return geometry;

    }

    /**
     * 创建多面几何对象
     *
     * @param baseGeomBean
     * @param geometryFactory
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private Geometry createMultipolygonGeometry(BaseGeomBean baseGeomBean, GeometryFactory geometryFactory) throws IOException, ParseException {
        DataTypeEnum dataTypeEnum = baseGeomBean.getDataType();
        Geometry geometry;
        switch (dataTypeEnum) {
            case BASE:
                geometry = processBaseGeometry(baseGeomBean, geometryFactory);
                break;
            case WKT:
                WKTReader wktReader = new WKTReader(geometryFactory);
                geometry = wktReader.read(baseGeomBean.getCoordinates());
                break;
            case GEOJSON:
                GeometryJSON gjson = new GeometryJSON();
                Reader reader = new StringReader(baseGeomBean.getCoordinates());
                geometry = gjson.readMultiPolygon(reader);
                break;
            default:
                geometry = null;
                break;
        }
        return geometry;
    }

    /**
     * 处理base的几何对象
     *
     * @param baseGeomBean
     * @param geometryFactory
     * @return
     */
    private Geometry processBaseGeometry(BaseGeomBean baseGeomBean, GeometryFactory geometryFactory) {
        GeomTypeEnum geomType = baseGeomBean.getGeomType();
        String points = baseGeomBean.getCoordinates();
        Geometry res;
        switch (geomType) {
            case POINT:
                Coordinate coordinate = processCoordinate(points);
                res = geometryFactory.createPoint(coordinate);
                break;
            case LINE:
                List<Coordinate> coordinates = processCoordinateList(points);
                res = geometryFactory.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
                break;
            case POLYGON:
                List<Coordinate> coordinateList = processCoordinateList(points);
                res = geometryFactory.createPolygon(coordinateList.toArray(new Coordinate[coordinateList.size()]));
                break;
            case MULTIPOINT:
                String[] multipointPoint = points.split(";");
                List<Point> pointArray = new ArrayList<>();
                for (String str :
                        multipointPoint) {
                    Coordinate coordinatPoint = processCoordinate(str);
                    pointArray.add(geometryFactory.createPoint(coordinatPoint));
                }
                res = geometryFactory.createMultiPoint(pointArray.toArray(new Point[pointArray.size()]));
                break;
            case MULTILINESTRING:
                String[] multiLinePoint = points.split(";");
                List<LineString> lineArray = new ArrayList<>();
                for (String str :
                        multiLinePoint) {
                    List<Coordinate> coordinatLineCoord = processCoordinateList(str);
                    lineArray.add(geometryFactory.createLineString(coordinatLineCoord.toArray(new Coordinate[coordinatLineCoord.size()])));
                }
                res = geometryFactory.createMultiLineString(lineArray.toArray(new LineString[lineArray.size()]));
                break;
            case MULTIPOLYGON:
                String[] multiPolygonPoint = points.split(";");
                List<Polygon> polygonArray = new ArrayList<>();
                for (String str :
                        multiPolygonPoint) {
                    List<Coordinate> coordinatLineCoord = processCoordinateList(str);
                    polygonArray.add(geometryFactory.createPolygon(coordinatLineCoord.toArray(new Coordinate[coordinatLineCoord.size()])));
                }
                res = geometryFactory.createMultiPolygon(polygonArray.toArray(new Polygon[polygonArray.size()]));
                break;
            default:
                res = null;
                break;
        }
        return res;
    }

    /**
     * 生成坐标对象
     *
     * @param str
     * @return
     */
    private Coordinate processCoordinate(String str) {
        String[] point = str.split(",");
        if (point.length != 2) {
            return null;
        }
        return new Coordinate(Double.valueOf(point[0]), Double.valueOf(point[1]));
    }

    /**
     * 生成坐标对象列表
     *
     * @param str
     * @return
     */
    private List<Coordinate> processCoordinateList(String str) {
        String[] linePoint = str.split(",");
        if (linePoint.length % 2 != 0) {
            return null;
        }
        List<Coordinate> array = new ArrayList<>();
        for (int i = 0, len = linePoint.length; i < len / 2; i++) {
            Coordinate coordinate = new Coordinate(Double.valueOf(linePoint[(i * 2)]), Double.valueOf(linePoint[(i * 2) + 1]));
            array.add(coordinate);
        }
        return array;
    }

}

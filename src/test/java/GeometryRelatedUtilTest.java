import bean.RelatedGeomBean;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTWriter;
import enums.DataTypeEnum;
import enums.GeomTypeEnum;
import factory.GeoFactory;
import org.junit.Test;
import utils.CoordinateTransformUtil;
import utils.GeometryBaseAnalysisUtil;
import utils.GeometryRelatedUtil;
import utils.GeometryUtil;

import java.io.IOException;
import java.util.List;

/**
 * @author gisShield
 * @title: GeometryRelatedUtilTest
 * @projectName gis-tools
 * @description: TODO
 * @date 2021/5/4 12:04
 */
public class GeometryRelatedUtilTest {
    GeoFactory myGeometryFactory = new GeoFactory();

    @Test
    public void disjointGeoTest() throws IOException, ParseException {
        // 测试点线相离
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POINT);
        point1.setDataType(DataTypeEnum.BASE);
        point1.setCoordinates("119.440,30.324");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.LINE);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("LINESTRING (119.440 30.539, 119.440 30.156)");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("点相离判断：" + GeometryRelatedUtil.disjointGeo(geom1, geom2));
    }

    @Test
    public void equalsGeoTest() throws IOException, ParseException {
        // 测试线线相等
        RelatedGeomBean relatedGeomBean1 = new RelatedGeomBean();
        relatedGeomBean1.setGeomType(GeomTypeEnum.LINE);
        relatedGeomBean1.setDataType(DataTypeEnum.BASE);
        relatedGeomBean1.setCoordinates("119.4401,30.539,119.440,30.156");
        RelatedGeomBean relatedGeomBean2 = new RelatedGeomBean();
        relatedGeomBean2.setGeomType(GeomTypeEnum.LINE);
        relatedGeomBean2.setDataType(DataTypeEnum.WKT);
        relatedGeomBean2.setCoordinates("LINESTRING (119.440 30.539, 119.440 30.156)");
        Geometry geom1 = myGeometryFactory.getGeometry(relatedGeomBean1);
        Geometry geom2 = myGeometryFactory.getGeometry(relatedGeomBean2);
        System.out.println("线线相等判断：" + GeometryRelatedUtil.equalsGeo(geom1, geom2));
    }

    @Test
    public void touchesGeoTest() throws IOException, ParseException {
        //测试线面相接
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.LINE);
        point1.setDataType(DataTypeEnum.BASE);
        // true 121.151931,30.284941,120.152074,30.295875
        point1.setCoordinates("120.151931,30.284941,120.152074,30.295875");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("线面相接判断：" + GeometryRelatedUtil.touchesGeo(geom1, geom2));
    }

    @Test
    public void crossesTest() throws IOException, ParseException {
        //测试线面交叉
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.LINE);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941,120.152074,30.295875
        point1.setCoordinates("120.1815,30.2847,120.1448,30.2847");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("线面相接判断：" + GeometryRelatedUtil.crossesGeo(geom1, geom2));
    }

    @Test
    public void withinGeoTest() throws IOException, ParseException {
        //测试点在面内
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POINT);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.1595,30.2818");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("点在面内判断：" + GeometryRelatedUtil.withinGeo(geom1, geom2));
    }

    @Test
    public void containsGeoTest() throws IOException, ParseException {
        //测试面包含点
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POINT);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.1595,30.2818");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("面包含点判断：" + GeometryRelatedUtil.containsGeo(geom2, geom1));
    }

    @Test
    public void overlapsGeoTest() throws IOException, ParseException {
        //测试面面交叠
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POLYGON);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.1554692,30.2872386,120.154711,30.280239,120.159814,30.279412,120.16834,30.28976,120.15899,30.29470,120.1554692,30.2872386");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("面面交叠判断：" + GeometryRelatedUtil.overlapsGeo(geom1, geom2));
    }

    @Test
    public void coversGeoTest() throws IOException, ParseException {
        //测试面面覆盖
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.MULTIPOLYGON);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.15547,30.28724,120.15471,30.28024,120.15981,30.27941,120.16398,30.28149,120.15905,30.28646,120.15547,30.28724;120.16425,30.28087,120.16036,30.27856,120.16539,30.27774,120.16425,30.28087");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("面面覆盖判断：" + GeometryRelatedUtil.coversGeo(geom2, geom1));
    }

    @Test
    public void coveredByGeoTest() throws IOException, ParseException {
        //测试线被面覆盖
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.MULTILINESTRING);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.15418,30.29101,120.15339,30.28739,120.15574,30.28856;120.15663,30.29011,120.17015,30.27718,120.15574,30.27698");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("线被面覆盖判断：" + GeometryRelatedUtil.coveredByGeo(geom1, geom2));
    }

    @Test
    public void intersectGeoTest() throws IOException, ParseException {
        // 测试线面相交
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.MULTILINESTRING);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.15418,30.29101,120.15339,30.28739,120.15574,30.28856;120.15663,30.29011,120.17015,30.27718,120.15574,30.27698");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        System.out.println("线面相交判断：" + GeometryRelatedUtil.intersectGeo(geom1, geom2));
    }

    @Test
    public void isInBufferTest() throws IOException, ParseException {
        RelatedGeomBean line = new RelatedGeomBean();
        line.setGeomType(GeomTypeEnum.LINE);
        line.setDataType(DataTypeEnum.BASE);
        // true 121.151931,30.284941,120.152074,30.295875
        line.setCoordinates("120.151931,30.284941,120.152074,30.295875");

        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POINT);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("121.1595,30.2818");
//        double angular = 1000.0/(Math.PI/180) / 6378137;
        Geometry geom1 = CoordinateTransformUtil.transformForEPSG(myGeometryFactory.getGeometry(line), "EPSG:4326", "EPSG:3857");
        Geometry geom2 = CoordinateTransformUtil.transformForEPSG(myGeometryFactory.getGeometry(point1), "EPSG:4326", "EPSG:3857");

        // buffer 以坐标系为单位 坐标系以米为单位 则buffer 为米 ，坐标系以度为单位 则buffer 为度
        Geometry geometry = GeometryBaseAnalysisUtil.getBuffer(geom1, 0.000001);
        System.out.println("缓冲区：" + GeometryRelatedUtil.isInBuffer(geometry, geom2));
    }

    @Test
    public void isWithinDistanceTest() throws IOException, ParseException {
        RelatedGeomBean line = new RelatedGeomBean();
        line.setGeomType(GeomTypeEnum.LINE);
        line.setDataType(DataTypeEnum.BASE);
        line.setSrid(4326);
        // true 121.151931,30.284941,120.152074,30.295875
        line.setCoordinates("120.151931,30.284941,120.152074,30.295875");

        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POINT);
        point1.setDataType(DataTypeEnum.BASE);
        line.setSrid(4326);
        // false 120.151931,30.284941
        point1.setCoordinates("121.1595,30.2818");
        // 进行米转度操作
        double angular = 0.000001;
        System.out.println(angular);
        Geometry geom1 = CoordinateTransformUtil.transformForEPSG(myGeometryFactory.getGeometry(line), "EPSG:4326", "EPSG:3857");
        Geometry geom2 = CoordinateTransformUtil.transformForEPSG(myGeometryFactory.getGeometry(point1), "EPSG:4326", "EPSG:3857");

        System.out.println("缓冲区：" + GeometryRelatedUtil.isWithinDistance(geom1, geom2, angular));
    }

    @Test
    public void geometryDifferenceTest() throws IOException, ParseException {
        //测试面面交叠
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POLYGON);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.1554692,30.2872386,120.154711,30.280239,120.159814,30.279412,120.16834,30.28976,120.15899,30.29470,120.1554692,30.2872386");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        Geometry geometry = GeometryRelatedUtil.geometryDifference(geom1, geom2);
        System.out.println("差异分析：" + geometry.getGeometryType());
        List<Coordinate> coordinateList = GeometryUtil.getGeomCoordinates(geometry);
        StringBuffer sb = new StringBuffer();
        for (Coordinate coord :
                coordinateList) {
            sb.append(coord.x + " " + coord.y + ",");
        }
        System.out.println(sb.toString());
    }

    @Test
    public void geometryUnionTest() throws IOException, ParseException {
//测试面面交叠
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POLYGON);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.1554692,30.2872386,120.154711,30.280239,120.159814,30.279412,120.16834,30.28976,120.15899,30.29470,120.1554692,30.2872386");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        Geometry geometry = GeometryRelatedUtil.geometryUnion(geom1, geom2);
        System.out.println("联合分析：" + geometry.getGeometryType());
        List<Coordinate> coordinateList = GeometryUtil.getGeomCoordinates(geometry);
        StringBuffer sb = new StringBuffer();
        for (Coordinate coord :
                coordinateList) {
            sb.append(coord.x + " " + coord.y + ",");
        }
        System.out.println(sb.toString());
    }

    @Test
    public void geometryIntersectionTest() throws IOException, ParseException {
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POLYGON);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.1554692,30.2872386,120.154711,30.280239,120.159814,30.279412,120.16834,30.28976,120.15899,30.29470,120.1554692,30.2872386");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        Geometry geometry = GeometryRelatedUtil.geometryIntersection(geom1, geom2);
        System.out.println("交叉分析：" + geometry.getGeometryType());
        List<Coordinate> coordinateList = GeometryUtil.getGeomCoordinates(geometry);
        StringBuffer sb = new StringBuffer();
        for (Coordinate coord :
                coordinateList) {
            sb.append(coord.x + " " + coord.y + ",");
        }
        System.out.println(sb.toString());
    }

    @Test
    public void geometrySymdifferenceTest() throws IOException, ParseException {
        RelatedGeomBean point1 = new RelatedGeomBean();
        point1.setGeomType(GeomTypeEnum.POLYGON);
        point1.setDataType(DataTypeEnum.BASE);
        // false 120.151931,30.284941
        point1.setCoordinates("120.1554692,30.2872386,120.154711,30.280239,120.159814,30.279412,120.16834,30.28976,120.15899,30.29470,120.1554692,30.2872386");
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point1);
        Geometry geom2 = myGeometryFactory.getGeometry(point2);
        Geometry geometry = GeometryRelatedUtil.geometrySymdifference(geom1, geom2);
        System.out.println("对称差异分析：" + geometry.getGeometryType());
        List<List<Coordinate>> coordinateList = GeometryUtil.getGeomCoordinates((GeometryCollection) geometry);
        for (List<Coordinate> coordList :
                coordinateList) {
            StringBuffer sb = new StringBuffer();
            for (Coordinate coord :
                    coordList) {
                sb.append(coord.x + " " + coord.y + ",");
            }
            System.out.println(sb.toString());
        }
    }

    @Test
    public void getEnvelope() throws IOException, ParseException {
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point2);
        Geometry geometry = GeometryBaseAnalysisUtil.getEnvelope(geom1);
        WKTWriter wktWriter = new WKTWriter();
        System.out.println(wktWriter.write(geometry));
    }

    @Test
    public void convexHull() throws IOException, ParseException {
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point2);
        Geometry geometry = GeometryBaseAnalysisUtil.convexHull(geom1);
        WKTWriter wktWriter = new WKTWriter();
        System.out.println(wktWriter.write(geometry));
    }

    @Test
    public void getCentroid() throws IOException, ParseException {
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = myGeometryFactory.getGeometry(point2);
        Geometry geometry = GeometryBaseAnalysisUtil.getCentroid(geom1);
        WKTWriter wktWriter = new WKTWriter();
        System.out.println(wktWriter.write(geometry));
    }

    @Test
    public void getLength() throws IOException, ParseException {
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = CoordinateTransformUtil.transformForEPSG(myGeometryFactory.getGeometry(point2), "EPSG:4326", "EPSG:3857");
        double length = GeometryBaseAnalysisUtil.getLength(geom1);

        System.out.println(length);
    }

    @Test
    public void getArea() throws IOException, ParseException {
        RelatedGeomBean point2 = new RelatedGeomBean();
        point2.setGeomType(GeomTypeEnum.POLYGON);
        point2.setDataType(DataTypeEnum.WKT);
        point2.setCoordinates("POLYGON ((120.152074 30.295875, 120.151816 30.276156, 120.176707 30.275637, 120.152074 30.295875))");
        Geometry geom1 = CoordinateTransformUtil.transformForEPSG(myGeometryFactory.getGeometry(point2), "EPSG:4326", "EPSG:3857");
        double area = GeometryBaseAnalysisUtil.getArea(geom1);
        System.out.println(String.valueOf(area));
    }

    @Test
    public void geojson2Geom() throws IOException, ParseException {
        RelatedGeomBean relatedGeomBean = new RelatedGeomBean();
        relatedGeomBean.setGeomType(GeomTypeEnum.POINT);
        relatedGeomBean.setDataType(DataTypeEnum.GEOJSON);
        relatedGeomBean.setCoordinates("{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[102,0.5]},\"properties\":{\"prop0\":\"value0\"}}");
        Geometry geom1 = myGeometryFactory.getGeometry(relatedGeomBean);
        System.out.println(GeometryUtil.getGeomCoordinatesString(geom1));
    }
}

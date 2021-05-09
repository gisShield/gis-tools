# gis-tools

## 1、背景

> 该仓库仅用于记录自身在项目实践过程中编写的一些GIS常用工具，不定时更新。



## 2、功能描述

> 1. 几何基本属性工具：提供几何的基本空间属性获取和操作
> 2. 坐标转换工具：提供百度，高德坐标系与标准坐标系的相互转化功能
> 3. 几何空间关系判断：提供几何关系（相交，包含等）判断



## 3、使用步骤

> 详细说明以及参考示例可见：[个人博客](https://darkgis.top),[CSDN](https://blog.csdn.net/u013420816/article/details/116423602)

### 3.1、基础类

1. BaseGeomBean：基本几何类型类

   | 名称        | 类型         | 说明     |
   | ----------- | ------------ | -------- |
   | geomType    | GeomTypeEnum | 几何类型 |
   | dataType    | DataTypeEnum | 数据类型 |
   | coordinates | String       | 坐标串   |

2. TransformGeomBean：坐标转换基础类，继承自`BaseGeomBean`

   | 名称        | 类型   | 说明             |
   | ----------- | ------ | ---------------- |
   | fromCrsCode | String | 几何对象源坐标系 |
   | toCrsCode   | String | 几何目标坐标系   |

   

3. RelatedGeomBean：空间关系判断基础类，继承自`BaseGeomBean`

   | 名称 | 类型 | 说明                                  |
   | ---- | ---- | ------------------------------------- |
   | srid | int  | 坐标参考系[EPSG代码](http://epsg.io/) |

### 3.2、几何基本属性工具：GeometryBaseAnalysisUtil

1. getBuffer(geometry,distance)

   > 给指定几何对象生成缓冲区

2. getEnvelope(geometry)

   > 获取结合对象的外包矩形

3. convexHull(geometry)

   > 获取几何对象最小凸包多边形

4. getCentroid(geometry)

   > 获取几何对象的质心点

5. getLength(geometry)

   > 获取长度 线几何返回长度 面几何返回周长，其他返回0.0

6. getArea(geometry)

   > 获取几何对象面积 面几何返回面积，其他几何返回0.0

### 3.3、 坐标转换工具：CoordinateTransformUtil

> 借助`proj4j`包进行实现坐标转换，补充百度，高德坐标系转换算法
>
> 提供以下转换功能：

1. transform(transformGeomBean)

   > 根据传入的 TransformGeomBean 进行坐标转换

2. transformFormOther2EPSG(geometry,source,target)

   > 给Geometry 从自定义坐标系转EPSG标准坐标系

3. transformFormEPSG2Other(geometry,source,target)

   > 给Geometry 从EPSG标准坐标系转自定义坐标系

4. transformForEPSG(geometry,source,target)

   > Geometry EPSG标准坐标系相互转化

5. transformForOther(geometry,source,target)

   > Geometry 自定义坐标系相互转化



### 3.4、几何空间关系判断：GeometryRelatedUtil

> 借助`jts`包进行几何空间关系判断，提供以下功能：

1. disjointGeo(geom1,geom2)

   > 测试此几何体geom1是否与几何体geom2不相交。 (几何形状没有共有的点)

2. equalsGeo(geom1,geom2)

   > 测试此geom1在拓扑上是否等于geom2。

3. touchesGeo(geom1,geom2)

   > 测试此几何geom1是否接触geom2几何。 (几何对象有至少一个公共的边界点，但是没有内部点。)

4. crossesGeo(geom1,geom2)

   > 测试此几何geom1是否穿过几何geom2。(存在共有的点，但并不全部共有)

5. withinGeo(geom1,geom2)

   > 测试几何体geom1是否在指定的几何geom2内。 (几何形状A的线都在几何形状B内部。)

6. containsGeo(geom1,geom2)

   > 测试此几何geom1是否包含几何geom2。

7. overlapsGeo(geom1,geom2)

   > 判断两个几何对象是否空间重叠

8. coversGeo(geom1,geom2)

   > 测试几何geom1是否覆盖几何geom2。

9. coveredByGeo(geom1,geom2)

   > 测试几何geom2是否覆盖此几何geom1。

10. intersectGeo(geom1,geom2)

    > 测试此几何geom1是否与几何geom2相交。

11. isInBuffer(geom1,geom2)

    > 判断geom2 在geom1 的缓冲区内

12. isWithinDistance(geom1,geom2,distance)

    > 测试从此geom1 到另一个 geom2 的距离是否小于或等于指定distance。

13. geometryDifference(geom1,geom2)

    > 差异分析，得到去除geom1 与geom2重叠部分剩下的geom1几何对象

14. geometryUnion(geom1,geom2)

    > 合并分析，得到几何对象geom1 和 geom2几何对象 合并成一个几何对象

15. geometryIntersection(geom1,geom2)

    > 叠加分析，得到geom1几何对象和geom2几何对象叠加部分

16. geometrySymdifference(geom1,geom2)

    > sym差异分析,得到去除geom1 与geom2重叠部分，剩下的geom1和geom2生成新的几何对象

## 4、作者

- GitHub：[gisShield](https://github.com/gisShield)
- Blog：[凌往昔GIS](https://darkgis.top/)
- CSDN：[凌往昔](https://blog.csdn.net/u013420816)
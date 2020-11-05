del -q bootstrap.jar
jar cvf0 bootstrap.jar -C out/production/diytomcat cn/ilqjx/diytomcat/Bootstrap.class -C out/production/diytomcat cn/ilqjx/diytomcat/classloader/CommonClassLoader.class
del -q lib/diytomcat.jar
cd out/production/diytomcat
jar cvf0 ../../../lib/diytomcat.jar *
cd../../..
java -cp bootstrap.jar cn.ilqjx.diytomcat.Bootstrap
pause
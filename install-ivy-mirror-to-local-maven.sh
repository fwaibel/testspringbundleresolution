curl http://build.eclipse.org/rt/virgo/ivy/bundles/release/org.eclipse.virgo.mirrored/javax.servlet/3.1.0.20150414/javax.servlet-3.1.0.20150414.jar > javax.servlet-3.1.0.20150414.jar
mvn install:install-file -Dfile=javax.servlet-3.1.0.20150414.jar -DgroupId=org.eclipse.virgo.mirrored -DartifactId=javax.servlet -Dversion=3.1.0.20150414 -Dpackaging=jar

curl http://build.eclipse.org/rt/virgo/ivy/bundles/release/org.eclipse.virgo.mirrored/javax.jms/1.1.0.v201205091237/javax.jms-1.1.0.v201205091237.jar > javax.jms-1.1.0.v201205091237.jar 
mvn install:install-file -Dfile=javax.jms-1.1.0.v201205091237.jar -DgroupId=org.eclipse.virgo.mirrored -DartifactId=javax.jms -Dversion=1.1.0.v201205091237 -Dpackaging=jar

curl http://build.eclipse.org/rt/virgo/ivy/bundles/release/org.eclipse.virgo.mirrored/javax.websocket/1.1.0.v201412180755/javax.websocket-1.1.0.v201412180755.jar > javax.websocket-1.1.0.v201412180755.jar
mvn install:install-file -Dfile=javax.websocket-1.1.0.v201412180755.jar -DgroupId=org.eclipse.virgo.mirrored -DartifactId=javax.websocket -Dversion=1.1.0.v201412180755 -Dpackaging=jar

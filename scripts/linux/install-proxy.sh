#!/bin/sh

# Installation des scripts de buid/deploy/exec d'un proxy
# Les scripts sont copiés dans le dossier /usr/local/bin et sont accessibles directement car présent dans PATH
#
# Demande les chemins aux repertoires necessaires (grails, java, template/instance tomcat) et construit les scripts
# relatifs à cette config

# @author Gregory Elleouet <gregory.elleouet@gmail.com>
#

# Config du user

echo -n "Java environnement"
echo -n "------------------"
read -p "Java home: " JAVA_HOME
read -p "Grails home: " GRAILS_HOME
read -p "Catalina home: " CATALINA_HOME

echo -n "Build environnement"
echo -n "-------------------"
read -p "Proxy path: " PROXY_PATH

echo -n "Deploy environnement"
echo -n "--------------------"
read -p "Instance path: " INSTANCE_PATH
read -p "HTTP port: " HTTP_PORT
read -p "HTTPS port: " HTTPS_PORT
read -p "AJP port: " AJP_PORT
read -p "SHUTDOWN port: " SHUTDOWN_PORT


# Init variable

PROXY_NAME=`basename $PROXY_PATH`
PATH_SCRIPT="/usr/local/bin"


# Construction du script build "[proxy-name]-build.sh"
# Execute la commande "war" de grails dans le dossier du proxy
# synchronise d'abord le projet avec le depot git

cat <<EOF > ${PATH_SCRIPT}/${PROXY_NAME}-build.sh
#!/bin/sh
JAVA_HOME="$JAVA_HOME"
GRAILS_HOME="$GRAILS_HOME"
cd $PROXY_PATH
git pull origin master
$GRAILS_HOME/bin/grails war
EOF

chmod +x ${PATH_SCRIPT}/${PROXY_NAME}-build.sh


# Construction du script deploy "[proxy-name]-deploy.sh"
# Cree une instance tomcat à partir d'un template
# il est possible de creer plusieurs instances en specifiant un id (numero) dans le contexte d'un cluster HA

cat <<EOF > ${PATH_SCRIPT}/${PROXY_NAME}-deploy.sh
#!/bin/sh
read -p "Instance ID [0-9]: " INSTANCE_ID
read -p "Proxy version [x.y.z]: " PROXY_VERSION
INSTANCE_NAME="${PROXY_NAME}-${INSTANCE_ID}"
INSTANCE="$INSTANCE_PATH/$INSTANCE_NAME"
WAR_FILE="$PROXY_PATH/build/libs/${PROXY_NAME}-${PROXY_VERSION}"

if [ ! -f "$WAR_FILE" ]; then
  echo "War not exist ! ($WAR_FILE)"
  exit 1
fi

if [ -z "$INSTANCE_ID" ]; then
  echo "Instance ID is required !"
  exit 1
fi

rm -r $INSTANCE
cp -r $CATALINA_HOME $INSTANCE
mkdir $INSTANCE/logs
mkdir $INSTANCE/temp
mkdir $INSTANCE/work
mkdir $INSTANCE/webapps
cp $WAR_FILE $INSTANCE/webapps/${PROXY_NAME}.war

sed -i -e "s/serverId.pid/${INSTANCE_NAME}.pid/g" $INSTANCE/bin/setenv.sh
sed -i -e "s/serverId=serverId/serverId=${INSTANCE_NAME}/g" $INSTANCE/bin/setenv.sh
sed -i -e "s/port=\"8005\"/port=\"${INSTANCE_ID}${SHUTDOWN_PORT}\"/g" $INSTANCE/conf/server.xml
sed -i -e "s/port=\"8080\"/port=\"${INSTANCE_ID}${HTTP_PORT}\"/g" $INSTANCE/conf/server.xml
sed -i -e "s/redirectPort=\"8443\"/redirectPort=\"${INSTANCE_ID}${HTTPS_PORT}\"/g" $INSTANCE/conf/server.xml
sed -i -e "s/port=\"8009\"/port=\"${INSTANCE_ID}${AJP_PORT}\"/g" $INSTANCE/conf/server.xml
EOF

chmod +x ${PATH_SCRIPT}/${PROXY_NAME}-deploy.sh


# Construction des  fichiers [proxy-name]-start et [proxy-name]-stop 
# Ces scripts prennent en paramètre l'ID de l'instance à commander

cat <<EOF > ${PATH_SCRIPT}/${PROXY_NAME}-start.sh
#!/bin/sh
read -p "Instance ID [0-9]: " INSTANCE_ID
INSTANCE_NAME="${PROXY_NAME}-${INSTANCE_ID}"
INSTANCE="$INSTANCE_PATH/$INSTANCE_NAME"
export JAVA_HOME="$JAVA_HOME"
export CATALINA_HOME="$CATALINA_HOME"
export CATALINA_BASE="$INSTANCE"
cd $CATALINA_BASE
$CATALINA_HOME/bin/startup.sh
EOF


cat << EOF > $INSTANCE_DIR/$SERVER_ID-stop.sh
#!/bin/sh
read -p "Instance ID [0-9]: " INSTANCE_ID
INSTANCE_NAME="${PROXY_NAME}-${INSTANCE_ID}"
INSTANCE="$INSTANCE_PATH/$INSTANCE_NAME"
export JAVA_HOME="$JAVA_HOME"
export CATALINA_HOME="$CATALINA_HOME"
export CATALINA_BASE="$INSTANCE"
$CATALINA_HOME/bin/shutdown.sh
EOF

chmod +x ${PATH_SCRIPT}/${PROXY_NAME}-start.sh
chmod +x ${PATH_SCRIPT}/${PROXY_NAME}-stop.sh

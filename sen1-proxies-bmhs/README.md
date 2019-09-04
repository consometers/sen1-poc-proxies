# sen1-proxies-bmhs

## 1. Présentation

Proxy de données entre la fédération et l'application BeMyHomeSmart. Les données
à extraire et à envoyer sur BMHS transitent par [l'API publique](https://github.com/gelleouet/smarthome-application/wiki/API)

Ce proxy est développé avec le framework [Grails](https://grails.org/) en version
3.3.8 et est déployé sous forme de webapp dans un container Web [Tomcat](http://tomcat.apache.org/)
avec un JDK v8.

## 2. Utilisation

Le projet étant développé avec le framework Grails, toutes les opérations de commande
peuvent s'exécuter depuis la console interactive Grails. Celle-ci requiert 2
variables d'environnement :

- JAVA_HOME : path vers le JDK
- GRAILS_HOME : path vers le framework Grails

Lancement de la console :

	 bash> grails

Lancement du proxy depuis la console Grails :

	 grails> run-app

Lancement du proxy en mode debug depuis la console Grails :

	 grails> run-app -debug-jvm

Construction de l'archive war en vue d'un déploiement en production :

	 grails> war

Le proxy s'appuie une base de données PostgreSQL version <= 9.6 (version 10 non
testée). La base de données peut être initialisée avec le script _ddl.sql_ enregistré
dans le dossier _resources/sql_ du projet parent _sen1-poc-proxies_

## 3. Développement

### 3.1. Librairies

- **grails** : c'est le framework de développement du projet et il intègre nativement
de noombreuses librairies pour gérer la construction d'une webapp. Les librairies
Spring, Hibernate sont déjà préconfigurées pour démarrer l'application sur une
base de données. De nombreuses autres librairies sont intégrées : les
libs commons apache, les libs de conversion json/xml, etc.

- **quartz** : librairie pour gérer les tâches planifiées à la manière du cron Linux.
[doc officielle](http://www.quartz-scheduler.org/)

- **rocks xmpp** : librairie pour la gestion du protocole XMPP. [doc officielle](https://sco0ter.bitbucket.io/babbler/)

- **sen1-poc-core** : librairie interne pour le développement de proxy en Java.
Contient toutes les classes communes à chaque proxy et permet de développer un
proxy plus rapidement. Sont inclus les entités de base (inbox, outbox), les services
abstraits (proxy, data, federation)

### 3.2. Structure du projet

- **grails-app** : le dossier principal d'un projet sous Grails et structuration
des couches du modèle MVC. Les dossiers principaux sont :
    - controllers : C du modèle MVC  
    - services : services métier et d'accès aux données  
    - views : V du modèle MVC  
    - domain : entités mappées en base de données  
    - conf : configuration du projet (datasource, config projet, logger, spring)  
    - assets : les resources html (css, javascript, images)  

- **src/main/groovy** : les sources Groovy

- **src/main/java** : les sources Java

- **test/groovy** : les tests unitaires

### 3.3. Assemblage

L'utilisation du projet annexe sen1-proxy-core permet de récupérer toutes les
classes principales d'un proxy. Afin d'adapter le comportement de chaque proxy
à la gestion d'un système local spécifique, les classes génériques doivent être
"branchées" sur des implémentations spécifiques. Cet assemblage se fait dans le
fichier du container applicatif Spring _conf/resources/spring.groovy_.

Ce fichier doit préciser des composants obligatoires (les noms des beans doivent
être respectés pour le bon fonctionnement de l'injection de dépendances) :  

- **defaultScheduler** : le gestionnaire de tâches planifiées avec la liste des jobs
à exécuter. Les implémentations classiques des jobs sont enregistrées dans le
projet _sen1-proxy-core_, package _sen1.proxies.core.job_

- **federationService** : le bean prenant en charge les échanges avec le protocole
fédéré. 

- **messageConsumer** : le handler de message pour la réception des messages de la
fédération. Ce bean sera utilisé par le service "federationService" qui délèguera
le traitement des messages reçus au consumer

- **messageSerializer** : le serialiser de message pour convertir les objets Java
au format XML. Ce serialiser doit respecter le formalisme choisi par la fédération,
en l'occurence le format SenML

Ensuite, un service transversal assurant le lien entre tous les composants doit
être déclaré et implémenter le contrat _sen1.proxies.core.service.ProxyService_
défini dans le projet sen1-proxy-poc. Ce contrat définit les opérations de base
pour extraire et injecter des données dans le système local.

## 4. Production

Service systemd dans fichier /etc/systemd/system/sen1-proxy-bmhs.service :

	[Unit]
	Description=SEN1 Proxy BMHS Service
	After=syslog.target network.target

	[Service]
	Type=forking
	Environment="JAVA_HOME=path_to_jdk"
	Environment="CATALINA_HOME=path_to_apache_tomcat"
	Environment="CATALINA_BASE=path_to_apache_tomcat_template_with_war"
	PIDFile=/var/run/sen1-proxy-bmhs.pid
	ExecStart=path_to_apache_tomcat/bin/startup.sh
	ExecStop=/bin/kill -15 $MAINPID
	Restart=on-failure
	RestartSec=5s
	TimeoutSec=300

	[Install]
	WantedBy=multi-user.target
	
Rechargement de la config du service :
	
	systemctl daemon-reload
	
Démarrage auto du service :

	systemctl enable sen1-proxy-bmhs
	
Démarrage/Arrêt manuel du service :

	systemctl start sen1-proxy-bmhs
	systemctl stop sen1-proxy-bmhs
	

	
	

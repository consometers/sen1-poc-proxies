# sen1-proxies-lognact

## 1. Présentation

Proxy de données entre la fédération et l'application lognact sous [Zabbix](https://www.zabbix.com/)  
Ce proxy est développé en Python et utilise [l'API ZAbbix](https://www.zabbix.com/documentation/4.2/manual/api)
pour la connexion avec le système local.


## 2. Configuration

Avec une version de python >= 3.6.
S'assurer que le programme pip est installé. Sinon (sous Linux)
> apt install python3-pip

Pour compilation du driver PostgreSQL
> apt-get install postgresql-server-dev-9.6

Installation (depuis le dossier du projet) d'un script d'exécution
> python setup.py install    
ou     
> python3 setup.py install    

Exécution du programme
> lognact  
ou  
> python -m proxieslognact.proxy    
ou      
> python3 -m proxieslognact.proxy     

Prérequis runtime
* créer les variables d'environnement : 
    * DATASOURCE_URL (ex : postgresql://user:password@host:port/database)
    * PROXY_ENV : [dev, prod (défaut)] permet d'ajuster les loggers et d'autres paramètres en fonction environnement d'exécution
    
* le proxy requiert un service PostreSQL version 9.6 (pas testé avec les versions
supérieures). La structure de la base de données peut être créée avec le fichier
_resources/sql/ddl.sql_ du projet

Pour l'installation en tant que service, un fichier de type service systemd est
présent dans le dossier _resources/systemd_. Le proxy sera alors démarré dans ce
contexte en mode "production"


## 3. Développement

### 3.1. Librairies

- **schedule** : planificateur de tâches. Gére l'exécution automatisée des jobs.
Les implémentations des jobs se situent dans le dossier _proxieslognact/job_. Le
référencement des jobs est fait manuellement au démarrage du proxy dans le module
principal _proxieslognact/proxy_. Les intervalles d'exécution des jobs sont configurés
dans le fichier root _init_. Précision : les jobs ne sont pas planifiés à la manière
d'un cron Linux à X heure ou X minute mais déclenchés tous les X intervalles à
à partir du démarrage du proxy.

- **sqlalchemy** : framework ORM / SQL. Gère la connexion à la base de données et le
mapping ORM des entités mappées en base. Utilisé aussi pour requêter sur les entités
La connexion à la base de données est fournie avec la variable d'environnement
_DATASOURCE_URL_

- **sleekxmpp** : librairie pour la gestion du protocole XMPP. Les identifiants
de connexion au serveur XMPP sont récupérés via les objets _Config_ mappés en base

- **pyzabbix, protobix** : librairies pour les échanges avec le serveur Zabbix.
**pyzabbix** est utilisé pour communiquer en HTTP avec l'API Zabbix (récupération
des historiques de données). **protobix** est utilisé pour communiquer avec le
protocole Zabbix Sender pour envoyer les données vers Zabbix.

### 3.2. Structure du projet

- **proxy.py** : module principal du projet. Démarre le proxy, le gestionnaire de
jobs, le service XMPP

- **application.py** : container application. Gère l'instanciation des beans et les
injections de dépendances (IOC) entre beans

- **__init__.py** : configuration générale. Déclaration des _beans_ et du container
applicatif, logger et config du projet dans _settings_

- **proxieslognact/api** : gestion api zabbix pour envoi et récupération des données

- **proxieslognact/federation** : gestion protocole et message du réseau fédéré XMPP
    - **handler** : service de gestion des messages 
    - **serializer** : service de conversion des messages 

- **proxieslognact/job** : implémentation des jobs planifiés. A minima, 3 implémentations
sont prévues :
    - **FetchInboxJob** : traite les messages en attente du réseau fédéré et les envoit
    sur Zabbix
    - **FetchOutboxJob** : traite les messages en attente extraits de Zabbix et les envoit
    sur la fédération
    - **PushOutboxJob** : extrait les historiques de données de Zabbix et les envoit sur
    la fédération (ou sur la boite à lettres Inbox en fonction du handler de message) 

- **proxieslognact/model** : déclaration des entités mappés en base (ie Config,
App, Consumer, etc.)

- **proxieslognact/persistance** : modules pour la gestion des accès bases de données.

- **proxieslognact/service** : services métier et d'accès aux données

- **proxieslognact/util** : modules utilitaires
 
### 3.3. Container applicatif

Afin d'éviter les dépendances cycliques entre module et l'instanciation d'objets
directement dans le code du projet, un container applicatif a été mis en oeuvre pour gérer
ces opérations et mettre en place l'injection de dépendances (IOC) entre ces objets.
Le code est ainsi plus évolutif et facilement maintenable car changer d'implémentation
d'un objet est centralisé à un seul endroit, dans la configuration principale
du projet _ __init__.py_. Ce container est géré dans le module _proxieslognact/application.py_.
Il s'appuie sur les beans déclarés dans le fichier root ___init__.py_. 
Ce système injecte aussi automatiquement un logger dans chaque bean instancié.

### 3.4. Transactions

Les transactions sont gérées dans la couche métier au travers d'un décorator _@transactionnal_.
disponible dans le module _proxieslognact/persistance/datasource.py_
En pratique, ce decorator peut s'appliquer sur n'importe quel objet/méthode mais
il est recommandé de ne l'utiliser que dans la couche service. Ce decorator prend
en charge tout l'aspect technique de création/commit/rollback/fermeture session
pour la gestion de la persistance / requêtage des entités.

Pour l'utiliser, il faut :

- Annoter la méthode du service avec le decorator @transactionnal  
- Ajouter un paramètre keyword _session_ avec valeur par défaut _None_ en dernier
paramètre. Avec cette version, c'est le fait que le paramètre soit en dernière
position qui est important et non le fait qu'il soit nommé _session_. Par contre,
en terme d'amélioration, l'implémentation du decorator devrait s'appuyer sur le
keyword pour une utilisation plus simple.

Cette implémentation est thread-safe car la session crée est instanciée dans le 
thread local. Dès lors qu'une session est créée dans le thread local, elle
sera utilisée pendant tout le traitement de la transaction et pour tous les services
déclarant une transaction. Une transaction sera exécutée par défaut en lecture seule.
Si une opération d'écriture est exécutée dans une transaction en lecture seule, il
n'y aura pas d'erreur déclenchée mais les données ne seront pas persistées en base.
Pour passer une transaction en écriture, utiliser le paramètre _readonly_ du decorator.

### 3.5. Message handler

Dans cette version, les messages à envoyer ou reçus transitent systématiquement
par les boites à lettres inbox et outbox. Ce fonctionnement a un léger inconvénient,
c'est qu'il entraine une légère latence sur le traitement des messages, car il dépend
de l'intervalle d'exécution des jobs. Dans le cas des extractions de données, le
1er job extrait les données et les stockent dans la outbox à la 1ère passe. Si le
job de traitement de cette boite à lettres s'est exécuté en même temps, la donnée 
ne sera alors traitée qu'à la passe suivante du job de traitement de la outbox.

Ce fonctionnement peut être changé très facilement car des handler de message sont
injectés dans les jobs et c'est leurs implémentations qui déterminent ce fonctionnement.
En créant de nouvelles implémentations et en les injectant des les jobs, la gestion
des messages peut être complètement changée. On peut par exemple créer une implémentation
qui essaie d'abord d'envoyer le message sur le réseau fédéré, et si le message n'est
pas envoyé, alors il est stocké dans la boite outbox. Cela peut renforcer le
caractère "temps réel" du proxy. 

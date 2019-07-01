# sen1-poc-proxies

## 1. Présentation

Proxies de données entre la fédération et les applications connectées. Les proxy
sont construits globalement de la même façon mais peuvent être développés avec des
langages différents (java, python) ou sur des modèles différents (webapp, daemon).

Le principe reste le même :
- aspirer des données d'un système local  et les envoyer sur un autre système à
travers le réseau fédéré (fetch)
- recevoir des données de systèmes tiers et les envoyer vers un système local (push)

## 2. Réseau fédéré

### 2.1 Protocole XMPP

Le protocole choisi pour échanger les données à travers la fédération est XMPP.
Les proxy s'appuient sur des serveurs [Ejabberd](https://www.ejabberd.im/) qui
prennent en charge le routage des messages. Les proxy ne sont donc que des clients
XMPP.

### 2.2 Formalisme SenML

Les messages échangés sur le réseau sont au format XMPP. Le corps du message
contenant les données échangées est au format [SenML](https://tools.ietf.org/html/draft-ietf-core-senml-16)

## 3. Architecture

### 3.1 Inbox / Outbox

Les données échangées transitent via des "boîtes à lettres" à la manière d'une
application fédérée pour réseau social :
- **inbox** : stocke les messages reçus de la fédération et en attente d'intégration
dans le système local
- **outbox** : stocke les messages extraits du système local et en attente d'envoi
vers la fédération.

Ces boîtes à lettres ne sont pas obligatoires pour le fonctionnement des proxy
mais servent de stockage temporaire en cas de problème d'envoi. Si le service ne
répond pas au moment de la transaction, la donnée n'est perdue et peut être traitée
ultérieurement.

### 3.2 Gestionnaire de tâches / Jobs

La partie extraction des données depuis le système local n'est pas événementielle.
C'est-à-dire que les données ne sont envoyées sur le réseau fédéré suite à un événement
survenu dans le système local (cela pourrait être le cas par la suite). Donc un
gestionnaire de tâche (CRON) est intégré dans chaque proxy pour planifier les extractions
de nouvelles données de manière régulière. Des tâches sont aussi créées pour traiter
les boîtes à lettres et traiter les messages en attente.

Il existe au minimum 3 jobs dans chaque proxy :
- **Push Outbox** : pousse les données vers la Outbox. Ce job extrait les données du
système local avec l'API correspondante et stocke les informations dans la boîte 
à lettres.
- **Fetch Outbox** : charge les messages en attente de la Outbox (issu du système local)
et les envoit sur le réseau fédéré
- **Fetch Inbox** : charge les messages en attente de la Inbox (issu du réseau fédéré)
et les envoit sur le système local

### 3.3 Evénements fédération

Les messages de la fédération sont gérés sous forme d'événements. Il sont donc
traités en temps réel dès leurs réceptions. chaque proxy peut par contre décider
si ces messages transitent par la boîte à lettres _Inbox_ ou s'ils sont immédiatement
envoyés sur le système local (ils seront stockés dans la boîte _Inbox_ en cas 
d'erreur d'envoi et traités ensuite par le job associé).

### 3.4 Consumers

Les données à extraire et leurs destinations sont gérées avec les consumers. Un
consumer est une information indiquant quelle donnée doit être extraite (identifiant
de l'objet, caractéristique (nom, unité, etc.) et pour qui cette donnée est destinée.
Pour chaque consumer il faut associer des credentials pour la connexion au système local
et l'identifiant du destinaire sur le réseau fédéré (jid pour le protocole XMPP).
Le proxy assurant l'extraction des données depuis le système local ne dispose pas
des credentials pour la connexion au système cible. C'est le proxy de la fédération
qui recevra le message qui gèrera la connexion au système cible.

## 4. Modèle base de données

Les proxy développés s'appuient sur une base de données PostgreSQL pour enregistrer
de façon permanente les informations de configuration et les messages temporaires
de la férédation.

### 4.1 Entités

- **Config** : les informations de configuration du proxy. Ex : les identifiants 
clients du proxy pour la connexion au serveur XMPP, les urls de connexion au
système local
- **App** : la liste des applications de la fédération avec leurs identifiants XMPP.
Cet identifiant est utilisé pour la création du header d'un message XMPP (from, to).
Seules les applications connues du proxy (ie celles à qui ils envoient des messages)
ont besoin d'être enrgistrées dans cette table. Le process de référencement des
applications ne fait pas l'objet de cette documentation).
- **User** : la liste des utilisateurs enrgistrés en tant que consumer avec leurs
identifiants sur la fédération. Cet identifant est unique sur l'ensemble des
applications connectées à la fédération
- **UserApp** : les credentials d'un utilisateur pour une application (en l'occurence
l'application du sytème local). Cela permet de gérer des rôles individuels pour
chaque utilisateur du sytème local
- **Inbox** : Les données reçues de la fédération et en attente de traitement pour 
envoi vers le système local. Le format des datas enregistrées dans la boite à lettre
dépend de chaque proxy. Celui peut décider d'enregistrer le message tel quel,
et dans ce cas, le message est au format XMPP/SenML. Sinon, il peut très bien décider
de faire une 1ère serialisation du message et l'enregistrer avec son propre format.
Le choix n'a aucune incidence sur le fonctionnement général du proxy. Cela dépend
surtout de la manière de traiter les messages et de la simplicité à effectuer les
conversions.
- **Outbox** : Les données extraites du système local et en attente d'envoi sur le
réseau fédéré. De la même façon que la boite à lettre Inbox, le format choisi pour
enregistrer le message dépend du proxy. Le message peut être enregistré dans un 
format quelconque et ensuite converti au format XMPP/SenML juste avant l'envoi sur
le réseau fédéré
- **Consumer** : une association d'un user avec la donnée source (à extraire du
système local) et de l'application cible (sur le réseau fédéré). Donnée optionnelle
de mapping avec les identifiants objets du système source et du système cible.
Cela apporte de la souplesse sur les transferts de données entre des objets existants
de chaque système

### 4.2 Relations

Consumer  
    userApp -> UserApp (@ManyToOne) : les identifiants de connexion du user dans le système
  		local  
    consumerApp -> App (@ManyToOne) : l'application destinatrice du message


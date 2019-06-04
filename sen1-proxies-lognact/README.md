# sen1-proxies-lognact
Proxy de données entre la fédération et l'application lognact

## Développement

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


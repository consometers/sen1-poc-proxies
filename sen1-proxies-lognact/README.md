# sen1-proxies-lognact
Proxy de données entre la fédération et l'application lognact

## Développement

Installation (depuis le dossier du projet) d'un script d'exécution
> python setup.py install

Exécution du programme
> lognact  
ou  
> python -m proxieslognact.proxy

Prérequis runtime
* créer les variables d'environnement : 
    * DATASOURCE_URL (ex : postgresql://user:password@host:port/database)
    * PROXY_ENV : [dev, prod (défaut)] permet d'ajuster les loggers et d'autres paramètres en fonction environnement d'exécution

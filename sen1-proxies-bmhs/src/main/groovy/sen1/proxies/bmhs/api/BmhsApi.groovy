package sen1.proxies.bmhs.api

import org.grails.web.json.JSONElement

/**
 * L'interface API pour l'accès à l'application BMHS
 * 
 * @see https://github.com/gelleouet/smarthome-application/wiki/API
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface BmhsApi {
	/**
	 * Envoi des données sur l'application BMHS
	 * 
	 * @param url connexion au serveur
	 * @param message datas à envoyer
	 * 
	 * @throws Exception
	 */
	void push(String url, BmhsPushMessage message) throws Exception


	/**
	 * Récupération de données depuis l'application BMHS
	 * 
	 * @param url connexion au serveur
	 * @param message paramètres de la requete
	 * @return liste de valeur
	 * 
	 * @throws Exception
	 */
	List<JSONElement> fetch(String url, BmhsFetchMessage message) throws Exception
}

package sen1.proxies.bmhs.api

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
	void push(String url, BmhsMessage message) throws Exception
}

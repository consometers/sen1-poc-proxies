package sen1.proxies.core.service

import sen1.proxies.core.Consumer
import sen1.proxies.core.io.Message

/**
 * Service ProxyService
 * 
 * Contrat à implémenter par le service d'un proxy pour gérer les échanges avec le système du proxy
 *  - Permet d'envoyer des données sur le système local "pushData"
 *  - Permet de lire des données depuis le système local "fetchData"
 * 
 * <T> Le type de données renvoyé par le système local
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface ProxyService<T> {
	/**
	 * Lecture des datas dans le système local pour un consumer
	 * !! Les données sont renvoyées sans transformation
	 * 
	 * @param consumer
	 * @return
	 * @throws Exception
	 */
	List<T> fetchData(Consumer consumer) throws Exception

	/**
	 * Envoit de nouvelles données vers le système local
	 * 
	 * @param message
	 * @throws Exception
	 */
	void pushData(Message message) throws Exception
}

package sen1.proxies.core.io

import sen1.proxies.core.DataTypeEnum

/**
 * Représentation d'un message transitant sur le réseau fédéré
 * Un message fait transiter des données de type <T> d'une application A vers B
 * 
 * Pour construire un message, toujours utiliser le MessageBuilder
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
trait Message {
	/**
	 * Nom/identifiant de la données
	 */
	String name
	/**
	 * Si donnée multi-value ou clé multiple, indique le nom/identifiant de la méta-donnée associée
	 */
	String metaname
	/**
	 * Si donnée multi-value ou clé multiple, indique la valeur de la méta-donnée associée
	 */
	String metavalue
	/**
	 * Unité (si valeur métrique?)
	 */
	String unite
	/**
	 * Type de la données
	 */
	DataTypeEnum type
	/**
	 * Nom de l'application émettrice du message
	 */
	String applicationSrc
	/**
	 * Nom de l'application destinatrice du message
	 */
	String applicationDst
	/**
	 * Liste de valeurs de données
	 */
	List<MessageData> datas = []


	/**
	 * Construit une instance data
	 * Le type dépend de l'implémentation du message
	 * 
	 * @return
	 */
	abstract MessageData newMessageDataInstance()


	/**
	 * La date de la valeur la plus récente dans la liste des datas
	 * 
	 * @return
	 */
	Date dateLastValue() {
		MessageData lastData = datas?.max { it.timestamp }
		return lastData?.timestamp
	}
}

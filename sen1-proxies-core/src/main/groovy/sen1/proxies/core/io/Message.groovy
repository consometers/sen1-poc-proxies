package sen1.proxies.core.io

import sen1.proxies.core.DataTypeEnum

/**
 * Représentation d'un message transitant sur le réseau fédéré
 * Un message fait transiter des données de type <T> d'une application A vers B
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
trait Message<T extends MessageData> {
	/**
	 * Nom/identifiant de la données
	 */
	String name
	/**
	 * Nom/identifiant de la sous-donnée si donnée multi-valeur
	 */
	String metaname
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
	List<T> datas = []
}

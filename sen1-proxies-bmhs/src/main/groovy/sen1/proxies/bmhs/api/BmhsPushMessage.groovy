package sen1.proxies.bmhs.api

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

/**
 * Un message pour envoyer des données 
 * 
 * @see https://github.com/gelleouet/smarthome-application/wiki/API
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Builder(builderStrategy=SimpleStrategy, prefix="")
class BmhsPushMessage {
	/**
	 *  [required] Token de connexion à l'API
	 */
	String token

	/**
	 * [required] Identifiant de l'application envoyant le message
	 * Cet identifiant doit correspondre à l'ID d'une application tierce référencée dans BMHS
	 * 
	 * @see menu Applications
	 */
	String application

	/**
	 * [required] Identifiant/mac d'un objet référencé dans le système
	 * 
	 * @see smarthome.automation.Device.mac
	 */
	String name

	/**
	 * [optional] Si objet multi-value, précise le name/identifiant de la metadatas associée à la valeur envoyée
	 *  
	 * @see smarthome.automation.DeviceMetavalue.name
	 */
	String metaname

	/**
	 * [optional] L'unité de mesure des valeurs de cet objet
	 * 
	 * @see smarthome.automation.DeviceMetavalue.unite
	 */
	String unite

	/**
	 * Les données du message
	 */
	List<BmhsMessageData> datas = []


	/**
	 * Ajoute une nouvelle donnée
	 * 
	 * @param value
	 * @param timestamp
	 * 
	 * @return this
	 */
	BmhsPushMessage addData(Object value, Date timestamp) {
		datas << new BmhsMessageData(value: value, timestamp: timestamp.time)
		return this
	}


	/**
	 * Ajoute une nouvelle donnée
	 *
	 * @param value
	 * @param timestamp en millisecond
	 *
	 * @return this
	 */
	BmhsPushMessage addData(Object value, long timestamp) {
		datas << new BmhsMessageData(value: value, timestamp: timestamp)
		return this
	}
}

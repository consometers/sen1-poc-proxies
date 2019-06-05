package sen1.proxies.bmhs.api

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

/**
 * Un message pour recevoir des données 
 * 
 * @see https://github.com/gelleouet/smarthome-application/wiki/API
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Builder(builderStrategy=SimpleStrategy, prefix="")
class BmhsFetchMessage {
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
	 * [optional] début de sélection d'un période
	 */
	Date start

	/**
	 * [optional] fin de sélection d'un période
	 */
	Date end

	/**
	 * [optional] sens du tri : asc | desc
	 */
	String order = "asc"

	/**
	 * [optional] Limite le nombre de résultats retournés
	 */
	Long limit

	/**
	 * [optional] index de pagination
	 */
	Long offset = 0
}

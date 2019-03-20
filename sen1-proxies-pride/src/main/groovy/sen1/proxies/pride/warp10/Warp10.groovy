package sen1.proxies.pride.warp10

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONElement

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

/**
 * Classe de manipulation de l'API Warp10 https://www.warp10.io/
 * Class de de type Fluent pour chainer les appels de méthode sur l'instance
 * 
 * Implémentation thead-safe : un même objet peut être utilisé plusieurs fois pour exécuter différents appels
 * à l'API
 * 
 * @see https://www.warp10.io/doc/reference
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
trait Warp10 {

	/**
	 * Vérifie les paramètres avant exécution d'un appel à l'API
	 */
	void asserts() {
	}

	/**
	 * Exécution d'un appel fetch pour retrouver les données au format "text"
	 * 
	 * The text format uses a less verbose version of the GTS Input Format as output format.
	 * In this format the data for each GTS is grouped and classname and labels are only written in the first line
	 * of each group.
	 *
	 * Documentation :
	 * The Fetch API allows to recover raw GTS data in a extremely quick and efficient way.
	 * The Fetch API must be accessed using the GET method.
	 * The HTTP endpoint used by the Fetch API is https://HOST:PORT/api/vX/fetch, where HOST:PORT is a valid endpoint
	 * for the Warp 10™ instance and vX is the version of the API you want to use (currently v0).
	 * Note that the amount of data that can be retrieved via the /fetch endpoint is not limited in any way.
	 * You can use this endpoint to dump all the data accessible with a given token. The time needed for data retrieval
	 * is dependent on the size of the retrieved dataset.
	 * 
	 * @param url server
	 * @param fetchParam
	 * @return
	 * @throws Exception
	 */
	abstract String fetchText(String url, Warp10Fetch fetchParam) throws Exception

	/**
	 * Exécution d'un appel fetch pour retrouver les données au format "fulltext"
	 * 
	 * The fulltext format uses the GTS Input Format as output format.
	 * 
	 * Documentation :
	 * The Fetch API allows to recover raw GTS data in a extremely quick and efficient way.
	 * The Fetch API must be accessed using the GET method.
	 * The HTTP endpoint used by the Fetch API is https://HOST:PORT/api/vX/fetch, where HOST:PORT is a valid endpoint
	 * for the Warp 10™ instance and vX is the version of the API you want to use (currently v0).
	 * Note that the amount of data that can be retrieved via the /fetch endpoint is not limited in any way.
	 * You can use this endpoint to dump all the data accessible with a given token. The time needed for data retrieval
	 * is dependent on the size of the retrieved dataset.
	 * 
	 * @param url
	 * @param fetchParam
	 * @return
	 * @throws Exception
	 */
	abstract String fetchFulltext(String url, Warp10Fetch fetchParam) throws Exception

	/**
	 * Exécution d'un appel "fetch" pour retrouver les données au format "json"
	 * 
	 * Documentation :
	 * The Fetch API allows to recover raw GTS data in a extremely quick and efficient way.
	 * The Fetch API must be accessed using the GET method.
	 * The HTTP endpoint used by the Fetch API is https://HOST:PORT/api/vX/fetch, where HOST:PORT is a valid endpoint
	 * for the Warp 10™ instance and vX is the version of the API you want to use (currently v0).
	 * Note that the amount of data that can be retrieved via the /fetch endpoint is not limited in any way.
	 * You can use this endpoint to dump all the data accessible with a given token. The time needed for data retrieval
	 * is dependent on the size of the retrieved dataset.
	 * 
	 * @param url
	 * @param fetchParam
	 * @return
	 * @throws Exception
	 */
	abstract JSONElement fetchJson(String url, Warp10Fetch fetchParam) throws Exception

	/**
	 * Exécution d'un appel "exec" pour exécuter un WarpScript et retourner du contenu JSON
	 *
	 * Documentation :
	 * The WarpScript™ data manipulation language can be used using a Warp 10™ API. This API is served by the egress
	 * component, also referred to as the Warp 10™ Analytics Engine.
	 * The WarpScript™ API must be accessed using the POST method.
	 * The HTTP endpoint used by the WarpScript™ API is https://HOST:PORT/api/vX/exec, where HOST:PORT is a valid
	 * endpoint for the public Warp 10™ API and vX is the version of the API you want to use (currently v0).
	 *
	 * @param url
	 * @param warpScript
	 * @return
	 * @throws Exception
	 */
	abstract JSONElement exec(String url, Warp10Script warpScript) throws Exception

	/**
	 * Parse le contenu d'une réponse Warp10 et renvoit les datapoints de la série correspondant à l'index demandé
	 * 
	 * @param warp10Result
	 * @param index
	 * @return liste datapoints
	 * @throws Exception
	 */
	abstract List<JSONArray> datapoints(JSONElement warp10Result, int index) throws Exception

	/**
	 * Parse un datapoint au format json et retourne sa valeur
	 * 
	 * @param jsonValue
	 * @return
	 * @throws Exception
	 */
	abstract String datapointValue(JSONArray jsonValue) throws Exception

	/**
	 * Parse un datapoint au format json et retourne sa date
	 *
	 * @param jsonValue
	 * @return
	 * @throws Exception
	 */
	abstract Date datapointTimestamp(JSONArray jsonValue) throws Exception
}
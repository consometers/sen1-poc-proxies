package sen1.proxies.pride.warp10

import org.grails.web.json.JSONElement

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import sen1.proxies.core.DateUtils
import sen1.proxies.core.http.Http
import sen1.proxies.core.http.transformer.JsonResponseTransformer
import sen1.proxies.core.http.transformer.ResponseTransformer
import sen1.proxies.core.http.transformer.StringResponseTransformer
import sen1.proxies.pride.warp10.script.Warp10Script


/**
 * Classe de manipulation de l'API Warp10 https://www.warp10.io/
 * Class de de type Fluent pour chainer les appels de méthode sur l'instance
 * 
 * Implémentation thead-safe : un même objet peut être utilisé plusieurs fois pour exécuter différents appels
 * à l'API
 * 
 * @see https://www.warp10.io/doc/reference
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Builder(builderStrategy=SimpleStrategy, prefix="")
class Warp10 {
	/**
	 * API version
	 */
	String apiVersion = "0"

	/**
	 * Protocole pour la requête HTTP
	 * 
	 */
	String protocol = "https"

	/**
	 * Infos de connexion au serveur Warp10
	 * Comme le protocole est par défaut https, le port aussi
	 */
	String server
	int port = 443



	/**
	 * Vérifie les paramètres avant exécution d'un appel à l'API
	 */
	void asserts() {
		assert server != null
		assert protocol != null
		assert apiVersion != null
		assert port > 0
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
	 * @param fetchParam
	 * @return
	 * @throws Exception
	 */
	String fetchText(Warp10Fetch fetchParam) throws Exception {
		return this.fetch(fetchParam, Warp10FormatEnum.text)
	}


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
	 * @param fetchParam
	 * @return
	 * @throws Exception
	 */
	String fetchFulltext(Warp10Fetch fetchParam) throws Exception {
		return this.fetch(fetchParam, Warp10FormatEnum.fulltext)
	}


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
	 * @param fetchParam
	 * @return
	 * @throws Exception
	 */
	JSONElement fetchJson(Warp10Fetch fetchParam) throws Exception {
		return this.fetch(fetchParam, Warp10FormatEnum.json)
	}


	/**
	 * Méthode interne pour faire les appels fetch
	 * Le type retourné dépend du paramètre format
	 * 
	 * @param fetchParam
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private Object fetch(Warp10Fetch fetchParam, Warp10FormatEnum format) throws Exception {
		this.asserts()
		assert fetchParam != null
		fetchParam.asserts()

		Http http = Http.Get("${protocol}://${server}:${port}/api/v${apiVersion}/fetch")
				.header("X-Warp10-Token", fetchParam.token)
				.queryParam("selector", fetchParam.selector)
				.queryParam("format", format.toString())

		// gère les paramètres optionnels
		if (fetchParam.dedup) {
			http.queryParam("dedup", "true")
		}
		if (fetchParam.showattr) {
			http.queryParam("showattr", "true")
		}
		if (fetchParam.showmeta) {
			http.queryParam("showmeta", "true")
		}
		if (fetchParam.showerrors) {
			http.queryParam("showerrors", "true")
		}

		// les paramètres pour sélectionner la période des données : 2 modes possibles
		if (fetchParam.start && fetchParam.stop) {
			http.queryParam("start", DateUtils.formatDateTimeIso(fetchParam.start))
					.queryParam("stop", DateUtils.formatDateTimeIso(fetchParam.stop))
		} else if (fetchParam.now && fetchParam.timespan) {
			http.queryParam("now", "${fetchParam.now.time * 1000}")
					.queryParam("timespan", fetchParam.timespan)
		}

		ResponseTransformer transformer = (format == Warp10FormatEnum.json ? new JsonResponseTransformer() :
				new StringResponseTransformer())

		return http.execute(transformer)?.content
	}


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
	 * @param warpScript
	 * @return
	 * @throws Exception
	 */
	JSONElement exec(Warp10Script warpScript) throws Exception {
		this.asserts()
		assert warpScript != null
		warpScript.asserts()

		Http http = Http.Post("${protocol}://${server}:${port}/api/v${apiVersion}/exec")
				.bodyString(warpScript.script())

		return http.execute(new JsonResponseTransformer())?.content
	}
}

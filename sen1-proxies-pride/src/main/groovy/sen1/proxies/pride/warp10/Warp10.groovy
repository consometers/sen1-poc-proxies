package sen1.proxies.pride.warp10

import org.grails.web.json.JSONElement

import sen1.proxies.core.DateUtils
import sen1.proxies.core.http.Http
import sen1.proxies.core.http.transformer.JsonResponseTransformer
import sen1.proxies.core.http.transformer.ResponseTransformer
import sen1.proxies.core.http.transformer.StringResponseTransformer


/**
 * Classe de manipulation de l'API Warp10 https://www.warp10.io/
 * Class de de type Fluent pour chainer les appels de méthode sur l'instance
 * 
 * Implémentation thead-safe : un même objet peut être utilisé plusieurs fois pour exécuter différents appels
 * à l'API
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Warp10 {
	/**
	 * Authentification sur le serveur
	 */
	private String token

	/**
	 * API version
	 */
	private String version = "0"

	/**
	 * Protocole pour la requête HTTP
	 * 
	 */
	private String protocol = "https"

	/**
	 * Infos de connexion au serveur Warp10
	 * Comme le protocole est par défaut https, le port aussi
	 */
	private String server
	private int port = 443


	/**
	 * contructor privé pour forcer l'utilisation du builder
	 * 
	 * @param token
	 * @param server
	 * @param port
	 */
	private Warp10(String server, String token) {
		this.token = token
		this.server = server
	}


	/**
	 * Point d'entrée pour créer une instance Warp10
	 * 
	 * @param token
	 * @return
	 */
	static Warp10 build(String server, String token) {
		assert token != null : "token is required !"
		assert server != null : "server is required !"
		return new Warp10(server, token)
	}


	/**
	 * Change la version de l'API
	 * 
	 * @param version
	 * @return
	 */
	Warp10 version(String version) {
		this.version = version
		return this
	}


	/**
	 * Change le protocole
	 *
	 * @param protocol
	 * @return
	 */
	Warp10 protocol(String protocol) {
		this.protocol = protocol
		return this
	}


	/**
	 * Change le port
	 *
	 * @param port
	 * @return
	 */
	Warp10 port(int port) {
		this.port = port
		return this
	}


	/**
	 * Exécution d'un appel fetch pour retrouver les données au format "text"
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
	 * @param fetchParam
	 * @return
	 * @throws Exception
	 */
	String fetchFulltext(Warp10Fetch fetchParam) throws Exception {
		return this.fetch(fetchParam, Warp10FormatEnum.fulltext)
	}


	/**
	 * Exécution d'un appel fetch pour retrouver les données au format "json"
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
	 * 
	 * @param fetchParam
	 * @param format
	 * @return
	 * @throws Exception
	 */
	private Object fetch(Warp10Fetch fetchParam, Warp10FormatEnum format) throws Exception {
		assert fetchParam != null
		fetchParam.assertFetch()

		Http http = Http.Get("${protocol}://${server}:${port}/api/v${version}/fetch")
				.header("X-Warp10-Token", token)
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
			http.queryParam("now", "${fetchParam.now}")
					.queryParam("timespan", fetchParam.timespan)
		}

		ResponseTransformer transformer = (format == Warp10FormatEnum.json ? new JsonResponseTransformer() :
				new StringResponseTransformer())

		return http.execute(transformer)?.content
	}
}

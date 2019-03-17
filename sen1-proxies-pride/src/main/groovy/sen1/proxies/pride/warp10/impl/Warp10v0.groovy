package sen1.proxies.pride.warp10.impl

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONElement
import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy
import sen1.proxies.core.DateUtils
import sen1.proxies.core.http.Http
import sen1.proxies.core.http.transformer.JsonResponseTransformer
import sen1.proxies.core.http.transformer.ResponseTransformer
import sen1.proxies.core.http.transformer.StringResponseTransformer
import sen1.proxies.pride.warp10.Warp10
import sen1.proxies.pride.warp10.Warp10Fetch
import sen1.proxies.pride.warp10.Warp10FormatEnum
import sen1.proxies.pride.warp10.Warp10Script


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
class Warp10v0 implements Warp10 {


	/**
	 * Default constructor
	 */
	Warp10v0() {
		apiVersion = "0"
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.pride.warp10.Warp10#fetchText(sen1.proxies.pride.warp10.Warp10Fetch)
	 */
	@Override
	String fetchText(Warp10Fetch fetchParam) throws Exception {
		return this.fetch(fetchParam, Warp10FormatEnum.text)
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.pride.warp10.Warp10#fetchFulltext(sen1.proxies.pride.warp10.Warp10Fetch)
	 */
	@Override
	String fetchFulltext(Warp10Fetch fetchParam) throws Exception {
		return this.fetch(fetchParam, Warp10FormatEnum.fulltext)
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.pride.warp10.Warp10#fetchJson(sen1.proxies.pride.warp10.Warp10Fetch)
	 */
	@Override
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


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.pride.warp10.Warp10#exec(sen1.proxies.pride.warp10.Warp10Script)
	 */
	@Override
	JSONElement exec(Warp10Script warpScript) throws Exception {
		this.asserts()
		assert warpScript != null
		warpScript.asserts()

		Http http = Http.Post("${protocol}://${server}:${port}/api/v${apiVersion}/exec")
				.bodyString(warpScript.script())

		return http.execute(new JsonResponseTransformer())?.content
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.pride.warp10.Warp10#parseDatapoints(org.grails.web.json.JSONElement, int)
	 */
	@Override
	List<JSONArray> datapoints(JSONElement warp10Result, int index) throws Exception {
		if (! (warp10Result instanceof JSONArray)) {
			throw new Exception("Warp10 result must be a valid array/list !")
		}

		JSONArray array = warp10Result as JSONArray
		assert index < array.size()

		return array[index].v
	}


	/** 
	 * Valeur toujours en dernière position
	 * @see https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/04_Fetching_data/02_GTS_output_format
	 *
	 * @see sen1.proxies.pride.warp10.Warp10#parseDatapointValue(org.grails.web.json.JSONArray)
	 */
	@Override
	String datapointValue(JSONArray jsonValue) throws Exception {
		assertDatapoint(jsonValue)
		return jsonValue.last().toString()
	}


	/** 
	 * timestamp toujours en 1èere position
	 * @see https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/04_Fetching_data/02_GTS_output_format
	 *
	 * @see sen1.proxies.pride.warp10.Warp10#parseDatapointTimestamp(org.grails.web.json.JSONArray)
	 */
	@Override
	Date datapointTimestamp(JSONArray jsonValue) throws Exception {
		assertDatapoint(jsonValue)
		// TODO : micro ou milli second
		return new Date((jsonValue.first() as Long) / 1000)
	}


	/**
	 * Vérifie un datapoint
	 * 
	 * @param jsonValue
	 * 
	 * @see https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/04_Fetching_data/02_GTS_output_format
	 */
	private void assertDatapoint(JSONArray jsonValue) {
		assert jsonValue != null
		assert jsonValue.size() >= 2 && jsonValue.size() <= 5
	}
}

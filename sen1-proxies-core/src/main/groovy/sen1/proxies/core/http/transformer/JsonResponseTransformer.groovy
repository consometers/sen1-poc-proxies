package sen1.proxies.core.http.transformer

import org.grails.web.json.JSONElement

import grails.converters.JSON

/**
 * Implémentation response transformer pour une conversion en objet Json
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class JsonResponseTransformer implements ResponseTransformer<JSONElement> {

	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.core.http.transformer.ResponseTransformer#transform(java.io.InputStream)
	 */
	@Override
	JSONElement transform(InputStream inputStream) throws Exception {
		if (inputStream) {
			String content = inputStream.text

			if (content) {
				return JSON.parse(content)
			}
		}

		return null
	}
}

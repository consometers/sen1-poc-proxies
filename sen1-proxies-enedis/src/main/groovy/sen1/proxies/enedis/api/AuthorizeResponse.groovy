package sen1.proxies.enedis.api

import org.apache.commons.lang.StringUtils

/**
 * AuthorizeResponse
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AuthorizeResponse {
	String code
	String usagePointId


	/**
	 * Vérification response
	 *
	 * @throws Exception
	 */
	void asserts() {
		assert StringUtils.isNotEmpty(code)
		assert StringUtils.isNotEmpty(usagePointId)
	}
}

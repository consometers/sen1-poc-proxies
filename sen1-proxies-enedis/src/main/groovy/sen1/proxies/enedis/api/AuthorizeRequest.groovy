/**
 * 
 */
package sen1.proxies.enedis.api

import org.apache.commons.lang.StringUtils

/**
 * AuthorizeRequest
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AuthorizeRequest {
	String clientId
	String redirectUri
	String duration
	String state


	/**
	 * Vérification request
	 *
	 * @throws Exception
	 */
	void asserts() {
		assert StringUtils.isNotEmpty(redirectUri)
		assert StringUtils.isNotEmpty(clientId)
		assert StringUtils.isNotEmpty(duration)
		assert StringUtils.isNotEmpty(state)
	}
}

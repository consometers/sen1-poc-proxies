/**
 * 
 */
package sen1.proxies.enedis.api

import org.apache.commons.lang.StringUtils

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class TokenResponse {
	String accessToken
	String tokenType
	Long expiresIn
	String refreshToken
	String scopes
	String issuedAt
	String refreshTokenIssuedAt


	/**
	 * Vérification response
	 *
	 * @throws Exception
	 */
	TokenResponse asserts() {
		assert StringUtils.isNotEmpty(accessToken)
		assert StringUtils.isNotEmpty(tokenType)
		assert tokenType != null
		assert StringUtils.isNotEmpty(refreshToken)
		assert StringUtils.isNotEmpty(scopes)
		assert StringUtils.isNotEmpty(issuedAt)
		assert StringUtils.isNotEmpty(refreshTokenIssuedAt)
		return this
	}
}

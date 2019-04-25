package sen1.proxies.enedis.api

import org.apache.commons.lang.StringUtils

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class TokenRequest {
	String redirectUri
	GrantTypeEnum grantType
	String clientId
	String clientSecret

	String code
	String refreshToken


	/**
	 * Vérification request
	 * 
	 * @throws Exception
	 */
	TokenRequest asserts() {
		assert StringUtils.isNotEmpty(redirectUri)
		assert StringUtils.isNotEmpty(clientId)
		assert StringUtils.isNotEmpty(clientSecret)
		assert grantType != null

		if (grantType == GrantTypeEnum.authorization_code) {
			assert StringUtils.isNotEmpty(code)
		} else if (grantType == GrantTypeEnum.refresh_token) {
			assert StringUtils.isNotEmpty(refreshToken)
		}
		
		return this
	}
}

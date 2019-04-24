/**
 * 
 */
package sen1.proxies.enedis.api

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
}

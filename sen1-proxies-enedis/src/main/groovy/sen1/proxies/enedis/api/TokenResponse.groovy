/**
 * 
 */
package sen1.proxies.enedis.api

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class TokenResponse {
	String accessToken
	String tokenType
	Long expiresIn
	String refreshToken
	List<String> scopes = []
	Date issuedAt
	Date refreshTokenIssuedAt


	/**
	 * Vérification response
	 *
	 * @throws Exception
	 */
	void asserts() {
	}
}

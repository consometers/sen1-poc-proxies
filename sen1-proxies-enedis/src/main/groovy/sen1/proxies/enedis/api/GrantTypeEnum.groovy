/**
 * 
 */
package sen1.proxies.enedis.api

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
enum GrantTypeEnum {
	/**
	 * utilisaiton d’un code d’autorisation
	 */
	authorization_code,
	/**
	 * utilisation d’un jeton de rafraîchissement
	 */
	refresh_token
}

/**
 * 
 */
package sen1.proxies.enedis.api.impl

import grails.util.Environment
import sen1.proxies.enedis.api.AuthorizeRequest
import sen1.proxies.enedis.api.DataConnect
import sen1.proxies.enedis.api.TokenRequest
import sen1.proxies.enedis.api.TokenResponse

/**
 * Impl DataConnect v0
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DataConnectV0 implements DataConnect {
	
	private static final Map URLS = [
		(Environment.DEVELOPMENT): [
			authorize: "https://gw.hml.api.enedis.fr/group/espace-particuliers/consentement-linky"
		]
	] 
	
	/**
	 * 
	 */
	@Override
	void authorize(AuthorizeRequest request) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * 
	 */
	@Override
	TokenResponse token(TokenRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

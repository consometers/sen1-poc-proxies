/**
 * 
 */
package sen1.proxies.enedis.api.impl

import grails.util.Environment
import sen1.proxies.enedis.api.AuthorizeRequest
import sen1.proxies.enedis.api.AuthorizeResponse
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
			authorize: "https://gw.hml.api.enedis.fr/group/espace-particuliers/consentement-linky",
			token: "https://gw.hml.api.enedis.fr"
		],
		(Environment.PRODUCTION): [
			authorize: "https://espace-client-particuliers.enedis.fr/group/espace-particuliers/consentement-linky",
			token: "https://gw.prd.api.enedis.fr"
		]
	]


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.enedis.api.DataConnect#authorize(sen1.proxies.enedis.api.AuthorizeRequest)
	 */
	@Override
	AuthorizeResponse authorize(AuthorizeRequest request) throws Exception {
		// TODO Auto-generated method stub

	}


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.enedis.api.DataConnect#token(sen1.proxies.enedis.api.TokenRequest)
	 */
	@Override
	TokenResponse token(TokenRequest request) throws Exception {
		// TODO Auto-generated method stub
		return null
	}
}

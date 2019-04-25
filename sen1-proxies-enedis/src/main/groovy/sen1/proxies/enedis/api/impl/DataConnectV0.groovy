/**
 * 
 */
package sen1.proxies.enedis.api.impl

import org.grails.web.json.JSONElement

import grails.util.Environment
import sen1.proxies.core.http.Http
import sen1.proxies.core.http.transformer.JsonResponseTransformer
import sen1.proxies.enedis.api.AuthorizeRequest
import sen1.proxies.enedis.api.AuthorizeResponse
import sen1.proxies.enedis.api.DataConnect
import sen1.proxies.enedis.api.GrantTypeEnum
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
		return null
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.enedis.api.DataConnect#token(sen1.proxies.enedis.api.TokenRequest)
	 */
	@Override
	TokenResponse token(TokenRequest request) throws Exception {
		request.asserts()

		Http httpRequest = Http.Get("${URLS[Environment.getCurrentEnvironment()].token}/v1/oauth2/token")
				.queryParam("redirect_uri", request.redirectUri)
				.formField("client_id", request.clientId)
				.formField("client_secret", request.clientSecret)
				.formField("grant_type", request.grantType.toString())

		JSONElement result = httpRequest.execute(new JsonResponseTransformer())?.content
		TokenResponse response

		if (result) {
			response = new TokenResponse(result)
		}

		response.asserts()

		return response
	}


	static void main(String[] args) {
		DataConnect api = new DataConnectV0()
		TokenRequest request = new TokenRequest()
		request.clientId = "4c11020c-b09b-4f90-ac8a-d72cea1a21c2"
		request.clientSecret = "3e9cafae-84ff-48f3-8340-e04e17bd945e"
		request.grantType = GrantTypeEnum.authorization_code
		request.redirectUri = "https://www.jdevops.com/smarthome/application/oauth"
		request.code = "vr213QcKD1y9yCtPXNwpJkdFgMy4pw"

		TokenResponse response = api.token(request)
	}
}

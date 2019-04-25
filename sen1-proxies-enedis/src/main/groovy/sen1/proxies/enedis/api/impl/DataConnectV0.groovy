/**
 * 
 */
package sen1.proxies.enedis.api.impl

import org.grails.web.json.JSONElement

import grails.util.Environment
import groovy.time.TimeCategory
import sen1.proxies.core.DateUtils
import sen1.proxies.core.http.Http
import sen1.proxies.core.http.transformer.JsonResponseTransformer
import sen1.proxies.enedis.api.AuthorizeRequest
import sen1.proxies.enedis.api.AuthorizeResponse
import sen1.proxies.enedis.api.DataConnect
import sen1.proxies.enedis.api.GrantTypeEnum
import sen1.proxies.enedis.api.MetricRequest
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
			token: "https://gw.hml.api.enedis.fr",
			metric: "https://gw.hml.api.enedis.fr"
		],
		(Environment.PRODUCTION): [
			authorize: "https://espace-client-particuliers.enedis.fr/group/espace-particuliers/consentement-linky",
			token: "https://gw.prd.api.enedis.fr",
			metric: "https://gw.prd.api.enedis.fr"
		]
	]


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.enedis.api.DataConnect#authorize(sen1.proxies.enedis.api.AuthorizeRequest)
	 */
	@Override
	AuthorizeResponse authorize(AuthorizeRequest request) throws Exception {
		request.asserts()
		AuthorizeResponse response
		
		return response.asserts()
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.enedis.api.DataConnect#token(sen1.proxies.enedis.api.TokenRequest)
	 */
	@Override
	TokenResponse token(TokenRequest request) throws Exception {
		request.asserts()
		String url = "${URLS[Environment.getCurrentEnvironment()].token}/v1/oauth2/token"
		TokenResponse response
		
		Http httpRequest = Http.Post(url)
				.queryParam("redirect_uri", request.redirectUri)
				.formField("client_id", request.clientId)
				.formField("client_secret", request.clientSecret)
				.formField("grant_type", request.grantType.toString())
				
		if (request.grantType == GrantTypeEnum.authorization_code) {
			httpRequest.formField("code", request.code)
		} else {
			httpRequest.formField("refresh_token", request.refreshToken)
		}

		JSONElement result = httpRequest.execute(new JsonResponseTransformer())?.content

		if (result) {
			response = new TokenResponse()
			response.accessToken = result.access_token
			response.refreshToken = result.refresh_token
			response.tokenType = result.token_type
			response.expiresIn = result.expires_in as Long
			response.scopes = result.scope
			response.refreshTokenIssuedAt = result.refresh_token_issued_at
			response.issuedAt = result.issued_at
		}

		return response.asserts()
	}
	
	
	/**
	 * 
	 * @see sen1.proxies.enedis.api.DataConnect#consumptionLoadCurve(sen1.proxies.enedis.api.MetricRequest)
	 */
	@Override
	List<JSONElement> consumptionLoadCurve(MetricRequest request) throws Exception {
		request.asserts()
		String url = "${URLS[Environment.getCurrentEnvironment()].metric}/v3/metering_data/consumption_load_curve"
		
		JSONElement response = Http.Get(url)
			.queryParam("start", DateUtils.formatDateTimeIso(request.start))
			.queryParam("end", DateUtils.formatDateTimeIso(request.end))
			.queryParam("usage_point_id", request.usagePointId)
			.header("Authorization", "Bearer ${request.token}")
			.header("Accept", "application/json")
			.execute(new JsonResponseTransformer())?.content
			
		List<JSONElement> datapoints = response.usage_point[0].meter_reading.interval_reading
		Date rankStart = DateUtils.parseDateUser(response.usage_point[0].meter_reading.start)
		
		datapoints.each {
			use (TimeCategory) {
				it.timestamp = rankStart + ((it.rank as Integer) * 30).minutes
			}
		}
		
		return datapoints
	}
	
}

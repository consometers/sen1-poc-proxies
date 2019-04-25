/**
 * 
 */
package sen1.proxies.enedis.api

import sen1.proxies.enedis.api.impl.DataConnectV0

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DataConnectV0Spec {
	static void main(String[] args) {
		DataConnect api = new DataConnectV0()
		TokenRequest request = new TokenRequest()
		request.clientId = "4c11020c-b09b-4f90-ac8a-d72cea1a21c2"
		request.clientSecret = "3e9cafae-84ff-48f3-8340-e04e17bd945e"
		request.grantType = GrantTypeEnum.authorization_code
		request.redirectUri = "https://www.jdevops.com/smarthome/application/oauth"

		TokenResponse response = api.token(request)
	}
}

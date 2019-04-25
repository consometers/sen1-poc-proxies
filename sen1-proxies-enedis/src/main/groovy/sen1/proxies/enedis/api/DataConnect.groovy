package sen1.proxies.enedis.api

import org.grails.web.json.JSONElement

/**
 * API DataConnect
 * 
 * @see https://datahub-enedis.fr/data-connect/documentation/
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface DataConnect {
	/**
	 * Etape 1 : Appel de la page de consentement
	 * Etape 2 : Obtention d’un code
	 * 
	 * Doc :
	 * Cette page retourne un code d’autorisation dans votre URL de redirection.
	 * C’est à dire que votre client est redirigé vers la page de votre choix et
	 * dans l’URL se trouve un code que vous devez récupérer pour la suite des
	 * opérations. Cet appel vous permet également de récupérer l’identifiant du
	 * point de consommation ou de production (usage_point_id) : cette
	 * information sera à fournir dans les appels API pour la récupération des
	 * données du client
	 * 
	 * @param request
	 * @return response with code
	 * @throws Exception
	 */
	AuthorizeResponse authorize(AuthorizeRequest request) throws Exception


	/**
	 * Obtention des jetons
	 * 
	 * Doc : 
	 * Il s’agit maintenant d’échanger le code d’autorisation contre des
	 * jetons qui vous permettront d’accéder aux données.
	 * En retour, vous récupérez deux jetons ! Un jeton d’accès (access_token)
	 * qui est la clé d’entrée auprès d’Enedis pour les données auxquelles le
	 * client vient de vous donner accès. Il a une durée de validité de 3h30.
	 * Vous disposez également d’un jeton de rafraîchissement (refresh_token)
	 * qui a une durée de validité d’un an et permet de renouveler le jeton
	 * d’accès.
	 * 
	 * @param request
	 * @return response with jetons
	 * @throws Exception
	 */
	TokenResponse token(TokenRequest request) throws Exception
	
	
	/**
	 * Récupération de la puissance moyenne consommée quotidiennement,
	 * par tranche d'une demi-heure.
	 * 
	 * @throws Exception
	 */
	List<JSONElement> consumptionLoadCurve(MetricRequest request) throws Exception
}

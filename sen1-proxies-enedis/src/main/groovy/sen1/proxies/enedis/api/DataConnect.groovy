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
	 * Récupération de la puissance moyenne consommée quotidiennement, par
	 * tranche d'une demi-heure.
	 * 
	 * Doc :
	 * Cette sous ressource renvoie les valeurs correspondant à des journées de
	 * mesure de la courbe de charge de consommation d’un client pour chaque
	 * jour de la période demandée. Les valeurs retournées sont des puissances
	 * moyennes de consommation sur des tranches de 30 minutes. Chaque valeur
	 * est associée à un numéro, la valeur portant le numéro 1 correspond à la
	 * puissance moyenne mesurée entre minuit et minuit trente le premier jour
	 * de la période demandée. La valeur portant le numéro le plus élevé
	 * correspond à la puissance moyenne mesurée entre 23h30 et minuit, la
	 * veille du dernier jour demandé. Les éventuelles périodes de données
	 * absentes se manifesteront par un saut dans la numérotation. La courbe de
	 * charge s’obtient sur des journées complètes de minuit à minuit du jour
	 * suivant en heures locales. Un appel peut porter au maximum sur 7 jours
	 * consécutifs. Un appel peut porter sur des données datant au maximum de
	 * 24 mois et 15 jours avant la date d’appel.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	List<JSONElement> consumptionLoadCurve(MetricRequest request) throws Exception
}

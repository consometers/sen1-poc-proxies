package sen1.proxies.enedis.api

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
	 * 
	 */
	void authorize(AuthorizeRequest request) throws Exception
	
	
	/**
	 * Obtention d’un code
	 * 
	 * En réponse cette page retourne un code d’autorisation dans votre URL de
	 * redirection. C’est à dire que votre client est redirigé vers la page de
	 * votre choix et dans l’URL se trouve un code que vous devez récupérer pour
	 * la suite des opérations. Cet appel vous permet également de récupérer
	 * l’identifiant du point de consommation ou de production (usage_point_id)
	 * cette information sera à fournir dans les appels API pour la récupération
	 * des données du client.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	TokenResponse token(TokenRequest request) throws Exception
}

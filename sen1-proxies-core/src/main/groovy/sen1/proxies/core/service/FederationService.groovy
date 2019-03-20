package sen1.proxies.core.service

import java.util.function.Consumer

import sen1.proxies.core.io.Message

/**
 * Service FederationService
 * 
 * Service dédié aux échanges sur le réseau fédéré
 * Permet d'envoyer/recevoir des messages
 * 
 * Ce service est à disposition pour chaque proxy.
 * Il peut être utilisé soit pour des échanges unidirectionnels ou bidirectionnels en fonction des besoins du proxy
 * C'est pour cela que 2 méthodes sont prévues pour initialiser le service en fonction des modes voulues
 * 
 * Ces 2 méthodes init (l'une ou l'autre ou les 2) sont à utiliser dans le bootstrap d'un projet
 * ensuite connecter le service au réseau avec la méthode connect pour pouvoir envoyer/recevoir des messages 
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface FederationService {

	/**
	 * Init le service pour l'envoi de messages sur le réseau
	 *
	 * @throws Exception
	 */
	abstract void initSender() throws Exception


	/**
	 * Init le service pour l'envoi de messages sur le réseau
	 *
	 * @throws Exception
	 */
	abstract void initListener() throws Exception


	/**
	 * Connexion au réseau fédéré après l'appel des 2 init méthodes
	 *
	 * @throws Exception
	 */
	abstract void connect() throws Exception


	/**
	 * déconnexion du réseau fédéré
	 *
	 * @throws Exception
	 */
	abstract void close() throws Exception


	/**
	 * Envoi d'un message sur le réseau fédéré
	 *
	 * @param message
	 * @throws Exception
	 */
	abstract void sendMessage(Message message) throws Exception
}

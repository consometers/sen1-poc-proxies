package sen1.proxies.core

/**
 * Domain Outbox
 * 
 * Contient les données à envoyer aux consumers
 * Elles sont extraites depuis le système local
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Outbox {

	/**
	 * Les données à envoyer au consumer et extraites depuis le système local
	 */
	byte[] data
	/**
	 * Date de réception des données
	 */
	Date receivedDate
	/**
	 * Le consumer associé
	 */
	OutboxConsumer consumer


	/**
	 * Bidirectionnal @ToMany
	 */
	static belongsTo = [consumer: OutboxConsumer]


	/**
	 * Domain Validation
	 */
	static constraints = {
		data nullable: false
		receivedDate nullable: false
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "Outbox Data To Consumer"
		table schema: ProxyConstantes.DBSCHEMA
		version false
	}
}

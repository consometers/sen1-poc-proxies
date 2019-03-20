package sen1.proxies.core

/**
 * Domain Outbox
 * 
 * Les données qui viennt du système local et qui doivent être envoyées sur le réseau fédéré
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Outbox {

	/**
	 * Un objet de type sen1.proxies.core.io.Message sérialisé
	 */
	byte[] data
	/**
	 * Date de réception des données
	 */
	Date receivedDate


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
		comment "Outbox Data"
		table schema: ProxyConstantes.DBSCHEMA
		version false
	}
}

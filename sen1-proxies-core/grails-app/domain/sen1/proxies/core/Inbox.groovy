package sen1.proxies.core

/**
 * Domain Inbox
 * 
 * Les données qui viennent du réseau fédéré et doivent être envoyées au système local
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Inbox {

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
		comment "Inbox Data"
		table schema: ProxyConstantes.DBSCHEMA
		version false
	}
}

package sen1.proxies.core

/**
 * Domain Inbox
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Inbox {

	static constraints = {
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "Inbox Data To Consumer"
		table schema: ProxyConstantes.DBSCHEMA
		version false
	}
}

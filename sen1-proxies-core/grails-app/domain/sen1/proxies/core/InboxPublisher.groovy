package sen1.proxies.core

/**
 * Domain InboxPublisher
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class InboxPublisher {

	static constraints = {
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment 'Inbox Publisher'
		table schema: ProxyConstantes.DBSCHEMA
	}
}

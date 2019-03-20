package sen1.proxies.core

/**
 * Domain App
 * 
 * Les applications référencées sur le réseau
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class App {

	String name
	String jid


	/**
	 * Domain Validation
	 */
	static constraints = {
		name unique: true, nullable: false
		jid nullable: false
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "Apps"
		table schema: ProxyConstantes.DBSCHEMA
		name index: 'App_Idx'
	}
}

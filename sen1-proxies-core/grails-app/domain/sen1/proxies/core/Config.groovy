package sen1.proxies.core

/**
 * Domain Config
 * 
 * Configuration Application (ie constantes, token, etc.)
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Config {

	String name
	String value


	/**
	 * Domain Validation
	 */
	static constraints = {
		name unique: true, nullable: false
		value nullable: false
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "Config"
		table schema: ProxyConstantes.DBSCHEMA
		name index: 'Config_Idx'
		cache true
	}
}

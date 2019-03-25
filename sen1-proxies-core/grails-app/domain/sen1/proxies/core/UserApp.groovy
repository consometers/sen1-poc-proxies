package sen1.proxies.core

/**
 * Domain UserApp
 * 
 * Identifiants de connexion d'un user dans une application
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class UserApp {

	User user
	App app
	String token

	/**
	 * @ToMany bidirectionnal
	 */
	static belongsTo = [user: User]


	/**
	 * Domain Validation
	 */
	static constraints = {
		user unique: 'app', nullable: false
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "UserApp"
		table schema: ProxyConstantes.DBSCHEMA
		user index: 'UserApp_Idx'
		app index: 'UserApp_Idx'
	}
}

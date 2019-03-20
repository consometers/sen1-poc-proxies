package sen1.proxies.core

/**
 * Domain User
 * 
 * Les users référencées dans le système local
 * Pour une donnée qui transite d'une app A vers B, le username doit exister
 * dans les 2 systèmes
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class User {

	/**
	 * Identifiant unique
	 */
	String username
	/**
	 * Token de connexion au système local 
	 */
	String token


	/**
	 * Domain Validation
	 */
	static constraints = {
		username unique: true, nullable: false
		token nullable: false
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "User"
		table schema: ProxyConstantes.DBSCHEMA
		username index: 'User_Idx'
	}
}

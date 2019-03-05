package sen1.proxies.pride.warp10

/**
 * Paramètres HTTP pour un appel "exec"
 * 
 * @see https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/09_Analysing_data
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Warp10Exec {

	/**
	 * The request body contains the WarpScript™ code to execute.
	 */
	Warp10Script warpScript


	/**
	 * Vérification des paramètres
	 * 
	 * @throws AssertionError
	 */
	void assertParam() {
		assert warpScript != null
	}


	/**
	 * Le script est passé dans le body de la request
	 * On convertit le script en String
	 *  
	 * @return
	 */
	String body() {
		return warpScript.toString()
	}
}

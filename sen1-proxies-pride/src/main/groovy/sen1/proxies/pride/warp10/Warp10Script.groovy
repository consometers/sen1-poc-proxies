package sen1.proxies.pride.warp10


/**
 * Création de scripts WarpScript avec une API Fluent
 * 
 * Documentation :
 * We created WarpScript™, an extensible stack oriented programming language dedicated to Geo Time Series™ analytics
 * which offers more than 900 functions and several high level frameworks to ease and speed your data analysis. Simply
 * create scripts containing your data analysis code and submit them to the platform, they will execute close to where
 * the data resides and you will get the result of that analysis as a JSON object that you can integrate into your
 * application.
 * 
 * The WarpScript™ approach is another differentiating factor of Warp 10™. Traditional time series platforms offer few
 * manipulation options, usually only providing a SQL like query language which cannot express complex analysis, or
 * providing a reduced set of aggregation functions. These approaches force you to produce more code on the client side
 * thus increasing your development time and leading to massive transfers of unprocessed data from the platform to your
 * applications. Our approach lets you focus on your business use cases, simplifying IoT and sensor data applications by
 * taking care of a larger chunk of the data analysis in a very efficient way.
 * 
 * It is a stack-oriented language with solid foundations.
 * WarpScript™ is a concatenative programming language.
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
abstract class Warp10Script<T> {
	protected Map params = [:]


	/**
	 * Point d'entrée pour la création d'une instance
	 * 
	 * @param scriptType sous type Warp10Script
	 * @return script <T>
	 */
	public static T build(Class<T> scriptType) throws Exception {
		return scriptType.newInstance()
	}


	/**
	 * Le nom de la fonction à exécuter
	 * 
	 * @return
	 */
	abstract String function()


	/**
	 * Formatage par défaut du script avec concaténation de tous les valeurs
	 * Prend la map de paramètres et affiche à la suite la fonction à exécuter
	 *
	 * @see java.lang.Object#toString()
	 */
	String toString() {
		"${mapToString()} ${function()}"
	}


	/**
	 * Formatte la map de paramètres au format WarpScript
	 * 
	 * Maps of key/values { key1 value1 key2 value2 }
	 * 
	 * @return
	 */
	protected String mapToString() {
		StringBuilder builder = new StringBuilder()

		if (params) {
			builder.append("{")

			params.each { entry ->
				builder.append("\n${entry.key} ${entry.value.toString()}")
			}

			builder.append("\n}")
		}

		return builder.toString()
	}
}

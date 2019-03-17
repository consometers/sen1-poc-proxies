package sen1.proxies.pride.warp10

import sen1.proxies.core.DateUtils


/**
 * Contrat de base pour les scripts WarpScrip
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
trait Warp10Script {
	/**
	 * Le nom de la fonction à exécuter
	 *
	 * @return
	 */
	abstract String function()


	/**
	 * Le script au format texte
	 * 
	 * @return
	 */
	abstract String script()


	/**
	 * Vérification du script
	 *
	 */
	void asserts() {
	}


	/**
	 * Formattage d'une map selon la syntaxe WarpScript
	 * Maps of key/values { key1 value1 key2 value2 }
	 * 
	 * @param map
	 * @return
	 */
	String mapToString(Map map) {
		StringBuilder builder = new StringBuilder()

		if (map) {
			builder.append("{")

			map.each { key, value ->
				String format

				if (value instanceof Number) {
					format = "$value"
				} else if (value instanceof Date) {
					format = "'${DateUtils.formatDateTimeIso(value)}'"
				} else {
					format = "'${value.toString()}'"
				}

				builder.append("\n'${key}' $format")
			}

			builder.append("\n}")
		}

		return builder.toString()
	}
}

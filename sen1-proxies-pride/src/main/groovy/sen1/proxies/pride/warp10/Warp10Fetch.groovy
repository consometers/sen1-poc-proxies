package sen1.proxies.pride.warp10

/**
 * Encapsulation des données pour un appel fetch
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Warp10Fetch {
	String selector
	Date start
	Date stop
	long now
	String timespan
	boolean dedup
	boolean showattr
	boolean showmeta
	boolean showerrors


	/**
	 * Vérification des paramètres
	 * 
	 * @throws AssertionError
	 */
	void assertFetch() {
		assert selector != null
		// soit mode debut/fin, soit repère/duration
		assert (start && stop) || (now && timespan)
	}
}

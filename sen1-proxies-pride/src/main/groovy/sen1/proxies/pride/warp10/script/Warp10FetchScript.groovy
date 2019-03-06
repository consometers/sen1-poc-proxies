package sen1.proxies.pride.warp10.script

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

/**
 * Création d'un script pour la fonction "fetch"
 * 
 * Documentation :
 * FETCH accepts a list of 5 paremeters, or a map with the parameter you want. Read below to find out parameter priority.
 * 
 * Fetch will select gts :
 * - Fetch look at selectors parameter (list of selector)
 * - If selectors are not found, fetch will use selector parameter (selector)
 * - If selector is not found, fetch will use both class and labels parameters.
 * 
 * Fetch will collect data values in the past. FETCH always start from the end, and always stops when the oldest value
 * is collected :
 * - First step is to go to end. end must be defined in your request. If end is anterior to your first value, result
 *   will be empty (no GTS).
 * - Second step si to collect data in the timespan window.
 * - If timespan is not defined, FETCH collect a maximum of count point.
 * - If count is not defined, FETCH define a timespan with start. If start is more recent than end, end and start are
 *   permuted internaly.
 *   
 * A valid read token is needed to read data with fetch. If you use a metaset and also specify a token, the token
 * included in the metaset will be ignored.
 * 
 * @see https://www.warp10.io/doc/FETCH
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Builder(builderStrategy=SimpleStrategy, prefix="")
class Warp10FetchScript implements Warp10Script {
	static Logger log = LoggerFactory.getLogger(Warp10FetchScript)


	/**
	 * Read token to use to fetch the data.
	 */
	String token

	/**
	 * A Geo Time Series™ selector with the syntax class{labels} where class is an exact match or a
	 *  regular expression starting with ~ and labels a comma separated list of labels selector of the form name=exact
	 *  or name~regexp. Names and values must be percent URL encoded if needed.
	 */
	String selector

	/**
	 * Most recent timestamp to consider when fetching datapoints.
	 */
	Date end

	/**
	 * Start timestamp to consider when fetching data, in ISO-8601 format.
	 */
	Date start

	/**
	 * Depth to consider when fetching the datapoints, expressed in time units. Incompatible with 'count'.
	 */
	long timespan

	/**
	 * Maximum number of datapoints to fetch for each GTS. Incompatible with 'timespan'.
	 */
	long count


	/**
	 * Fonction "FETCH"
	 *
	 * @see sen1.proxies.pride.warp10.script.Warp10Script#function()
	 */
	@Override
	String function() {
		"FETCH"
	}


	/** 
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.pride.warp10.script.Warp10Script#script()
	 */
	@Override
	String script() {
		// les champs obligatoires
		Map map = [token: token, end: end, selector: selector]

		// les champs optionnels
		if (start) {
			map.start = start
		}
		if (timespan) {
			map.timespan = timespan
		}
		if (count) {
			map.count = count
		}

		String script = "${mapToString(map)} ${function()}"
		log.debug(script)

		return script
	}


	/**
	 * Vérifie si les paramètres obligatoires sont présents
	 *
	 * @see sen1.proxies.pride.warp10.script.Warp10Script#asserts()
	 */
	@Override
	void asserts() {
		assert (token != null)
		assert (selector != null)
		assert (end != null)
		assert (start || timespan || count)
		assert !(timespan && count)
	}
}

package sen1.proxies.pride.warp10.script

import sen1.proxies.pride.warp10.Warp10Script

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
class Warp10FetchScript extends Warp10Script<Warp10FetchScript> {

	/** 
	 * Fonction "FETCH"
	 *
	 * @see sen1.proxies.pride.warp10.Warp10Script#function()
	 */
	@Override
	String function() {
		"FETCH"
	}


	/**
	 * Injecte le token dans la liste des paramètres
	 *  
	 * @param token Read token to use to fetch the data.
	 * @return this
	 */
	Warp10FetchScript token(String token) {
		this.params.token = token
		return this
	}


	/**
	 * Injecte le selector dans la liste des paramètres
	 *
	 * @param selector A Geo Time Series™ selector with the syntax class{labels} where class is an exact match or a
	 *  regular expression starting with ~ and labels a comma separated list of labels selector of the form name=exact
	 *  or name~regexp. Names and values must be percent URL encoded if needed.
	 * @return this
	 */
	Warp10FetchScript selector(String selector) {
		this.params.selector = selector
		return this
	}
}

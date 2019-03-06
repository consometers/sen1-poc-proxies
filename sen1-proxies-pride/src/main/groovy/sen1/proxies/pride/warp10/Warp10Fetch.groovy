package sen1.proxies.pride.warp10

import groovy.transform.builder.Builder
import groovy.transform.builder.SimpleStrategy

/**
 * Paramètres HTTP pour un appel fetch
 * 
 * @see https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/04_Fetching_data/01_Fetching_data
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Builder(builderStrategy=SimpleStrategy, prefix="")
class Warp10Fetch {
	/**
	 * To be authenticated you need to add a X-Warp10-Token header with a valid READ token.
	 */
	String token

	/**
	 * the selector is a string that allows to select one or several GTS. Its composed of the concatenation of a
	 * classname selector and a labels selector. 
	 * 
	 * The class name selector can be a sequence of characters representing an exact match if it starts with an =, or
	 * a regular expression that the class name must match if it starts with ~. When there is no ambiguity,
	 * the leading = can be omitted.
	 * 
	 * The labels selector is delimited by curly braces ({}) and is comma separated list of individual label selectors.
	 * Each of these label selectors is composed of the label name and the value of the associated selector.
	 * Those selectors can also be exact matches if they start with = or a regular expression if they start with ~.
	 * 
	 * Example:
	 * ~warp.*{freq~2.*,steps=100}
	 * 
	 * It selects all GTS whose classname begin with warp and whose label steps has a value of 100 and whose label
	 * freq begins with 2.
	 * 
	 * The class and label elements of the selector can be percent encoded.
	 */
	String selector

	/**
	 * the start and end timestamps defining the interval of the GTS to fetch. They are both in ISO8601 format,
	 * i.e. YYYY-MM-DDTHH:MM:SS.SSSSSSZ. Only those readings whose timestamps lie between those two timestamps
	 * (most recent inclusive, oldest exclusive) will be retained.
	 */
	Date start
	Date stop

	/**
	 * instead of the basic interval search, with the start and end timestamps in ISO8601 format, you can use two
	 * alternative formats for the interval parameters:
	 * 
	 * 1. If you want to fetch readings whose timestamps lie in a specific timespan before a timestamp (for example all
	 *  the readings in the last minute) you can use the end timestamp (in time units since the Unix epoch) as now
	 *  parameter and the timespan (in platform time units) as timespan.
	 * 
	 * 2. If you want to recover the last n readings before a given instant, you can use the instant timestamp (in
	 *  microseconds since the Unix epoch) as now parameter and -n as timespan.
	 * 
	 * You can use now as the value of the now parameter to dynamically retrieve the current time.The timespan parameter
	 * can be specified as an ISO-8601 duration. Note that supported durations cannot contain years or months.
	 */
	Date now
	String timespan

	/**
	 * if this parameter is true, sequences of successive datapoints with the same locations and value are compressed
	 * in the response, giving only the first and the last datapoints in the sequence.
	 */
	boolean dedup

	/**
	 * can be set to true to display the attributes of the retrieved Geo Time Series™.
	 */
	boolean showattr

	/**
	 * can be set to true to sort the labels so they are always displayed in the same way for a given GTS.
	 */
	boolean showmeta

	/**
	 * can be set to true to display a special string starting with # ERROR: at the end of the output if an error was
	 * encountered while fetching data. We cannot rely on the HTTP error mechanism since we may have already started
	 * outputting content when the error is encountered.
	 */
	boolean showerrors


	/**
	 * Vérification des paramètres
	 * 
	 * @throws AssertionError
	 */
	void asserts() {
		assert token != null
		assert selector != null
		// soit mode debut/fin, soit repère/duration
		assert (start && stop) || (now && timespan)
	}
}

package sen1.proxies.pride.warp10;

/**
 * Format de rendu d'une réponse HTTP
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
public enum Warp10FormatEnum {
	/**
	 * The fulltext format uses the GTS Input Format as output format.
	 * https://www.warp10.io/content/03_Documentation/03_Interacting_with_Warp_10/03_Ingesting_data/02_GTS_input_format
	 */
	fulltext,
	/**
	 * Conversion json du format GTS
	 */
	json,
	/**
	 * The text format uses a less verbose version of the GTS Input Format as output
	 * format. In this format the data for each GTS is grouped and classname and
	 * labels are only written in the first line of each group.
	 */
	text;
}

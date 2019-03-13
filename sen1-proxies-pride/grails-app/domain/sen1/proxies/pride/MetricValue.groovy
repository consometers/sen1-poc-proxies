package sen1.proxies.pride

/**
 * Les données d'une metric stockées sous forme de String
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class MetricValue {

	Metric metric
	String value
	Date date


	/**
	 * Bidirectionnal @ToMany
	 */
	static belongsTo = [metric: Metric]


	/**
	 * Domain Validation
	 */
	static constraints = {
		metric unique: 'date', nullable: false
		value nullable: false
		date nullable: false
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "Metric Series Values"
		table schema: ProxyPrideConstantes.DBSCHEMA
	}
}

package sen1.proxies.pride

/**
 * Référence les applications qui sont abonnées à cette métrique
 * Chaque nouvelle valeur d'une métrique sera alors envoyée à ce consumer
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class MetricConsumer {

	Metric metric

	/**
	 * Le nom/code de l'application abonnée
	 */
	String application


	/**
	 * Bidirectionnal @ToMany
	 */
	static belongsTo = [metric: Metric]


	/**
	 * Domain Validation
	 */
	static constraints = {
		metric unique: 'application', nullable: false
		application nullable: false
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "Metric Series Consumers"
		table schema: ProxyPrideConstantes.DBSCHEMA
	}
}

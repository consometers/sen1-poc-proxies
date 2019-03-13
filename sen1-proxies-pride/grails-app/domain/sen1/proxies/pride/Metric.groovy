package sen1.proxies.pride

/**
 * Référencement d'une série GTS dans le système Warp10
 * Ex : compteur, capteur, etc.
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Metric {

	/**
	 * Sa clé unique 
	 */
	String classname

	/**
	 * Le nom du label
	 */
	String labelname

	/**
	 * La valeur du label
	 */
	String labelvalue

	/**
	 * La date de la dernière valeur enregistrée
	 * Mis à jour à chaque synchro avec Pride, pour ne charger que les données plus récentes à cette date
	 */
	Date dateLastValue

	/**
	 * Les valeurs de la metric
	 */
	Set<MetricValue> values = []

	/**
	 * Les consumers abonnés
	 */
	Set<MetricConsumer> consumers = []


	/**
	 * @ToMany associations
	 */
	static hasMany = [
		values: MetricValue,
		consumers: MetricConsumer
	]


	/**
	 * Domain Validation
	 */
	static constraints = {
		classname unique: ['labelname', 'labelvalue'], nullable: false
		labelname nullable: false
		labelvalue nullable: false
		dateLastValue nullable: true
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment "Metric Series"
		table schema: ProxyPrideConstantes.DBSCHEMA
		values cascade: 'all-delete-orphan'
		consumers cascade: 'all-delete-orphan'
	}
}

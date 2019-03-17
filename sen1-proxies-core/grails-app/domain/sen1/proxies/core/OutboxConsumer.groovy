package sen1.proxies.core

/**
 * Domain OutboxConsumer
 * 
 * Les consumers d'une donnée
 * Les propriétés de la donnée sont à interpréter dans le contexte du système local
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class OutboxConsumer {

	/**
	 * Nom de l'application consumer dans le réseau fédéré
	 */
	String consumer
	/**
	 * Nom/identifiant de la donnée dans le système local
	 */
	String name
	/**
	 * Si donnée multi-value ou clé multiple, indique le nom/identifiant de la méta-donnée associée
	 */
	String metaname
	/**
	 * Si donnée multi-value ou clé multiple, indique la valeur de la méta-donnée associée
	 */
	String metavalue
	/**
	 * L'unité de la donnée
	 */
	String unite
	/**
	 * Le type de la données
	 */
	DataTypeEnum type = DataTypeEnum.dt_string
	/**
	 * Le timestamp de la dernière data "consumée"
	 */
	Date dateLastValue
	/**
	 * Les données à envoyer au consumer
	 */
	Set<Outbox> outbox = []


	/**
	 * @ToMany associations
	 */
	static hasMany = [
		outbox: Outbox,
	]


	/**
	 * Domain Validation
	 */
	static constraints = {
		consumer unique: ['name', 'metaname']
		metaname nullable: true
		metavalue nullable: true
		unite nullable: true
		dateLastValue nullable: true
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment 'Outbox Consumer'
		table schema: ProxyConstantes.DBSCHEMA
		outbox cascade: 'all-delete-orphan'
	}
}

package sen1.proxies.core

/**
 * Domain Consumer
 * 
 * Les consumers d'une donnée dans le système local
 * Les propriétés de la donnée sont à interpréter dans le contexte du système local
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Consumer {

	/**
	 * Application dans le réseau fédéré à qui envoyer les données
	 */
	App consumerApp
	/**
	 * Utilisateur dans le système local
	 */
	UserApp userApp
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
	 * Mapping name (= name si pas de mapping)
	 */
	String consumerName

	/**
	 * Mapping metaname (= metaname si pas de mapping)
	 */
	String consumerMetaname


	/**
	 * Domain Validation
	 */
	static constraints = {
		userApp unique: [
			'name',
			'metaname',
			'metavalue'
		]
		metaname nullable: true
		consumerMetaname nullable: true
		metavalue nullable: true
		unite nullable: true
		dateLastValue nullable: true
	}


	/**
	 * Domain Database Mapping
	 */
	static mapping = {
		comment 'Consumer'
		table schema: ProxyConstantes.DBSCHEMA
	}
}

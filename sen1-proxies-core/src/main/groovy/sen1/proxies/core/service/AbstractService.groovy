package sen1.proxies.core.service

import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormValidateable

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

/**
 * Class de base pour les services transactionnels
 * Définit les méthodes de base, la gestion des transactions
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
abstract class AbstractService<T> {

	/**
	 * Injecte par défaut pour chaque service le contexte Grails
	 * Permet d'accéder à la config et aux autres objets du contexte
	 */
	GrailsApplication grailsApplication
}

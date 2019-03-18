package sen1.proxies.core.service

import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormValidateable

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

/**
 * Class de base pour les services data/transactionnels 
 * Définit les méthodes de base pour l'accès aux données
 * 
 * L'utilisation de l'AST data service de GORM @Service permet d'implémenter dynamiquement ces méthodes
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
abstract class AbstractDataService<T> extends AbstractService {

	/**
	 * Enregistrement (création ou modification) d'une entité
	 * Déclenche les validator pour vérifier l'intégrité du domain
	 * 
	 * @param domain
	 * @return domain
	 * @throws Exception si erreurs de validation
	 */
	@Transactional(readOnly = false, rollbackFor = Exception)
	T save(T domain) throws Exception {
		assert (domain instanceof GormEntity)

		if (!domain.validate()) {
			String errorMessage = domain.errors.allErrors[0].toString()
			throw new Exception(errorMessage)
		}

		return domain.save()
	}
}

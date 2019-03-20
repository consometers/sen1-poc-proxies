package sen1.proxies.core.service

import java.lang.reflect.ParameterizedType

import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.gorm.GormValidateable

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import sen1.proxies.core.App
import sen1.proxies.core.Consumer

/**
 * Class de base pour les services data/transactionnels 
 * Définit les méthodes de base pour l'accès aux données
 * 
 * L'utilisation de l'AST data service de GORM @Service permet d'implémenter dynamiquement ces méthodes
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
abstract class AbstractDataService<T> extends AbstractService {


	/**
	 * Renvoit le type paramétré <T> du service actuel
	 * 
	 * @return Class<T> sur l'instance actuelle
	 */
	private Class<T> getGenericType() {
		Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]
		return type
	}


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


	/**
	 * Suppression d'une entité
	 * 
	 * @param domain
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception)
	void delete(T domain) throws Exception {
		domain.delete()
	}


	/**
	 * Recherche d'une entité par son  ID et charge les associations
	 *
	 * @param id
	 * @param associations
	 * @return <T>
	 */ 
	T findByIdFetch(long id, String... associations) {
		Map fetchMap = [:]

		if (associations) {
			for (String association : associations) {
				fetchMap[(association)] = 'join'
			}
		}

		return getGenericType().findById(id, [fetch: fetchMap])
	}


	/**
	 * Compte le nombre total d'entités
	 *
	 * @return nb entité
	 */
	long count() {
		getGenericType().count()
	}


	/**
	 * Recherche par identifiant
	 *
	 * @param id
	 * @return <T>
	 */
	T findById(long id) {
		getGenericType().findById(id)
	}


	/**
	 * Liste les entités par pagination et sans filtre
	 *
	 * @param pagination control pagination and ordering
	 * @return
	 */
	List<T> list(Map pagination) {
		getGenericType().list(pagination)
	}
}

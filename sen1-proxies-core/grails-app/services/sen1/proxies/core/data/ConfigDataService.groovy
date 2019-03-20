package sen1.proxies.core.data

import grails.gorm.services.Service
import sen1.proxies.core.Config


/**
 * Service ConfigDataService
 * 
 * Service Data associé au domain sen1.proxies.core.Config
 * Utilise l'AST GORM Service pour l'implémentation dynamique de l'interface
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(Config)
interface ConfigDataService {
	/**
	 * Compte le nombre total d'entités
	 *
	 * @return
	 */
	long count()


	/**
	 * Liste les entités par pagination et sans filtre
	 * 
	 * @param args control pagination and ordering
	 * @return
	 */
	List<Config> list(Map args)


	/**
	 * Recherche par identifiant
	 * 
	 * @param id
	 * @return
	 */
	Config findById(long id)


	/**
	 * Recherche par name unique
	 *
	 * @param name
	 * @return
	 */
	Config findByName(String name)
}

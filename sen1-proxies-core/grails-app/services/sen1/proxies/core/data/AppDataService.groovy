package sen1.proxies.core.data

import grails.gorm.services.Service
import sen1.proxies.core.App


/**
 * Service AppDataService
 * 
 * Service Data associé au domain sen1.proxies.core.App
 * Utilise l'AST GORM Service pour l'implémentation dynamique de l'interface
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(App)
interface AppDataService {
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
	List<App> list(Map args)


	/**
	 * Recherche par identifiant
	 * 
	 * @param id
	 * @return
	 */
	App findById(long id)
}

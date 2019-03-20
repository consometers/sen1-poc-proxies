package sen1.proxies.core.data

import grails.gorm.services.Service
import sen1.proxies.core.Consumer


/**
 * Service ConsumerDataService
 * 
 * Service Data associé au domain sen1.proxies.core.Consumer
 * Utilise l'AST GORM Service pour l'implémentation dynamique de l'interface
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(Consumer)
interface ConsumerDataService {
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
	List<Consumer> list(Map args)


	/**
	 * Recherche par identifiant
	 * 
	 * @param id
	 * @return
	 */
	Consumer findById(long id)
}

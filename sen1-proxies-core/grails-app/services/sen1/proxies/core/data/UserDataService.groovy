package sen1.proxies.core.data

import grails.gorm.services.Service
import sen1.proxies.core.User


/**
 * Service UserDataService
 * 
 * Service Data associé au domain sen1.proxies.core.User
 * Utilise l'AST GORM Service pour l'implémentation dynamique de l'interface
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(User)
interface UserDataService {
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
	List<User> list(Map args)


	/**
	 * Recherche par identifiant
	 * 
	 * @param id
	 * @return
	 */
	User findById(long id)
}

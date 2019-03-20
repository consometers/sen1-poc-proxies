package sen1.proxies.core.data

import grails.gorm.services.Service
import sen1.proxies.core.Inbox


/**
 * Service InboxDataService
 * 
 * Service Data associé au domain sen1.proxies.core.Inbox
 * Utilise l'AST GORM Service pour l'implémentation dynamique de l'interface
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(Inbox)
interface InboxDataService {
	/**
	 * Compte le nombre total d'entités
	 * 
	 * @return
	 */
	long count()


	/**
	 * Liste les entités par pagination et sans filtre
	 *
	 * @param args control pagination, ordering, fetching
	 * @return
	 */
	List<Inbox> list(Map args)


	/**
	 * Recherche par identifiant
	 *
	 * @param id
	 * @return
	 */
	Inbox findById(long id)
}

package sen1.proxies.core.data

import grails.gorm.services.Service
import sen1.proxies.core.Outbox


/**
 * Service OutboxDataService
 * 
 * Service Data associé au domain sen1.proxies.core.Outbox
 * Utilise l'AST GORM Service pour l'implémentation dynamique de l'interface
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(Outbox)
interface OutboxDataService {
	/**
	 * Compte le nombre total d'entités sen1.proxies.core.Outbox
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
	List<Outbox> list(Map args)
}

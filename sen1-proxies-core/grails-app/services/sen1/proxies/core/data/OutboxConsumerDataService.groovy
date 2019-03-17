package sen1.proxies.core.data

import grails.gorm.services.Service
import sen1.proxies.core.OutboxConsumer


/**
 * Service OutboxConsumerDataService
 * 
 * Service Data associé au domain sen1.proxies.core.OutboxConsumer
 * Utilise l'AST GORM Service pour l'implémentation dynamique de l'interface
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Service(OutboxConsumer)
interface OutboxConsumerDataService {
	/**
	 * Compte le nombre total d'entités sen1.proxies.core.OutboxConsumer
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
	List<OutboxConsumer> list(Map args)


	/**
	 * Recherche par identifiant
	 * 
	 * @param id
	 * @return
	 */
	OutboxConsumer findById(long id)
}

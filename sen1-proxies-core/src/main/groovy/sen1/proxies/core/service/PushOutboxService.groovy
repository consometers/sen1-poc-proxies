package sen1.proxies.core.service

import sen1.proxies.core.OutboxConsumer

/**
 * Service PushOutboxService
 * 
 * Contrat à implémenter par le service d'un proxy pour "pousser" les données du système local vers la outbox
 * Lit les dernières données
 * Ne fait aucune transformation
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface PushOutboxService {
	/**
	 * Lecture des datas dans le système local pour un consumer
	 * !! Les données sont renvoyées sans transformation
	 * 
	 * @param outboxConsumer
	 * @return
	 * @throws Exception
	 */
	List fetchData(OutboxConsumer outboxConsumer) throws Exception
}

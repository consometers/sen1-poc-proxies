package sen1.proxies.core.io.consumer

import java.util.function.Consumer

import org.springframework.beans.factory.annotation.Autowired

import sen1.proxies.core.io.Message
import sen1.proxies.core.service.ProxyService

/**
 * Consumer ProxyMessageConsumer
 * 
 * Consume des messages du réseau fédéré pour les envoyer directement vers le système local
 * Un proxy peut déclarer un bean de ce type et l'injecter au moment de l'init du service fédéré
 * 
 * @see sen1.proxies.core.service.FederationService.initListener(Consumer<Message>)
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ProxyMessageConsumer implements Consumer<Message> {

	@Autowired
	ProxyService proxyService


	/** (non-Javadoc)
	 *
	 * @see java.util.function.Consumer#accept(java.lang.Object)
	 */
	@Override
	void accept(Message message) {
		proxyService.pushData(message)
	}
}

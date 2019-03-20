package sen1.proxies.core.io.consumer

import java.util.function.Consumer

import org.springframework.beans.factory.annotation.Autowired

import sen1.proxies.core.Inbox
import sen1.proxies.core.InboxService
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer

/**
 * Consumer InboxMessageConsumer
 * 
 * Consume des messages du réseau fédéré pour les stocker dans la inbox
 * Un proxy peut déclarer un bean de ce type et l'injecter au moment de l'init du service fédéré
 * 
 * @see sen1.proxies.core.service.FederationService.initListener(Consumer<Message>)
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class InboxMessageConsumer implements Consumer<Message> {

	@Autowired
	InboxService inboxService

	@Autowired
	MessageSerializer messageSerializer


	/** (non-Javadoc)
	 *
	 * @see java.util.function.Consumer#accept(java.lang.Object)
	 */
	@Override
	void accept(Message message) {
		Inbox inbox = new Inbox(receivedDate: new Date())
		inbox.data = messageSerializer.write(message)
		inboxService.save(inbox)
	}
}

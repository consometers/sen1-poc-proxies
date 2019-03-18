package sen1.proxies.core.io.message

import sen1.proxies.core.DataTypeEnum
import sen1.proxies.core.io.Message

/**
 * Builder pour les instances sen1.proxies.core.io.Message<T extends MessageData>
 * Utilisation type Fluent
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class MessageBuilder {

	/**
	 * Implémentation par défaut.
	 * A changer ssi nouvelle implémentation (c'est le seul endroit si utilisation conseillée du builder)
	 */
	Message message


	/**
	 * Constructeur privé pour forcer l'utilisation de la méthode static builder()
	 */
	private MessageBuilder() {
		message = new DefaultMessage()
	}


	/**
	 * Construit un builder
	 * 
	 * @return
	 */
	static MessageBuilder builder() {
		return new MessageBuilder()
	}


	/**
	 * Retourne l'instance sen1.proxies.core.io.Message<T extends MessageData>
	 * 
	 * @return
	 */
	Message build() {
		return message
	}


	/**
	 * Set name
	 * @see sen1.proxies.core.io.Message.name
	 * 
	 * @param name
	 * @return
	 */
	MessageBuilder name(String name) {
		message.name = name
		return this
	}


	/**
	 * Set metaname
	 * @see sen1.proxies.core.io.Message.metaname
	 *
	 * @param metaname
	 * @return
	 */
	MessageBuilder metaname(String metaname) {
		message.metaname = metaname
		return this
	}


	/**
	 * Set metavalue
	 * @see sen1.proxies.core.io.Message.metavalue
	 *
	 * @param metaname
	 * @return
	 */
	MessageBuilder metavalue(String metavalue) {
		message.metavalue = metavalue
		return this
	}


	/**
	 * Set unite
	 * @see sen1.proxies.core.io.Message.unite
	 *
	 * @param unite
	 * @return
	 */
	MessageBuilder unite(String unite) {
		message.unite = unite
		return this
	}


	/**
	 * Set type
	 * @see sen1.proxies.core.io.Message.type
	 *
	 * @param unite
	 * @return
	 */
	MessageBuilder type(DataTypeEnum type) {
		message.type = type
		return this
	}


	/**
	 * Set applicationSrc
	 * @see sen1.proxies.core.io.Message.applicationSrc
	 *
	 * @param applicationSrc
	 * @return
	 */
	MessageBuilder applicationSrc(String applicationSrc) {
		message.applicationSrc = applicationSrc
		return this
	}


	/**
	 * Set applicationDst
	 * @see sen1.proxies.core.io.Message.applicationDst
	 *
	 * @param applicationSrc
	 * @return
	 */
	MessageBuilder applicationDst(String applicationDst) {
		message.applicationDst = applicationDst
		return this
	}
}

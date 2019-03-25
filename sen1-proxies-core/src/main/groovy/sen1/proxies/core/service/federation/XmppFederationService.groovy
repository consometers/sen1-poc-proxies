package sen1.proxies.core.service.federation

import java.security.SecureRandom
import java.util.function.Consumer

import javax.net.ssl.SSLContext

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import grails.gorm.transactions.Transactional
import rocks.xmpp.addr.Jid
import rocks.xmpp.core.XmppException
import rocks.xmpp.core.net.ChannelEncryption
import rocks.xmpp.core.net.client.SocketConnectionConfiguration
import rocks.xmpp.core.sasl.AuthenticationException
import rocks.xmpp.core.session.Extension
import rocks.xmpp.core.session.XmppClient
import rocks.xmpp.core.session.XmppSessionConfiguration
import rocks.xmpp.core.stanza.MessageEvent
import sen1.proxies.core.App
import sen1.proxies.core.AppService
import sen1.proxies.core.ConfigService
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.message.MessageBuilder
import sen1.proxies.core.service.AbstractService
import sen1.proxies.core.service.FederationService

/**
 * Service XmppFederationService
 * 
 * Implémentation XMPP pour le réseau fédéré
 * 
 * Utilisation de la librairie Babbler
 * @see https://sco0ter.bitbucket.io/babbler/index.html
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Transactional(readOnly = true)
class XmppFederationService extends AbstractService implements FederationService {

	static Logger log = LoggerFactory.getLogger(XmppFederationService)

	private static final DOMAIN_CONFIG = "XMPP_DOMAIN_NAME"
	private static final USERNAME_CONFIG = "XMPP_USERNAME"
	private static final PASSWORD_CONFIG = "XMPP_PASSWORD"


	private XmppClient xmppClient
	private boolean xmppClientConnected

	// doit être injecté à la construction du service
	Consumer<Message> messageConsumer

	/**
	 * Injecté depuis les properties
	 * @see application.yml
	 */
	@Value('${info.app.name}')
	String applicationName

	@Autowired
	ConfigService configService

	@Autowired
	AppService appService


	/** (non-Javadoc)
	 *
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	private XmppClient getXmppClient() {
		if (!xmppClient) {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2")
			sslContext.init(null, null, new SecureRandom())

			// forcer le chiffrement des échanges
			SocketConnectionConfiguration sslConfiguration = SocketConnectionConfiguration.builder()
					.channelEncryption(ChannelEncryption.REQUIRED)
					.sslContext(sslContext)
					.build()

			// rajoute une extension pour serialiser un @see sen1.proxies.core.io.Message dans le stanza XMPP
			XmppSessionConfiguration sessionConfiguration = XmppSessionConfiguration.builder()
					.extensions(Extension.of(MessageBuilder.builder().build().getClass()))
					.build()

			// attention à l'ordre des configuration : d'abord la session puis les connections...
			xmppClient = XmppClient.create(configService.value(DOMAIN_CONFIG), sessionConfiguration, sslConfiguration)
		}

		return xmppClient
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.service.FederationService#initSender()
	 */
	@Override
	void initSender() throws Exception {
		log.info "Active sender mode..."
		getXmppClient()
	}


	/** 
	 * Branche un listener pour la réception des messages
	 * Le service ne traite pas directement les messages, il se contente de le retranscrire et de le déléguer
	 * à une fonction
	 *
	 * @see sen1.proxies.core.service.FederationService#initListener()
	 */
	@Override
	void initListener() throws Exception {
		log.info "Active listener mode..."

		// conflit entre java8 functionnal et closure groovy
		// obligé de passer par la syntaxe d'avant
		getXmppClient().addInboundMessageListener(new Consumer<MessageEvent>() {
					@Override
					void accept(MessageEvent event) {
						rocks.xmpp.core.stanza.model.Message xmppMessage = event.getMessage()
						Message message = xmppMessage.getExtension(MessageBuilder.builder().build().getClass())

						// on ne traite que des messages valides
						if (message) {
							message.asserts()

							if (messageConsumer) {
								messageConsumer.accept(message)
							} else {
								log.warn("cannot consume message : no function !")
							}
						} else {
							log.warn("Empty xmpp message !")
						}
					}
				})
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.service.FederationService#connect()
	 */
	@Override
	void connect() throws Exception {
		assert xmppClient != null
		log.info "Connect XMPP session : {}", applicationName

		try {
			xmppClient.connect()
			xmppClient.login(configService.value(USERNAME_CONFIG), configService.value(PASSWORD_CONFIG))
		} catch (AuthenticationException  | XmppException  e) {
			throw new Exception(e)
		}

		xmppClientConnected = true
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.service.FederationService#close()
	 */
	@Override
	void close() throws Exception {
		if (xmppClientConnected) {
			log.info "Disconnect XMPP session : {}", applicationName
			xmppClient?.close()
		}
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.service.FederationService#sendMessage(sen1.proxies.core.io.Message)
	 */
	@Override
	void sendMessage(Message message) throws Exception {
		if (xmppClientConnected) {
			assert message != null
			// on n'envoit que des messages valides
			message.asserts()

			// recherche de l'application destination (le consumer)
			App app = appService.findByName(message.applicationDst)

			log.debug "Send XMPP message to {} [{}]", message.applicationDst, app.jid

			// construction d'un message/stanza XMPP
			// utiliser le type CHAT car que les messages soient conservés en attente sur le serveur XMPP
			// (d'ailleurs en mode normal, le message n'est pas reçu .....)
			rocks.xmpp.core.stanza.model.Message xmppMessage = new rocks.xmpp.core.stanza.model.Message(Jid.of(app.jid),
					rocks.xmpp.core.stanza.model.Message.Type.CHAT)
			xmppMessage.addExtension(message)
			xmppClient.send(xmppMessage)
		}
	}
}

package sen1.proxies.core.io.serialiser

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import grails.converters.XML
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer
import sen1.proxies.core.io.message.MessageBuilder

/**
 * Implémentation XML d'un message
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class XmlMessageSerializer implements MessageSerializer {

	static Logger log = LoggerFactory.getLogger(XmlMessageSerializer)

	// déclare une seule fois le contexte à partir de l'implémentation par défaut
	JAXBContext context = JAXBContext.newInstance(MessageBuilder.builder().build().getClass())


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.MessageSerializer#read(byte[])
	 */
	@Override
	Message read(byte[] buffer) throws Exception {
		Unmarshaller unmarshaller = context.createUnmarshaller()
		return unmarshaller.unmarshal(new ByteArrayInputStream(buffer))
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.MessageSerializer#write(sen1.proxies.core.io.Message)
	 */
	@Override
	byte[] write(Message message) throws Exception {
		Marshaller marshaller = context.createMarshaller()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		ByteArrayOutputStream outStream = new ByteArrayOutputStream()
		marshaller.marshal(message, outStream)
		return outStream.toByteArray()
	}


	//	static void main(String[] args) {
	//		XmlMessageSerializer serializer = new XmlMessageSerializer()
	//		Message message = MessageBuilder.builder().applicationDst("sen-pride").username("greg").build()
	//		message.addData(message.newMessageDataInstance(100, new Date()))
	//		message.addData(message.newMessageDataInstance(200, new Date()))
	//		byte[] xml = serializer.write(message)
	//
	//		println new String(xml)
	//	}
}

package sen1.proxies.core.io.message

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlSchema

import sen1.proxies.core.DataTypeEnum
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageData

/**
 * Implémentation standard d'un message sur réseau fédéré avec liste de datapoints
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@XmlRootElement(name = "Sen1Message", namespace = "http://xmpp.rocks")
@XmlAccessorType(XmlAccessType.FIELD)
class DefaultMessage implements Message<DefaultMessageData> {
	String username
	String name
	String metaname
	String metavalue
	String unite
	DataTypeEnum type
	String applicationSrc
	String applicationDst
	@XmlElementWrapper(name = "Sen1Datas")
	@XmlElement(name = "Sen1Data")
	List<DefaultMessageData> datas = []


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.Message#newMessageDataInstance(java.lang.Object, java.util.Date)
	 */
	@Override
	DefaultMessageData newMessageDataInstance(Object value, Date timestamp) {
		return new DefaultMessageData(value: value, timestamp: timestamp)
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.Message#newMessageDataInstance()
	 */
	@Override
	DefaultMessageData newMessageDataInstance() {
		return new DefaultMessageData()
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.Message#dateLastValue()
	 */
	@Override
	Date dateLastValue() {
		MessageData lastData = datas?.max { it.timestamp }
		return lastData?.timestamp
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.Message#addData(sen1.proxies.core.io.MessageData)
	 */
	@Override
	Message addData(DefaultMessageData data) {
		datas << data
		return this
	}


	/** (non-Javadoc)
	 *
	 * @see sen1.proxies.core.io.Message#asserts()
	 */
	@Override
	void asserts() {
		assert username != null
		assert applicationSrc != null
		assert applicationDst != null
		assert type != null
		assert name != null
	}
}

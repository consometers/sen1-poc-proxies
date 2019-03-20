package sen1.proxies.core.io.serialiser

import org.grails.testing.GrailsUnitTest

import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer
import sen1.proxies.core.io.message.MessageBuilder
import spock.lang.Shared
import spock.lang.Specification

class XmlMessageSerializerSpec extends Specification implements GrailsUnitTest {

	@Shared
	XmlMessageSerializer messageSerializer = new XmlMessageSerializer()


	/**
	 * Setup avant chaque test
	 *
	 * @return
	 */
	def setup() {
	}


	void "write message"() {
		when:
		Message message = MessageBuilder.builder().build()
		byte[] buffer = messageSerializer.write(message)

		then:
		buffer != null
	}


	void "read message"() {
		when:
		String xml = """
<message>
    <metaname>metaname1</metaname>
    <datas>
        <data>
            <value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:decimal">10.5</value>
            <timestamp>2019-03-19T17:04:48.456+01:00</timestamp>
        </data>
        <data>
            <value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema" xsi:type="xs:int">75</value>
            <timestamp>2019-03-19T17:04:48.470+01:00</timestamp>
        </data>
    </datas>
</message>
"""
		Message message = messageSerializer.read(xml.bytes)

		then:
		message != null
		message.metaname == "metaname1"
	}
}
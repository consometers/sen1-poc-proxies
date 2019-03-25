@XmlSchema(
		namespace = "http://xmpp.rocks",
		elementFormDefault = XmlNsForm.QUALIFIED,
		xmlns = {
				@XmlNs(prefix = "xsi", namespaceURI = "http://www.w3.org/2001/XMLSchema-instance"),
				@XmlNs(prefix = "xs", namespaceURI = "http://www.w3.org/2001/XMLSchema")
		}
)
package sen1.proxies.core.io.message;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
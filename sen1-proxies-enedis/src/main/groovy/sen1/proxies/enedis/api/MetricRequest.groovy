/**
 * 
 */
package sen1.proxies.enedis.api

import org.apache.commons.lang.StringUtils

/**
 * MetricRequest
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class MetricRequest {
	String token
	Date start
	Date end
	String usagePointId
	
	
	/**
	 * Vérification request
	 *
	 * @throws Exception
	 */
	MetricRequest asserts() {
		assert StringUtils.isNotEmpty(token)
		assert StringUtils.isNotEmpty(usagePointId)
		assert start != null
		assert end != null
		
		return this
	}
}

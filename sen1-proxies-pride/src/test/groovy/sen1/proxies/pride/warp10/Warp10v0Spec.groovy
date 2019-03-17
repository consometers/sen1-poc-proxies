package sen1.proxies.pride.warp10

import org.grails.testing.GrailsUnitTest
import org.grails.web.json.JSONElement
import sen1.proxies.pride.warp10.impl.Warp10v0
import sen1.proxies.pride.warp10.script.Warp10FetchScript
import spock.lang.Shared
import spock.lang.Specification

class Warp10v0Spec extends Specification implements GrailsUnitTest {

	private static final String TOKEN = "JrwmTJHEYRs4CfQ81_ftw_hKKfhzPl8SSb5t_LFOYYrLfjd8UnPoJM0l9teHvuGN8JkCzy5LgicYb.uxHCpBwzrX15xfTkeX6hgTpujwLfwZbUzZwzaxeaXibW3_xVIiP_twQIp6tIaJDiirhjEKPL3DNv0FRtRp"
	private static final String PRM = "30001450710424"

	@Shared
	Warp10 warp10Read


	/**
	 * Exécuté une seule fois avant le 1er test
	 * 
	 * @return
	 */
	def setupSpec() {
		warp10Read = new Warp10v0()
		warp10Read.server = "161.106.242.27"
		warp10Read.protocol = "http"
		warp10Read.port = 8080
	}


	void "API FETCH : invalid params throw AssertionError"() {
		when:"Invalid params"
		warp10Read.fetchText(new Warp10Fetch())

		then:"Throw AssertionError"
		thrown AssertionError
	}


	void "API FETCH : valid params get text response"() {
		when:"Valid params"
		// récupère les 10 dernières valeurs
		def result = warp10Read.fetchText(new Warp10Fetch()
				.token(TOKEN)
				.selector("=pride.enedis.cdc{PRM=${PRM}}")
				.now(new Date())
				.timespan("-10"))

		then:"Get text response"
		notThrown Exception
		result instanceof String
	}


	void "API EXEC : invalid params throw AssertionError"() {
		when:"Invalid params"
		warp10Read.exec(new Warp10FetchScript())

		then:"Throw AssertionError"
		thrown AssertionError
	}


	void "API EXEC : valid params get json response"() {
		when:"Valid params"
		// récupère les 10 dernières valeurs
		def result = warp10Read.exec(new Warp10FetchScript()
				.token(TOKEN)
				.selector("=pride.enedis.cdc{PRM=${PRM}}")
				.end(new Date())
				.count(10))

		then:"Get json response"
		notThrown Exception
		result instanceof JSONElement
	}
}
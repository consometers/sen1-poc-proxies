package sen1.proxies.pride.warp10

import org.grails.testing.GrailsUnitTest

import spock.lang.Shared
import spock.lang.Specification

class Warp10Spec extends Specification implements GrailsUnitTest {

	@Shared
	Warp10 warp10Read


	/**
	 * Exécuté une seule fois avant le 1er test
	 * 
	 * @return
	 */
	def setupSpec() {
		warp10Read = Warp10.build("161.106.242.27", "JrwmTJHEYRs4CfQ81_ftw_hKKfhzPl8SSb5t_LFOYYrLfjd8UnPoJM0l9teHvuGN8JkCzy5LgicYb.uxHCpBwzrX15xfTkeX6hgTpujwLfwZbUzZwzaxeaXibW3_xVIiP_twQIp6tIaJDiirhjEKPL3DNv0FRtRp")
				.protocol("http").port(8080)
	}


	/**
	 * Test la levée d'une error de type Assertion car l'objet fetch n'est pas complet
	 */
	void "Not valid fetch params throw AssertionError"() {
		when:
		warp10Read.fetchText(new Warp10Fetch())

		then:
		thrown AssertionError
	}


	/**
	 * Test la bonne exécution d'un fetch sur le PRM 30001450710424
	 */
	void "Fetch PRM 30001450710424 not throw Exception"() {
		when:
		def result = warp10Read.fetchText(new Warp10Fetch(selector: "=pride.enedis.cdc{PRM=30001450710424}",
		start: new Date(), stop: new Date()))

		then:
		notThrown Exception
		result != null
	}
}
package sen1.proxies.enedis

import sen1.proxies.core.scheduler.DefaultScheduler
import sen1.proxies.core.service.FederationService

class BootStrap {

	// auto inject
	DefaultScheduler defaultScheduler

	// auto inject
	FederationService federationService
	
	
    def init = { servletContext ->
		// démarre le gestionnaire de jobs
		defaultScheduler.start()

		// connexion au réseau fédéré
		federationService.initSender()
		federationService.initListener()
		federationService.connect()
    }
	
	
    def destroy = {
		// arrête le gestionnaire de jobs
		defaultScheduler.shutdown()

		// ferme les connexions au réseau fédéré
		federationService.close()
    }
}

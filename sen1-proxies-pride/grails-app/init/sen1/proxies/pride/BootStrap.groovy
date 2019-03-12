package sen1.proxies.pride

import sen1.proxies.core.scheduler.DefaultScheduler

class BootStrap {

	// auto inject
	DefaultScheduler defaultScheduler


	/**
	 * Démarrage container servlet
	 */
	def init = { servletContext ->
		// démarre le gestionnaire de jobs
		defaultScheduler.start()
	}


	/**
	 * Arrêt container servlet
	 */
	def destroy = {
		// arrête le gestionnaire de jobs
		defaultScheduler.shutdown()
	}
}

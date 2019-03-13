package spring

import sen1.proxies.core.scheduler.DefaultScheduler
import sen1.proxies.pride.warp10.Warp10

// Place your Spring DSL code here
beans = {


	// instance warp10 préconfigurée
	warp10(Warp10) {
		server = grailsApplication.config.sen1.proxies.pride.warp10.host
		port = grailsApplication.config.sen1.proxies.pride.warp10.port
		protocol = grailsApplication.config.sen1.proxies.pride.warp10.protocol
	}


	// gestionnaire de crons
	// seconde | minute | heure | jour du mois (1-31) | mois | jour semaine (1-7) | année
	defaultScheduler(DefaultScheduler) {
		jobs = [:
			// monitoring des devices toutes les minutes
			//'smarthome.automation.scheduler.DeviceAlertMonitoringCronMainJob' : "0 * * * * ?",
			// déclenchement des events planifiés toutes les minutes
		]
	}
}

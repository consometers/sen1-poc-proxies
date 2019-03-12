import sen1.proxies.core.scheduler.DefaultScheduler

// Place your Spring DSL code here
beans = {

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

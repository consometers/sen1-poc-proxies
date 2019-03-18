package spring


// Place your Spring DSL code here
beans = {

	// instance warp10 préconfigurée
	warp10(sen1.proxies.pride.warp10.impl.Warp10v0) {
		server = grailsApplication.config.sen1.proxies.pride.warp10.host
		port = grailsApplication.config.sen1.proxies.pride.warp10.port
		protocol = grailsApplication.config.sen1.proxies.pride.warp10.protocol
	}


	// gestionnaire de crons
	// seconde | minute | heure | jour du mois (1-31) | mois | jour semaine (1-7) | année
	defaultScheduler(sen1.proxies.core.scheduler.DefaultScheduler) {
		jobs = [
			// Fetch les données Pride toutes les 5 minutes des compteurs référencés dans le proxy
			'sen1.proxies.core.job.PushOutboxMainJob' : '0 * * * * ?',
			// Fetch les données de la outbox et les envoit sur le réseau fédéré
			'sen1.proxies.core.job.FetchOutboxMainJob': '0 * * * * ?'
		]
	}

	// un converter Outbox (conversion JSON GTS -> Message)
	// Utiliser le name "outboxConverter" pour qu'il soit injecté dans les jobs génériques
	outboxConverter(sen1.proxies.pride.io.GTSOutboxConverter) {

	}

	// le serializer des messages sur le réseau fédéré
	// utilisation du serializer par défaut XML
	// Utiliser le name "messageSerializer" pour qu'il soit injecté dans les jobs génériques
	messageSerializer(sen1.proxies.core.io.serialiser.XmlMessageSerializer) {

	}
}

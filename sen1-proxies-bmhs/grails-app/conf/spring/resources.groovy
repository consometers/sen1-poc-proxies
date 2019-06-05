package spring

// Place your Spring DSL code here
beans = {

	// implémentation BMHS API
	bmhsApi(sen1.proxies.bmhs.api.impl.BmhsApiv0) {

	}

	// gestionnaire de crons
	// seconde | minute | heure | jour du mois (1-31) | mois | jour semaine (1-7) | année
	defaultScheduler(sen1.proxies.core.scheduler.DefaultScheduler) {
		jobs = [
			// Fetch les données BMHS pour les consumers
			'sen1.proxies.core.job.PushOutboxMainJob' : '0 0/5 * * * ?',
			// Fetch les données de la outbox et les envoit sur le réseau fédéré
			'sen1.proxies.core.job.FetchOutboxMainJob': '0 * * * * ?',
			// Fetch les données de la inbox et les envoit vers BMHS
			'sen1.proxies.core.job.FetchInboxMainJob': '0 * * * * ?'
		]
	}

	// le serializer des messages sur le réseau fédéré
	// utilisation du serializer par défaut XML
	// Utiliser le name "messageSerializer" pour qu'il soit injecté dans les jobs génériques
	messageSerializer(sen1.proxies.core.io.serialiser.XmlMessageSerializer) {

	}

	// le consumer pour le listener des messages reçues par le service fédéré
	// cette implémentation stocke les messages dans la inbox
	messageConsumer(sen1.proxies.core.io.consumer.InboxMessageConsumer) {

	}

	// implémentation XMPP pour le réseau fédéré
	// pour démarrer le service et l'initialiser correctement : @see sen1.proxies.pride.BootStrap
	federationService(sen1.proxies.core.service.federation.XmppFederationService) {
		// injecte le consumer de message
		messageConsumer = ref('messageConsumer')
	}
}

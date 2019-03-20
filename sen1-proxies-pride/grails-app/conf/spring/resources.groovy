package spring


// Place your Spring DSL code here
beans = {

	// instance warp10
	warp10(sen1.proxies.pride.warp10.impl.Warp10v0) {

	}


	// gestionnaire de crons
	// seconde | minute | heure | jour du mois (1-31) | mois | jour semaine (1-7) | année
	defaultScheduler(sen1.proxies.core.scheduler.DefaultScheduler) {
		jobs = [
			// Fetch les données Pride des compteurs référencés dans le proxy (toutes les 5 min)
			'sen1.proxies.core.job.PushOutboxMainJob' : '0 * * * * ?',
			// Fetch les données de la outbox et les envoit sur le réseau fédéré (toutes les 5 min)
			'sen1.proxies.core.job.FetchOutboxMainJob': '0 * * * * ?'
			// Fetch les données de la inbox et les envoit vers Pride (toutes les 5 min)
			//'sen1.proxies.core.job.FetchInboxMainJob': '0 * * * * ?'
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

	// le consumer pour le listener des messages reçues par le service fédéré
	// cette implémentation stocke les messages dans la inbox
	messageConsumer(sen1.proxies.core.io.consumer.InboxMessageConsumer) {

	}

	// implémentation XMPP pour le réseau fédéré
	// pour démarrer le service et l'initialiser correctement : @see sen1.proxies.pride.BootStrap
	federationService(sen1.proxies.core.service.federation.XmppFederationService) { messageConsumer = ref('messageConsumer') }


}

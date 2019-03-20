package sen1.proxies.core.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import grails.core.GrailsApplication
import sen1.proxies.core.Outbox
import sen1.proxies.core.OutboxService
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer
import sen1.proxies.core.io.OutboxConverter
import sen1.proxies.core.io.message.MessageBuilder
import sen1.proxies.core.service.FederationService
import sen1.proxies.core.service.ProxyService

/**
 * Job FetchOutboxSubJob
 * 
 * Traite les taches que lui délègue le job parent @see sen1.proxies.core.job.FetchOutboxMainJob
 * Ce job ne traite qu'une seule Outbox qui lui est passé en paramètre et l'envoit sur le réseau fédéré
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class FetchOutboxSubJob implements Job {

	@Autowired
	OutboxService outboxService

	@Autowired
	MessageSerializer messageSerializer

	@Autowired
	GrailsApplication grailsApplication

	@Autowired
	FederationService federationService


	/**
	 * 1. Charge la outbox
	 * 2. Charge le consumer associé 
	 * 3. l'envoit sur le réseau fédéré
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long outboxId = context.getJobDetail().getJobDataMap().getLong("outboxId")
		Outbox outbox = outboxService.findById(outboxId)
		assert outbox != null

		// récupère le message enregistré et l'envoit sur le réseau
		Message message = messageSerializer.read(outbox.data)
		federationService.sendMessage(message)

		// si pas d'erreur à l'envoi, le message peut être supprimé
		outboxService.delete(outbox)
	}
}

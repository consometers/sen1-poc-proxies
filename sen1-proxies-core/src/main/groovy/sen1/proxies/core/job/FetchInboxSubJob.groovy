package sen1.proxies.core.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import sen1.proxies.core.Inbox
import sen1.proxies.core.InboxService
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer
import sen1.proxies.core.scheduler.DefaultScheduler
import sen1.proxies.core.service.ProxyService

/**
 * Job FetchInboxSubJob
 * 
 * Sous-tâche pour traiter un élément de la inbox
 * Charge la donnée, la convertir en message et l'envoit au service proxy 
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class FetchInboxSubJob implements Job {

	static final Logger log = LoggerFactory.getLogger(FetchInboxSubJob)

	@Autowired
	InboxService inboxService

	@Autowired
	MessageSerializer messageSerializer

	@Autowired
	ProxyService proxyService


	/** 
	 * 1. charge une inbox
	 * 2. convertit la data en message
	 * 3. délègue la gestion du message au proxy pour qu'il l'envoit sur le système local
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long inboxId = context.getJobDetail().getJobDataMap().getLong("inboxId")
		Inbox inbox = inboxService.findById(inboxId)
		assert inbox != null

		// récupère le message enregistré et l'envoit sur le système local
		Message message = messageSerializer.read(inbox.data)
		proxyService.pushData(message)

		// si pas d'erreur à l'envoi, le message peut être supprimé
		inboxService.delete(inbox)
	}
}

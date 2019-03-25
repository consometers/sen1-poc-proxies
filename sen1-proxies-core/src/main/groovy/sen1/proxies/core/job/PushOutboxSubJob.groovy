package sen1.proxies.core.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import grails.core.GrailsApplication
import sen1.proxies.core.Consumer
import sen1.proxies.core.ConsumerService
import sen1.proxies.core.Outbox
import sen1.proxies.core.OutboxService
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer
import sen1.proxies.core.io.OutboxConverter
import sen1.proxies.core.io.message.MessageBuilder
import sen1.proxies.core.service.ProxyService

/**
 * Job PushOutboxSubJob
 * 
 * Traite les taches que lui délègue le job parent @see sen1.proxies.core.job.PushOutboxMainJob
 * Ce job ne traite qu'un seul consumer qui lui est passé en paramètre
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class PushOutboxSubJob implements Job {

	@Autowired
	ConsumerService consumerService

	@Autowired
	OutboxService outboxService

	@Autowired
	ProxyService proxyService

	@Autowired
	OutboxConverter outboxConverter

	@Autowired
	MessageSerializer messageSerializer

	@Autowired
	GrailsApplication grailsApplication


	/** 
	 * 1. charge les données du consumer depuis le système local sans transformation
	 * 2. passe toutes les valeurs dans le converter
	 * 4. enregistre toutes les valeurs dans la outbox via le serializer
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long consumerId = context.getJobDetail().getJobDataMap().getLong("consumerId")
		Consumer consumer = consumerService.findByIdFetchAll(consumerId)

		// lecture des données depuis le système local
		List datas = proxyService.fetchData(consumer)

		if (datas) {
			// créé un nouveau message complété avec les infos du consumer
			// et le passe dans le converter pour remplir les données
			Message message = MessageBuilder.builder()
					// les infos d'identification
					.username(consumer.userApp.user.username)
					.applicationDst(consumer.consumerApp.name)
					.applicationSrc(consumer.userApp.app.name)
					// les infos pour la donnée
					.name(consumer.name)
					.metaname(consumer.metaname)
					.metavalue(consumer.metavalue)
					.unite(consumer.unite)
					.type(consumer.type)
					.build()

			for (def data : datas) {
				message.datas << outboxConverter.convert(message, data)
			}

			// on ne stocke que des messages valides
			message.asserts()

			// construit un élément dans la outbox serialisé à partir du message
			// et sauvegarde en base
			Outbox outbox = new Outbox(receivedDate: new Date())
			outbox.data = messageSerializer.write(message)
			outboxService.save(outbox)

			// met à jour le consumer avec la date de la dernière valeur
			consumer.dateLastValue = message.dateLastValue()
			consumerService.save(consumer)
		}
	}
}

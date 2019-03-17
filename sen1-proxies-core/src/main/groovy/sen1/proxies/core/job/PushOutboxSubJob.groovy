package sen1.proxies.core.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.beans.factory.annotation.Autowired
import sen1.proxies.core.Outbox
import sen1.proxies.core.OutboxConsumer
import sen1.proxies.core.OutboxConsumerService
import sen1.proxies.core.io.Message
import sen1.proxies.core.io.MessageSerializer
import sen1.proxies.core.io.OutboxConverter
import sen1.proxies.core.service.PushOutboxService

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
	OutboxConsumerService outboxConsumerService

	@Autowired
	PushOutboxService pushOutboxService

	@Autowired
	OutboxConverter outboxConverter

	@Autowired
	MessageSerializer messageSerializer


	/** 
	 * 1. charge les données du consumer depuis le système local sans transformation
	 * 2. passe toutes les valeurs dans le converter
	 * 4. enregistre toutes les valeurs dans la outbox via le serializer
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long outboxConsumerId = context.getJobDetail().getJobDataMap().getLong("outboxConsumerId")
		OutboxConsumer outboxConsumer = outboxConsumerService.findById(outboxConsumerId)
		assert outboxConsumer != null

		// lecture des données depuis le système local
		List datas = pushOutboxService.fetchData(outboxConsumer)

		for(def data : datas) {
			// passe chaque donnée reçue dans le converter de Message
			Message message = outboxConverter.convert(outboxConsumer, data)

			// construit un élément dans la outbox serialisé à partir du message
			Outbox outbox = new Outbox(consumer: outboxConsumer, receivedDate: new Date())
			outbox.data = messageSerializer.write(message)

			// sauvegarde en base
			outboxConsumerService.save(outbox)
		}
	}
}

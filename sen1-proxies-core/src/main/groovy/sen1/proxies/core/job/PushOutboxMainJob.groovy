package sen1.proxies.core.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import sen1.proxies.core.Consumer
import sen1.proxies.core.ConsumerService
import sen1.proxies.core.scheduler.DefaultScheduler

/**
 * Job PushOutboxMainJob
 * 
 * Job commun à tous les proxy pour "pousser" les données du système local vers la outbox
 * Ce job ne se charge pas d'envoyer les données vers le réseau fédéré, ce n'est pas son "job" (arff)
 * Il aspire les données du système local et les enregistre dans la outbox temporaire. Il ne le fait que si au
 * moins 1 consumer est déclaré
 * 
 * Ce job peut servir à tous les proxy, il suffit de lui injecter :
 * - le service pour lire les données du système local @see sen1.proxies.core.service.PushOutboxService
 * - le converter pour transformer les données vers l'objet générique @see sen1.proxies.core.io.OutboxConverter<T, U extends MessageData>
 * - le serializer du format utilisé par le réseau fédéré @see sen1.proxies.core.io.MessageSerializer
 * 
 * Ce job ne va pas tout faire car ca pourrait prendre du temps si le nombre de consumers est élevé. Il va seulement charger
 * les consumers par lot, et lancer des tâches que des sous-jobs vont trouver. Cela va permettre de paralléliser le
 * traitement de tous les consumers
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class PushOutboxMainJob implements Job {

	static final Logger log = LoggerFactory.getLogger(PushOutboxMainJob)


	@Autowired
	ConsumerService consumerService

	@Autowired
	DefaultScheduler defaultScheduler

	/**
	 * Injecté depuis les properties
	 * @see application.yml
	 */
	@Value('${sen1.proxies.core.pagination.maxBackend}')
	int paginationMaxBackend


	/** 
	 * 1. charge tous les consumers associés à la outbox par lot
	 * 2. délègue le traitement par parralélisation aux sous-jobs
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long consumerCount = consumerService.count()
		log.info("Push datas to outbox : {} consumers", consumerCount)

		// lecture par page de tous les consumers et délègue le traitement aux sous-jobs
		if (consumerCount) {
			for (long page=0; page<=consumerCount/paginationMaxBackend; page++) {
				// WARN : ne pas oublier le sort dans le chargement des consumers du fait de la pagination
				// sinon si plusieurs appels, les éléments retournés ne seront pas cohérents
				List<Consumer> consumers = consumerService.list([offset: page*paginationMaxBackend,
					max: paginationMaxBackend, sort: 'id'])

				for (Consumer consumer : consumers) {
					defaultScheduler.scheduleOneShotJob(PushOutboxSubJob, new Date(), [consumerId: consumer.id])
				}
			}
		}
	}
}

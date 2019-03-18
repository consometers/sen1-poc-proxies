package sen1.proxies.core.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

import sen1.proxies.core.Outbox
import sen1.proxies.core.OutboxService
import sen1.proxies.core.scheduler.DefaultScheduler

/**
 * Job FetchOutboxMainJob
 * 
 * Job commun à tous les proxy pour "fetcher" les données de la outbox et les envoyer sur le réseau fédéré
 * 
 * Le volume de données peut être important à traiter en fonction du nombre de consumers et d'objets référencés
 * Ce job parent va juste calculer le nombre de datas à traiter et les envoyer une par une à des sous-jobs
 * pour paralléliser les tâches
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class FetchOutboxMainJob implements Job {

	static final Logger log = LoggerFactory.getLogger(FetchOutboxMainJob)

	@Autowired
	OutboxService outboxService

	@Autowired
	DefaultScheduler defaultScheduler

	/**
	 * Injecté depuis les properties
	 * @see application.yml
	 */
	@Value('${sen1.proxies.core.pagination.maxBackend}')
	int paginationMaxBackend


	/** 
	 * 1. charge la outbox par lot
	 * 2. délègue le traitement des datas de la outbox aux sous-jobs
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long outboxCount = outboxService.count()
		log.info("Fetch {} datas from outbox ", outboxCount)

		if (outboxCount) {
			// charge la outbox par lot
			for (long page=0; page<=outboxCount/paginationMaxBackend; page++) {
				// WARN : ne pas oublier le sort dans le chargement des datas du fait de la pagination
				// sinon si plusieurs appels, les éléments retournés ne seront pas cohérents
				List<Outbox> outboxs = outboxService.list([offset: page*paginationMaxBackend,
					max: paginationMaxBackend, sort: 'id'])

				// délègue chaque traitement d'une outbox à un sous-job
				for (Outbox outbox : outboxs) {
					defaultScheduler.scheduleOneShotJob(FetchOutboxSubJob, new Date(), [outboxId: outbox.id])
				}
			}
		}
	}
}

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
import sen1.proxies.core.scheduler.DefaultScheduler

/**
 * Job FetchInboxMainJob
 * 
 * Job commun à tous les proxy pour "fetcher" les données de la inbox et les envoyer vers le système local
 * 
 * Le volume de données peut être important à traiter. C'est pourquoi ce job parent va juste calculer le nombre de
 * datas à traiter et les envoyer une par une à des sous-jobs pour paralléliser les tâches
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class FetchInboxMainJob implements Job {

	static final Logger log = LoggerFactory.getLogger(FetchInboxMainJob)

	@Autowired
	InboxService inboxService

	@Autowired
	DefaultScheduler defaultScheduler

	/**
	 * Injecté depuis les properties
	 * @see application.yml
	 */
	@Value('${sen1.proxies.core.pagination.maxBackend}')
	int paginationMaxBackend


	/** 
	 * 1. charge la inbox par lot
	 * 2. délègue le traitement des datas de la inbox aux sous-jobs
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long inboxCount = inboxService.count()
		log.info("Fetch {} datas from inbox ", inboxCount)

		if (inboxCount) {
			// charge la outbox par lot
			for (long page=0; page<=inboxCount/paginationMaxBackend; page++) {
				// WARN : ne pas oublier le sort dans le chargement des datas du fait de la pagination
				// sinon si plusieurs appels, les éléments retournés ne seront pas cohérents
				List<Inbox> inboxs = inboxService.list([offset: page*paginationMaxBackend,
					max: paginationMaxBackend, sort: 'id'])

				// délègue chaque traitement d'une outbox à un sous-job
				for (Inbox inbox : inboxs) {
					defaultScheduler.scheduleOneShotJob(FetchInboxSubJob, new Date(), [inboxId: inbox.id])
				}
			}
		}
	}
}

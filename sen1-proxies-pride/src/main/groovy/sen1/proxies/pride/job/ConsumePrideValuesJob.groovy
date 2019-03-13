package sen1.proxies.pride.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.beans.factory.annotation.Autowired

import sen1.proxies.pride.MetricService
import sen1.proxies.pride.PrideService

/**
 * Récupère les valeurs dans Pride des metrics référencées dans le proxy qui ont au moins un consumer
 * Charge les données et les stockent en base
 * 
 * Le job va calculer toutes les metric à synchroniser et déléguer la gestion d'une metric (chargement pride +
 * stockage donnée) à des sous jobs. Chaque metric sera donc découper en une tâche
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ConsumePrideValuesJob implements Job {

	private static final int MAX_PAGE = 100


	@Autowired
	PrideService prideService

	@Autowired
	MetricService metricService


	/** (non-Javadoc)
	 *
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	void execute(JobExecutionContext context) throws JobExecutionException {
		// calcule d'abord le nombre total de metric avec consumer pour les charger par pagination
		long nbMetric = metricService.countWithConsumer()

		if (nbMetric) {
			// chargement par paquet de 100
			for (long page=0; page<=nbMetric / MAX_PAGE; page++) {

			}
		}
	}
}
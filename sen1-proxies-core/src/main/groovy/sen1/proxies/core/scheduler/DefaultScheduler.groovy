package sen1.proxies.core.scheduler

import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.Trigger
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import grails.core.GrailsApplication
import static org.quartz.TriggerBuilder.*
import static org.quartz.CronScheduleBuilder.*
import static org.quartz.JobBuilder.*
import static org.quartz.SimpleScheduleBuilder.*

/**
 * Gestionnaire principal pour la planification des taches planifiées.
 * Il suffit de référencer un objet de ce type dans le contexte Spring
 * et de lui associer des jobs
 * 
 * Démarrer le gestionnaire depuis le bootstrap pour démarrer les jobs uniquement quand le contexte webapp est prêt
 * et non pas avant quand le contexte Spring est démarré (trop tôt)
 * 
 * Utiliser la Map "jobs" pour injecter les jobs :
 * - key : job class
 * - value : cron expression
 * 
 * Pour créer le schéma, @see https://github.com/quartz-scheduler/quartz/tree/master/quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DefaultScheduler implements InitializingBean, ApplicationContextAware {

	private static final Logger log = LoggerFactory.getLogger(DefaultScheduler)


	AutowireCapableBeanFactory beanFactory
	Scheduler scheduler
	@Autowired
	GrailsApplication grailsApplication
	Map<String, String> jobs


	/**
	 * Inject jobs
	 * 
	 * @param jobs
	 */
	void setJobs(Map<String, String> jobs) {
		this.jobs = jobs
	}


	/**
	 * Récupère le contexte Spring
	 *
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	void setApplicationContext(final ApplicationContext context) {
		beanFactory = context.getAutowireCapableBeanFactory()
	}


	/**
	 * Récupère la config quartz depuis la config grails et instancie le moteur quartz
	 * avec sa config
	 *
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	void afterPropertiesSet() throws Exception {
		Properties quartzProperties = new Properties()

		// chargement des configs
		if (grailsApplication.config.containsKey('quartz')) {
			ConfigObject configObject = new ConfigObject()
			configObject.putAll(grailsApplication.config.quartz)
			quartzProperties << configObject.toProperties('org.quartz')
		}

		SchedulerFactory factory = new StdSchedulerFactory()
		factory.initialize(quartzProperties)
		scheduler = factory.getScheduler()
	}


	/**
	 * Démarre le gestionnaire
	 * 
	 * @return
	 */
	void start() {
		log.info "Starting jobs scheduling..."

		scheduler.getListenerManager().addJobListener(new AutowireJobListener(beanFactory))
		scheduler.start()

		// ajout des jobs injectés
		jobs?.each { className, cron ->
			Class jobClass = Class.forName(className)
			Set triggers = [getTrigger(jobClass, cron)]
			scheduler.scheduleJob(getJobDetail(jobClass), triggers, true)
		}
	}


	/**
	 * Arrete le gestionnaire
	 */
	void shutdown() {
		log.info "Stopping jobs scheduling..."
		scheduler.shutdown()
	}


	/**
	 * Déclare les infos du job
	 * 
	 * @param jobClass
	 * @return
	 */
	JobDetail getJobDetail(Class<Job> jobClass) {
		return newJob(jobClass)
				.withIdentity(jobClass.getSimpleName())
				.storeDurably()
				.build()
	}


	/**
	 * Déclare le déclencheur du job
	 * 
	 * @param jobClass
	 * @param cron
	 * @return
	 */
	Trigger getTrigger(Class<Job> jobClass, String cron) {
		return newTrigger()
				.withIdentity(jobClass.getSimpleName() + "Trigger")
				.withSchedule(cronSchedule(cron))
				.build()
	}


	/**
	 * Créé un job one-shot (une seule exécution)
	 */
	void scheduleOneShotJob(Class<Job> jobClass, Date scheduledDate, Map data) {
		def jobName = jobClass.getSimpleName() + UUID.randomUUID().toString()

		def jobDetail = newJob(jobClass)
				.withIdentity(jobName)
				.storeDurably(false)
				.usingJobData(new JobDataMap(data))
				.build()

		def trigger = newTrigger().forJob(jobName).startAt(scheduledDate).build()

		scheduler.scheduleJob(jobDetail, trigger)
	}
}

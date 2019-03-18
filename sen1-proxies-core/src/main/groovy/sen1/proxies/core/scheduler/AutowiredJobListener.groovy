package sen1.proxies.core.scheduler

import org.quartz.JobExecutionContext
import org.quartz.listeners.JobListenerSupport
import grails.core.GrailsApplication
import sen1.proxies.core.ApplicationUtils

/**
 * Listener job pour injecter les propriéts Autowired
 * Ceci n'est pas fait automatiquement car les instances jobs ne sont pas créées depuis le contexte Spring
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AutowiredJobListener extends JobListenerSupport {

	GrailsApplication grailsApplication


	/**
	 * Constructor
	 * 
	 * @param beanFactory
	 */
	AutowiredJobListener(GrailsApplication grailsApplication) {
		this.grailsApplication = grailsApplication
	}


	/**
	 * Exécuter avant le démarrage du job
	 * On injecte les propriétés Autowired
	 *
	 * @see org.quartz.listeners.JobListenerSupport#jobToBeExecuted(org.quartz.JobExecutionContext)
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		super.jobToBeExecuted(context)
		ApplicationUtils.autowireBean(grailsApplication, context.getJobInstance())
	}


	/** (non-Javadoc)
	 *
	 * @see org.quartz.JobListener#getName()
	 */
	@Override
	String getName() {
		this.getClass().getName()
	}
}

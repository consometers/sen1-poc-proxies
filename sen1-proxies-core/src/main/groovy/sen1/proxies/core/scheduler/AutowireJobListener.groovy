package sen1.proxies.core.scheduler

import org.quartz.JobExecutionContext
import org.quartz.listeners.JobListenerSupport
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/**
 * Listener job pour injecter les propriéts Autowired
 * Ceci n'est pas fait automatiquement car les instances jobs ne sont pas créées depuis le contexte Spring
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AutowireJobListener extends JobListenerSupport {

	AutowireCapableBeanFactory beanFactory


	/**
	 * Constructor
	 * 
	 * @param beanFactory
	 */
	AutowireJobListener(AutowireCapableBeanFactory beanFactory) {
		this.beanFactory = beanFactory
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
		beanFactory.autowireBean(context.getJobInstance())
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

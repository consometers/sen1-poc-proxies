package sen1.proxies.core

import grails.core.GrailsApplication

/**
 * Classe utilitaire sur l'applicatio, donc les contextes Spring/Grails
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ApplicationUtils {
	/**
	 * Effectue manuellement les injections de dépendance sur un bean
	 * 
	 * @param grailsApplication
	 * @param bean
	 * @return
	 */
	static Object autowireBean(GrailsApplication grailsApplication, Object bean) {
		grailsApplication.mainContext.autowireCapableBeanFactory.autowireBean(bean)
		return bean
	}
}

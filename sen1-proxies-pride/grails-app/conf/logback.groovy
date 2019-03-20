import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
	encoder(PatternLayoutEncoder) {
		charset = Charset.forName('UTF-8')

		pattern = '%clr(%date{yyyy-MM-dd HH:mm:ss}){faint} ' + // Date
				'%clr(%-5level) ' + // Log level
				'%clr([%-15.15thread]){faint} ' + // Thread
				'%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
				'%message%n%wex' // Message
	}
}


switch (Environment.getCurrentEnvironment()) {
	case Environment.PRODUCTION:
		logger "sen1.proxies", INFO
		logger "grails.app", INFO
		root ERROR, ['STDOUT']
		break

	case Environment.TEST:
	case Environment.DEVELOPMENT:
		logger "sen1.proxies", DEBUG
		logger "org.hibernate.SQL", DEBUG
		logger "grails.app", DEBUG
		root INFO, ['STDOUT']
		break
}




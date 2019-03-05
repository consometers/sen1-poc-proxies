package sen1.proxies.core

/**
 * Classe utilitaire sur les dates
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DateUtils {
	static final String FORMAT_DATETIME_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'"
	static final String FORMAT_DATE_ISO = "yyyy-MM-dd"


	/**
	 * Format d'une date/time ISO8601
	 *
	 * @param date
	 * @return
	 */
	static String formatDateTimeIso(Date date) {
		return date?.format(FORMAT_DATETIME_ISO)
	}


	/**
	 * Format d'une date ISO8601
	 *
	 * @param date
	 * @return
	 */
	static String formatDateIso(Date date) {
		return date?.format(FORMAT_DATE_ISO)
	}
}

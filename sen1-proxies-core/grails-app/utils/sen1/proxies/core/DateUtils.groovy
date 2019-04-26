package sen1.proxies.core

import java.text.SimpleDateFormat

/**
 * Classe utilitaire sur les dates
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DateUtils {
	static final String FORMAT_DATETIME_ISO = "yyyy-MM-dd'T'HH:mm:ss'Z'"
	static final String FORMAT_DATE_ISO = "yyyy-MM-dd"
	static final String FORMAT_DATE_USER = "dd-MM-yyyy"


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


	/**
	 * Parse date user
	 *
	 * @param date
	 * @return
	 */
	static Date parseDateUser(String user) {
		return new SimpleDateFormat(FORMAT_DATE_USER).parse(user)
	}


	/**
	 * Parse date ISO
	 *
	 * @param date
	 * @return
	 */
	static Date parseDateIso(String iso) {
		return new SimpleDateFormat(FORMAT_DATE_ISO).parse(iso)
	}
}

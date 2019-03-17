package sen1.proxies.core

/**
 * Les différents types d'une donnée
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
enum DataTypeEnum {
	/**
	 * Represents a string value
	 */
	dt_string,
	/**
	 * Represents integer value
	 */
	dt_integer,
	/**
	 * Represents a numerical value.
	 */
	dt_numeric,
	/**
	 * Represents a date value
	 */
	dt_date,
	/**
	 * Represents a date and optional time value
	 */
	dt_datetime,
	/**
	 * Represents a duration value 
	 */
	dt_duration,
	/**
	 * Represents a time value
	 */
	dt_time,
	/**
	 * Represents a boolean value that can be either true or false.
	 */
	dt_boolean
}

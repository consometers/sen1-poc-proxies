package sen1.proxies.core.http.transformer

/**
 * Interface de transformation des contenus d'une requête HTTP
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface ResponseTransformer<T> {

	/**
	 * Transformation d'un flux
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	T transform(InputStream inputStream) throws Exception
}

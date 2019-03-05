package sen1.proxies.core.http.transformer

/**
 * Implémentation response transformer vers du texte
 * ie sans transformation
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class StringResponseTransformer implements ResponseTransformer<String> {

	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.core.http.transformer.ResponseTransformer#transform(java.io.InputStream)
	 */
	@Override
	String transform(InputStream inputStream) throws Exception {
		return inputStream?.text
	}
}

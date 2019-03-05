package sen1.proxies.core.http.transformer

/**
 * Implémentation response transformer vers un buffer simple 
 * ie sans transformation
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ByteArrayResponseTransformer implements ResponseTransformer<byte[]> {

	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.core.http.transformer.ResponseTransformer#transform(java.io.InputStream)
	 */
	@Override
	byte[] transform(InputStream inputStream) throws Exception {
		return inputStream?.bytes
	}
}

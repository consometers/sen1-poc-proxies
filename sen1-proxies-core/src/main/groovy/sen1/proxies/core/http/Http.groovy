package sen1.proxies.core.http

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.ResponseHandler
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.FormBodyPartBuilder
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import sen1.proxies.core.http.transformer.ByteArrayResponseTransformer
import sen1.proxies.core.http.transformer.ResponseTransformer



/**
 * Classe de type Fluent pour exécuter des requêtes HTTP
 * Surcouche de la librairie HttpClient
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class Http {
	static final Logger log = LoggerFactory.getLogger(Http.class);

	URIBuilder uriBuilder
	HttpRequestBase httpRequest
	MultipartEntityBuilder entityBuilder
	FormBodyPartBuilder formBuilder


	/**
	 * Point d'entrée création d'une requete get
	 *
	 * @param url
	 * @return
	 */
	static Http Get(String url) {
		return build(url, new HttpGet())
	}


	/**
	 * Point d'entrée création d'une requete post
	 *
	 * @param url
	 * @return
	 */
	static Http Post(String url) {
		return build(url, new HttpPost())
	}


	/**
	 * Méthode interne pour instancier un objet Http
	 * 
	 * @param url
	 * @param requestBase
	 * @return
	 */
	private static build(String url, HttpRequestBase requestBase) {
		Http http = new Http(uriBuilder: new URIBuilder(url))
		http.httpRequest = requestBase
		return http
	}


	/**
	 * Créé un singleton pour le multipart
	 *
	 * @return
	 */
	private MultipartEntityBuilder entityBuilder() {
		if (! entityBuilder) {
			entityBuilder = MultipartEntityBuilder.create()
		}
		return entityBuilder
	}


	/**
	 * Crée un singleton pour les formulaires
	 *
	 * @return
	 */
	private FormBodyPartBuilder formBuilder() {
		if (! formBuilder) {
			formBuilder = FormBodyPartBuilder.create()
		}
		return formBuilder
	}


	/**
	 * Query param
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	Http queryParam(String name, String value) {
		uriBuilder.setParameter(name, value)
		return this
	}


	/**
	 * Body param
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	Http formField(String name, String value) {
		formBuilder().addField(name, value)
		return this
	}


	/**
	 * Body depuis un buffer.
	 * Attention cette méthode ne peut pas êre appelée en plus de formField ou multipart..
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	Http bodyByteArray(byte[] buffer) {
		httpRequest.setEntity(new ByteArrayEntity(buffer))
		return this
	}


	/**
	 * Body depuis un String.
	 * Attention cette méthode ne peut pas êre appelée en plus de formField ou multipart..
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	Http bodyString(String buffer) {
		httpRequest.setEntity(new StringEntity(buffer))
		return this
	}


	/**
	 * Header param
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	Http header(String name, String value) {
		httpRequest.addHeader(name, value)
		return this
	}


	/**
	 * Ajout d'un contenu binaire
	 *
	 * @param name
	 * @param buffer
	 * @return
	 */
	Http multiPartByteArray(String fieldName, String fileName, byte[] buffer, ContentType contentType = ContentType.DEFAULT_BINARY ) {
		entityBuilder().addBinaryBody(fieldName, buffer, contentType, fileName)
		return this
	}


	/**
	 * Exécute la requête
	 * si le code http n'est pas ok, une exception est levée. cela évite à chaque appelant de gérer le code retour
	 * mais plutot de gérer une exception
	 *
	 * @param transformer transforme le contenu de la réponse HTTP
	 * @return HttpResult
	 * @throws Exception
	 */
	HttpResult execute(ResponseTransformer transformer = null) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault()
		HttpResult httpResult = null

		try {
			httpRequest.setURI(uriBuilder.build())

			if (formBuilder) {
				entityBuilder().addPart(formBuilder.build())
			}

			if (entityBuilder) {
				if (httpRequest instanceof HttpEntityEnclosingRequestBase) {
					(httpRequest as HttpEntityEnclosingRequestBase).setEntity(entityBuilder.build())
				} else {
					throw new Exception("cannot set entity on simple HttpRequestBase !")
				}
			}

			// on passe par le handler, comme ca c'est l'objet httpClient qui gère la fermeture des ressources
			httpResult = httpClient.execute(httpRequest, new ResponseHandler<HttpResult>() {
						HttpResult handleResponse(HttpResponse response) {
							StatusLine status = response.statusLine
							HttpEntity entity = response.entity

							HttpResult result = new HttpResult(code: status.statusCode, message: status.reasonPhrase,
							contentType: entity.contentType?.value, contentLength: entity.contentLength,
							encoding: entity.contentEncoding?.value)

							// passe le contenu de la réponse au transformer
							if (!transformer) {
								transformer = new ByteArrayResponseTransformer()
							}

							result.content = transformer.transform(entity.content)

							return result
						}
					})

			if (httpResult.code != 200) {
				throw new Exception("http request error [${httpResult.code}] : ${httpResult.message} !")
			}
		} catch (Exception ex) {
			throw ex
		} finally {
			httpClient.close()
		}

		return httpResult
	}
}

package sen1.proxies.core.io

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper

import sen1.proxies.core.DataTypeEnum

/**
 * Représentation d'un message transitant sur le réseau fédéré
 * Un message fait transiter des données de type <T> d'une application A vers B
 * 
 * Pour construire un message, toujours utiliser le MessageBuilder
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface Message<T extends MessageData> {
	/**
	 * Nom identifiant de l'utilisateur
	 */
	String getUsername()
	void setUsername(String username)

	/**
	 * Nom/identifiant de la données
	 */
	String getName()
	void setName(String name)

	/**
	 * Si donnée multi-value ou clé multiple, indique le nom/identifiant de la méta-donnée associée
	 */
	String getMetaname()
	void setMetaname(String metaname)

	/**
	 * Si donnée multi-value ou clé multiple, indique la valeur de la méta-donnée associée
	 */
	String getMetavalue()
	void setMetavalue(String metavalue)

	/**
	 * Unité (si valeur métrique?)
	 */
	String getUnite()
	void setUnite(String unite)

	/**
	 * Type de la données
	 */
	DataTypeEnum getType()
	void setType(DataTypeEnum type)

	/**
	 * Nom de l'application émettrice du message
	 */
	String getApplicationSrc()
	void setApplicationSrc(String applicationSrc)

	/**
	 * Nom de l'application destinatrice du message
	 */
	String getApplicationDst()
	void setApplicationDst(String applicationDst)

	/**
	 * Liste de valeurs de données
	 */
	List<T> getDatas()
	void setDatas(List<T> datas)

	/**
	 * Ajout d'une nouvelle donnée
	 *
	 * @param data <T>
	 * @return this
	 */
	Message addData(T data)


	/**
	 * Construit une instance data complétée avec les valeurs passées en paramètre
	 * Le type dépend de l'implémentation du message
	 * 
	 * @param value
	 * @param timestamp
	 * @return <T>
	 */
	T newMessageDataInstance(Object value, Date timestamp)


	/**
	 * Construit une instance data vide
	 * Le type dépend de l'implémentation du message
	 *
	 * @param value
	 * @param timestamp
	 * @return <T>
	 */
	T newMessageDataInstance()


	/**
	 * La date de la valeur la plus récente dans la liste des datas
	 * 
	 * @return
	 */
	Date dateLastValue()
}

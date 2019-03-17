package sen1.proxies.core.io.message

import sen1.proxies.core.io.Message

/**
 * Implémentation standard d'un message sur réseau fédéré avec liste de datapoints
 * Les valeurs de données sont enregistrées en String pour tout faire passer
 * C'est le type de la donnée fournie dans le header qui permettra au consumer de la convertir dans son format
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DefaultMessage implements Message<DefaultMessageData> {
}

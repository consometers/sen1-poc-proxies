package sen1.proxies.core.io

/**
 * Serialisation d'un message sur le réseau fédéré
 * Permet de lire/écrire dans un format particulier
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
interface MessageSerializer {

	/**
	 * Lit un message depuis un buffer
	 * 
	 * @param buffer
	 * @return Message
	 * @throws Exception
	 */
	Message read(byte[] buffer) throws Exception


	/**
	 * Ecrit un message dans un buffer
	 * 
	 * @param message
	 * @return buffer
	 * @throws Exception
	 */
	byte[] write(Message message) throws Exception
}

package net.ornithemc.osl.text.api;

/**
 * Common interface for objects accepting {@linkplain TextComponent} messages.
 * 
 * @see TextComponent
 * @see TextComponents
 */
public interface MessageConsumer {

	/**
	 * Send a {@linkplain TextComponent} message.
	 */
	void sendMessage(TextComponent message);

}

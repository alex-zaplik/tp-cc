package edu.pwr.tp.game.desktop.net;

import edu.pwr.tp.game.desktop.message.builder.IMessageBuilder;
import edu.pwr.tp.game.desktop.message.builder.JSONMessageBuilder;
import edu.pwr.tp.game.desktop.message.parser.IMessageParser;
import edu.pwr.tp.game.desktop.message.parser.JSONMessageParser;
import edu.pwr.tp.game.desktop.views.IView;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class representing the client functionality
 *
 * @author Aleksander Lasecki
 */
public class Client {

	/**
	 * Singleton instance
	 */
	private static Client instance = null;

	/**
	 * The socket used for communicating with the server
	 */
	private Socket socket = null;
	/**
	 * PrintWriter used for sending messages to the server
	 */
	private PrintWriter out = null;
	/**
	 * BufferedReader used to receive messages from the server
	 */
	private BufferedReader in = null;

	// TODO: public is bad
	/**
	 * Used to parse received messages
	 */
	public IMessageParser parser;
	/**
	 * Used to build messages that are to be sent throw a socket
	 */
	public IMessageBuilder builder;

	/**
	 * Reference to the current view
	 */
	private IView view;

	/**
	 * Thread used to wait for messages sent by the server
	 */
	private Thread inputListener = new Thread(() -> {
		while (true) {
			if (getIn() != null) {
				try {
					final String input = getIn().readLine();

					if (input != null)
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								view.handleInput(input);
							}
						});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	});

	/**
	 * Starts the listening thread
	 */
	public void startListening() {
		inputListener.setDaemon(true);
		inputListener.start();
	}

	void startGame() {
		getOut().println(builder.put("s_start", "StartGame").get());
	}

	void sendMove(int fx, int fy, int tx, int ty) {
		getOut().println(builder.put("i_action", 0).put("i_fx", fx).put("i_fy", fy).put("i_tx", tx).put("i_ty", ty).get());
	}

	void skipMove() {
		getOut().println(builder.put("i_action", 1).get());
	}

	/**
	 * Sends a join request to the server
	 *
	 * @param name  Name of the party that the users wants to join
	 */
	public void joinParty(String name) {
		String msg = getBuilder()
				.put("i_action", 1)
				.put("s_name", name)
				.get();

		System.out.println(msg);
		getOut().println(msg);
	}

	/**
	 * Sends a create request to the server
	 *
	 * @param name  Name of the party to be created
	 * @param max   Maximum number of users connected to the new party
	 */
	public void sendPartySettings(String name, int max) {
		getOut().println(
				getBuilder().put("i_action", 0)
						.put("s_name", name)
						.put("i_max", max)
						.get()
		);
	}

	/**
	 * Sends a message to the server
	 *
	 * @param msg   The message to be sent
	 */
	public void sendMessage(String msg) {
		getOut().println(msg);
	}

	/**
	 * Gets a list of all running parties from the server
	 *
	 * @return              The received list
	 * @throws IOException  Thrown by the BufferedReader
	 */
	List<Party> getParties() throws IOException {
		List<Party> parties = new ArrayList<>();

		String response = getIn().readLine();
		if (response != null) {
			Map<String, Object> responseMap = Client.getInstance().getParser().parse(response);

			if (!responseMap.containsKey("s_msg")) {
				for (int i = (int) responseMap.get("i_size") - 1; i >= 0; i--) {
					parties.add(new Party((String) responseMap.get("s_name"), (int) responseMap.get("i_left"), (int) responseMap.get("i_max")));

					if (i > 0)
						response = Client.getInstance().getIn().readLine();

					if (response == null)
						throw new IOException();

					responseMap = Client.getInstance().getParser().parse(response);
				}
			} else {
				parties.add(new Party((String) responseMap.get("s_msg"), -1, -1));
			}
		}

		return parties;
	}

	/**
	 * Initializes the connection with a server
	 *
	 * @param address   The address of the server
	 * @param port      The port of the server
	 *
	 * @return 			True if the connection was set up properly
	 */
	public boolean initConnection(IView view, String address, int port) {
		changeView(view);

		try {
			setSocket(new Socket(address, port));
			setOut(new PrintWriter(Client.getInstance().getSocket().getOutputStream(), true));
			setIn(new BufferedReader(new InputStreamReader(Client.getInstance().getSocket().getInputStream())));

			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void changeView(IView view) {
		this.view = view;
	}

	/**
	 * Returns the singleton instance
	 *
	 * @return  The instance
	 */
	public static Client getInstance() {
		if (instance == null) {
			synchronized (Client.class) {
				if (instance == null) {
					instance = new Client();
					instance.init();
				}
			}
		}

		return instance;
	}

	/**
	 * Returns the client's socket
	 *
	 * @return  The client's socket
	 */
	private Socket getSocket() {
		return socket;
	}

	/**
	 * Sets the client's socket
	 *
	 * @param socket The socket
	 */
	private void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Returns the output of the client
	 *
	 * @return  The output of the client
	 */
	private PrintWriter getOut() {
		return out;
	}

	/**
	 * Sets the output of the client
	 *
	 * @param out   The output PrintWriter
	 */
	private void setOut(PrintWriter out) {
		this.out = out;
	}

	/**
	 * Returns the input of the client
	 *
	 * @return  The input of the client
	 */
	private BufferedReader getIn() {
		return in;
	}

	/**
	 * Sets the input of the client
	 *
	 * @param in   The input BufferedReader
	 */
	private void setIn(BufferedReader in) {
		this.in = in;
	}

	/**
	 * Returns the parser used by the client
	 *
	 * @return  The parser used by the client
	 */
	public IMessageParser getParser() {
		return parser;
	}

	/**
	 * Returns the builder used by the client
	 *
	 * @return  The builder used by the client
	 */
	public IMessageBuilder getBuilder() {
		return builder;
	}

	/**
	 * Disconnects the user from the server
	 */
	void disconnect() {
		try {
			out.close();
			in.close();
			socket.close();

			out = null;
			in = null;
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the client is connected to a server
	 *
	 * @return  True if the client is connected to a server
	 */
	boolean isConnected() {
		return socket != null && out != null && in != null;
	}

	/**
	 * Initializes the client instance and start the view's thread
	 */
	private void init() {
		parser = new JSONMessageParser();
		builder = new JSONMessageBuilder();
	}
}


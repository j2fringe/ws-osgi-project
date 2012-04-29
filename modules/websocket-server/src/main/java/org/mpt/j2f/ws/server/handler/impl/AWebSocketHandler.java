package org.mpt.j2f.ws.server.handler.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebSocketHandler;

public abstract class AWebSocketHandler implements WebSocketHandler{

	private static List<WebSocketConnection> connections_ = new ArrayList<WebSocketConnection>();
	
	@Override
	public void onOpen(WebSocketConnection connection) throws Exception {
		// TODO Auto-generated method stub
		connections_.add(connection);
	}

	@Override
	public void onClose(WebSocketConnection connection) throws Exception {
		// TODO Auto-generated method stub
		connections_.remove(connection);
	}
	
	public void closeAllConnections() {
		Iterator<WebSocketConnection> iter = connections_.iterator();
		
		while(iter.hasNext()) {
			WebSocketConnection con = iter.next();
			System.out.println("Closing connection: " + con);
			iter.remove();
			
			con.close();
		}
	}

}

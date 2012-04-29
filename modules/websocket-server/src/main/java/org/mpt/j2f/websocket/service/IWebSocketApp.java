package org.mpt.j2f.websocket.service;

import org.webbitserver.WebSocketHandler;

public interface IWebSocketApp {

	public static final String PROPERTY_WEBSOCKET_APP_ID = "org.mpt.j2f.websocket.app.id";
	
	public String getWebSocketAppID();
	
	public String getResourcePackage();
	
	public WebSocketHandler getWebSocketHandler();
	
	public String getWebSocketURL();

	public String getHttpURL();
}

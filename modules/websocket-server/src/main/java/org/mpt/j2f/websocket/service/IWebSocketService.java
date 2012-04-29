package org.mpt.j2f.websocket.service;


public interface IWebSocketService {
	public static final String PROPERTY_DEFAULT_WEBSOCKET_SERVER_PORT = "org.mpt.j2f.websocket.port";
	public static final String DEFAULT_WEBSOCKET_SERVER_PORT = "9002";
	
	public void registerWebSocketApp(IWebSocketApp app);
	
	
	public void unregisterWebSocketApp(String id);
}

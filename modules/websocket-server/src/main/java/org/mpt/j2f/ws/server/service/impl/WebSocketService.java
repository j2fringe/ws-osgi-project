package org.mpt.j2f.ws.server.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mpt.j2f.ws.server.OSGiNettyWebServer;
import org.mpt.j2f.ws.server.handler.ClassPathHandler;
import org.mpt.j2f.ws.server.service.IWebSocketApp;
import org.mpt.j2f.ws.server.service.IWebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketService implements IWebSocketService{
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);
	
	private static HashMap<String, IWebSocketApp> wsApps_ = new HashMap<String, IWebSocketApp>();
	private static OSGiNettyWebServer webServer_ = null;
	private static int port = -1;
	
	public void startup(Map<?, ?> properties)  {
		String port_str = (String)properties.get(PROPERTY_DEFAULT_WEBSOCKET_SERVER_PORT);
		if(port_str == null || port_str.length() == 0) {
			port_str = System.getProperty(PROPERTY_DEFAULT_WEBSOCKET_SERVER_PORT);
		} 
		if(port_str == null || port_str.length() == 0) {
			port_str = DEFAULT_WEBSOCKET_SERVER_PORT;
		}
		try {
			port = Integer.parseInt(port_str);
		} catch(Exception ex) {
			System.err.println("Port invalid: " + port_str + " set to default websocket port: " + DEFAULT_WEBSOCKET_SERVER_PORT);
			port = Integer.parseInt(DEFAULT_WEBSOCKET_SERVER_PORT);
		}
//		port = ((Integer)properties.get(PROPERTY_DEFAULT_WEBSOCKET_SERVER_PORT)).intValue();
//		System.out.println(""+context.getBundle().getLocation());
		webServer_ = new OSGiNettyWebServer(port);
		webServer_.start();
		logger.info("Server running at " + webServer_.getUri());
//		LogUtility.logInfo(this, "Server Running at " + webServer_.getUri());
	}
	
	public static int getPort() {
		return port;
	}
	
	
	
	
	public void shutdown() {
		try {
			webServer_.stop().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Stopping server at " + webServer_.getUri());
	}
	
	public void update(Map<?,?> properties) {
		port = Integer.parseInt((String)properties.get(PROPERTY_DEFAULT_WEBSOCKET_SERVER_PORT));
		System.out.println("New port: " + port);
		try {
			webServer_.stop().get();
			webServer_ = null;
			webServer_ = new OSGiNettyWebServer(port);
			webServer_.start().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




	@Override
	public void registerWebSocketApp(IWebSocketApp app) {
		logger.info("Register WebSocketApp ("+app.getWebSocketAppID()+"): HTTP_URL: " +app.getHttpURL() + " WS: " + app.getWebSocketURL() );
		wsApps_.put(app.getWebSocketAppID(), app);
		webServer_.add(app.getWebSocketAppID(), app.getWebSocketURL(), app.getWebSocketHandler());
		webServer_.add(app.getWebSocketAppID(), new ClassPathHandler(app));
		
	}




	@Override
	public void unregisterWebSocketApp(String id) {
		logger.info("Un-Register WebSocketApp ("+id+")" );
		wsApps_.remove(id);
		webServer_.removeHandlers(id);
		
	}


	
}

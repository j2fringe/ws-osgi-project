package org.mpt.j2f.websocket.sample.app;

import java.util.Map;

import org.mpt.j2f.websocket.service.IWebSocketApp;
import org.mpt.j2f.websocket.service.IWebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebSocketHandler;

public class WSSampleApp implements IWebSocketApp {

	private Logger logger = LoggerFactory.getLogger(WSSampleApp.class);
	
	private static final String WS_SAMPLE_APP_ID = "org.mpt.j2f.websocket.sample";
	private static final String WS_SAMPLE_WEBSOCKET_URL= "/ws-sample";
	private static final String WS_SAMPLE_HTTP_URL= "/sample";
	private static final String WS_SAMPLE_RESOURCE_PACKAGE = "/org/mpt/j2f/websocket/res/sample";
	
	private WSSampleHandler handler = new WSSampleHandler();
	
	public void startup(Map<?,?> properties) {
		logger.debug("WSSampleApp startup");
	}

	public void shutdown(Map<?,?> properties) {
		logger.debug("WSSampleApp shutdown");
	}
	
	public void registerWebSocketService(IWebSocketService service) {
		service.registerWebSocketApp(this);
	}
	
	
	public void unregisterWebSocketService(IWebSocketService service) {
		service.unregisterWebSocketApp(WS_SAMPLE_APP_ID);	
		
	}
	
	@Override
	public String getWebSocketAppID() {
		return WS_SAMPLE_APP_ID;
	}

	@Override
	public String getResourcePackage() {
		return WS_SAMPLE_RESOURCE_PACKAGE;
	}

	@Override
	public WebSocketHandler getWebSocketHandler() {
		return handler;
	}

	@Override
	public String getWebSocketURL() {
		return WS_SAMPLE_WEBSOCKET_URL;
	}

	@Override
	public String getHttpURL() {
		return WS_SAMPLE_HTTP_URL;
	}
	

}

package org.mpt.j2f.ws.server.sample.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.mpt.j2f.ws.server.handler.impl.AWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebSocketConnection;

public class WSSampleHandler extends AWebSocketHandler {

	private Logger logger = LoggerFactory.getLogger(WSSampleHandler.class);
	
	private static List<WebSocketConnection> connections_ = new ArrayList<WebSocketConnection>();
	
	private TimeShow timeShow = null;
	
	
	@Override
	public void onOpen(WebSocketConnection connection) throws Exception {
		connections_.add(connection);
		logger.info("Open WebSocket: Starting TimeShow");
		timeShow = new TimeShow(connection);
		timeShow.startThread();
	}

	@Override
	public void onClose(WebSocketConnection connection) throws Exception {
		logger.info("Close WebSocket: Stopping TimeShow");
		connections_.remove(connection);
		timeShow.stopThread();
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
	
	@Override
	public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
		logger.info("Message received: " + msg);
		String[] data = msg.split("\\|");
		if(data.length == 2) {
			if(data[0].equals("testButton")) {
				connection.send("result|"+data[1]);
			} else if (data[0].equals("testTimer")) {
				int updateTime = Integer.parseInt(data[1]);
				logger.info("Update timer to " + updateTime + " seconds");
				timeShow.updateTimer(updateTime);
				connection.send("updateTime|"+updateTime);
				
			}
		}
		
	}

	@Override
	public void onMessage(WebSocketConnection connection, byte[] msg) throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPing(WebSocketConnection connection, byte[] msg) throws Throwable {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPong(WebSocketConnection connection, byte[] msg) throws Throwable {
		// TODO Auto-generated method stub
		
	}
	
	class TimeShow extends Thread {
		private WebSocketConnection con;
		private boolean isRunning = false;
		private int updateTime = 5*1000;
		
		public TimeShow(WebSocketConnection conn) {
			con = conn;
		}
		
		public void startThread() {
			if(!isRunning) {
				isRunning = true;
				start();
			}
		}
		
		public void stopThread() {
			if(isRunning) {
				isRunning = false;
			}
		}
		
		public void updateTimer(int sec) {
			updateTime = sec*1000;
		}
		
		public void run() {
			
			while(isRunning) {
				Date date = new Date(System.currentTimeMillis());
				con.send("time|"+date.toString());
				try {
					Thread.sleep(updateTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}

}

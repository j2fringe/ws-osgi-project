package org.mpt.j2f.ws.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.EventSourceHandler;
import org.webbitserver.HttpHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebSocketHandler;
import org.webbitserver.handler.HttpToEventSourceHandler;
import org.webbitserver.handler.HttpToWebSocketHandler;
import org.webbitserver.handler.PathMatchHandler;
import org.webbitserver.netty.NettyWebServer;

public class OSGiNettyWebServer extends NettyWebServer {
	
	private static final Logger logger = LoggerFactory.getLogger(OSGiNettyWebServer.class);
	
	private static final HashMap<String, List<HttpHandler>> wsHandlers_ = new HashMap<String, List<HttpHandler>>();
    
    public OSGiNettyWebServer(int port) {
        super(port);
    }

    private OSGiNettyWebServer(ExecutorService executorService, int port) {
        super((Executor)executorService, port);
        // If we created the executor, we have to be responsible for tearing it down.
        executorServices.add(executorService);
    }

    public OSGiNettyWebServer(final Executor executor, int port) {
        this(executor, new InetSocketAddress(port), localUri(port));
    }

    public OSGiNettyWebServer(final Executor executor, SocketAddress socketAddress, URI publicUri) {
       super(executor, socketAddress, publicUri);
    }

    private void addNewHandler(String id, HttpHandler handler) {
    	List<HttpHandler> list = wsHandlers_.get(id);
    	if(list == null) {
    		list = new ArrayList<HttpHandler>();
    		wsHandlers_.put(id, list);
    	}
 
    	list.add(handler);
    }
	
	public NettyWebServer add(String id, HttpHandler handler) {
		addNewHandler(id, handler);
    	logger.info("Adding new handler: " + id);
		return super.add(handler);
	}

	
	public NettyWebServer removeHandlers(String id) {
		List<HttpHandler> list = wsHandlers_.get(id);
		Iterator<HttpHandler> iter = list.iterator();
		while(iter.hasNext()) {
			handlers.remove(iter.next());
		}
		wsHandlers_.remove(id);
		return this;
	}
	
	 

	
	public NettyWebServer add(String id, String path, HttpHandler handler) {
		// TODO Auto-generated method stub
		HttpHandler hd = new PathMatchHandler(path, handler);
    	logger.info("Adding new path handler: " + id + "(" + path + ")");
		return add(id, hd);
	}


	public NettyWebServer add(String id, String path, WebSocketHandler handler) {
		HttpHandler hd = new HttpToWebSocketHandler(handler);
		return add(id, path, hd);
	}

	public WebServer add(String id, String path, EventSourceHandler handler) {
		HttpHandler hd = new HttpToEventSourceHandler(handler);
		return add(id, path, hd);
	}

    

}

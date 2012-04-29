/**
 * @author jmartinez
 */
package org.mpt.j2f.ws.server.handler;

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;


import org.webbitserver.HttpControl;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;
import org.webbitserver.handler.AbstractResourceHandler;

import org.mpt.j2f.ws.server.service.IWebSocketApp;
import org.mpt.j2f.ws.server.service.impl.WebSocketService;


public class ClassPathHandler extends AbstractResourceHandler {

	private static final String DEFAULT_WS_JS_PATH = "/_mpt_/mpt-ws.js";
	private static final String DEFAULT_WS_JS_PACK = "/org/mpt/j2f/ws/server/res/js/mpt-ws.js";
	
	
	private IWebSocketApp wsApp_ = null;

    
	
	public ClassPathHandler(IWebSocketApp app,  Executor ioThread) {
        super(ioThread);
        wsApp_ = app;
    }

   
    public ClassPathHandler(IWebSocketApp app) {
        this(app, newFixedThreadPool(4));
    }

   
	@Override
	protected IOWorker createIOWorker(HttpRequest request, HttpResponse response, HttpControl control) {
		return new ClassPathHandler.ClassPathWorker(request.uri(), request, response, control);
	}
	
	public static String getLocalhost() {
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			return InetAddress.getByName(hostname).getHostAddress() ;
		} catch (UnknownHostException e) {
			return "unknown";
		}
	}
	
	protected class ClassPathWorker extends IOWorker {
        private String pack;

        private ClassPathWorker(String path, HttpRequest request, HttpResponse response, HttpControl control) {
            super(path, request, response, control);
        }

        @Override
        protected boolean exists() throws IOException {
        	
        	String baseURL = wsApp_.getHttpURL();
        	if(wsApp_.getHttpURL() == null || wsApp_.getHttpURL().length() == 0) {
        		baseURL = "/";
        	}
        	if(!path.equals(baseURL+DEFAULT_WS_JS_PATH)) {
        		pack = resolveURL(path);
        	} else {
        		pack = DEFAULT_WS_JS_PACK;
        	}
            
            return (pack != null);
        }

        @Override
        protected ByteBuffer fileBytes() throws IOException {
        	InputStream is = null;
        	if(wsApp_ != null) {
        		if(pack.equals(DEFAULT_WS_JS_PACK)) {
        			String temp = readDefaultWSJS();
        			temp = temp.replace("<$mptWS.global.url$>", "mptWS.global.url='ws://"+getLocalhost()+":"+WebSocketService.getPort()+wsApp_.getWebSocketURL()+"';");
        			is = new ByteArrayInputStream(temp.getBytes());
        		} else {
        			is = wsApp_.getClass().getResourceAsStream(pack);
        		} 
        	} else {
    			is = getClass().getResourceAsStream(pack);
    		}
            return ByteBuffer.wrap(read(is));
        }
        
        

        @Override
        protected ByteBuffer welcomeBytes() throws IOException {
        	return null;
//            File welcome = new File(file, welcomeFileName);
//            return welcome.isFile() ? read(welcome) : null;
        }

        private byte[] read(InputStream is) throws IOException {
        	
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	int c;
            while ((c = is.read()) != -1) 
            {
               bos.write(c);
            }

            is.close();
            bos.close();
        	return bos.toByteArray();
        }
        
        private String readDefaultWSJS() throws IOException {
        	InputStream is = getClass().getResourceAsStream(pack);
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	int c;
            while ((c = is.read()) != -1) 
            {
               bos.write(c);
            }

            is.close();
            bos.close();
        	return bos.toString();
        }


        private String resolveURL(String path) throws IOException {
        	String baseURL = wsApp_.getHttpURL();
        	if(wsApp_.getHttpURL() == null || wsApp_.getHttpURL().length() == 0) {
        		baseURL = "/";
        	}
        	String resource = path.replaceFirst(baseURL, wsApp_.getResourcePackage());
//        	String resource = wsApp_.getResourcePackage()+path;
        	InputStream is = null;
        	if(wsApp_ != null) {
        		is = wsApp_.getClass().getResourceAsStream(resource);
        	} else {
        		is = getClass().getResourceAsStream(resource);
        	}
        	
        	if(is == null) {
        		return null;
        	} else {
        		return resource;
        	}
            
        }
    }

}

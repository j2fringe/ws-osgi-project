/***************************************************************************************************
 * MPTWS.NAMESPACE  --  contains: global namespace object and related functions
 **************************************************************************************************/

/**
 * Create global namespace object
 */
if (typeof mptWS == "undefined") {
    var mptWS = {};
}

/**
 * Returns the namespace specified and creates it if it doesn't exist. For example both,
 * 'mptWS.namespace("property.package");' and 'mptWS.namespace("mptWS.property.package");'
 * would create mptWS.property, then mptWS.property.package --> (@see YAHOO.namespace()).
 * @param {String} arguments - 1 to n namespaces to create (separated with '.')
 * @return {Object} reference to the last namespace object created
 */
mptWS.namespace = function() {
    var a=arguments, o=null, i, j, d;
    for (i=0; i<a.length; i=i+1) {
        d=a[i].split(".");
        o=mptWS;
        
        // mptWS is implied, so it is ignored if it is included
        for (j=(d[0] == "mptWS") ? 1 : 0; j<d.length; j=j+1) {
            o[d[j]]=o[d[j]] || {};
            o=o[d[j]];
        }
    }
    return o;
};

/***************************************************************************************************
 * mptWS.global  --  contains: global variables and functions, function extensions, etc.
 **************************************************************************************************/

// Create module namespace
mptWS.namespace("global");



//mptWS.global.url = 'ws://192.168.1.84:9002/ws-config';
<$mptWS.global.url$>
mptWS.global.socket;

mptWS.global.openWebSocket = function ( ) { 
	//alert("Open WebSocket: "+mptWS.global.url);
   if (window.WebSocket) {
   		mptWS.global.socket = new WebSocket(mptWS.global.url);
       //socket = new WebSocket('ws://192.168.1.84:9002/ws-config');
       //socket = new WebSocket('ws://192.168.0.198:8992/websocket');
       mptWS.global.socket.onerror  = function(event) { mptWS.global.error(event); };
       mptWS.global.socket.onopen    = function(event) { mptWS.global.toggle(true);  };
       mptWS.global.socket.onclose   = function(event) { mptWS.global.toggle(false); };
       mptWS.global.socket.onmessage = function(event) { mptWS.global.async(event.data); };
   } else {
       alert('Your browser does not support WebSockets yet.');
   }
}

mptWS.global.closeWebSocket = function () { 
  	mptWS.global.socket.close(); 
} 

function $(id) { 
  return document.getElementById(id);
} 

mptWS.global.send = function (message) {
//	alert("Sending message: " + message);
  if (!window.WebSocket) { return; }
  if (mptWS.global.socket.readyState == WebSocket.OPEN) {
//  		alert("Sending message now");
      mptWS.global.socket.send(message);
  } else {
      alert('The WebSocket is not open!');
  }
}

mptWS.global.async = function ( response ) {

//	alert("response: " + response);
   var data = response;
   var arr = data.split("|");
   var msg  = arr[0];
   
   
   
   
   
    if ( msg == 'printForm' ) {
    	var data = document.getElementById('CHK_data');
    	data.innerHTML = arr[1];

    } else if ( msg == 'readCheck' ) {
    	var data = document.getElementById('CHK_data')
    	data.innerHTML = arr[1];
    	var image = document.getElementById('CHK_image');
    	image.innerHTML="<a href='"+arr[2]+"' rel='lightbox'><img id='scanned_image' src='data:image/jpeg;base64," + arr[2]+"'/></a>";
    	
      
    } else if (msg == "demoStatus") {
    	var statusmesg = document.getElementById('status_text')
    	statusmesg.innerHTML = arr[2];
    	var progress = document.getElementById('status_progress');
    	if(arr[1] != -1) {
    		progress.value=arr[1];
    	}
    	progress.max='3';
    }

}

//mptWS.global.toggle = function(flag){};

// mptWS.global.toggle = function (flag) {
  //$('on').disabled=flag; 
  //$('off').disabled=!flag;
  //$('snd').disabled=!flag;
  //$('gc').disabled=!flag;
//  if ( flag ) {
//    alert('WebSocket is opened!');
//  } else {
//    alert('WebSocket is closed!');
//  }
//}
//function clr() {
//   $('out').innerHTML ='';
//   $('sys').innerHTML ='';
//   $('cpu').innerHTML ='';
//   $('vmem').innerHTML ='';
//}

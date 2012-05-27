/***************************************************************************************************
 * JF.NAMESPACE  
 **************************************************************************************************/
/**
 * Create global namespace object
 */
if (typeof jf === "undefined") {
    var jf = {};
}

/***************************************************************************************************
 * jf Constants (Types)
 **************************************************************************************************/

 	const TYPE_NULL = 1000;
	const TYPE_INTEGER = 1001;
	const TYPE_LONG = 1002;
	const TYPE_FLOAT = 1003;
	const TYPE_DOUBLE = 1004;
	const TYPE_CHARACTER = 1005;
	const TYPE_BOOLEAN = 1006;
	const TYPE_BASE64 = 1007;
	const TYPE_BYTE = 1008;
	const TYPE_STRING = 1009;
	const TYPE_DATE = 1010;
	
	const TYPE_INTEGERARRAY = 2000;
	const TYPE_LONGARRAY = 2001;
	const TYPE_FLOATARRAY = 2002;
	const TYPE_DOUBLEARRAY = 2003;
	const TYPE_CHARACTERARRAY = 2004;
	const TYPE_BOOLEANARRAY = 2005;
	const TYPE_STRINGARRAY = 2006;

	
	const TYPE_VECTOR = 3000;
	const TYPE_LIST = 3001;
	const TYPE_TABLE = 3002;
	const TYPE_MAP = 3004;
	
	const TYPE_OBJECT = 4000;
	const TYPE_OBJECTARRAY = 5000;


/***************************************************************************************************
 * mptWS.global  --  contains: global variables and functions, function extensions, etc.
 **************************************************************************************************/
/**
 * Constructs a WS object
 * @constructor
 * @return {WS} A new WS instance
 */
 jf.WS = function() {
 	//mptWS.global.url = 'ws://192.168.1.84:9002/ws-config';
	<$wsurl$>
 	
 	this.ws = $.gracefulWebSocket(this.ws_url);
 	
 	this.init = function() {
 		
 		// this.ws.onopen = function(event) { ws_open(event); };
 		// this.ws.onclose = function(event) { ws_close(event); };
 		// this.ws.onerror = function(event) { ws_error(event); };
 		// this.ws.onmessage = function(event) { ws_onmessage(event); };
 		
 	}
 	
 	this.setOnOpen = function(method) {
 		this.ws.onopen = function(event) { method(event); };
 	}
 	
 	this.setOnClose = function(method) {
 		this.ws.onclose = function(event) { method(event); };
 	}
 	
 	this.setOnMessage = function(method) {
 		this.ws.onmessage = function(event) { method(event.data); };
 	}
 	
 	this.setOnError = function(method) {
 		this.ws.onerror = function(event) { method(event); };
 	}
 	
 	
 	
 	this.send = function(data) {
 		this.ws.send(data);
 	}
 	
 };
 
 /**
  * Arrays
  **
		var arr = [];
		var len = oFullResponse.results.length;
		for (var i = 0; i < len; i++) {
		    var obj = {
		        key: oFullResponse.results[i].label,
		        sortable: true,
		        resizeable: true
		    };
		    arr.push(obj);
		} 
 */
 jf.Query = function ( serviceid, method, o ) {
 	this.jsonQuery = new Object();
 
 	this.jsonQuery.sid = serviceid;
 	this.jsonQuery.meth = method;
 	
 	
 	
 	
 	
 	
 	this.getJFQuery = function() {
 		
 	
 	}
 
 
 
 };
 

 
 jf.Param = function ( o, name ) {
 
 	
 
 	this.jsonParam = new Object();
 	
 
 	console.log("TYPE: " + type);
 
 	if( name === null ) {
 		this.jsonParam.name = "";
 	} else {
 		this.jsonParam.name = name;
 	}
 	
 	
 	
 	if ( o === null ) {
 			this.jsonParam.type = TYPE_NULL;
	} else {

		var type = typeof o;
	
		if ( type === 'undefined' ) {
	 			this.jsonParam.type = TYPE_NULL;
		}
		if ( type === 'number' ) {
	 			this.jsonParam.type = TYPE_INTEGER;
		}
		if ( type === 'boolean' ) {
	 			this.jsonParam.type = TYPE_BOOLEAN;
		}
		
		if ( type === 'string') {
	 			this.jsonParam.type = TYPE_STRING;
		}
		if ( type === 'object' ) {
			console.log("OBJECT: " + o);
			for(var property in o) {
				if (typeof o[property] == 'function') {
 				   console.log("FUNCTION: " + property);
 				} else {
 				   console.log("ATTR: " + property + " VALUE: " + o[property]);
 				}
			}
			if ( typeof o.toJSON === 'function' ) {
				console.log("FUNCTION: " + $.toJSON( o.toJSON() ));
				return $.toJSON( o.toJSON() );
			}
			if ( o.constructor === Date ) {
				var	month = o.getUTCMonth() + 1,
					day = o.getUTCDate(),
					year = o.getUTCFullYear(),
					hours = o.getUTCHours(),
					minutes = o.getUTCMinutes(),
					seconds = o.getUTCSeconds(),
					milli = o.getUTCMilliseconds();
	
				if ( month < 10 ) {
					month = '0' + month;
				}
				if ( day < 10 ) {
					day = '0' + day;
				}
				if ( hours < 10 ) {
					hours = '0' + hours;
				}
				if ( minutes < 10 ) {
					minutes = '0' + minutes;
				}
				if ( seconds < 10 ) {
					seconds = '0' + seconds;
				}
				if ( milli < 100 ) {
					milli = '0' + milli;
				}
				if ( milli < 10 ) {
					milli = '0' + milli;
				}
				return '"' + year + '-' + month + '-' + day + 'T' +
					hours +  '-' + minutes +  '-' + seconds +
					'.' + milli + 'Z"';
			}
			if ( o.constructor === Array ) {
				var ret = [];
				for ( var i = 0; i < o.length; i++ ) {
					ret.push( $.toJSON( o[i] ) || 'null' );
				}
				return '[' + ret.join(',') + ']';
			}
			var	name,
				val,
				pairs = [];
			for ( var k in o ) {
				type = typeof k;
				if ( type === 'number' ) {
					name = '"' + k + '"';
				} else if (type === 'string') {
					name = $.quoteString(k);
				} else {
					// Keys must be numerical or string. Skip others
					continue;
				}
				type = typeof o[k];
	
				if ( type === 'function' || type === 'undefined' ) {
					// Invalid values like these return undefined
					// from toJSON, however those object members
					// shouldn't be included in the JSON string at all.
					continue;
				}
				val = $.toJSON( o[k] );
				pairs.push( name + ':' + val );
			}
			console.log('{' + pairs.join( ',' ) + '}');
			//return '{' + pairs.join( ',' ) + '}';
		}
	}
 	
 	this.jsonParam.value = o;
 	
 	
 	
 	this.getJFParam = function() {
 		var res;
 		console.log("JSONOBJ: " + this.jsonParam);
 		console.log("JFPARAM: " + $.toJSON(this.jsonParam));
 		res = $.toJSON(this.jsonParam);
 		
 		return res;
 	
 	}
 	
 	 
 
 };
 





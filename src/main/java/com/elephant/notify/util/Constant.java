package com.elephant.notify.util;

import com.jfinal.kit.PropKit;

/**
 * 
 * @author clark
 * 
 * Oct 29, 2016
 */
public interface Constant {
	
	public static final String NODE_HTTP_REST_BEST_HEIGHT = PropKit.get("node_http_rest_best_height");
	
	public static final String NODE_HTTP_REST_BLOCK_INFO = PropKit.get("node_http_rest_block_info");
	
	public static final String DEFAULT_MESSAGE = "恭喜发财，大吉大利";

	public static final String DEFAULT_MESSAGE_ENG = "Good luck , Good fortune";
	
	public static final double  FEE_PER_TX = Double.valueOf(PropKit.get("recomend_gas_per_tx"));
	
	public static final double SUSHI = 100000000D;
	
	public static final String REDIS_RED_PACKET_SEPARATOR = "_";
	
	public static final String NODE_HTTP_REST_ADDR_UTXO = PropKit.get("node_http_rest_addr_utxo");
	
	public static final String NODE_HTTP_REST_SENDRAWTX = PropKit.get("node_http_rest_sendRawTx");
	
	public static final String NODE_HTTP_REST_GETTRANSACTIONBYHASH = PropKit.get("node_http_rest_getTransactionByHash");
	
	public static final String SESSION_ID_NAME_WECHAT = "redPacket_wechat";
	
//	public static final String SESSION_ID_NAME_TELEGRAM = "redPacket_telegram";
	
//	public static final String API_REQ_BODY_NULL = "request boy can not be blank";
//
//	public static final String API_REQ_BODY_NOT_VALID_JSON = "request boy is not a valid json";
//
//	public static final String API_REQ_ACTION_NOT_MATCH = "request url is not match actionName or actionName is blank";
//
//	public static final String API_INTERNAL_ERROR = "api internal error";
	
//	public static final String SMS_ACCOUNT_SID = PropKit.get("SMS_ACCOUNT_SID");
	
//	public static final String SMS_AUTH_TOKEN = PropKit.get("SMS_AUTH_TOKEN");
	
//	public static final String SMS_FROM = PropKit.get("SMS_FROM");
	
//	public static final String SMS_NUM = PropKit.get("SMS_NUM");
	
//	public static final String APP_ID = PropKit.get("appId");
	
//	public static final String APP_SECRET = PropKit.get("secret");
	
//	public static final Integer BATCH_GENERATE_KEY_SIZE = Integer.valueOf(PropKit.get("batch_generate_key_size"));
	
//	public static final Double USER_FEE = Double.valueOf(PropKit.get("user_fee"));
	
	public static final double CONG = 0.00000001d;
	
//	public static final String PLATFORM_PUB_KEY = PropKit.get("platform_pub_key");
	
//	public static final String PLATFORM_PRIV_KEY = PropKit.get("platform_priv_key");
	
	public static final String SUCC = "SUCCESS";

	public static final String ELAPHANT_RETURN = "{\"desc\":\"pong\"}";
}

package com.elephant.notify.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elephant.notify.entity.base.TransferParam;
import com.elephant.notify.util.Constant;
import com.elephant.notify.util.JsonUtil;
import org.elastos.api.SingleSignTransaction;
import org.elastos.entity.RawTxEntity;
import org.elastos.entity.ReturnMsgEntity;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;

import net.sf.json.JSONObject;

/**
 * 
 * @author clark
 * 
 * Apr 21, 2018 12:45:54 PM
 */
public class NodeService {
	
	private SingleSignTransaction ela = new SingleSignTransaction();
	
	@SuppressWarnings("unchecked")
	public Integer getBestHeight() {

		String result = HttpKit.get(Constant.NODE_HTTP_REST_BEST_HEIGHT);

		Map<String,Object> resMap = ((Map<String,Object>)JSON.parse(result));
		
		int height = Integer.valueOf(""+resMap.get("Result"));
		
		return height;
	}
	
	public String getBlockInfo(int height){
		
		String result = HttpKit.get(Constant.NODE_HTTP_REST_BLOCK_INFO + "/" + height);
		
		return result;
	}
	
	public String getUtxoByAddr(String addr) {
		
		String result = HttpKit.get(Constant.NODE_HTTP_REST_ADDR_UTXO + "/" + addr);
		
		return result;
	}
	
	public String getTxByHash(String hash) {
		
		String result = HttpKit.get(Constant.NODE_HTTP_REST_GETTRANSACTIONBYHASH+"/"+hash);
		
		return result;
	}
	
	/**
	 * send a transaction to blockchain.
	 * @param smAmt
	 * @param privateKey
	 * @param addr
	 * @param addrs
	 * @param amts
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String gen(double smAmt , String privateKey , String addr ,List<String> addrs , List<Double> amts , String data) throws Exception {
		
		String utxoStr = getUtxoByAddr(addr);
		
		List<Map> utxo = stripUtxo(utxoStr);
		
		String response = genTx(smAmt, utxo, privateKey, addr, addrs, amts, data);
		
		return response;
	}
	
	/**
	 * generate raw transaction.
	 * @param smAmt the total spend money
	 * @param utxolm utxo 
	 * @param privateKey sender private key 
	 * @param addr sender public address
	 * @param addrs receiver addresses
	 * @param amts receiver output money
	 * @param data memo data 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public String genTx(double smAmt , List<Map> utxolm , String privateKey , String addr ,List<String> addrs , List<Double> amts , String data) throws Exception {
		
		if(addrs == null || addrs.size() == 0) {
			throw new RuntimeException("output can not be blank");
		}
		
		Map<String,Object> paraListMap = new HashMap<>();
		List txList = new ArrayList<>();
		paraListMap.put("Transactions", txList);
		Map<String,Object> txListMap = new HashMap<>();
		txList.add(txListMap);
		if(!StrKit.isBlank(data)) {
			txListMap.put("Memo", data);
		}
		
		int index = -1;
		double spendMoney = 0.0;
		for( int i=0; i<utxolm.size(); i++) {
			index = i;
			spendMoney += Double.valueOf(utxolm.get(i).get("Value")+"");
			if( spendMoney >= new BigDecimal(smAmt).add(new BigDecimal(Constant.FEE_PER_TX)).doubleValue()) {
				break;
			}
		}
		
		if(spendMoney < smAmt) {
			return null;
		}
		
		List utxoInputsArray = new ArrayList<>();
		txListMap.put("UTXOInputs", utxoInputsArray);
		for(int i=0;i<=index;i++) {
			Map<String,Object> utxoInputsDetail = new HashMap<>();
			Map<String,Object> utxoM = utxolm.get(i);
			utxoInputsDetail.put("txid",  utxoM.get("Txid"));
			utxoInputsDetail.put("index",  utxoM.get("Index"));
			utxoInputsDetail.put("privateKey",  privateKey);
			utxoInputsDetail.put("address",  addr);
			utxoInputsArray.add(utxoInputsDetail);
		}
		List utxoOutputsArray = new ArrayList<>();
		txListMap.put("Outputs", utxoOutputsArray);
		for(int i=0;i<addrs.size();i++) {
			Map<String,Object> utxoOutputsDetail = new HashMap<>();
			utxoOutputsDetail.put("address", addrs.get(i));
			utxoOutputsDetail.put("amount", new BigDecimal(amts.get(i)+"").multiply(new BigDecimal(Constant.SUSHI+"")).longValue());
			utxoOutputsArray.add(utxoOutputsDetail);
		}
		long leftMoney = Math.round(new BigDecimal(spendMoney).subtract(new BigDecimal(Constant.FEE_PER_TX).add(new BigDecimal(smAmt))).multiply(new BigDecimal(Constant.SUSHI)).doubleValue());
		if(leftMoney > 0) {
			Map<String,Object> utxoOutputsDetail = new HashMap<>();
			utxoOutputsDetail.put("address", addr);
			utxoOutputsDetail.put("amount",leftMoney);
			utxoOutputsArray.add(utxoOutputsDetail);
		}
		JSONObject par = new JSONObject();
		par.accumulateAll(paraListMap);
		System.out.println("sending : " + par);
		String result = ela.genRawTransaction(par);
		System.out.println("receiving : " + result);
		return result;
	}
	
	@SuppressWarnings("static-access")
	public boolean sendTx(String rawData) {
		RawTxEntity rawTxEntity = new RawTxEntity();
		rawTxEntity.setData(rawData);
		String rawTx = JSON.toJSONString(rawTxEntity);
		ReturnMsgEntity.ELAReturnMsg msg = org.elastos.util.JsonUtil.jsonStr2Entity(org.elastos.util.HttpKit.post(Constant.NODE_HTTP_REST_SENDRAWTX,rawTx),ReturnMsgEntity.ELAReturnMsg.class);
		Object result = msg.getResult();
		String desc = msg.getDesc();
		Long error = msg.getError();
		System.out.println("SendRawTransaction result : " + result +" desc :" + desc);
//		Map<String,Object> rm = JsonUtil.jsonToMap(JSONObject.fromObject(result));
		if(error == 0) {
			return true;
		}
		throw new RuntimeException(" build transaction error : " + result);
	}
	
	/**
	 * @param result
	 * @return
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	public List<Map> stripUtxo(String result) {
		
		Map m = JsonUtil.jsonToMap(JSONObject.fromObject(result));
		List<Map> lm = null;
		try {
			lm = ((List<Map>)m.get("Result"));
		}catch(Exception ex) {
			throw new RuntimeException(" address has no utxo yet .");
		}
		List<Map> l = null;
		if(lm != null) {
			l = (List<Map>) lm.get(0).get("Utxo");
		}
		return l;
	}
	
	@SuppressWarnings("unchecked")
	public String transfer(TransferParam param) throws Exception {
		
		String senderPrivateKey = param.getSenderPrivateKey();
		String senderAddr = param.getSenderAddress();
		String memo = param.getMemo();
		
		List<String> addrList = param.getAddrList();
		List<Double> valList = param.getAmtList();
		
		String response = gen(param.getTotalAmt(), senderPrivateKey , senderAddr,
				addrList, valList, memo);
		Map<String,Object> rawM = (Map<String, Object>) ((Map<String, Object>) JSON.parse(response)).get("Result");
		String rawTx = (String) rawM.get("rawTx:");
		String txHash = (String) rawM.get("txHash:");
		System.out.println("rawTx:" + rawTx + ", txHash :" + txHash);
		
		sendTx(rawTx);
		
		return txHash.toLowerCase();
	}
}

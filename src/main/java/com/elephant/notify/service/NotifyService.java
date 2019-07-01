/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.service;

import com.alibaba.fastjson.JSON;
import com.elephant.notify.entity.NotifyUser;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfplugin.mail.MailKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * clark
 * <p>
 * 4/10/19
 */
public class NotifyService {
    private Logger logger = LoggerFactory.getLogger(NotifyService.class);
    private final String memo_format = "type:text,msg:";
    public void handleMsg(String msg) {
        Map<String,Object> m = (Map) JSON.parse(msg);
        String action = (String)m.get("Action");
        if("sendrawblock".equals(action)){
            logger.info("Blcok : Receiving a block");
            Map<String,Object> result = (Map<String, Object>) m.get("Result");
            List<Map<String,Object>> txs = (List<Map<String, Object>>) result.get("tx");
            for (int i=0;i<txs.size();i++){
                Map<String,Object> tx = txs.get(i);
                Integer type = (Integer)tx.get("type");
                if(type == 0){
                    continue;
                }
                List<String> inputAddresses = new ArrayList<>();
                List<Map<String,Object>> outputs = (List<Map<String, Object>>) tx.get("vout");
                List<Map<String,Object>> vin = (List<Map<String, Object>>) tx.get("vin");
                for(int j=0;j<vin.size();j++){
                    Map<String,Object> in = vin.get(j);
                    String txid = (String)in.get("txid");
                    Integer index = (Integer)in.get("vout");
                    String url = PropKit.get("node_http_rest_tx")+txid;
                    String data = HttpKit.get(url);
                    Map<String,Object> _m = (Map) JSON.parse(data);
                    Map<String,Object> _mm = (Map)_m.get("Result");
                    List<Map<String,Object>>  _mmm = (List)_mm.get("vout");
                    String inputAddress = (String)_mmm.get(index).get("address");
                    inputAddresses.add(inputAddress);
                }
                List<Map<String,Object>> memos = (List<Map<String, Object>>) tx.get("attributes");
                String memo = null;
                if(memos.size() > 0){
                    String data = (String)memos.get(0).get("data");
                    String rawData = new String(DatatypeConverter.parseHexBinary(data));
                    if(rawData.indexOf("type:text,msg:") != -1){
                        memo = rawData.substring(memo_format.length());
                    }
                }
                for (int j=0;j<outputs.size();j++){
                    Map<String,Object> output = outputs.get(j);
                    String address = (String)output.get("address");
                    if(inputAddresses.contains(address)){
                        continue;
                    }
                    Object val = output.get("value");
                    NotifyUser user = NotifyUser.dao.findFirst("select * from notify_user where ela_address = '" + address +"' and status = 2 order by id desc limit 1");
                    if(user != null){
                        String email = user.getEmail();
                        for(int z=0;z<3;z++){
                            try{
                                if(memo != null){
                                    MailKit.send((String)email ,null, "Elephant Wallet Notification Service", "You have received "+val+ " ELA, memo:"+memo);
                                }else{
                                    MailKit.send((String)email ,null, "Elephant Wallet Notification Service", "You have received "+val+" ELA");
                                }
                                break;
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

}

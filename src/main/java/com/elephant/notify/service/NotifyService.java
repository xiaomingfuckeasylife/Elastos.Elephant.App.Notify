/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.service;

import com.alibaba.fastjson.JSON;
import com.elephant.notify.entity.NotifyUser;
import com.jfplugin.mail.MailKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * clark
 * <p>
 * 4/10/19
 */
public class NotifyService {
    private Logger logger = LoggerFactory.getLogger(NotifyService.class);
    public void handleMsg(String msg) {
        Map<String,Object> m = (Map) JSON.parse(msg);
        String action = (String)m.get("Action");
        if("sendrawblock".equals(action)){
            logger.info("Blcok :" + msg);
            Map<String,Object> result = (Map<String, Object>) m.get("Result");
            List<Map<String,Object>> txs = (List<Map<String, Object>>) result.get("tx");
            for (int i=0;i<txs.size();i++){
                Map<String,Object> tx = txs.get(i);
                List<Map<String,Object>> outputs = (List<Map<String, Object>>) tx.get("vout");
                for (int j=0;j<outputs.size();j++){
                    Map<String,Object> output = outputs.get(j);
                    String address = (String)output.get("address");
                    Object val = output.get("value");
                    NotifyUser user = NotifyUser.dao.findFirst("select * from notify_user where ela_address = '" + address +"' and status = 1 order by id desc limit 1");
                    if(user != null){
                        String email = user.getEmail();
                        try{
                            MailKit.send((String)email ,null, "Elephant Wallet Notification Service", "You have received "+val+ " ELA");
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}

/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.task;

import com.alibaba.fastjson.JSON;
import com.elephant.notify.entity.NotifyUser;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;

import java.util.List;
import java.util.Map;

/**
 * clark
 * <p>
 * 7/1/19
 */
public class ConfirmTask implements Runnable{

    @Override
    public void run() {
        List<NotifyUser> users = NotifyUser.dao.find("select * from notify_user where status = 1");
        for(int i=0;i<users.size();i++){
            NotifyUser user = users.get(i);
            String address = user.getAddress();
            String url = PropKit.get("node_http_rest_balances")+address;
            String data = HttpKit.get(url);
            Map<String,Object> m = (Map)JSON.parse(data);
            Double value = Double.valueOf(m.get("Result")+"");
            Integer period = user.getPeriod();
            if(value != null && period != null && value >= period){
                user.setStatus(2);
                user.update();
            }
        }
    }
}

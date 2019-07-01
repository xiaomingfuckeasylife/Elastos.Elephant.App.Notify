/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.controller;

import com.elephant.notify.entity.NotifyUser;
import com.elephant.notify.util.Constant;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import io.github.novacrypto.hashing.Sha256;
import org.elastos.ela.Ela;

import javax.xml.bind.DatatypeConverter;
import java.net.URLEncoder;

/**
 * clark
 * <p>
 * 4/8/19
 */
public class Verify extends Controller {

    public void index() throws Exception {
        String uuid = getPara("uuid");
        String emailCode = getPara("email_code");
        Integer period = getParaToInt("period");
        //prevent sql injection
        NotifyUser user = NotifyUser.dao.findFirst("select * from notify_user where uuid = ?",uuid);

        if (user == null){
            user.setStatus(-1);
            user.update();
            setAttr("msg","Can not find User, Invalid request");
            render(Constant.BASE_URL+"/error.jsp");
            return;
        }

        if (emailCode != null && !emailCode.equals(user.getVerifyCode())){
            user.setStatus(-1);
            user.update();
            setAttr("msg","Invalid Email Code");
            render(Constant.BASE_URL+"/error.jsp");
            return;
        }

        if(period == null || (period != 1 && period != 3 && period != 12)){
            user.setStatus(-1);
            user.update();
            setAttr("msg","Paying Period Invalid");
            render(Constant.BASE_URL+"/error.jsp");
            return;
        }
        user.setStatus(1);
        user.setPeriod(period);


        String returnUrl = URLEncoder.encode(getAttr("ctx")+"/success","utf-8");
        String desc = "notification";
        String appId = PropKit.get("app_id");
        String publicKey = PropKit.get("did_pubkey");
        String signature = PropKit.get("did_signature");
        String did = PropKit.get("did");
        String randomNumber = DatatypeConverter.printHexBinary(Sha256.sha256((System.currentTimeMillis()+"").getBytes()));
        String appName = PropKit.get("app_name");
        String privKey = Ela.getPrivateKey();
        String address = Ela.getAddressFromPrivate(privKey);
        String schema = String.format(PropKit.get("elephant_pay"),returnUrl,desc,appId,randomNumber,publicKey,signature,address,did,appName,Double.valueOf(period+""));
        String periodStr = "";
        if(period < 12){
            periodStr = period + " month";
        }else{
            periodStr = "1 year";
        }
        setAttr("elephant", schema).setAttr("step",3).setAttr("periodStr",periodStr);
        user.setPrivKey(privKey);
        user.setAddress(address);
        user.update();
        render(Constant.BASE_URL+"/index.jsp");
    }

}

/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.controller;

import com.alibaba.fastjson.JSON;
import com.elephant.notify.entity.NotifyUser;
import com.jfinal.core.Controller;
import com.jfplugin.mail.MailKit;
import org.elastos.util.ela.ElaSignTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;

/**
 * clark
 * <p>
 * 4/9/19
 */
public class Load extends Controller {

    private Logger logger = LoggerFactory.getLogger(Load.class);

    public void index(){
        String response = getPara("response");
        Map<String,Object> param =(Map<String, Object>) JSON.parse(response);
        String sign = (String)param.get("Sign");
        String publicKey = (String)param.get("PublicKey");
        String data = (String)param.get("Data");

        if(!ElaSignTool.verify(data.getBytes(),DatatypeConverter.parseHexBinary(sign), DatatypeConverter.parseHexBinary(publicKey))){
            setAttr("msg","Data verification Error");
            render("/error.jsp");
            return;
        }

        Map<String,Object> detail =(Map<String, Object>) JSON.parse(data);
        String ELAAddress = (String)detail.get("ELAAddress");
        if (ELAAddress == null) {
            setAttr("msg","ELA Address can not be blank");
            render("/error.jsp");
            return;
        }
        Object email = detail.get("EMail");
        if (email == null){
            email = detail.get("Email");
        }

        if ( email == null ) {
            setAttr("msg","Go to Elephant Wallet , \"My profile -> Email \" , fill in your Email information");
            render("/error.jsp");
            return;
        }

        logger.info("Email = {}",email);
        String code = genCode();
        try{
            MailKit.send((String)email ,null, "Elephant Wallet Notification Service", "Verification Code is " + code);
        }catch (Exception ex){
            ex.printStackTrace();
            setAttr("msg","Illegal email address");
            render("/error.jsp");
            return;
        }
        NotifyUser notifyUser = new NotifyUser();
        notifyUser.setElaAddress(ELAAddress);
        notifyUser.setEmail((String)email);
        notifyUser.setStatus(0);
        notifyUser.setVerifyCode(code);
        notifyUser.setUuid(UUID.randomUUID().toString());
        notifyUser.save();
        setAttr("step" , 2);
        setAttr("uuid",notifyUser.getUuid());
        setAttr("action","verify");
        render("/index.jsp");
    }

    private String genCode(){
        SecureRandom sr = new SecureRandom();
        String ret = "";
        for (int i=0;i<4;i++){
            ret += sr.nextInt(10);
        }
        return ret;
    }
}

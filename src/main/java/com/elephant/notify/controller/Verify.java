/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.controller;

import com.elephant.notify.entity.NotifyUser;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import io.github.novacrypto.hashing.Sha256;

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
        //prevent sql injection
        NotifyUser user = NotifyUser.dao.findFirst("select * from notify_user where uuid = '" + uuid +"'");

        if (user == null){
            user.setStatus(2);
            user.update();
            setAttr("msg","Can not find User, Invalid request");
            render("/error.jsp");
            return;
        }

        if (emailCode != null && !emailCode.equals(user.getVerifyCode())){
            user.setStatus(2);
            user.update();
            setAttr("msg","Invalid Email Code");
            render("/error.jsp");
            return;
        }
        user.setStatus(1);
        user.update();
        render("/success.jsp");
    }

}

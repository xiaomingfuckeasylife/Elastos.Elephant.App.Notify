/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.controller;

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
public class Index extends Controller {

    public void index() throws Exception{
        String callbackUrl = URLEncoder.encode(getAttr("ctx")+"/subscribe","utf-8");
        String returnUrl = URLEncoder.encode(getAttr("ctx")+"/load","utf-8");
        String desc = "notification";
        String appId = PropKit.get("app_id");
        String publicKey = PropKit.get("did_pubkey");
        String signature = PropKit.get("did_signature");
        String did = PropKit.get("did");
        String randomNumber = DatatypeConverter.printHexBinary(Sha256.sha256((System.currentTimeMillis()+"").getBytes()));
        String appName = PropKit.get("app_name");
        String schema = String.format(PropKit.get("elephant_login_auth"),callbackUrl,returnUrl,desc,appId,publicKey,signature,did,randomNumber,appName);
        setAttr("elephant_auth", schema).setAttr("step",1);
        render("/index.jsp");
    }

}

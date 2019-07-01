/**
 * Copyright (c) 2017-2019 The Elastos Developers
 * <p>
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package com.elephant.notify.controller;

import com.elephant.notify.util.Constant;
import com.jfinal.core.Controller;

/**
 * clark
 * <p>
 * 7/1/19
 */
public class Success extends Controller {

    public void index(){
        render(Constant.BASE_URL+"/success.jsp");
    }
}

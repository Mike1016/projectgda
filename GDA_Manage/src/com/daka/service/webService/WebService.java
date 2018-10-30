/**
 * Project Name:Bus
 * File Name:WebService.java
 * Package Name:com.bus.service.webService
 * Date:2016��6��5������3:43:20
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 */

package com.daka.service.webService;

import javax.jws.WebMethod;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Endpoint;

/**
 * @author xuemin
 * @WebService �� ����һ��ע�⣬��������ָ�������෢����һ��ws. Endpoint �C ����Ϊ�˵�����࣬��ķ���publish���ڽ�һ���Ѿ������@WebServiceע�����󶨵�һ����ַ�Ķ˿��ϡ�
 */

@javax.jws.WebService
public class WebService {
    public static HttpServletRequest request;

    public String HelloWord(String name) {
        return "Hello: " + name;
    }

    /**
     * ���exclude=true��HelloWord2()�������ᱻ����
     *
     * @param name
     * @return
     */
    @WebMethod(exclude = true)
    public String HelloWord2(String name) {
        return "Hello: " + name;
    }

    public static void main(String[] args) {
        /**
         * ����1������ķ�����ַ ����2�������ʵ����
         */
        Endpoint.publish("http://192.168.0.110:456/helloWord", new WebService());

    }

}

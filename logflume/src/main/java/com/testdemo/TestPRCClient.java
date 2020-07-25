package com.testdemo;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.util.Properties;

/**
 * @author guobing
 * @version 1.0
 * @date 2019/9/10 下午1:25
 * @description
 */

public class TestPRCClient {
    public static void main(String[] args) throws EventDeliveryException {

        Properties props = new Properties();
        props.put("client.type", "default_loadbalance");
        props.put("hosts", "h1 h2 h3");
        String host1 = "spark:44444";
        String host2 = "spark:44444";
        props.put("hosts.h1", host1);
        props.put("hosts.h2", host2);
        props.put("host-selector", "random");
        props.put("backoff", "true");

        RpcClient rpcClient = RpcClientFactory.getInstance(props);
        for (int i = 0; i < 10; i++) {
            String log = "INFO com.baizhi.controller.UserController#userLogin 2019-09-10 11:42:40 success aap1 zhangsan 123456 西安市 108.92859,34.2583 758,2328,1743 [Mobile Safari Browser (mobile) 11.0 APPLE iOS 11 (iPhone)]";
            Event event = EventBuilder.withBody(log.getBytes());
            rpcClient.append(event);
        }

        rpcClient.close();

    }
}

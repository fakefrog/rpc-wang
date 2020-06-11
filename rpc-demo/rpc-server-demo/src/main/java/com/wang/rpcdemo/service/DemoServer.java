package com.wang.rpcdemo.service;

import com.wang.rpc.rpc.starter.rpckids.demo.ExpRequest;
import com.wang.rpc.rpc.starter.rpckids.server.RPCServer;

public class DemoServer {

	public static void main(String[] args) {
		RPCServer server = new RPCServer("localhost", 8888, 2, 16);
		server.register("fib", Integer.class, new FibRequestHandler()).register("exp", ExpRequest.class,
				new ExpRequestHandler());
		server.start();
	}

}

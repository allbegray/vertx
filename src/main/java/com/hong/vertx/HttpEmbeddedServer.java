package com.hong.vertx;

import java.awt.Event;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;

public class HttpEmbeddedServer {

	private static Logger logger = LoggerFactory.getLogger(HttpEmbeddedServer.class);

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();
		HttpServer httpServer = vertx.createHttpServer();
		Handler<HttpServerRequest> httpHandler = new Handler<HttpServerRequest>() {

			@Override
			public void handle(HttpServerRequest event) {
				HttpServerResponse httpResponse = event.response();
				httpResponse.end("Hello World!");

				logger.info("헬로우 월드!");
			}
		};
		
		vertx.setPeriodic(60 * 1000, new Handler<Long>() {

			@Override
			public void handle(Long event) {
				logger.info("60초마다 도는 이벤트!!");
			}
		});
		
		vertx.executeBlocking(new Handler<Future<String>>() {

			@Override
			public void handle(Future<String> event) {
				
				String result = "블락킹 된 서비스(예로 DB 에서 가져옴)에서 가져온 결과값";
				event.complete(result);
				
			}
		}, new Handler<AsyncResult<String>>() {

			@Override
			public void handle(AsyncResult<String> event) {
				
				event.result();
				
			}
		});
		
		vertx.deployVerticle("com.hong.vertx.MyVerticle");
		vertx.deployVerticle("com.hong.vertx.OtherVerticle");
		
		
		EventBus eventBus = vertx.eventBus();
		eventBus.consumer("com.hong.consumer", message -> {
			logger.info("받은 메시지 : " + message.body());
			message.reply("답장이다.");
		});
		eventBus.publish("com.hong.consumer", "publish 로 전송. 메시지 잘 가냐?");
		eventBus.send("com.hong.consumer", "send 로 전송. 메시지 잘 가냐?", event -> {
			if (event.succeeded()) {
				logger.info("답장 옴 올~ : " + event.result().body());
			}
		});
		

		httpServer.requestHandler(httpHandler).listen(8080);

	}

}

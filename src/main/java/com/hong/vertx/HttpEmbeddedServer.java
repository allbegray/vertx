package com.hong.vertx;

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
import io.vertx.ext.apex.Route;
import io.vertx.ext.apex.Router;
import io.vertx.ext.apex.handler.BodyHandler;

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
		
		Router router = Router.router(vertx);
		
		PersonJsonApiHandler personJsonApiHandler = new PersonJsonApiHandler();
		
		router.route().handler(BodyHandler.create());
		router.get("/person/list").handler(personJsonApiHandler::handleGetPersons);
		router.post("/person/add").handler(personJsonApiHandler::handleAddPerson);

		router.get("/").handler(routingContext -> {

			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/plain");

			response.end("Hello World from Vert.x-Web!");

		});

		httpServer.requestHandler(router::accept).listen(8080);

	}

}

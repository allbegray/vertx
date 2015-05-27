package com.hong.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;

public class MyVerticle extends AbstractVerticle {

	private static Logger logger = LoggerFactory.getLogger(MyVerticle.class);

	@Override
	public void start() throws Exception {
		logger.info("MyVerticle 이 시작 되었다.");
	}

	@Override
	public void stop() throws Exception {
		logger.info("MyVerticle 이 종료 되었다.");
	}

	// 아래는 비동기로 시작, 종료 하고 싶을 때

	@Override
	public void start(Future<Void> startFuture) throws Exception {

		// OtherVerticle 이 디플로이 된 이후에 MyVerticle 디플로이
		vertx.deployVerticle("com.hong.vertx.OtherVerticle", event -> {/*new Handler<AsyncResult<String>>() {

			@Override
			public void handle(AsyncResult<String> event) {*/

				if (event.succeeded()) {
					startFuture.complete();
				} else {
					startFuture.failed();
				}

			});
		/*});*/

	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {

		boolean condition = true;

		if (condition) {
			stopFuture.complete();
		} else {
			stopFuture.failed();
		}

	}

}

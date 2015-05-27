package com.hong.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;

public class OtherVerticle extends AbstractVerticle {
	
	private static Logger logger = LoggerFactory.getLogger(OtherVerticle.class);

	@Override
	public void start() throws Exception {
		logger.info("OtherVerticle 이 시작 되었다.");
	}

	@Override
	public void stop() throws Exception {
		logger.info("OtherVerticle 이 종료 되었다.");
	}

}

package jp.co.sony.csl.dcoes.apis.common.util.vertx;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ApisLoggerFormatterTest {

	public ApisLoggerFormatterTest() {
		super();
	}

	@Test public void nullProgramId(TestContext context) {
		VertxConfig.config.setJsonObject(null);
		LogRecord record = new LogRecord(Level.INFO, "test message");
		record.setLoggerName("testLogger");
		String result = new ApisLoggerFormatter().format(record);
		// Format: [[[programId]]] timestamp LEVEL loggerName : message
		context.assertTrue(result.startsWith("[[[]]]"), "Should start with [[[]]]");
		context.assertTrue(result.contains("INFO"), "Should contain INFO level");
		context.assertTrue(result.contains("testLogger"), "Should contain logger name");
		context.assertTrue(result.contains("test message"), "Should contain message");
	}

	@Test public void emptyProgramId(TestContext context) {
		VertxConfig.config.setJsonObject(new JsonObject("{\"programId\":\"\"}"));
		LogRecord record = new LogRecord(Level.INFO, "test message");
		record.setLoggerName("testLogger");
		String result = new ApisLoggerFormatter().format(record);
		// Format: [[[programId]]] timestamp LEVEL loggerName : message
		context.assertTrue(result.startsWith("[[[]]]"), "Should start with [[[]]]");
		context.assertTrue(result.contains("INFO"), "Should contain INFO level");
		context.assertTrue(result.contains("testLogger"), "Should contain logger name");
		context.assertTrue(result.contains("test message"), "Should contain message");
	}

	@Test public void normalProgramId(TestContext context) {
		VertxConfig.config.setJsonObject(new JsonObject("{\"programId\":\"apis-log\"}"));
		LogRecord record = new LogRecord(Level.INFO, "test message");
		record.setLoggerName("testLogger");
		String result = new ApisLoggerFormatter().format(record);
		// Format: [[[programId]]] timestamp LEVEL loggerName : message
		context.assertTrue(result.startsWith("[[[apis-log]]]"), "Should start with [[[apis-log]]]");
		context.assertTrue(result.contains("INFO"), "Should contain INFO level");
		context.assertTrue(result.contains("testLogger"), "Should contain logger name");
		context.assertTrue(result.contains("test message"), "Should contain message");
	}

}

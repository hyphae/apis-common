
import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jp.co.sony.csl.dcoes.apis.common.util.DateTimeUtil;
import jp.co.sony.csl.dcoes.apis.common.util.NumberUtil;
import jp.co.sony.csl.dcoes.apis.common.util.StringUtil;
import jp.co.sony.csl.dcoes.apis.common.util.vertx.JsonObjectUtil;


public class ApisCommonFuzzer {
	public static void fuzzerTestOneInput(FuzzedDataProvider data) {
		switch (data.consumeInt(0, 5)) {
			case 0:
				fuzzStringAndNumber(data);
				break;
			case 1:
				fuzzDateTime(data);
				break;
			case 2:
				fuzzJsonObject(data);
				break;
			case 3:
				fuzzJsonArray(data);
				break;
			case 4:
				fuzzJsonObjectUtil(data);
				break;
			default:
				fuzzStringAndNumber(data);
				break;
		}
	}

	private static void fuzzStringAndNumber(FuzzedDataProvider data) {
		String s = data.consumeString(4096);
		try {
			StringUtil.nullIfEmpty(s);
			StringUtil.fixFilePath(s);
			StringUtil.urlEncode(s);
			NumberUtil.toInteger(s);
			Float f = data.consumeBoolean() ? data.consumeRegularFloat() : null;
			NumberUtil.negativeValue(f);
		} catch (RuntimeException ignored) {
		}
	}

	private static void fuzzDateTime(FuzzedDataProvider data) {
		String s = data.consumeString(512);
		try {
			DateTimeUtil.toLocalDateTime(s);
			DateTimeUtil.toSystemDefaultZonedDateTime(s);
		} catch (RuntimeException ignored) {
		}
	}

	private static void fuzzJsonObject(FuzzedDataProvider data) {
		String raw = data.consumeString(8192);
		try {
			new JsonObject(raw).encode();
		} catch (RuntimeException ignored) {
		}
	}

	private static void fuzzJsonArray(FuzzedDataProvider data) {
		String raw = data.consumeString(8192);
		try {
			new JsonArray(raw).encode();
		} catch (RuntimeException ignored) {
		}
	}

	private static void fuzzJsonObjectUtil(FuzzedDataProvider data) {
		String raw = data.consumeString(8192);
		final JsonObject jsonobject;
		try {
			jsonobject = new JsonObject(raw);
		} catch (RuntimeException e) {
			return;
		}
		int depth = data.consumeInt(1, 8);
		String[] keys = new String[depth];
		for (int i = 0; i < depth; i++) {
			keys[i] = data.consumeString(64);
		}
		try {
			JsonObjectUtil.getValue(jsonobject, keys);
			JsonObjectUtil.getString(jsonobject, keys);
			JsonObjectUtil.getFloat(jsonobject, keys);
			JsonObjectUtil.getInteger(jsonobject, keys);
			JsonObjectUtil.getLong(jsonobject, keys);
			JsonObjectUtil.getBoolean(jsonobject, keys);
			JsonObjectUtil.getLocalDateTime(jsonobject, keys);
			JsonObjectUtil.getJsonArray(jsonobject, keys);
			JsonObjectUtil.getJsonObject(jsonobject, keys);
		} catch (RuntimeException ignored) {
		}
	}
}

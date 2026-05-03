// Copyright 2026 The APIS Common Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import jp.co.sony.csl.dcoes.apis.common.util.DateTimeUtil;
import jp.co.sony.csl.dcoes.apis.common.util.NumberUtil;
import jp.co.sony.csl.dcoes.apis.common.util.StringUtil;
import jp.co.sony.csl.dcoes.apis.common.util.vertx.JsonObjectUtil;

/**
 * Jazzer entry point for OSS-Fuzz / ClusterFuzzLite. Not on the Maven test classpath;
 * compiled only by {@code .clusterfuzzlite/build.sh} inside the fuzzing image.
 */
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
		final JsonObject jo;
		try {
			jo = new JsonObject(raw);
		} catch (RuntimeException e) {
			return;
		}
		int depth = data.consumeInt(1, 8);
		String[] keys = new String[depth];
		for (int i = 0; i < depth; i++) {
			keys[i] = data.consumeString(64);
		}
		try {
			JsonObjectUtil.getValue(jo, keys);
			JsonObjectUtil.getString(jo, keys);
			JsonObjectUtil.getFloat(jo, keys);
			JsonObjectUtil.getInteger(jo, keys);
			JsonObjectUtil.getLong(jo, keys);
			JsonObjectUtil.getBoolean(jo, keys);
			JsonObjectUtil.getLocalDateTime(jo, keys);
			JsonObjectUtil.getJsonArray(jo, keys);
			JsonObjectUtil.getJsonObject(jo, keys);
		} catch (RuntimeException ignored) {
		}
	}
}

package jp.co.sony.csl.dcoes.apis.common.util.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.sony.csl.dcoes.apis.common.util.DateTimeUtil;

public class JsonObjectUtil {
	private static final Logger log = LoggerFactory.getLogger(JsonObjectUtil.class);

	private JsonObjectUtil() { }

	public static void toJsonObject(String value, Handler<AsyncResult<JsonObject>> completionHandler) {
		JsonObject result;
		try {
			result = new JsonObject(value);
		} catch (Exception e) {
			if (log.isWarnEnabled()) log.warn(e);
			completionHandler.handle(Future.failedFuture(e));
			return;
		}
		completionHandler.handle(Future.succeededFuture(result));
	}

	public static void toJsonObject(Buffer value, Handler<AsyncResult<JsonObject>> completionHandler) {
		JsonObject result;
		try {
			result = new JsonObject(value);
		} catch (Exception e) {
			if (log.isWarnEnabled()) log.warn(e);
			completionHandler.handle(Future.failedFuture(e));
			return;
		}
		completionHandler.handle(Future.succeededFuture(result));
	}

	public static void toJsonArray(String value, Handler<AsyncResult<JsonArray>> completionHandler) {
		JsonArray result;
		try {
			result = new JsonArray(value);
		} catch (Exception e) {
			if (log.isWarnEnabled()) log.warn(e);
			completionHandler.handle(Future.failedFuture(e));
			return;
		}
		completionHandler.handle(Future.succeededFuture(result));
	}

	public static void toJsonArray(Buffer value, Handler<AsyncResult<JsonArray>> completionHandler) {
		JsonArray result;
		try {
			result = new JsonArray(value);
		} catch (Exception e) {
			if (log.isWarnEnabled()) log.warn(e);
			completionHandler.handle(Future.failedFuture(e));
			return;
		}
		completionHandler.handle(Future.succeededFuture(result));
	}

	public static class DefaultString {
		public final String value;
		public DefaultString(String value) {
			this.value = value;
		}
	}

	public static final DefaultString NULL_DEFAULT_STRING = new DefaultString(null);

	public static final DefaultString EMPTY_DEFAULT_STRING = new DefaultString("");

	public static DefaultString defaultString(String value) {
		if (null == value) return NULL_DEFAULT_STRING;
		if (0 == value.length()) return EMPTY_DEFAULT_STRING;
		return new DefaultString(value);
	}


	private static JsonObject minusOneJsonObject_(JsonObject jsonObject, String... keys) {
		for (int i = 0; jsonObject != null && i < keys.length - 1; i++) {
			Object subJsonObject = jsonObject.getValue(keys[i]);
			jsonObject = (subJsonObject instanceof JsonObject) ? (JsonObject) subJsonObject : null;
		}
		return jsonObject;
	}

	private static String lastKey_(String... keys) {
		return keys[keys.length - 1];
	}

	public static Object getValue(JsonObject jsonObject, String... keys) {
		return getValue(jsonObject, null, keys);
	}

	public static Object getValue(JsonObject jsonObject, Object def, String... keys) {
		Object result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			result = minusOneJsonObject.getValue(lastKey_(keys));
		}
		return (result != null) ? result : def;
	}

	public static Float getFloat(JsonObject jsonObject, String... keys) {
		return getFloat(jsonObject, null, keys);
	}

	public static Float getFloat(JsonObject jsonObject, Float def, String... keys) {
		Float result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = minusOneJsonObject.getFloat(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getFloat(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return (result != null) ? result : def;
	}

	public static Integer getInteger(JsonObject jsonObject, String... keys) {
		return getInteger(jsonObject, null, keys);
	}

	public static Integer getInteger(JsonObject jsonObject, Integer def, String... keys) {
		Integer result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = minusOneJsonObject.getInteger(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getInteger(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return (result != null) ? result : def;
	}

	public static Long getLong(JsonObject jsonObject, String... keys) {
		return getLong(jsonObject, null, keys);
	}

	public static Long getLong(JsonObject jsonObject, Long def, String... keys) {
		Long result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = minusOneJsonObject.getLong(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getLong(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return (result != null) ? result : def;
	}

	public static String getString(JsonObject jsonObject, String... keys) {
		return getString(jsonObject, NULL_DEFAULT_STRING, keys);
	}

	public static String getString(JsonObject jsonObject, DefaultString def, String... keys) {
		String result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = minusOneJsonObject.getString(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getString(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return (result != null) ? result : ((def != null) ? def.value : null);
	}

	public static Boolean getBoolean(JsonObject jsonObject, String... keys) {
		return getBoolean(jsonObject, null, keys);
	}
	public static Boolean getBoolean(JsonObject jsonObject, Boolean def, String... keys) {
		Boolean result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = minusOneJsonObject.getBoolean(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getBoolean(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return (result != null) ? result : def;
	}

	public static LocalDateTime getLocalDateTime(JsonObject jsonObject, String... keys) {
		return getLocalDateTime(jsonObject, NULL_DEFAULT_STRING, keys);
	}

	public static LocalDateTime getLocalDateTime(JsonObject jsonObject, DefaultString def, String... keys) {
		LocalDateTime result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = DateTimeUtil.toLocalDateTime(minusOneJsonObject.getString(lastKey_(keys)));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getLocalDateTime(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return (result != null) ? result : ((def != null) ? DateTimeUtil.toLocalDateTime(def.value) : null);
	}

	public static JsonArray getJsonArray(JsonObject jsonObject, String... keys) {
		JsonArray result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = minusOneJsonObject.getJsonArray(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getJsonArray(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return result;
	}

	public static List<Float> getFloatList(JsonObject jsonObject, String... keys) {
		JsonArray jsonArray = getJsonArray(jsonObject, keys);
		if (jsonArray != null) {
			List<Float> result = new ArrayList<Float>(jsonArray.size());
			for (int i = 0; i < jsonArray.size(); i++) {
				try {
					result.add(jsonArray.getFloat(i));
				} catch (Exception e) {
					log.error("JsonObjectUtil.getFloatList(); keys : " + Arrays.toString(keys) + ", value : " + jsonArray.getValue(i), e);
				}
			}
			return result;
		}
		return null;
	}

	public static List<Integer> getIntegerList(JsonObject jsonObject, String... keys) {
		JsonArray jsonArray = getJsonArray(jsonObject, keys);
		if (jsonArray != null) {
			List<Integer> result = new ArrayList<Integer>(jsonArray.size());
			for (int i = 0; i < jsonArray.size(); i++) {
				try {
					result.add(jsonArray.getInteger(i));
				} catch (Exception e) {
					log.error("JsonObjectUtil.getIntegerList(); keys : " + Arrays.toString(keys) + ", value : " + jsonArray.getValue(i), e);
				}
			}
			return result;
		}
		return null;
	}
	
	public static List<String> getStringList(JsonObject jsonObject, String... keys) {
		JsonArray jsonArray = getJsonArray(jsonObject, keys);
		if (jsonArray != null) {
			List<String> result = new ArrayList<String>(jsonArray.size());
			for (int i = 0; i < jsonArray.size(); i++) {
				try {
					result.add(jsonArray.getString(i));
				} catch (Exception e) {
					log.error("JsonObjectUtil.getStringList(); keys : " + Arrays.toString(keys) + ", value : " + jsonArray.getValue(i), e);
				}
			}
			return result;
		}
		return null;
	}

	public static JsonObject getJsonObject(JsonObject jsonObject, String... keys) {
		JsonObject result = null;
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject != null) {
			try {
				result = minusOneJsonObject.getJsonObject(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.getJsonObject(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
		}
		return result;
	}

	public static Object remove(JsonObject jsonObject, String... keys) {
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		return (minusOneJsonObject != null) ? minusOneJsonObject.remove(lastKey_(keys)) : null;
	}
	
	public static JsonArray removeJsonArray(JsonObject jsonObject, String... keys) {
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject == null) return null;
		Object obj = minusOneJsonObject.getValue(lastKey_(keys));
		return (obj instanceof JsonArray) ? (JsonArray) minusOneJsonObject.remove(lastKey_(keys)) : null;
	}
	
	public static JsonObject removeJsonObject(JsonObject jsonObject, String... keys) {
		JsonObject minusOneJsonObject = minusOneJsonObject_(jsonObject, keys);
		if (minusOneJsonObject == null) return null;
		Object obj = minusOneJsonObject.getValue(lastKey_(keys));
		return (obj instanceof JsonObject) ? (JsonObject) minusOneJsonObject.remove(lastKey_(keys)) : null;
	}

	private static JsonObject minusOneJsonObjectCreateIfNot_(JsonObject jsonObject, String... keys) {
		for (int i = 0; jsonObject != null && i < keys.length - 1; i++) {
			Object subJsonObject = jsonObject.getValue(keys[i]);
			if (!(subJsonObject instanceof JsonObject)) {
				subJsonObject = new JsonObject();
				jsonObject.put(keys[i], subJsonObject);
			}
			jsonObject = (JsonObject) subJsonObject;
		}
		return jsonObject;
	}

	public static void put(JsonObject jsonObject, Object value, String... keys) {
		JsonObject minusOneJsonObject = minusOneJsonObjectCreateIfNot_(jsonObject, keys);
		minusOneJsonObject.put(lastKey_(keys), value);
	}

	public static void mergeIn(JsonObject jsonObject, JsonObject value, String... keys) {
		if (value != null) {
			JsonObject minusOneJsonObject = minusOneJsonObjectCreateIfNot_(jsonObject, keys);
			JsonObject lastJsonObject = null;
			try {
				lastJsonObject = minusOneJsonObject.getJsonObject(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.mergeIn(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
			if (lastJsonObject == null) {
				minusOneJsonObject.put(lastKey_(keys), value);
			} else {
				lastJsonObject.mergeIn(value, true);
			}
		} else {
			if (log.isWarnEnabled()) log.warn("JsonObjectUtil#mergeIn(); value is null; keys : " + Arrays.toString(keys));
		}
	}

	public static void add(JsonObject jsonObject, Object value, String... keys) {
		if (value != null) {
			JsonObject minusOneJsonObject = minusOneJsonObjectCreateIfNot_(jsonObject, keys);
			JsonArray jsonArray = null;
			try {
				jsonArray = minusOneJsonObject.getJsonArray(lastKey_(keys));
			} catch (Exception e) {
				log.error("JsonObjectUtil.add(); keys : " + Arrays.toString(keys) + ", value : " + minusOneJsonObject.getValue(lastKey_(keys)), e);
			}
			if (jsonArray == null) {
				jsonArray = new JsonArray();
				minusOneJsonObject.put(lastKey_(keys), jsonArray);
			}
			jsonArray.add(value);
		} else {
			if (log.isWarnEnabled()) log.warn("JsonObjectUtil#add(); value is null; keys : " + Arrays.toString(keys));
		}
	}

}

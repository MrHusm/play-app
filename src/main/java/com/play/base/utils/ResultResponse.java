package com.play.base.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.play.base.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultResponse {
	private ResultResponse.Meta meta;
	private Object data;
	private Object ext;

	public ResultResponse() {
	}

	public ResultResponse success() {
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setMeta(new ResultResponse.Meta(ResultMessage.SUCCESS));
		resultResponse.setData((Object)null);
		return resultResponse;
	}

	public ResultResponse success(Object data) {
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setMeta(new ResultResponse.Meta(ResultMessage.SUCCESS));
		resultResponse.setData(data);
		if (data instanceof String || data instanceof Number) {
			Map<String, Object> map = new HashMap();
			map.put("value", data);
			resultResponse.setData(map);
		}

		return resultResponse;
	}

	public ResultResponse success(String key, Object value) {
		Map<String, Object> map = new HashMap();
		map.put(key, value);
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setMeta(new ResultResponse.Meta(ResultMessage.SUCCESS));
		resultResponse.setData(map);
		return resultResponse;
	}

	public ResultResponse success(Object data, Object ext, ResultMessage resultMessage) {
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setMeta(new ResultResponse.Meta(resultMessage));
		resultResponse.setData(data);
		resultResponse.setExt(ext);
		if (data != null && (data instanceof String || data instanceof Number)) {
			Map<String, Object> map = new HashMap();
			map.put("value", data);
			resultResponse.setData(map);
		}

		return resultResponse;
	}

	public ResultResponse success(ResultMessage resultMessage, Object... args) {
		ResultResponse resultResponse = new ResultResponse();
		String message = resultMessage.getMessage();
		if (args.length > 0) {
			message = String.format(resultMessage.getMessage().contains("%s") ? resultMessage.getMessage() : resultMessage.getMessage() + "，%s", args);
		}

		resultResponse.setMeta(new ResultResponse.Meta(resultMessage.getCode(), message));
		resultResponse.setData((Object)null);
		return resultResponse;
	}

	public ResultResponse failure() {
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setMeta(new ResultResponse.Meta(ResultMessage.FAILURE));
		resultResponse.setData((Object)null);
		return resultResponse;
	}

	public ResultResponse failure(ServiceException ex) {
		ResultResponse resultResponse = new ResultResponse();
		resultResponse.setMeta(new ResultResponse.Meta(ex.getResultMessage().getCode(), ex.getMessage()));
		resultResponse.setData((Object)null);
		return resultResponse;
	}

	public ResultResponse failure(ResultMessage resultMessage, Object... args) {
		ResultResponse resultResponse = new ResultResponse();
		String message = resultMessage.getMessage();
		if (args.length > 0) {
			message = String.format(resultMessage.getMessage().contains("%s") ? resultMessage.getMessage() : resultMessage.getMessage() + "，%s", args);
		}

		resultResponse.setMeta(new ResultResponse.Meta(resultMessage.getCode(), message));
		resultResponse.setData((Object)null);
		return resultResponse;
	}

	public ResultResponse.Meta getMeta() {
		return this.meta;
	}

	public Object getData() {
		return this.data;
	}

	public void setMeta(ResultResponse.Meta meta) {
		this.meta = meta;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getExt() {
		return this.ext;
	}

	public void setExt(Object ext) {
		this.ext = ext;
	}

	class Meta {
		private String message;
		private int code;

		public Meta() {
		}

		public Meta(int code) {
			this.code = code;
		}

		public Meta(ResultMessage resultMessage) {
			this.code = resultMessage.getCode();
			this.message = resultMessage.getMessage();
		}

		public Meta(int code, String message) {
			this.code = code;
			this.message = message;
		}

		public int getCode() {
			return this.code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMessage() {
			return this.message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}

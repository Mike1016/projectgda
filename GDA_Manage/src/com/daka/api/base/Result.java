package com.daka.api.base;

import java.io.Serializable;
import java.util.Optional;

/**
 * 接口返回实体
 * @param <T>
 */
public class Result<T> implements Serializable {
	private int status;
	private T data;
	private String msg;

	public Result() {}
	public Result(int status, T data, String msg) {
		this.status = status;
		this.data = data;
		this.msg = msg;
	}

	public static <T> Result<T> newSuccess() {
		return newSuccess(null, null);
	}

	public static <T> Result<T> newSuccess(String message, Integer ...status) {
		return newSuccess(null, message, status);
	}

	public static <T> Result<T> newSuccess(T object, Integer ...status) {
		return newSuccess(object, null, status);
	}

	public static <T> Result<T> newSuccess(T object, String message, Integer ...status) {
		return new Result<>(status != null && status.length > 0 ? status[0] : 1, Optional.ofNullable(object).orElse((T) ""), Optional.ofNullable(message).orElse("成功"));
	}

	public static <T> Result<T> newFailure(){
		return newFailure(null, null);
	}

	public static <T> Result<T> newFailure(T object, Integer ...status){
		return newFailure(object, null, status);
	}

	public static <T> Result<T> newFailure(String message, Integer ...status){
		return newFailure(null, message, status);
	}

	public static <T> Result<T> newFailure(T object, String message, Integer ...status){
		return new Result<>(status != null && status.length > 0 ? status[0] : 0, Optional.ofNullable(object).orElse((T) ""), Optional.ofNullable(message).orElse("失败"));
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

package com.son.daou.common.exception;

import com.son.daou.common.ErrorCode;
import lombok.Data;

@Data
public class ApiException extends RuntimeException {

	private ErrorCode errorCode;

	public ApiException(ErrorCode errorCode, String detailMessage){
		super(detailMessage);
		this.errorCode = errorCode;
	}
}
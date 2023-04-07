package com.son.api.common.exception;

import com.son.api.common.ErrorCode;
import lombok.Data;

@Data
public class ApiException extends RuntimeException {

	private ErrorCode errorCode;

	public ApiException(ErrorCode errorCode, String detailMessage){
		super(detailMessage);
		this.errorCode = errorCode;
	}
}
package com.lxf.common.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class BadException extends RuntimeException {

	private String code;

    public BadException(ResponseCodes code, String message) {
        super(message);
        this.code = code.getCode();
    }

    public BadException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BadException(ResponseCodes responseCodes){
        this(responseCodes,responseCodes.getMsg());
    }

    public BadException(String message) {
        super(message);
        this.code = ResponseCodes.SYSTEM_ERROR.getCode();
    }

}

package com.play.base.exception;

import com.play.base.utils.ResultMessage;

public class ServiceException extends Exception {
    private static final long serialVersionUID = 6654830544228411564L;
    private ResultMessage errorMessage;

    public ResultMessage getResultMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorMessage.getCode();
    }

    public void setErrorCode(ResultMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ServiceException() {
        super(ResultMessage.FAILURE.getMessage());
        this.errorMessage = ResultMessage.FAILURE;
    }

    public ServiceException(ResultMessage errorMessage, Object... args) {
        super(String.format(errorMessage.getMessage().contains("%s") ? errorMessage.getMessage() : errorMessage.getMessage() + "，%s", args));
        this.errorMessage = new ResultMessage(errorMessage.getCode(), this.getMessage());
    }

    public ServiceException(ResultMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}

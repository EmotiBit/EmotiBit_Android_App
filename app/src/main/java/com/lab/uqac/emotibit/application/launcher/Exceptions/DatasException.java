package com.lab.uqac.emotibit.application.launcher.Exceptions;

public class DatasException extends Exception{

    private ErrorNumber mErrorNumber;

    public DatasException(ErrorNumber errorNumber){
        mErrorNumber = errorNumber;
    }

    public DatasException(ErrorNumber errorNumber, String message, Throwable cause) {
        super(message, cause);
        this.mErrorNumber = errorNumber;
    }

    public DatasException(ErrorNumber errorNumber, String message) {
        super(message);
        this.mErrorNumber = errorNumber;
    }

    public DatasException(ErrorNumber errorNumber, Throwable cause) {
        super(cause);
        this.mErrorNumber = errorNumber;
    }

    public ErrorNumber getmErrorNumber(){
        return mErrorNumber;
    }

}

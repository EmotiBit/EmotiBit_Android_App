package com.lab.uqac.emotibit.application.launcher.Exceptions;

public enum ErrorNumber {

    ERR_FORMAT(101), ERR_LENGTH(102), ERR_PACKETNUMBER(103), ERR_TAG(104), ERR_SIZE(105),
    ERR_RELIABILITY(106), ERR_PROTOCOL(107), ERR_TIMESTAMP(108), ERR_NULL(109);

    private int mId;

    ErrorNumber(int id) {
        this.mId = id;
    }

    public int getmId() {
        return mId;
    }
}

// Word2 for History Out Menu.
package com.e.mywallet;

public class Word2 {

    private String mTransactionID;
    private String mStealthReceiver;
    private String mValue;
    private String mTime;


    public Word2(String transactionID,String stealthReceiver, String value, String time){
        mTransactionID=transactionID;
        mStealthReceiver=stealthReceiver;
        mValue=value;
        mTime=time;
    }

    //Return String
    public String getTransactionID(){
        return mTransactionID;
    }
    public String getStealthReceiver(){
        return mStealthReceiver;
    }
    public String getValue_HisOut(){
        return mValue;
    }
    public String getTime_HisOut(){
        return mTime;
    }
}
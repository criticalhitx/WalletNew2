package com.e.mywallet;

public class Word {

    /*Default translation for the word */
    private String mDefaultTranslation;

    /*Miwok translation for the word */
    private String mMiwokTranslation;

    //Wamok
    private String mWamokTranslation;


    public Word(String miwokTranslation,String defaultTranslation, String wamokTranslation){
        mDefaultTranslation=defaultTranslation;
        mMiwokTranslation=miwokTranslation;
        mWamokTranslation=wamokTranslation;
    }

    //Return String
    public String getDefaultTranslation(){
        return mDefaultTranslation;
    }
    public String getMiwokTranslation(){
        return mMiwokTranslation;
    }
    public String getWamokTranslation(){
        return mWamokTranslation;
    }
}
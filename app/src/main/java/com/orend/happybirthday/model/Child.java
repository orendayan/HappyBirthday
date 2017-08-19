package com.orend.happybirthday.model;

import android.net.Uri;

/**
 * Created by orendayan on 20/08/2017.
 *
 * A builder pattern that present the child model
 */

public class Child {

    private String mName;
    private String mBirthday;
    private Uri mImage;

    private Child(BuilderChild builder) {
        mName = builder.mName;
        mBirthday = builder.mBirthday;
        mImage = builder.mImage;
    }
    public String getName() {
        return mName;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public Uri getImage() {
        return mImage;
    }

    public static class BuilderChild {
        private String mName;
        private String mBirthday;
        private Uri mImage;

        public BuilderChild(String name, String birthday, Uri imageUri){
            mName = name;
            mBirthday = birthday;
            mImage = imageUri;
        }

        public void setBirthday(String birthday) {
            mBirthday = birthday;
        }

        public void setmImage(Uri image) {
            mImage = image;
        }

        public void setName(String name) {
            mName = name;
        }

        public Child build(){
            return new Child(this);
        }

    }
}

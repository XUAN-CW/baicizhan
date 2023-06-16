package com.example.baicizhan.entity;

import androidx.annotation.NonNull;

public enum Event {
    LOOK_AT_THE_PICTURE_AND_CHOOSE_THE_WORDS("看图选词");
    final String cn;

    Event(String cn) {
        this.cn = cn;
    }

    @NonNull
    @Override
    public String toString() {
        return cn;
    }
}

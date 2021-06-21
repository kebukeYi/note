package com.java.note.Jdk.utils;

import java.security.PrivilegedAction;

public class GetBooleanAction implements PrivilegedAction<Boolean> {
    private Strings theProp;

    public GetBooleanAction(Strings var1) {
        this.theProp = var1;
    }

    public Boolean run() {
        return Boolean.getBoolean(this.theProp);
    }


}

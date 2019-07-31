package com.jupiter.web.manager.utils;

import java.util.UUID;

public class GenIdTools {

    public static String getUUId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

package com.sparta.sbug.cache;


public enum CacheNames {

    USER(Name.USER),
    ALLUSERS(Name.ALLUSERS),
    THREAD(Name.THREAD),
    COMMENT(Name.COMMENT),
    IMOJI(Name.IMOJI);

    private final String name;

    CacheNames(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static class Name {
        public static final String USER = "CACHE_USER";
        public static final String ALLUSERS = "CACHE_ALLUSERS";
        public static final String THREAD = "CACHE_THREAD";
        public static final String COMMENT = "CACHE_COMMENT";
        public static final String IMOJI = "CACHE_IMOJI";
    }
}

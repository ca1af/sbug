package com.sparta.sbug.admin.entity;

public enum AdminRole {
    ADMIN(Authority.ADMIN);  // 관리자 권한
    private final String authority;

    AdminRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public static class Authority {

        public static final String ADMIN = "ROLE_ADMIN";
    }

}
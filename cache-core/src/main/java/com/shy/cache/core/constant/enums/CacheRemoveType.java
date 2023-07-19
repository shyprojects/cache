package com.shy.cache.core.constant.enums;

/***
 * @author shy
 * @date 2023-07-20 0:05
 */
public enum CacheRemoveType {

    EXPIRE("expire", "过期"),
    EVICT("evict", "淘汰"),
    ;

    private final String code;

    private final String desc;

    CacheRemoveType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }

    @Override
    public String toString() {
        return "CacheRemoveType{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}

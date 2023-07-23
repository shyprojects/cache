package com.shy.cache.core.model;

import lombok.Data;

/***
 * 持久化明细-aof
 * @author shy
 * @date 2023-07-23 1:39
 */
@Data
public class PersistAofEntity {

    private Object[] params;

    private String methodName;

    public static PersistAofEntity newInstance(){
        return new PersistAofEntity();
    }
}

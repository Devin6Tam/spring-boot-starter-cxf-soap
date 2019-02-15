package com.mzbloc.springboot.cxf.soap.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * Created by tanxw on 2019/2/1.
 */
public abstract class AbstractJsonSeriable implements Serializable {
    protected static final long serialVersionUID = 4538004751694592044L;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public AbstractJsonSeriable() {
    }

    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException var2) {
            return super.toString();
        }
    }
}

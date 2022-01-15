package com.miaosha.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class ValidationResult {

    public Boolean hasErrors = false;
    public Map<String, String> errorMsgMap = new HashMap<>();

    public String getErrorMsg() {
        return StringUtils.join(this.errorMsgMap.values().toArray(), ",");
    }

}

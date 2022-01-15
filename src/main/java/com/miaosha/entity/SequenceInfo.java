package com.miaosha.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yeme
 * @since 2021-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sequence_info")
public class SequenceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序列名称
     */
    private String name;

    /**
     * 当前值
     */
    private Integer currentValue;

    /**
     * 步长
     */
    private Integer step;


}

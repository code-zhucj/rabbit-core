package core.view.collector;

/**
 * 采集状态
 */
public enum Status {

    /**
     * 新增 从无到有 null -> value
     */
    ADD,
    /**
     * 删除 从有到无 value -> null
     */
    DELETE,
    /**
     * 变化 从值1变为值2并且两个值不等 value1 -> value2 && value1 != value2
     */
    CHANGE,

    /**
     * 过程 从值1变为值2 两个值可能相等，并且包含中间值 value1 -> value2 -> value3, value1 -> value2 -> value1
     */
    COURSE,
}

package core.thread;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolExecutorMBean
 *
 * @author zhuchuanji
 * @date 2021/2/8
 */
class ThreadPoolExecutorMBean implements DynamicMBean {

    private final ThreadPoolExecutor executor;
    private final MBeanInfo info;

    public ThreadPoolExecutorMBean(String name, ThreadPoolExecutor executor) {
        MBeanAttributeInfo[] attrinfo = {
                new MBeanAttributeInfo("TaskCount", "java.lang.Long", "TaskCount", true, false, false),
                new MBeanAttributeInfo("CompletedTaskCount", "java.lang.Long", "CompletedTaskCount", true, false,
                        false),
                new MBeanAttributeInfo("ActiveCount", "java.lang.Integer", "ActiveCount", true, false, false),
                new MBeanAttributeInfo("PoolSize", "java.lang.Integer", "PoolSize", true, false, false),
                new MBeanAttributeInfo("CorePoolSize", "java.lang.Integer", "CorePoolSize", true, true, false),
                new MBeanAttributeInfo("MaximumPoolSize", "java.lang.Integer", "MaximumPoolSize", true, true, false),
                new MBeanAttributeInfo("LargestPoolSize", "java.lang.Integer", "LargestPoolSize", true, false, false),
                new MBeanAttributeInfo("KeepAliveTime", "java.lang.Long", "KeepAliveTime", true, true, false),
                new MBeanAttributeInfo("QueueLength", "java.lang.Integer", "QueueLength", true, false, false),};
        this.executor = executor;
        this.info = new MBeanInfo(getClass().getName(), name, attrinfo, null, null, null);
    }

    @Override
    public Object getAttribute(String attribute) {
        switch (attribute) {
            case "TaskCount":
                return executor.getTaskCount();
            case "CompletedTaskCount":
                return executor.getCompletedTaskCount();
            case "ActiveCount":
                return executor.getActiveCount();
            case "PoolSize":
                return executor.getPoolSize();
            case "CorePoolSize":
                return executor.getCorePoolSize();
            case "MaximumPoolSize":
                return executor.getMaximumPoolSize();
            case "LargestPoolSize":
                return executor.getLargestPoolSize();
            case "KeepAliveTime":
                return executor.getKeepAliveTime(TimeUnit.MILLISECONDS);
            case "QueueLength":
                return executor.getQueue().size();
            default:
                return null;
        }
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return this.info;
    }
}

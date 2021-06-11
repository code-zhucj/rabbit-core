package core.config;

import core.exception.Alarm;
import core.exception.ConfigException;
import core.exception.DefaultAlarm;
import core.util.StringUtils;

public class GlobalConfig {

    private boolean openAlarm;

    private Alarm alarm;

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        if (StringUtils.isBlank(alarm)) {
            this.alarm = new DefaultAlarm();
        } else {
            try {
                Class<?> clazz = Class.forName(alarm);
                this.alarm = (Alarm) clazz.newInstance();
            } catch (Exception e) {
                throw new ConfigException("");
            }
        }
    }

    public boolean isOpenAlarm() {
        return openAlarm;
    }

    public void setOpenAlarm(boolean openAlarm) {
        this.openAlarm = openAlarm;
    }
}

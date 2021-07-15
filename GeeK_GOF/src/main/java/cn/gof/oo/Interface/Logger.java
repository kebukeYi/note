package cn.gof.oo.Interface;

import java.io.IOException;
import java.util.logging.Level;

/**
 * @author : kebukeyi
 * @date :  2021-07-14 12:44
 * @description :
 * @question :
 * @usinglink :
 **/
public abstract class Logger {

    private String name;
    private boolean enabled;
    private Level minPermittedLevel;

    public Logger(String name, boolean enabled, Level minPermittedLevel) {
        this.name = name;
        this.enabled = enabled;
        this.minPermittedLevel = minPermittedLevel;
    }

    //模板模式
    public void log(Level level, String message) throws IOException {
        boolean loggable = enabled && (minPermittedLevel.intValue() <= level.intValue());
        if (!loggable) return;
        doLog(level, message);
    }

    protected abstract void doLog(Level level, String message) throws IOException;
}
 

package tr.com.turktelecom.lighthouse.domain.exceptions;

/**
 * Created by 010235 on 29.04.2016.
 */
public class PluginRunFailedException extends Exception {

    public PluginRunFailedException(String message, Exception e) {
        super(message, e);
    }

    public PluginRunFailedException(String s) {
        super(s);
    }
}

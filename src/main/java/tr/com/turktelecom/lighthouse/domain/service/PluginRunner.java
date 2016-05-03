package tr.com.turktelecom.lighthouse.domain.service;

import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;

/**
 * Created by 010235 on 22.04.2016.
 */
public interface PluginRunner {
    void run(Plugin plugin) throws PluginContextNotSupportedException, PluginRunFailedException;
}

package tr.com.turktelecom.lighthouse.domain.service.collector;

import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;

/**
 * Created by 010235 on 05.05.2016.
 */
public abstract class AbstractPluginResultCollector implements PluginResultCollector {

    protected abstract Boolean canHandlePlugin(Plugin plugin);
    protected abstract Scan collectResultsInternal(Plugin plugin);

    @Override
    public Scan collectResults(Plugin plugin) {
        if (canHandlePlugin(plugin)) {
            return this.collectResultsInternal(plugin);
        }
        return null;
    }
}

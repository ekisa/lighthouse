package tr.com.turktelecom.lighthouse.domain.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.domain.service.PluginRunner;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by 010235 on 22.04.2016.
 */
public abstract class AbstractPluginRunnerImpl implements PluginRunner {

    @Inject
    Environment environment;

    private final Logger log = LoggerFactory.getLogger(AbstractPluginRunnerImpl.class);

    @Override
    public void run(Plugin plugin) throws PluginContextNotSupportedException, PluginRunFailedException {
        runInternal(plugin);
    }


    protected abstract void runInternal(Plugin plugin) throws PluginContextNotSupportedException, PluginRunFailedException;


    protected String findBaseDirectoryURI() {
        String baseDirectoryURI = "";
        if (StringUtils.isEmpty(environment.getProperty("pluginRunner.python.baseDirectoryURI"))) {
            baseDirectoryURI = Paths.get(".").toAbsolutePath().normalize().toString();
        }
        else {
            baseDirectoryURI = environment.getProperty("pluginRunner.python.baseDirectoryURI");
        }
        return baseDirectoryURI;
    }

    protected File findWorkingDirectory(Plugin plugin) {
        return new File(this.findBaseDirectoryURI()
            + File.separator + "plugins"
            + File.separator + plugin.getFolderName());
    }
}

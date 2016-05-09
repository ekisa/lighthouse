package tr.com.turktelecom.lighthouse.domain.service.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.domain.service.collector.PluginResultCollector;
import tr.com.turktelecom.lighthouse.repository.ScanRepository;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Created by 010235 on 22.04.2016.
 */
public abstract class AbstractPluginRunner implements PluginRunner {

    @Inject
    protected PluginResultCollector resultCollector;

    @Inject
    protected Environment environment;

    @Inject
    private ScanRepository scanRepository;

    private final Logger log = LoggerFactory.getLogger(AbstractPluginRunner.class);

    @Override
    public Scan run(Plugin plugin) throws PluginContextNotSupportedException, PluginRunFailedException {
        runInternal(plugin);
        Scan previousScan = null;
        if (plugin.getId() != null) {
            previousScan = scanRepository.findTopByPluginIdOrderByCreatedDateDesc(plugin.getId());
        }
        Scan lastScan = resultCollector.collectResults(plugin);

        lastScan.analyzeDifferences(previousScan);

        return lastScan;
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

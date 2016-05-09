package tr.com.turktelecom.lighthouse.domain.service.collector.reader;

import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;

/**
 * Created by 010235 on 05.05.2016.
 */
public interface FileReader {
    Scan readFile(String filePath) throws PluginRunFailedException;
}

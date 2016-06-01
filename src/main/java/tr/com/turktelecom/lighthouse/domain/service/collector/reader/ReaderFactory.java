package tr.com.turktelecom.lighthouse.domain.service.collector.reader;

import org.springframework.context.annotation.Bean;
import tr.com.turktelecom.lighthouse.domain.Plugin;

/**
 * Created by 010235 on 09.05.2016.
 */
public class ReaderFactory {

    public static synchronized FileReader createFileReader(Plugin plugin) {
        switch (plugin.getOutputFormat()) {
            case "SIMPLE":
                return new SimpleFileReader();
            case "NESSUS":
                return new NessusXMLReader();
            case "O-SAFT":
                return new OSaftFileReader();
            default:
                return new NessusXMLReader();
        }
    }
}

package tr.com.turktelecom.lighthouse.domain.service.collector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.com.turktelecom.lighthouse.domain.ExecutablePluginContext;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.service.collector.reader.*;
import tr.com.turktelecom.lighthouse.domain.service.collector.reader.FileReader;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;

/**
 * Created by 010235 on 04.05.2016.
 */
@Component
public class ResultCollectorFromFile extends AbstractPluginResultCollector {

    private FileReader fileReader;

    @Override
    protected Boolean canHandlePlugin(Plugin plugin) {
        return plugin.getPluginContext() instanceof ExecutablePluginContext;
    }

    @Override
    public Scan collectResultsInternal(Plugin plugin) {
        ExecutablePluginContext executablePluginContext = (ExecutablePluginContext) plugin.getPluginContext();
        String filePath = "plugins" + File.separator + plugin.getFolderName() + File.separator + executablePluginContext.getOutputFilePath();
        Scan scan = null;
        try{
            fileReader = ReaderFactory.createFileReader(plugin);
            scan = fileReader.readFile(filePath);
        }catch(Exception e){
            e.printStackTrace();
        }
        return scan;
    }
}

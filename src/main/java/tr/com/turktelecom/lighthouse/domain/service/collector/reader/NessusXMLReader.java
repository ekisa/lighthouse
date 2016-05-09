package tr.com.turktelecom.lighthouse.domain.service.collector.reader;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.Severity;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.service.util.DateTimeUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by 010235 on 05.05.2016.
 */
@Component
public class NessusXMLReader implements FileReader {

    @Override
    public Scan readFile(String filePath) throws PluginRunFailedException{
        Scan scan = null;
        try {
            scan = new Scan();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            Document doc = null;
            XPathExpression expr = null;
            builder = factory.newDocumentBuilder();
            doc = builder.parse(filePath);

            // create an XPathFactory
            XPathFactory xFactory = XPathFactory.newInstance();

            // create an XPath object
            XPath xpath = xFactory.newXPath();

            // ReportHosts'lari tek tek oku
            expr = xpath.compile("/NessusClientData_v2/Report/*");

            // run the query and get a nodeset
            NodeList reportHosts = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            for (int i=0; i<reportHosts.getLength();i++){
                Node reportHost = reportHosts.item(i);
                String hostName = Optional.ofNullable(reportHost.getAttributes().getNamedItem("name")).map(Node::getNodeValue).orElse("");
                String sourceIP = Optional.ofNullable((Node) xpath.compile("HostProperties/tag[@name='host-ip']").evaluate(reportHost, XPathConstants.NODE)).map(Node::getTextContent).orElse("");

                NodeList reportItems = (NodeList) xpath.compile("ReportItem").evaluate(reportHost, XPathConstants.NODESET);
                for(int j=0; j<reportItems.getLength();j++){
                    Node reportItem = reportItems.item(j);

                    String port = Optional.ofNullable(reportItem.getAttributes().getNamedItem("port")).map(Node::getNodeValue).orElse("");
                    String protocol = Optional.ofNullable(reportItem.getAttributes().getNamedItem("protocol")).map(Node::getNodeValue).orElse("");
                    String severity = Optional.ofNullable(reportItem.getAttributes().getNamedItem("severity")).map(Node::getNodeValue).orElse("");
                    //String pluginId = Optional.ofNullable(reportItem.getAttributes().getNamedItem("pluginId")).map(Node::getNodeValue).orElse("");
                    String pluginName = Optional.ofNullable(reportItem.getAttributes().getNamedItem("pluginName")).map(Node::getNodeValue).orElse("");
                    //String pluginFamily = Optional.ofNullable(reportItem.getAttributes().getNamedItem("pluginFamily")).map(Node::getNodeValue).orElse("");
                    String description = Optional.ofNullable((Node) xpath.compile("description").evaluate(reportItem, XPathConstants.NODE)).map(Node::getTextContent).orElse("");

                    Defect defect = new Defect();

                    defect.setSourceIP(sourceIP);
                    defect.setHostName(hostName);
                    defect.setScan(scan);
                    defect.setPort(port);
                    defect.setProtocol(protocol);
                    defect.setSeverity(Severity.fromNo(severity));
                    defect.setTitle(pluginName);
                    defect.setFalsePositive(Boolean.FALSE);
                    defect.setExplanation(description);
                    defect.setNeedManuelCheck(Boolean.FALSE);
                    scan.addDefect(defect);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new PluginRunFailedException("Nessus formatlı XML dosyası okunamadı", e);
        }

        return scan;
    }
}

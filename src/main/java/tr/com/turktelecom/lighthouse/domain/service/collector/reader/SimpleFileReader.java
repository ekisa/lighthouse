package tr.com.turktelecom.lighthouse.domain.service.collector.reader;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.Severity;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by 010235 on 05.05.2016.
 */
@Component
public class SimpleFileReader implements FileReader {

    @Override
    public Scan readFile(String filePath) throws PluginRunFailedException{
        Scan scan = null;
        BufferedReader br = null;
        FileInputStream fstream = null;
        try {
            scan = new Scan();
            // Open the file

            fstream = new FileInputStream(filePath);

            br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            //Read File Line By Line
            int lineCouter = 0;
            while (!StringUtils.isEmpty(strLine = br.readLine()))   {
                //İlk satırda header var, onu okumadan geç
                if(lineCouter==0){
                    lineCouter++;
                    continue;
                }
                String[] values = StringUtils.tokenizeToStringArray(strLine, "|",true, false);
                String sourceIP = null, hostName = null, port = null, protocol = null, severity = null, pluginName = null, description = null, resultCode = null;
                int i = 0;
                for(String val : values){
                    i++;
                    if(i==1){sourceIP=val;continue;}
                    if(i==2){hostName=val;continue;}
                    if(i==3){port=val;continue;}
                    if(i==4){protocol=val;continue;}
                    if(i==5){severity=val;continue;}
                    if(i==6){pluginName=val;continue;}
                    if(i==7){resultCode=val;continue;}
                    if(i==8){description=val;continue;}
                }
                if("VULNERABLE".equals(resultCode) || "MANUEL_CHECK".equals(resultCode)){
                    Defect defect = new Defect();
                    defect.setSourceIP(sourceIP);
                    defect.setHostName(hostName);
                    defect.setPort(port);
                    defect.setProtocol(protocol);
                    defect.setSeverity(Severity.valueOf(severity));
                    defect.setTitle(pluginName);
                    defect.setFalsePositive(Boolean.FALSE);
                    defect.setExplanation(description);
                    if ("MANUEL_CHECK".equals(resultCode)) {
                        defect.setNeedManuelCheck(Boolean.TRUE);
                    }else {
                        defect.setNeedManuelCheck(Boolean.FALSE);
                    }
                    scan.addDefect(defect);
                }
            }
            fstream.close();
            //Close the input stream
            br.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new PluginRunFailedException("Dosya okunamadı", e);
        }finally {
            try {
                if (fstream != null) {
                    fstream.close();
                }
                if(br != null){
                    br.close();
                }
            }catch (Exception e){
                throw new PluginRunFailedException("Dosya okunamadı", e);
            }
        }

        return scan;
    }
}

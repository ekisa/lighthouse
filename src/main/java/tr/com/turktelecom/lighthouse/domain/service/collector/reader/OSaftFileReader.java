package tr.com.turktelecom.lighthouse.domain.service.collector.reader;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.Severity;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.service.util.DateTimeUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 010235 on 05.05.2016.
 */
@Component
public class OSaftFileReader implements FileReader {

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

            String strLine = "";

            //Create RegEx patterns to extract meaningful data
            Pattern givenHostNamePattern = Pattern.compile("Given hostname:\\s*([^\\n\\r]*)");
            Pattern ipForGivenHostNamePattern = Pattern.compile("IP for given hostname:\\s*([^\\n\\r]*)");
            Pattern selectedCipherPattern = Pattern.compile("Selected Cipher:\\s*([^\\n\\r]*)");
            Pattern tlsSessionTicketRandomPattern = Pattern.compile("TLS Session Ticket random:\\s*([^\\n\\r]*)");
            Pattern noRC4CiphersPattern = Pattern.compile("No RC4 ciphers:\\s*([^\\n\\r]*)");
            Pattern safeToFreakPattern = Pattern.compile("Safe to FREAK:\\s*([^\\n\\r]*)");
            Pattern safeToBEASTPattern = Pattern.compile("Safe to BEAST (cipher):\\s*([^\\n\\r]*)");
            Pattern safeToLogjamPattern = Pattern.compile("Safe to Logjam:\\s*([^\\n\\r]*)");
            Pattern safeToRC4attackPattern = Pattern.compile("Safe to RC4 attack:\\s*([^\\n\\r]*)");
            Pattern safeToHeartbleedPattern = Pattern.compile("Safe to Heartbleed:\\s*([^\\n\\r]*)");
            Pattern safeToCRIMEPattern = Pattern.compile("Safe to CRIME:\\s*([^\\n\\r]*)");
            Pattern signatureIsSHA2Pattern = Pattern.compile("Signature is SHA2:\\s*([^\\n\\r]*)");
            Pattern safeToPOODLEPattern = Pattern.compile("Safe to POODLE:\\s*([^\\n\\r]*)");
            Pattern fingerprintNotMD5Pattern = Pattern.compile("Fingerprint not MD5:\\s*([^\\n\\r]*)");
            Pattern noExportCiphersPattern = Pattern.compile("No EXPORT ciphers:\\s*([^\\n\\r]*)");
            Pattern noNULLCiphersPattern = Pattern.compile("No NULL ciphers:\\s*([^\\n\\r]*)");
            Pattern PCICompliantPattern = Pattern.compile("PCI compliant:\\s*([^\\n\\r]*)");
            Pattern noSSLV3Pattern = Pattern.compile("No SSLv3:\\s*([^\\n\\r]*)");

            Pattern weakYes = Pattern.compile("yes[\\t]weak");
            Pattern mediumYes = Pattern.compile("yes[\\t]MEDIUM");

            String sourceIP = null, hostName = null; //, port = null, protocol = null, pluginName = null,  resultCode = null;
            String selectedCipher = null, tlsSessionTicketRandom = null, noRC4Ciphers = null, safeToFreak = null, safeToBEAST = null, safeToLogjam = null,
                safeToHeartbleed = null, safeToCRIME = null, signatureIsSHA2 = null, safeToPOODLE = null, fingerprintNotMD5 = null, noExportCiphers = null,
                noNULLCiphers = null, PCICompliant = null, noSSLV3 = null, safeToRC4attack = null;

            //Read File line by line
            while (strLine != null)   {
                strLine = br.readLine();
                if (StringUtils.isEmpty(strLine)) {
                    continue;
                }
                // Read Host SSL Configuration details
                if (givenHostNamePattern.matcher(strLine).find()) {
                    hostName = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                }
                else if (ipForGivenHostNamePattern.matcher(strLine).find()) {
                    sourceIP = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                }
                else if (selectedCipherPattern.matcher(strLine).find()) {
                    selectedCipher = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    Defect defect = new Defect();
                    defect.setTitle("Selected Cipher : " + selectedCipher);
                    defect.setSeverity(Severity.INFO);
                    defect.setExplanation("Selected Cipher : " + selectedCipher);
                    defect.setHostName(hostName);
                    defect.setNeedManuelCheck(Boolean.FALSE);
                    defect.setSourceIP(sourceIP);
                    scan.addDefect(defect);
                }
                else if (tlsSessionTicketRandomPattern.matcher(strLine).find()) {
                    tlsSessionTicketRandom = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(tlsSessionTicketRandom)  && !"no (no reply)".equalsIgnoreCase(tlsSessionTicketRandom)) {
                        Defect defect = new Defect();
                        defect.setTitle("TLS Session Ticket Random : " + tlsSessionTicketRandom);
                        defect.setSeverity(Severity.MEDIUM);
                        defect.setExplanation("TLS Session Ticket Random : " + tlsSessionTicketRandom);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (noRC4CiphersPattern.matcher(strLine).find()) {
                    noRC4Ciphers = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(noRC4Ciphers) && !"no (no reply)".equalsIgnoreCase(noRC4Ciphers)) {
                        Defect defect = new Defect();
                        defect.setTitle("No RC4 Ciphers : " + noRC4Ciphers);
                        defect.setSeverity(Severity.MEDIUM);
                        defect.setExplanation("No RC4 Ciphers : " + noRC4Ciphers);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }

                }
                else if (safeToFreakPattern.matcher(strLine).find()) {
                    safeToFreak = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(safeToFreak)  && !"no (no reply)".equalsIgnoreCase(safeToFreak)) {
                        Defect defect = new Defect();
                        defect.setTitle("Safe to FREAK : " + safeToFreak);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Safe to FREAK : " + safeToFreak);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (safeToBEASTPattern.matcher(strLine).find()) {
                    safeToBEAST = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(safeToBEAST) &&  !"no (no reply)".equalsIgnoreCase(safeToBEAST)) {
                        Defect defect = new Defect();
                        defect.setTitle("Safe to BEAST : " + safeToBEAST);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Safe to BEAST : " + safeToBEAST);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (safeToLogjamPattern.matcher(strLine).find()) {
                    safeToLogjam = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(safeToLogjam) &&  !"no (no reply)".equalsIgnoreCase(safeToLogjam)) {
                        Defect defect = new Defect();
                        defect.setTitle("Safe to Logjam : " + safeToLogjam);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Safe to Logjam : " + safeToLogjam);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (safeToRC4attackPattern.matcher(strLine).find()) {
                    safeToRC4attack = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(safeToRC4attack) &&  !"no (no reply)".equalsIgnoreCase(safeToRC4attack)) {
                        Defect defect = new Defect();
                        defect.setTitle("Safe to RC4 attack : " + safeToRC4attack);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Safe to RC4 Attack : " + safeToRC4attack);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }

                else if (safeToHeartbleedPattern.matcher(strLine).find()) {
                    safeToHeartbleed = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(safeToHeartbleed) && !"no (no reply)".equalsIgnoreCase(safeToHeartbleed)) {
                        Defect defect = new Defect();
                        defect.setTitle("Safe to Heartbleed : " + safeToHeartbleed);
                        defect.setSeverity(Severity.CRITICAL);
                        defect.setExplanation("Safe to Heartbleed : " + safeToHeartbleed);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (safeToCRIMEPattern.matcher(strLine).find()) {
                    safeToCRIME = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(safeToCRIME) &&  !"no (no reply)".equalsIgnoreCase(safeToCRIME)) {
                        Defect defect = new Defect();
                        defect.setTitle("Safe to CRIME : " + safeToCRIME);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Safe to CRIME : " + safeToCRIME);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (signatureIsSHA2Pattern.matcher(strLine).find()) {
                    signatureIsSHA2 = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(signatureIsSHA2) &&  !"no (no reply)".equalsIgnoreCase(signatureIsSHA2)) {
                        Defect defect = new Defect();
                        defect.setTitle("Signature is SHA2 : " + signatureIsSHA2);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Signature is SHA2 : " + signatureIsSHA2);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (safeToPOODLEPattern.matcher(strLine).find()) {
                    safeToPOODLE = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(safeToPOODLE) &&  !"no (no reply)".equalsIgnoreCase(safeToPOODLE)) {
                        Defect defect = new Defect();
                        defect.setTitle("Safe to POODLE " + safeToPOODLE);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Safe to POODLE : " + safeToPOODLE);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (fingerprintNotMD5Pattern.matcher(strLine).find()) {
                    fingerprintNotMD5 = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(fingerprintNotMD5) &&  !"no (no reply)".equalsIgnoreCase(fingerprintNotMD5)) {
                        Defect defect = new Defect();
                        defect.setTitle("Fingerprint not MD5 : " + fingerprintNotMD5);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("Fingerprint not MD5 : " + fingerprintNotMD5);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (noExportCiphersPattern.matcher(strLine).find()) {
                    noExportCiphers = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(noExportCiphers) &&  !"no (no reply)".equalsIgnoreCase(noExportCiphers)) {
                        Defect defect = new Defect();
                        defect.setTitle("No Export Ciphers : " + noExportCiphers);
                        defect.setSeverity(Severity.CRITICAL);
                        defect.setExplanation("No Export Ciphers : " + noExportCiphers);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (noNULLCiphersPattern.matcher(strLine).find()) {
                    noNULLCiphers = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(noNULLCiphers) &&  !"no (no reply)".equalsIgnoreCase(noNULLCiphers)) {
                        Defect defect = new Defect();
                        defect.setTitle("No NULL Ciphers " + noNULLCiphers);
                        defect.setSeverity(Severity.CRITICAL);
                        defect.setExplanation("No NULL Ciphers : " + noNULLCiphers);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (PCICompliantPattern.matcher(strLine).find()) {
                    PCICompliant = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(PCICompliant) &&  !"no (no reply)".equalsIgnoreCase(PCICompliant)) {
                        Defect defect = new Defect();
                        defect.setTitle("PCI Compliant : " + PCICompliant);
                        defect.setSeverity(Severity.INFO);
                        defect.setExplanation("PCI Compliant " + PCICompliant);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                else if (noSSLV3Pattern.matcher(strLine).find()) {
                    noSSLV3 = strLine.substring(strLine.indexOf("\t"), strLine.length()).trim();
                    if (!"yes".equalsIgnoreCase(noSSLV3) &&  !"no (no reply)".equalsIgnoreCase(noSSLV3)) {
                        Defect defect = new Defect();
                        defect.setTitle("No SSLv3 : " + noSSLV3);
                        defect.setSeverity(Severity.HIGH);
                        defect.setExplanation("No SSLv3 : " + noSSLV3);
                        defect.setHostName(hostName);
                        defect.setNeedManuelCheck(Boolean.FALSE);
                        defect.setSourceIP(sourceIP);
                        scan.addDefect(defect);
                    }
                }
                // Read Supported SSL Algorithms to find if there is a vulnerable one
                else if (weakYes.matcher(strLine).find()) {
                    Defect defect = new Defect();
                    defect.setTitle("Supported cipher is weak : " + strLine);
                    defect.setSeverity(Severity.HIGH);
                    defect.setExplanation("Supported cipher is weak : " + strLine);
                    defect.setHostName(hostName);
                    defect.setNeedManuelCheck(Boolean.FALSE);
                    defect.setSourceIP(sourceIP);
                    scan.addDefect(defect);
                }
                else if (mediumYes.matcher(strLine).find()) {
                    Defect defect = new Defect();
                    defect.setTitle("Supported cipher is not recommended : " + strLine);
                    defect.setSeverity(Severity.MEDIUM);
                    defect.setExplanation("Supported cipher is not recommended : " + strLine);
                    defect.setHostName(hostName);
                    defect.setNeedManuelCheck(Boolean.FALSE);
                    defect.setSourceIP(sourceIP);
                    scan.addDefect(defect);
                }

                //System.out.println("NEW O-SAFT SCAN COMPLETED! Defect count : " + scan.getDefects() != null ? scan.getDefects().size() : "0");
            }
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

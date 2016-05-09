package tr.com.turktelecom.lighthouse.domain.service.collector;

import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
/**
 * Created by 010235 on 03.05.2016.
 */
public interface PluginResultCollector {
    Scan collectResults(Plugin plugin);
    //T:\Swf\Projects\Lighthouse\curl-7.48.0-win64-mingw\bin>curl.exe -k -X POST -H "Content-Type: application/json" -d "{\"username\":\"emre.kisa\",\"password\":\"Ankara132\"}" https://10.4.49.226:8834/session
}

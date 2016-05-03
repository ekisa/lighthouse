package tr.com.turktelecom.lighthouse.domain.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.ExecutablePluginContext;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.service.util.DateTimeUtil;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 010235 on 29.04.2016.
 */
@Component
public class PluginRunnerExecutableImpl extends AbstractPluginRunnerImpl {

    @Override
    protected void runInternal(Plugin plugin) throws PluginContextNotSupportedException, PluginRunFailedException{
        ExecutablePluginContext executablePluginContext = this.findPluginContext(plugin);
        ZonedDateTime pluginStartTimestamp = ZonedDateTime.now();
        File errorLogFile = this.createLogFile("error", plugin, pluginStartTimestamp);
        File outputLogFile = this.createLogFile("output", plugin, pluginStartTimestamp);
        try {
            this.logToFile(outputLogFile, "\nRun started at " + DateTimeUtil.formatTimeStamp(pluginStartTimestamp) + "\n");

            List<String> args = new ArrayList<String>();
            args.add(executablePluginContext.getCommand());
            args.add(executablePluginContext.getExecutableName());
            this.addOptionalArgs(plugin, args);

            ProcessBuilder processBuilder = new ProcessBuilder(args.toArray(new String[0]));
            this.setProcessPath(executablePluginContext, processBuilder);
            processBuilder.directory(super.findWorkingDirectory(plugin));
            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(errorLogFile));
            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(outputLogFile));
            Process process = processBuilder.start();
            int returnVal = process.waitFor();

            this.logToFile(outputLogFile, "\n Run completed successfully at " + DateTimeUtil.formatTimeStamp(ZonedDateTime.now()) + "\n");
            errorLogFile.delete();
        } catch (IOException e) {
            try {
                e.printStackTrace();
                logToFile(errorLogFile, "\nPlugin run failed at " + DateTimeUtil.formatTimeStamp(ZonedDateTime.now()) + ". Execution started at : " + DateTimeUtil.formatTimeStamp(pluginStartTimestamp) + ".\n" +
                    ExceptionUtils.getFullStackTrace(e));
                throw new PluginRunFailedException("Plugin run edilmek istediği sırada hata olustu", e);
            } catch (IOException e1) {
                e1.printStackTrace();
                throw new PluginRunFailedException("Plugin run edilmek istediği sırada hata olustu", e);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            throw new PluginRunFailedException("Plugin run edilmek istediği sırada hata olustu", e);
        }
    }

    private void setProcessPath(ExecutablePluginContext executablePluginContext, ProcessBuilder processBuilder) {
        String executableHomeDir = environment.getProperty("pluginRunner." + executablePluginContext.getCommand() +".home");
        String oldPath = processBuilder.environment().get("Path");
        String newPath = null;
        if(oldPath.contains(executableHomeDir)) {
            newPath = oldPath;
        }else{
            newPath = ((String) oldPath) + ";" + executableHomeDir;
        }
        processBuilder.environment().put("Path", newPath);
    }

    private void addOptionalArgs(Plugin plugin, List<String> args) {
        Iterator<Map.Entry<String, String>> iterator = plugin.getArgs().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if(StringUtils.isNotEmpty(entry.getKey())){
                args.add(entry.getKey());
            }
            if(StringUtils.isNotEmpty(entry.getValue())){
                args.add(entry.getValue());
            }
        }
    }

    private void logToFile(File outputLogFile, String text) throws IOException {
        FileWriter fileWriter = new FileWriter(outputLogFile, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        printWriter.append(text);
        printWriter.close();
        bufferedWriter.close();
        fileWriter.close();
    }

    private File createLogFile(String type, Plugin plugin, ZonedDateTime timeStamp) {
        return new File(findBaseDirectoryURI()
            + File.separator + "plugins"
            + File.separator + plugin.getFolderName()
            + File.separator + "processLogs"
            + File.separator + "process- "+ type +" -log-" + DateTimeUtil.formatTimeStamp(timeStamp) + ".txt");
    }



    private ExecutablePluginContext findPluginContext(Plugin plugin) throws PluginContextNotSupportedException {
        if (!(plugin.getPluginContext() instanceof ExecutablePluginContext)) {
            throw new PluginContextNotSupportedException("This plugin runner supports only the commands that can be run from console. Ex: \"python\", \"dir\",\"ls\" etc.");
        }
        return (ExecutablePluginContext) plugin.getPluginContext();
    }

}

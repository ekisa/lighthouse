package tr.com.turktelecom.lighthouse.domain.service.runner;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.ExecutablePluginContext;
import tr.com.turktelecom.lighthouse.domain.PluginArgument;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.service.util.DateTimeUtil;

import java.io.*;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Created by 010235 on 29.04.2016.
 */
@Component
public class PluginRunnerExecutableImpl extends AbstractPluginRunner {

    @Override
    protected void runInternal(Plugin plugin, ZonedDateTime pluginStartTimestamp) throws PluginContextNotSupportedException, PluginRunFailedException{
        ExecutablePluginContext executablePluginContext = this.findPluginContext(plugin);
        File errorLogFile = this.createLogFile("error", plugin, pluginStartTimestamp);
        File outputLogFile = this.createLogFile("output", plugin, pluginStartTimestamp);
        try {
            this.logToFile(outputLogFile, "\nRun started at " + DateTimeUtil.formatTimeStamp(pluginStartTimestamp, DateTimeUtil.PATTERN.DATE_TIME_PATTERN) + "\n");

            List<String> args = new ArrayList<String>();
            String command = executablePluginContext.getCommand();
            if ("bash".equals(command)) {
                args.add("/bin/bash");
            }else {
                args.add(command);
            }

            String executableName = executablePluginContext.getExecutableName();
            if (StringUtils.isNotEmpty(executableName)) {
                args.add(executableName);
            }

            this.addOptionalArgs(plugin, args);

            ProcessBuilder processBuilder = new ProcessBuilder(args.toArray(new String[0]));
            this.setProcessPath(executablePluginContext, processBuilder);
            processBuilder.directory(this.findWorkingDirectory(plugin));
            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(errorLogFile));
            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(outputLogFile));
            Process process = processBuilder.start();
            int returnVal = process.waitFor();

            this.logToFile(outputLogFile, "\nRun completed successfully at " + DateTimeUtil.formatTimeStamp(ZonedDateTime.now(), DateTimeUtil.PATTERN.DATE_TIME_PATTERN) + "\n");
            errorLogFile.delete();
        } catch (IOException e) {
            try {
                e.printStackTrace();
                logToFile(errorLogFile, "\nPlugin run failed at " + DateTimeUtil.formatTimeStamp(ZonedDateTime.now(), DateTimeUtil.PATTERN.DATE_TIME_PATTERN) + ". Execution started at : " + DateTimeUtil.formatTimeStamp(pluginStartTimestamp, DateTimeUtil.PATTERN.DATE_TIME_PATTERN) + ".\n" +
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
        String oldPath = Optional.ofNullable(processBuilder.environment().get("PATH")).orElse(processBuilder.environment().get("Path"));
        String newPath = null;
        if(oldPath.contains(executableHomeDir)) {
            newPath = oldPath;
        }else{
            newPath = ((String) oldPath) + ";" + executableHomeDir;
        }
        final String finalNewPath = newPath;
        Optional.ofNullable(processBuilder.environment().get("PATH")).map(p -> {
            return processBuilder.environment().put("PATH", finalNewPath);
        }).orElse(processBuilder.environment().put("Path", finalNewPath));

        //processBuilder.environment().put("Path", newPath);
        //processBuilder.environment().put("PATH", newPath);
    }

    private void addOptionalArgs(Plugin plugin, List<String> args) {
        Iterator<PluginArgument> iterator = plugin.getArgs().iterator();
        while(iterator.hasNext()) {
            PluginArgument pluginArgument = iterator.next();
            if(StringUtils.isNotEmpty(pluginArgument.getArg())){
                args.add(pluginArgument.getArg());
            }
            if(StringUtils.isNotEmpty(pluginArgument.getValue())){
                args.add(pluginArgument.getValue());
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

    private File createLogFile(String type, Plugin plugin, ZonedDateTime timeStamp) throws PluginRunFailedException {
        return new File(findBaseDirectoryURI(((ExecutablePluginContext) plugin.getPluginContext()).getCommand())
            + File.separator + "plugins"
            + File.separator + plugin.getFolderName()
            + File.separator + "processLogs"
            + File.separator + "process-" + type + "-log-" + DateTimeUtil.formatTimeStamp(timeStamp, DateTimeUtil.PATTERN.DATE_TIME_PATTERN_FOR_FILE_NAMES) + ".txt");
    }



    private ExecutablePluginContext findPluginContext(Plugin plugin) throws PluginContextNotSupportedException {
        if (!(plugin.getPluginContext() instanceof ExecutablePluginContext)) {
            throw new PluginContextNotSupportedException("This plugin runner supports only the commands that can be run from console. Ex: \"python\", \"dir\",\"ls\" etc.");
        }
        return (ExecutablePluginContext) plugin.getPluginContext();
    }

    protected String findBaseDirectoryURI(String command) throws PluginRunFailedException {
        String baseDirectoryURI = "";
        if (StringUtils.isEmpty(command)) {
            throw new PluginRunFailedException("Executable context command name bos birakilamaz");
        }
        if (StringUtils.isEmpty(environment.getProperty("pluginRunner." + command + ".baseDirectoryURI"))) {
            baseDirectoryURI = Paths.get(".").toAbsolutePath().normalize().toString();
        }
        else {
            baseDirectoryURI = environment.getProperty("pluginRunner." + command + ".baseDirectoryURI");
        }
        return baseDirectoryURI;
    }

    protected File findWorkingDirectory(Plugin plugin) throws PluginRunFailedException {
        return new File(this.findBaseDirectoryURI(((ExecutablePluginContext) plugin.getPluginContext()).getCommand())
            + File.separator + "plugins"
            + File.separator + plugin.getFolderName());
    }
}

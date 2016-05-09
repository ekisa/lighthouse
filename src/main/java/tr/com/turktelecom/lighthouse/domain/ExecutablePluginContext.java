package tr.com.turktelecom.lighthouse.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A PluginContext for Python Executables
 */
@Entity
@DiscriminatorValue("EXECUTABLE")
public class ExecutablePluginContext extends PluginContext implements Serializable {

    @Pattern(regexp = "[a-zA-Z0-9-_.]*")
    @Column(name = "command")
    @Length(max = 20)
    private String command;

    @Column(name = "executable_name")
    @Pattern(regexp = "[a-zA-Z0-9-_.]*")
    @Length(max = 20)
    private String executableName;

    @Column(name = "input_file_path")
    private String inputFilePath;

    @Column(name = "output_file_path")
    private String outputFilePath;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getExecutableName() {
        return executableName;
    }

    public void setExecutableName(String executableName) {
        this.executableName = executableName;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public String toString() {
        return "ExecutablePluginContext{" +
            "command='" + command + '\'' +
            ", executableName='" + executableName + '\'' +
            ", inputFilePath='" + inputFilePath + '\'' +
            ", outputFilePath='" + outputFilePath + '\'' +
            '}';
    }
}

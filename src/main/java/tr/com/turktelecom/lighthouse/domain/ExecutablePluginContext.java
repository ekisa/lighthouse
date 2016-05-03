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

    @Override
    public String toString() {
        return "ExecutablePluginContext{" +
            "command='" + command + '\'' +
            ", executableName='" + executableName + '\'' +
            '}';
    }
}

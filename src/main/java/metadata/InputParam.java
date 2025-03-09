package metadata;

public class InputParam extends AbstractData {
    private String projectName;
    private String input;
    private String output;

    public InputParam(String projectName, String input, String output) {
        this.projectName = projectName;
        this.input = input;
        this.output = output;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public InputParam getEncoding() {
        return new InputParam(
                encodingString(this.projectName),
                encodingString(this.input),
                encodingString(this.output));
    }
}


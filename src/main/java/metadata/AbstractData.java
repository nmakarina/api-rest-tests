package metadata;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AbstractData {
    protected String encodingString(String input) {
        byte[] bytes = input.getBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }

    protected String[] encodingString(String[] input) {
        List<String> output = new ArrayList();

        for (String str : input) {
            byte[] bytes = str.getBytes();
            output.add(new String(bytes, StandardCharsets.UTF_8));
        }
        return (String[]) output.toArray();
    }
}

package reports;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class ReportGenerator {
    public abstract boolean generate(List<LinkedHashMap<String, Object>> documents);
}

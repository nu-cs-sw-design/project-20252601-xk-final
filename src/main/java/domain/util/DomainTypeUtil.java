package domain.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DomainTypeUtil {

    public static final Set<String> PRIMITIVE_TYPE_NAMES = Set.of("boolean", "char", "byte", "short", "int", "float", "long", "double");

    public static List<String> splitIntoSignificantTypes(String name) {

        name = name.replace("[]", "");

        int typeParamStart = name.indexOf('<');

        List<String> results = new ArrayList<>();

        if (typeParamStart < 0) {
            if (!PRIMITIVE_TYPE_NAMES.contains(name))
                results.add(name);
            return results;
        }

        results.add(name.substring(0, typeParamStart));

        String[] typeParams =
                name.substring(typeParamStart + 1, name.indexOf('>'))
                        .replace(" ", "")
                        .split(",");

        for (String typeParam : typeParams) {
            results.addAll(splitIntoSignificantTypes(typeParam));
        }

        return List.copyOf(results);

    }

}

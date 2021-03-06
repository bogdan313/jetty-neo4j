package helpers;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for parsing parameters from HttpServletRequest provided by Servlet to Map
 */
public class ParseParametersHelper {
    private static final String NO_PARAMETERS = "^(?<parentParameter>\\w+)$";
    private static final String ONE_PARAMETER = "^(?<parentParameter>\\w+)\\[(?<parameterOne>\\w+)\\]$";
    private static final String TWO_PARAMETERS = "^(?<parentParameter>\\w+)\\[(?<parameterOne>\\w+)\\]\\[(?<parameterTwo>\\w+)\\]$";
    private static final String THREE_PARAMETERS = "^(?<parentParameter>\\w+)\\[(?<parameterOne>\\w+)\\]\\[(?<parameterTwo>\\w+)\\]\\[(?<parameterThree>\\w+)\\]$";

    /**
     * Converts parameters, which looks like "array[index_one][index_two][index_three]" to Map which contain every key
     * @param parameters from {@link HttpServletRequest#getParameterMap()}
     * @return Map with parameters
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parse(Map<String, String[]> parameters) {
        if (parameters == null) throw new IllegalArgumentException("parameters must be specified");

        Map<String, Object> result = new LinkedHashMap<>();
        Pattern noParametersPattern = Pattern.compile(ParseParametersHelper.NO_PARAMETERS);
        Pattern oneParameterPattern = Pattern.compile(ParseParametersHelper.ONE_PARAMETER);
        Pattern twoParametersPattern = Pattern.compile(ParseParametersHelper.TWO_PARAMETERS);
        Pattern threeParametersPattern = Pattern.compile(ParseParametersHelper.THREE_PARAMETERS);

        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()[0];

            Matcher matcher = noParametersPattern.matcher(key);
            while (matcher.find()) {
                String parentParameter = matcher.group("parentParameter");
                if (!result.containsKey(parentParameter))
                    result.put(parentParameter, value);
            }

            matcher = oneParameterPattern.matcher(key);
            while (matcher.find()) {
                String parentKey = matcher.group("parentParameter");
                String parameterOne = matcher.group("parameterOne");

                if (!result.containsKey(parentKey))
                    result.put(parentKey, new LinkedHashMap<>());

                if (result.get(parentKey) instanceof Map) {
                    Map<String, String> parentParameterMap = (Map<String, String>) result.get(parentKey);
                    parentParameterMap.put(parameterOne, value);
                }
            }

            matcher = twoParametersPattern.matcher(key);
            while (matcher.find()) {
                String parentParameter = matcher.group("parentParameter");
                String parameterOne = matcher.group("parameterOne");
                String parameterTwo = matcher.group("parameterTwo");

                if(!result.containsKey(parentParameter))
                    result.put(parentParameter, new LinkedHashMap<>());

                if (result.get(parentParameter) instanceof Map) {
                    Map<String, Map<String, String>> parentParameterMap =
                            (Map<String, Map<String, String>>) result.get(parentParameter);

                    if (!parentParameterMap.containsKey(parameterOne))
                        parentParameterMap.put(parameterOne, new LinkedHashMap<>());

                    Map<String, String> parameterOneMap = parentParameterMap.get(parameterOne);
                    parameterOneMap.put(parameterTwo, value);
                }
            }

            matcher = threeParametersPattern.matcher(key);
            while (matcher.find()) {
                String parentParameter = matcher.group("parentParameter");
                String parameterOne = matcher.group("parameterOne");
                String parameterTwo = matcher.group("parameterTwo");
                String parameterThree = matcher.group("parameterThree");

                if (!result.containsKey(parentParameter))
                    result.put(parentParameter, new LinkedHashMap<>());

                if (result.get(parentParameter) instanceof Map) {
                    Map<String, Object> parentParameterMap =
                            (Map<String, Object>) result.get(parentParameter);

                    if (!parentParameterMap.containsKey(parameterOne))
                        parentParameterMap.put(parameterOne, new LinkedHashMap<>());

                    if (parentParameterMap.get(parameterOne) instanceof Map) {
                        Map<String, Object> parameterOneMap =
                                (Map<String, Object>) parentParameterMap.get(parameterOne);

                        if (!parameterOneMap.containsKey(parameterTwo))
                            parameterOneMap.put(parameterTwo, new LinkedHashMap<>());

                        if (parameterOneMap.get(parameterTwo) instanceof Map) {
                            Map<String, String> parameterTwoMap =
                                    (Map<String, String>) parameterOneMap.get(parameterTwo);
                            parameterTwoMap.put(parameterThree, value);
                        }
                    }
                }
            }
        }
        return result;
    }
}
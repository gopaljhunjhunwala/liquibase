package liquibase.action;

import liquibase.ExtensibleObject;
import liquibase.Scope;
import liquibase.util.CollectionUtil;

import java.util.*;

public class TestObjectFactory {

    public <T extends ExtensibleObject> List<T> createAllPermutations(Class<T> type, Map<String, List<Object>> defaultValues) throws Exception {
        T typeObj = type.newInstance();

        Set<Set<String>> parameterSets = CollectionUtil.powerSet(typeObj.getStandardAttributeNames());

        List<T> returnList = new ArrayList<>();
        for (Collection<String> paramsToSet : parameterSets) {
            Map<String, List<Object>> parameterValues = new HashMap<>();
            for (String param : paramsToSet) {
                def values = getTestValues(param, defaultValues)
                if (values != null && values.size() > 0) {
                    parameterValues.put(param, new ArrayList(values));
                }

            }

            for (Map<String, ?> valuePermutation : CollectionUtil.permutations(parameterValues)) {
                T obj = type.newInstance();
                for (Map.Entry<String, ?> entry : valuePermutation.entrySet()) {
                    obj.set(entry.getKey(), entry.getValue());
                }
                returnList.add(obj);
            }
        }

        return returnList;
    }

    private List<Object> getTestValues(String key, Map<String, List<Object>> defaultValues) {
        if (defaultValues.containsKey(key)) {
            return defaultValues.get(key);
        }

        throw new RuntimeException("No test value set for "+key)
    }
}
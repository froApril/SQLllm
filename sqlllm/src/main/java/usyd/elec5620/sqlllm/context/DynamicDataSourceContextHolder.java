package usyd.elec5620.sqlllm.context;

import usyd.elec5620.sqlllm.constants.DataSourceConstants;

public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> DATASOURCE_CONTEXT_KEY_HOLDER = new ThreadLocal<>();

    public static void setContextKey(String key) {
        DATASOURCE_CONTEXT_KEY_HOLDER.set(key);
    }

    public static String getContextKey(){
        String key = DATASOURCE_CONTEXT_KEY_HOLDER.get();
        return key == null? DataSourceConstants.DS_KEY_DEFAULT:key;
    }

    public static void removeContextKey(){
        DATASOURCE_CONTEXT_KEY_HOLDER.remove();
    }
}

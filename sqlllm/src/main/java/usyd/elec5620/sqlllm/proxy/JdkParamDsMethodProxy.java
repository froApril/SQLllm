package usyd.elec5620.sqlllm.proxy;

import usyd.elec5620.sqlllm.context.DynamicDataSourceContextHolder;
import usyd.elec5620.sqlllm.util.DataSourceUtil;
import usyd.elec5620.sqlllm.vo.DbInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkParamDsMethodProxy implements InvocationHandler {

    private String dataSourceKey;

    private DbInfo dbInfo;

    private Object targetObject;

    public JdkParamDsMethodProxy(Object targetObject, String dataSourceKey, DbInfo dbInfo) {
        this.targetObject = targetObject;
        this.dataSourceKey = dataSourceKey;
        this.dbInfo = dbInfo;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DataSourceUtil.addDataSourceToDynamic(dataSourceKey, dbInfo);
        DynamicDataSourceContextHolder.setContextKey(dataSourceKey);

        Object result = method.invoke(targetObject, args);
        DynamicDataSourceContextHolder.removeContextKey();
        return result;
    }

    public static Object createProxyInstance(Object targetObject, String dataSourceKey, DbInfo dbInfo) throws Exception {
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces()
        , new JdkParamDsMethodProxy(targetObject, dataSourceKey, dbInfo));
    }
}

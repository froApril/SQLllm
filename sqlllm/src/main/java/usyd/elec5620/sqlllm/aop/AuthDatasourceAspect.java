package usyd.elec5620.sqlllm.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import usyd.elec5620.sqlllm.config.DynamicDataSourceConfig;
import usyd.elec5620.sqlllm.vo.DbStatus;

@Aspect
@Component
public class AuthDatasourceAspect {

//    private DbStatus beforeStates;
//
//    @Pointcut("execution(* usyd.elec5620.sqlllm.service.authdb.AuthDbService.*(..))")
//    public void databaseSwitch() {
//    }
//
//    @Before("databaseSwitch()")
//    public void doBefore(){
//        System.out.println("before");
//        beforeStates = DynamicDataSourceConfig.DATASOURCE_STATUS;
//        DynamicDataSourceConfig.DATASOURCE_STATUS = DbStatus.USER_DATABASE;
//    }
//
//    @After("databaseSwitch()")
//    public void doAfter(){
//        System.out.println("after");
//        DynamicDataSourceConfig.DATASOURCE_STATUS = beforeStates;
//    }
}

package demo.rpc.client;

import demo.rpc.client.discovery.ZkDiscovery;
import demo.rpc.common.annotation.RpcAutowire;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import demo.rpc.client.reflect.RpcProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Component
public class RpcClient implements ApplicationContextAware, DisposableBean {


    private final ZkDiscovery zkDiscovery;

    public RpcClient() {
        zkDiscovery = ZkDiscovery.getInstance();
    }

    public RpcClient(ZkDiscovery zkDiscovery) {
        this.zkDiscovery = zkDiscovery;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        String[] beanNames = ctx.getBeanDefinitionNames();

        //查找所有bean,找出字段中定义有注解的
        for (String beanName : beanNames) {
            //这里简单处理循环依赖的问题。
            if (beanName.equals("rpcClient")) continue;
            Object bean = ctx.getBean(beanName);
            Field[] declaredFields = bean.getClass().getDeclaredFields();
            try {
                for (Field field : declaredFields) {
                    RpcAutowire rpcAutowire = field.getAnnotation(RpcAutowire.class);
                    if (rpcAutowire != null) {
                        String version = rpcAutowire.version();
                        field.setAccessible(true);
                        //将被RpcAutowire注解标注的字段值设置成代理对象
                        log.info("获取的类别是:{}", field.getType());
                        field.set(bean, RpcProxy.createServiceProxy(field.getType(), version));

                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void destroy() throws Exception {

    }


}

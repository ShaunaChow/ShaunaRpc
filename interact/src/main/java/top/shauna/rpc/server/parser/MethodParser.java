package top.shauna.rpc.server.parser;

import top.shauna.rpc.bean.RequestBean;
import top.shauna.rpc.bean.ResponseBean;
import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.holder.MethodsHolder;
import top.shauna.rpc.type.ResponseEnum;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @Author   Shauna.Chou
 * @E-Mail   z1023778132@icloud.com
 */

public class MethodParser {

    private MethodParser(){}

    private static volatile MethodParser methodParser;

    public static MethodParser getMethodParser(){
        if(methodParser==null){
            synchronized (MethodParser.class){
                if(methodParser==null){
                    methodParser = new MethodParser();
                }
            }
        }
        return methodParser;
    }

    public ResponseBean getResponse(RequestBean request){
        String clazzName = request.getClazzName();
        ServiceBean<?> serviceBean = MethodsHolder.getServiceBean(clazzName);
        if(serviceBean==null) return new ResponseBean(ResponseEnum.NO_SUCH_CLASS,null);

        Method method = serviceBean.getMethods().get(request.getMethod());
        if(method==null) return new ResponseBean(ResponseEnum.NO_SUCH_METHOD,null);

        Parameter[] parameters = method.getParameters();
        List<Object> values = request.getValues();
        if(values.size()!=parameters.length) return new ResponseBean(ResponseEnum.MISSING_PARAMS,null);

        Object[] args = new Object[values.size()];
        Object res;
        try {
            for (int i = 0; i < parameters.length; i++) {
                args[i] = values.get(i);
            }
            res = method.invoke(serviceBean.getInterfaceImpl(), args);
        }catch (Exception e){
            return new ResponseBean(ResponseEnum.PARAM_ERROR,e.getMessage());
        }
        return new ResponseBean(ResponseEnum.SUCCESS,res);
    }
}

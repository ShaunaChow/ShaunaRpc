package top.shauna.rpc.server;

import top.shauna.rpc.bean.ServiceBean;
import top.shauna.rpc.interfaces.LocalExporter;

public class ExportFactory {

    public static LocalExporter getExporter(ServiceBean<?> serviceBean) throws Exception {
        String loc = serviceBean.getLocalExportBean().getServerClassLoc();
        if(loc!=null){
            Class clazz = Class.forName(loc);
            Object exporter = clazz.newInstance();
            if(exporter instanceof LocalExporter){
                return (LocalExporter) exporter;
            }else throw new Exception("Exporter指定的类必须实现LocalExporter接口!!!");
        }

        String protocal = serviceBean.getLocalExportBean().getProtocal();
        if(protocal==null) protocal = "netty";
        String className = "top.shauna.rpc.server.exporter."
                +protocal.substring(0,1).toUpperCase()
                +protocal.substring(1)+"Exporter";
        Class clazz = Class.forName(className);
        Object obj = clazz.newInstance();
        if(obj instanceof LocalExporter){
            return (LocalExporter) obj;
        }else throw new Exception("指定的类必须实现Register接口!!!");
    }
}

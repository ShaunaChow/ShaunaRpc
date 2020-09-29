package top.shauna.rpc.protocol.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import top.shauna.rpc.protocol.interfaze.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author Shauna.Chou
 * @Date 2020/9/29 9:48
 * @E-Mail z1023778132@icloud.com
 */
public class HessianSerializer implements Serializer {
    @Override
    public byte[] getData(Object obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(os);
        try {
            output.writeObject(obj);
            output.flush();
            return os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                output.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getObj(byte[] data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        Hessian2Input input = new Hessian2Input(is);
        try {
            Object obj = input.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                input.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

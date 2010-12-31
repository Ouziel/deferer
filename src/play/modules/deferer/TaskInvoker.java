package play.modules.deferer;

import java.lang.reflect.Method;

public interface TaskInvoker {
  
  public Object findModelById(String className, Long id) throws Exception;
  public Method findMethod(String fullClassName, String methodName, Class[] paramTypes) throws Exception;
  
}
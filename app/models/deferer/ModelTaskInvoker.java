package models.deferer;

import java.lang.reflect.Method;
import play.modules.deferer.Deferer;
import play.modules.deferer.TaskInvoker;
import play.modules.deferer.Task;
import play.modules.deferer.GAETask;
import play.modules.deferer.MockTask;
import play.modules.deferer.PlayTask;
import play.Play;

public class ModelTaskInvoker implements TaskInvoker {
  
  public static String classPrefix = "models.";
  public static String findByIdMethodName = "findById";
  
  public Object findModelById(String className, Long id) throws Exception {
    if (id > 0) {
      Object[] findByIdArgs = new Object[1];
      findByIdArgs[0] = id;
      Method findByIdMethod = findMethod(className, findByIdMethodName, Long.class);
      return findByIdMethod.invoke(null, findByIdArgs);
    } else {
      return null;
    }    
  }
  
  public Method findMethod(String className, String methodName, Class[] paramTypes) throws Exception {
    return Class.forName(classPrefix+className).getMethod(methodName, paramTypes);
  }
  
  public Method findMethod(String className, String methodName, Class paramType) throws Exception {
    Class[] paramTypes = new Class[1];
    paramTypes[0] = paramType;
    return findMethod(className, methodName, paramTypes);
  }
  
  public static void defer(String queueName, String className, String methodName, Long objectId, Object... params) {
    String taskClass = Play.configuration.getProperty("application.deferer.task");
    Task task;
    if ("GAETask".equals(taskClass)) {
      task = new GAETask(queueName, className, methodName);
    } else if ("MockTask".equals(taskClass)) {
      task = new MockTask(queueName, className, methodName, new ModelTaskInvoker());
    } else {
      task = new PlayTask(queueName, className, methodName, new ModelTaskInvoker());
    }    
    Deferer.defer(task, objectId, params);
  }
  
  public static void deferStatic(String queueName, String className, String methodName, Object... params) {
    defer(queueName, className, methodName, null, params);
  }
      
}
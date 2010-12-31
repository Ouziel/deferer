package play.modules.deferer;

import play.mvc.Scope;
import java.lang.reflect.Method;

public class Deferer {  
  
  public static void deferStatic(Task task, Object... params) {
    defer(task, null, params);
  }
  
  public static void defer(Task task, Long objectId, Object... params) {        
    if (objectId!=null) {
      task.param("objectId", objectId.toString());
    } else {
      task.param("objectId", "0");
    }    
    if (params!=null) {
      task.param("paramsCount", (new Integer(params.length)).toString());
      for(int i=0;i<params.length;++i) {
        task.param("paramsClass_"+i, params[i].getClass().getName());
        task.param("paramsValue_"+i, params[i].toString());
      }
    } else {
      task.param("paramsCount", "0");
    }    
    task.addToQueue();        
  }
  
  public static void doTask(String className, String methodName, Scope.Params params, TaskInvoker invoker) {
    System.out.println("doTask:"+className+"."+methodName);
    
    Integer paramsCount = params.get("paramsCount", Integer.class);
    Long objectId = params.get("objectId", Long.class);            
    Object[] args = null;
    Class[] paramTypes = null;      
    paramsCount = paramsCount==null?0:paramsCount;
    objectId = objectId==null?0:objectId;
    
    try {
      if (paramsCount>0) {
        paramTypes = new Class[paramsCount];
        args = new Object[paramsCount];        
        for (int p=0; p<paramsCount; p++) {
          paramTypes[p] = Class.forName(params.get("paramsClass_"+p, String.class));
          args[p] = params.get("paramsValue_"+p, paramTypes[p]);                    
        }
      }      
      invokeTaskMethod(className, methodName, paramTypes, args, objectId, invoker);  
                 
    } catch(Exception e) {
      System.out.println(e.getClass().getName()+":"+e.getMessage());
    }      
  }
  
  public static void invokeTaskMethod(String className, String methodName, Class[] paramTypes, Object[] args, Long objectId, TaskInvoker invoker) throws Exception  {
    Method taskMethod = invoker.findMethod(className, methodName, paramTypes);    
    Object taskObject = invoker.findModelById(className, objectId);
    taskMethod.invoke(taskObject, args);
  }
  
  
}
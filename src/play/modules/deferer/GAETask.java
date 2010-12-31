package play.modules.deferer;

import java.util.HashMap;

import play.mvc.Router;
import play.mvc.Scope;
import play.*;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.TaskOptions.*;
import java.lang.reflect.Method;

  
public class GAETask implements Task {
  
  String methodName;
  String className;
  Queue queue;
  TaskOptions taskOptions;
  
  public GAETask(String queueName, String className, String methodName) {
    this.className = className;
    this.methodName = methodName;
    HashMap map = new HashMap();
    map.put("className", this.className);
    map.put("methodName", this.methodName);
    String taskUrl = Router.reverse("deferer.GAETasks.doTask", map).url;           
    queue = QueueFactory.getQueue(queueName);
    taskOptions = Builder.url(taskUrl);
  }
  
  public void param(String name, String value) {    
    taskOptions.param(name, value);
  }
  
  public void addToQueue() {
    queue.add(taskOptions);
  }
}

  
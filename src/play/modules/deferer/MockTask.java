package play.modules.deferer;

import play.mvc.Scope;
  
public class MockTask implements Task {
  
  String methodName;
  String className;  
  Scope.Params parameters;
  TaskInvoker invoker;
  
  public MockTask(String queueName, String className, String methodName, TaskInvoker invoker) {
    this.className = className;
    this.methodName = methodName;
    this.invoker = invoker;
    parameters = new Scope.Params();
  }
  
  public void param(String name, String value) {
    parameters.put(name, value);
  }
  
  public void addToQueue() {
    Deferer.doTask(this.className, this.methodName, parameters, this.invoker);
  }
  
}

  
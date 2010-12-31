package controllers.deferer;

import play.mvc.Controller;
import play.modules.deferer.Deferer;
import models.deferer.ModelTaskInvoker;


public class GAETasks extends Controller {
               
    public static void doTask(String className, String methodName) {
      Deferer.doTask(className, methodName, params, new ModelTaskInvoker());
    }

}
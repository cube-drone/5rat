package jonahss.myfirstproject;

import com.android.uiautomator.core.UiSelector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * For parsing strings passed in for the "-android uiautomator" locator strategy
 * Created by jonahss on 3/28/14.
 */
public class UiSelectorParser {

  private String text;
  private UiSelector selector;
  private Method[] methods;

  public UiSelectorParser(String text) throws UiSelectorSyntaxException {
    methods = UiSelector.class.getDeclaredMethods();
    text.trim();

    this.text = text;
    if (text.startsWith("new UiSelector()")) {
      this.text = this.text.substring(16);
    }
    else if (text.startsWith("UiSelector()")) {
      this.text = this.text.substring(12);
    }
    else {
      throw new UiSelectorSyntaxException("UiSelector param must start with \"new UiSelector()\". Param: \"" + text + "\"");
    }

    this.selector = new UiSelector();
  }

  public UiSelector parse() throws UiSelectorSyntaxException {
    while (this.text.length() > 0) {
      consumePeriod();
      consumeFunctionCall();
    }

    return this.selector;
  }

  private void consumePeriod() throws UiSelectorSyntaxException {
    if (this.text.startsWith(".")) {
      this.text = this.text.substring(1);
    }
    else {
      throw new UiSelectorSyntaxException("Expected \".\" but saw \"" + this.text.charAt(0) + "\"");
    }
  }

  /*
   * consume [a-z]* then an open paren, this is our methodName
   * consume .* and count open/close parens until the original open paren is close, this is our argument
   *
   */
  private void consumeFunctionCall() throws UiSelectorSyntaxException {
    String methodName;
    StringBuilder argument = new StringBuilder();

    int parenIndex = this.text.indexOf('(');
    methodName = this.text.substring(0, parenIndex-1);

    int index = parenIndex+1;
    int parenCount = 1;
    while (parenCount > 0) {
      try {
        switch (this.text.charAt(index)) {
          case ')':
            parenCount--;
            if (parenCount > 0) {
              argument.append(this.text.charAt(index));
            }
            break;
          case '(':
            parenCount++;
            argument.append(this.text.charAt(index));
            break;
          default:
            argument.append(this.text.charAt(index));
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new UiSelectorSyntaxException("unclosed paren in expression");
      }
    }
    if (argument.length() < 1) {
      throw new UiSelectorSyntaxException(methodName + " method expects an argument");
    }

    this.text = this.text.substring(methodName.length() + argument.length() + 2);

    ArrayList<Method> overloadedMethods = getSelectorMethods(methodName);
    if (overloadedMethods.size() < 1) {
      throw new UiSelectorSyntaxException("UiSelector has no " + methodName + " method");
    }
    this.selector = applyArgToMethods(overloadedMethods, argument.toString());
  }

  private ArrayList<Method> getSelectorMethods(String methodName) {
    ArrayList<Method> ret = new ArrayList<Method>();
    for (Method method : this.methods) {
      if (method.getName() == methodName) {
        ret.add(method);
      }
    }
    return ret;
  }

  private UiSelector applyArgToMethods(ArrayList<Method> methods, String argument) throws UiSelectorSyntaxException {

    Object arg = null;
    Method ourMethod = null;
    for (Method method : methods) {
      try {
        Type parameterType = method.getGenericParameterTypes()[0];
        arg = coerceArgToType(parameterType, argument);
        ourMethod = method;
      } catch (IllegalArgumentException e) {
        continue;
      }
    }

    if (ourMethod == null || arg == null) {
      throw new UiSelectorSyntaxException("Could not apply argument " + argument + " to UiSelector method");
    }

    try {
      return (UiSelector)ourMethod.invoke(this.selector, arg);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      throw new UiSelectorSyntaxException("problem using reflection to call this method");
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      throw new UiSelectorSyntaxException("problem using reflection to call this method");
    }

  }

  private Object coerceArgToType(Type type, String argument) throws UiSelectorSyntaxException {
    if (type == boolean.class) {
      if (argument == "true") {
        return true;
      }
      if (argument == "false") {
        return false;
      }
      throw new UiSelectorSyntaxException(argument + " is not a boolean");
    }

    if (type == String.class) {
      if (argument.charAt(0) != '"' || argument.charAt(argument.length()-1) != '"') {
        throw new UiSelectorSyntaxException(argument + "is not a string");
      }
      return argument.substring(1, argument.length()-1);
    }

    if (type == int.class) {
      return Integer.parseInt(argument);
    }

    if (type == Class.class) {
      try {
        Class c = Class.forName(argument);
        return c;
      } catch (ClassNotFoundException e) {
        throw new UiSelectorSyntaxException(argument + " class could not be found");
      }
    }

    if (type == UiSelector.class) {
      UiSelectorParser parser = new UiSelectorParser(argument);
      return parser.parse();
    }

    throw new UiSelectorSyntaxException("Could not coerce " + argument + " to any sort of Type");
  }
}

package jonahss.myfirstproject;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

/**
 * Created by jonahss on 6/4/14.
 */
public class UiAutomatorTest extends UiAutomatorTestCase {

//ant build; adb push bin/MyFirstProject.jar /data/local/tmp; adb shell uiautomator runtest MyFirstProject.jar

  /*
  public void testEquality() {
    UiSelector selector0 = new UiSelector().clickable(true);
    UiSelector selector1 = new UiSelector().clickable(true);

    UiObject ob0 = new UiObject(selector0.instance(0));
    UiObject ob0copy = new UiObject(selector1.instance(0));
    UiObject ob1 = new UiObject(selector0.instance(3));
    UiObject ob1copy = new UiObject(selector1.instance(3));


    if (!(ob0.hashCode() == ob0copy.hashCode())) {
      throw new RuntimeException(ob0.hashCode() + " " + ob0copy.hashCode());
    }


    //assertEquals(ob0, ob0copy);
    //assertEquals(ob1, ob1copy);
    /*
    assertTrue(ob0.equals(ob0copy));
    assertTrue(ob0 == ob0copy);
    assertFalse(ob0.equals(ob1));
  }
*/

  public void testUiScrollable() throws UiObjectNotFoundException {
    UiScrollable scrollable = new UiScrollable(new UiSelector().scrollable(true).instance(0));

    UiObject object = scrollable.getChildByText(new UiSelector().className("android.widget.TextView"), "Views");
    UiSelector magicSelector = object.getSelector();

    object = new UiObject(magicSelector);

    assertEquals("Views", object.getText());
//object.getChild(new UiSelector().textMatches("Views")
  }

  public void testSimple() throws UiObjectNotFoundException {
    UiSelector textFieldSelector = new UiSelector().className("android.widget.EditText");
    UiObject[] textFields = {
            new UiObject(textFieldSelector.instance(0)),
            new UiObject(textFieldSelector.instance(1))
    };

    int[] values = {
            (int) Math.ceil(Math.random()*10),
            (int) Math.ceil(Math.random()*10)
    };

    textFields[0].setText(Integer.toString(values[0]));
    textFields[1].setText(Integer.toString(values[1]));

    UiSelector buttonSelector = new UiSelector().className("android.widget.Button");
    (new UiObject(buttonSelector)).click();

    String result = new UiObject(new UiSelector().className("android.widget.TextView")).getText();

    assertEquals(Integer.toString(values[0]+values[1]), result);
  }

}

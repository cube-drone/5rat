package jonahss.myfirstproject;



/**
 * An exception involving an {@link UiSelectorParser}.
 *
 * @param msg
 *          A descriptive message describing the error.
 */
@SuppressWarnings("serial")
public class UiSelectorSyntaxException extends Exception {

  public UiSelectorSyntaxException(final String msg) {
    super(msg);
  }
}

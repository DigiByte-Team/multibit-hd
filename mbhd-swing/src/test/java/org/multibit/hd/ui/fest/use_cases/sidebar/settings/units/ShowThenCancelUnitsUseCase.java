package org.multibit.hd.ui.fest.use_cases.sidebar.settings.units;

import org.fest.swing.fixture.FrameFixture;
import org.multibit.hd.ui.fest.use_cases.AbstractFestUseCase;
import org.multibit.hd.ui.languages.MessageKey;

import java.util.Map;

/**
 * <p>Use case to provide the following to FEST testing:</p>
 * <ul>
 * <li>Verify the "settings" screen units wizard shows</li>
 * </ul>
 * <p>Requires the "settings" screen to be showing</p>
 *
 * @since 0.0.1
 *
 */
public class ShowThenCancelUnitsUseCase extends AbstractFestUseCase {

  public ShowThenCancelUnitsUseCase(FrameFixture window) {
    super(window);
  }

  @Override
  public void execute(Map<String, Object> parameters) {

    // Click on "units"
    window
      .button(MessageKey.SHOW_UNITS_WIZARD.getKey())
      .click();

    // Verify the "units" wizard appears
    assertLabelText(MessageKey.UNITS_SETTINGS_TITLE);

    // Verify cancel is present
    window
      .button(MessageKey.CANCEL.getKey())
      .requireVisible()
      .requireEnabled();

    // Click Cancel
    window
      .button(MessageKey.CANCEL.getKey())
      .click();

    // Verify the underlying screen is back
    window
      .button(MessageKey.SHOW_LANGUAGE_WIZARD.getKey())
      .requireVisible()
      .requireEnabled();

  }

}

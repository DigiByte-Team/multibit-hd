package org.multibit.hd.ui.views.wizards.welcome;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import org.multibit.hd.core.api.seed_phrase.SeedPhraseGenerator;
import org.multibit.hd.core.api.seed_phrase.SeedPhraseSize;
import org.multibit.hd.core.services.CoreServices;
import org.multibit.hd.ui.events.view.VerificationStatusChangedEvent;
import org.multibit.hd.ui.events.view.ViewEvents;
import org.multibit.hd.ui.i18n.Languages;
import org.multibit.hd.ui.views.components.confirm_password.ConfirmPasswordModel;
import org.multibit.hd.ui.views.components.enter_seed_phrase.EnterSeedPhraseModel;
import org.multibit.hd.ui.views.components.select_file.SelectFileModel;
import org.multibit.hd.ui.views.wizards.AbstractWizardModel;
import org.multibit.hd.ui.views.wizards.WizardButton;

import java.util.List;

import static org.multibit.hd.ui.views.wizards.welcome.WelcomeWizardState.*;

/**
 * <p>Model object to provide the following to welcome wizard:</p>
 * <ul>
 * <li>Storage of panel data</li>
 * <li>State transition management</li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *  
 */
public class WelcomeWizardModel extends AbstractWizardModel<WelcomeWizardState> {

  /**
   * The "select wallet" radio button choice (as a state)
   */
  private String localeCode = Languages.currentLocale().getLanguage();

  /**
   * The "select wallet" radio button choice (as a state)
   */
  private WelcomeWizardState selectWalletChoice = WelcomeWizardState.CREATE_WALLET_SEED_PHRASE;

  /**
   * The seed phrase generator
   */
  private final SeedPhraseGenerator seedPhraseGenerator;

  /**
   * The confirm password model
   */
  private ConfirmPasswordModel confirmPasswordModel;
  private SelectFileModel selectFileModel;
  private EnterSeedPhraseModel enterSeedPhraseModel;
  private List<String> actualSeedPhrase;
  private String actualSeedTimestamp;

  /**
   * @param state The state object
   */
  public WelcomeWizardModel(WelcomeWizardState state) {
    super(state);

    this.seedPhraseGenerator = CoreServices.newSeedPhraseGenerator();

  }

  @Override
  public void showNext() {

    switch (state) {
      case WELCOME:
        state = SELECT_WALLET;
        break;
      case SELECT_WALLET:
        state = selectWalletChoice;
        break;
      case SELECT_BACKUP_LOCATION:
        state = CREATE_WALLET_SEED_PHRASE;
        break;
      case CREATE_WALLET_SEED_PHRASE:
        state = CONFIRM_WALLET_SEED_PHRASE;
        // Fail safe to ensure that the generator hasn't gone screwy
        Preconditions.checkState(SeedPhraseSize.isValid(getActualSeedPhrase().size()), "'actualSeedPhrase' is not a valid length");
        break;
      case CONFIRM_WALLET_SEED_PHRASE:
        state = CREATE_WALLET_PASSWORD;
        break;
      case CREATE_WALLET_PASSWORD:
        state = CREATE_WALLET_REPORT;
        break;
      case RESTORE_WALLET:
        state = CONFIRM_WALLET_SEED_PHRASE;
        break;
      case HARDWARE_WALLET:
        state = CONFIRM_WALLET_SEED_PHRASE;
        break;
    }

  }

  @Override
  public void showPrevious() {

    switch (state) {
      case WELCOME:
        state = WELCOME;
        break;
      case SELECT_WALLET:
        state = WELCOME;
        break;
      case SELECT_BACKUP_LOCATION:
        state = SELECT_WALLET;
        break;
      case CREATE_WALLET_SEED_PHRASE:
        state = SELECT_BACKUP_LOCATION;
        break;
      case CREATE_WALLET_REPORT:
        state = CONFIRM_WALLET_SEED_PHRASE;
        break;
      case RESTORE_WALLET:
        state = SELECT_WALLET;
        break;
      case HARDWARE_WALLET:
        state = SELECT_WALLET;
        break;
    }
  }

  @Override
  public String getPanelName() {
    return state.name();
  }

  @Subscribe
  public void onPasswordStatusChangedEvent(VerificationStatusChangedEvent event) {

    ViewEvents.fireWizardButtonEnabledEvent(CONFIRM_WALLET_SEED_PHRASE.name(), WizardButton.NEXT, event.isOK());

  }

  /**
   * @return The user selection for the locale
   */
  public String getLocaleCode() {
    return localeCode;
  }

  /**
   * <p>Reduced visibility for panel models</p>
   *
   * @param localeCode The locale code
   */
  public void setLocaleCode(String localeCode) {
    this.localeCode = localeCode;
  }

  /**
   * @return The "select wallet" radio button choice
   */
  public WelcomeWizardState getSelectWalletChoice() {
    return selectWalletChoice;
  }

  /**
   * @return The actual generated seed phrase
   */
  public List<String> getActualSeedPhrase() {
    return actualSeedPhrase;
  }

  /**
   * @return The actual generated seed timestamp (e.g. "1850/2")
   */
  public String getActualSeedTimestamp() {
    return actualSeedTimestamp;
  }

  /**
   * <p>Reduced visibility for panel models</p>
   *
   * @param actualSeedPhrase The actual seed phrase generated by the panel model
   */
  void setActualSeedPhrase(List<String> actualSeedPhrase) {
    this.actualSeedPhrase = actualSeedPhrase;
  }

  /**
   * <p>Reduced visibility for panel models</p>
   *
   * @param actualSeedTimestamp The actual seed timestamp generated by the panel model
   */
  void setActualSeedTimestamp(String actualSeedTimestamp) {
    this.actualSeedTimestamp = actualSeedTimestamp;
  }

  /**
   * @return The user entered password
   */
  public String getUserPassword() {
    return confirmPasswordModel.getValue();
  }

  /**
   * @return The user entered backup location
   */
  public String getBackupLocation() {
    return selectFileModel.getValue();
  }

  /**
   * @return The seed phrase generator
   */
  public SeedPhraseGenerator getSeedPhraseGenerator() {
    return seedPhraseGenerator;
  }

  /**
   * <p>Reduced visibility for panel models</p>
   *
   * @param confirmPasswordModel The "confirm password" model
   */
  void setConfirmPasswordModel(ConfirmPasswordModel confirmPasswordModel) {
    this.confirmPasswordModel = confirmPasswordModel;
  }

  /**
   * <p>Reduced visibility for panel models</p>
   *
   * @param selectFileModel The "select file" model
   */
  void setSelectFileModel(SelectFileModel selectFileModel) {
    this.selectFileModel = selectFileModel;
  }

  /**
   * <p>Reduced visibility for panel models</p>
   *
   * @param selectWalletChoice The wallet selection from the radio buttons
   */
  void setSelectWalletChoice(WelcomeWizardState selectWalletChoice) {
    this.selectWalletChoice = selectWalletChoice;
  }

  /**
   * <p>Reduced visibility for panel models</p>
   *
   * @param enterSeedPhraseModel The "enter seed phrase" model
   */
  void setEnterSeedPhraseModel(EnterSeedPhraseModel enterSeedPhraseModel) {
    this.enterSeedPhraseModel = enterSeedPhraseModel;
  }


}

package org.multibit.hd.ui.views.components.display_qrcode;

import com.google.common.base.Optional;
import net.miginfocom.swing.MigLayout;
import org.multibit.hd.ui.utils.ClipboardUtils;
import org.multibit.hd.ui.utils.QRCodes;
import org.multibit.hd.ui.views.AbstractView;
import org.multibit.hd.ui.views.components.Buttons;
import org.multibit.hd.ui.views.components.Labels;
import org.multibit.hd.ui.views.components.Panels;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * <p>View to provide the following to UI:</p>
 * <ul>
 * <li>Presentation of Bitcoin address</li>
 * <li>Support for clipboard copy operation</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class DisplayQRCodeView extends AbstractView<DisplayQRCodeModel> {

  private Optional<BufferedImage> qrCodeImage;

  /**
   * @param model The model backing this view
   */
  public DisplayQRCodeView(DisplayQRCodeModel model) {
    super(model);

  }

  @Override
  public JPanel newPanel() {

    panel = Panels.newPanel(new MigLayout(
      "insets 0", // Layout
      "[][]", // Columns
      "[][]" // Rows
    ));

    qrCodeImage = QRCodes.generateQRCode("Hello!", 2);

    // Add to the panel
    panel.add(Labels.newImageLabel(qrCodeImage), "span 2,grow,push,wrap");
    panel.add(Buttons.newCopyButton(getCopyClipboardAction()));
    panel.add(Buttons.newPanelCloseButton(getClosePopoverAction()), "wrap");

    panel.setSize(200, 200);

    return panel;

  }

  /**
   * @return A new action for copying the QR code image to the clipboard
   */
  private Action getCopyClipboardAction() {

    return new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {

        // Copy the image to the clipboard
        ClipboardUtils.copyImageToClipboard(qrCodeImage.get());

      }

    };
  }

  /**
   * @return A new action for closing the QR code popover
   */
  private Action getClosePopoverAction() {

    return new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {

        Panels.hideLightBoxPopover();

      }

    };
  }


  @Override
  public void updateModel() {
    // Do nothing the model is updated from key release events
  }

}
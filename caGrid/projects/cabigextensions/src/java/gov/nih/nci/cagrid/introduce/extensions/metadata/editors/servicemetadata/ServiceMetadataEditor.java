/**
*============================================================================
*  Copyright The Ohio State University Research Foundation, The University of Chicago - 
*	Argonne National Laboratory, Emory University, SemanticBits LLC, and 
*	Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetadata;

import gov.nih.nci.cagrid.common.SchemaValidationException;
import gov.nih.nci.cagrid.common.SchemaValidator;
import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorPanel;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription;
import gov.nih.nci.cagrid.metadata.common.Address;
import gov.nih.nci.cagrid.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.metadata.common.ResearchCenterDescription;
import gov.nih.nci.cagrid.metadata.common.ResearchCenterPointOfContactCollection;
import gov.nih.nci.cagrid.metadata.service.Service;
import gov.nih.nci.cagrid.metadata.service.ServicePointOfContactCollection;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import java.awt.Insets;


/**
 * @author oster
 */
public class ServiceMetadataEditor extends ResourcePropertyEditorPanel {
    private static final int MAXIMUM_CONTACTS = 10;
    private static final String HOSTING_CENTER_TAB_NAME = "Hosting Center";
    private static final String SERVICE_INFORMATION_TAB_NAME = "Service Information";

    private ValidationResultModel validationModel = new DefaultValidationResultModel();
    private ValidationResult validationResult = null;

    private ServiceMetadata serviceMetadata = null;
    private JTabbedPane metadataTabbedPane = null;
    private JPanel centerPanel = null;
    private JPanel servicePanel = null;
    private JPanel centerInfoPanel = null;
    private JTabbedPane centerTabbedPane = null;
    private JPanel centerPOCPanel = null;
    private JPanel centerAddressPanel = null;
    private JPanel centerAdditionalInfoPanel = null;
    private JPanel serviceInfoPanel = null;
    private JTabbedPane serviceTabbedPane = null;
    private JPanel servicePOCPanel = null;
    private JLabel centerDisplayNameLabel = null;
    private JTextField centerDisplayNameTextField = null;
    private JLabel centerShortNameLabel = null;
    private JTextField centerShortNameTextField = null;
    private AddressEditorPanel centerAddressEditorPanel = null;
    private PointsOfContactEditorPanel centerPointsOfContactEditorPanel = null;
    private PointsOfContactEditorPanel servicePointsOfContactEditorPanel = null;
    private JLabel centerDescLabel = null;
    private JTextField centerDescTextArea = null;
    private JLabel centerURLLabel = null;
    private JTextField centerHomepageTextField = null;
    private JLabel centerRSSLabel = null;
    private JTextField centerRSSTextField = null;
    private JLabel centerImageLabel = null;
    private JTextField centerImageTextField = null;
    private String result;
    private JButton searchCenterButton;
    private JLabel serviceDescriptionLabel = null;
    private JTextField serviceDescriptionTextField = null;
    private JLabel serviceVersionLabel = null;
    private JTextField serviceVersionTextField = null;


    public ServiceMetadataEditor(ResourcePropertyType type, String doc, File schemaFile, File schemaDir) {
        super(type, doc, schemaFile, schemaDir);
        if (doc != null) {
            try {
                setServiceMetadata(MetadataUtils.deserializeServiceMetadata(new StringReader(doc)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        initialize();
    }


    private void initialize() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(1);
        setLayout(gridLayout);
        add(getMetadataTabbedPane(), null);

        initValidation();
        validateInput();

    }


    public final class TextBoxListener implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {
            validateInput();
        }


        public void insertUpdate(DocumentEvent e) {
            validateInput();
        }


        public void removeUpdate(DocumentEvent e) {
            validateInput();
        }

    }


    private void initValidation() {
        ValidationComponentUtils.setMessageKey(getCenterDisplayNameTextField(), "service-display-name");
        ValidationComponentUtils.setMessageKey(getCenterShortNameTextField(), "service-short-name");
        ValidationComponentUtils.setMessageKey(getServiceDescriptionTextField(), "service-description");
        ValidationComponentUtils.setMessageKey(getServiceVersionTextField(), "service-version");

        validateInput();
    }


    private void validateInput() {
        validationResult = new ValidationResult();
        if (ValidationUtils.isBlank(getCenterDisplayNameTextField().getText())) {
            validationResult.add(new SimpleValidationMessage("Center Display Name must not be blank", Severity.ERROR,
                "service-display-name"));
        }
        if (ValidationUtils.isBlank(getCenterShortNameTextField().getText())) {
            validationResult.add(new SimpleValidationMessage("Center Short Name must not be blank", Severity.ERROR,
                "service-short-name"));
        }
        if (ValidationUtils.isBlank(getServiceDescriptionTextField().getText())) {
            validationResult.add(new SimpleValidationMessage("Service description should not be blank",
                Severity.WARNING, "service-description"));
        }
        if (ValidationUtils.isBlank(getServiceVersionTextField().getText())) {
            validationResult.add(new SimpleValidationMessage("Service version should not be blank", Severity.WARNING,
                "service-version"));
        }

        this.validationModel.setResult(validationResult);
        updateComponentTreeSeverity();
    }


    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, this.validationModel.getResult());
    }


    private boolean validatePanel() {
        if ((validationResult != null && validationResult.hasErrors())
            || !getCenterAddressEditorPanel().validatePanel() || !getCenterPointsOfContactEditorPanel().validatePanel()
            || !getServicePointsOfContactEditorPanel().validatePanel()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * This method initializes metadataTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getMetadataTabbedPane() {
        if (this.metadataTabbedPane == null) {
            this.metadataTabbedPane = new JTabbedPane();
            this.metadataTabbedPane.addTab(HOSTING_CENTER_TAB_NAME, null, new IconFeedbackPanel(this.validationModel,
                getCenterPanel()), "Information on the Hosting Research Center");
            this.metadataTabbedPane.addTab(SERVICE_INFORMATION_TAB_NAME, null, getServicePanel(),
                "Information about the service itself");
        }
        return this.metadataTabbedPane;
    }


    /**
     * This method initializes centerPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCenterPanel() {
        if (this.centerPanel == null) {
            this.centerPanel = new JPanel();
            this.centerPanel.setLayout(new BorderLayout());
            this.centerPanel.add(getCenterInfoPanel(), java.awt.BorderLayout.NORTH);
            this.centerPanel.add(getCenterTabbedPane(), java.awt.BorderLayout.CENTER);
        }
        return this.centerPanel;
    }


    /**
     * This method initializes servicePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServicePanel() {
        if (this.servicePanel == null) {
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = GridBagConstraints.BOTH;
            gridBagConstraints15.weightx = 1.0;
            gridBagConstraints15.weighty = 1.0;
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.gridy = 1;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.gridx = 0;
            gridBagConstraints14.gridy = 0;
            this.servicePanel = new JPanel();
            this.servicePanel.setLayout(new GridBagLayout());
            servicePanel.add(new IconFeedbackPanel(this.validationModel, getServiceInfoPanel()), gridBagConstraints14);
            servicePanel.add(getServiceTabbedPane(), gridBagConstraints15);
        }
        return this.servicePanel;
    }


    /**
     * This method initializes centerInfoPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCenterInfoPanel() {
        if (this.centerInfoPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 1;
            gridBagConstraints11.weightx = 1.0;
            gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints11.gridx = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints12.gridy = 1;
            gridBagConstraints12.weightx = 0.0;
            gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints12.gridx = 2;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints2.gridy = 1;
            this.centerShortNameLabel = new JLabel();
            this.centerShortNameLabel.setText("Short Name");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints1.weightx = 1.0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridy = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints.gridx = 0;
            this.centerDisplayNameLabel = new JLabel();
            this.centerDisplayNameLabel.setText("Display Name");
            this.centerInfoPanel = new JPanel();
            this.centerInfoPanel.setLayout(new GridBagLayout());
            this.centerInfoPanel.add(this.centerDisplayNameLabel, gridBagConstraints);
            this.centerInfoPanel.add(getCenterDisplayNameTextField(), gridBagConstraints1);
            this.centerInfoPanel.add(this.centerShortNameLabel, gridBagConstraints2);
            this.centerInfoPanel.add(getCenterShortNameTextField(), gridBagConstraints11);
        }
        return this.centerInfoPanel;
    }


    /**
     * This method initializes centerTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getCenterTabbedPane() {
        if (this.centerTabbedPane == null) {
            this.centerTabbedPane = new JTabbedPane();
            this.centerTabbedPane.addTab("Points of Contact", null, getCenterPOCPanel(), null);
            this.centerTabbedPane.addTab("Address", null, getCenterAddressPanel(), null);
            this.centerTabbedPane.addTab("Additional Information", null, getCenterAdditionalInfoPanel(), null);
        }
        return this.centerTabbedPane;
    }


    /**
     * This method initializes centerPOCPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCenterPOCPanel() {
        if (this.centerPOCPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.ipadx = 0;
            gridBagConstraints3.ipady = -28;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.weighty = 1.0;
            gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints3.gridy = 0;
            this.centerPOCPanel = new JPanel();
            this.centerPOCPanel.setLayout(new GridBagLayout());
            this.centerPOCPanel.add(getCenterPointsOfContactEditorPanel(), gridBagConstraints3);
        }
        return this.centerPOCPanel;
    }


    /**
     * This method initializes centerAddressPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCenterAddressPanel() {
        if (this.centerAddressPanel == null) {
            this.centerAddressPanel = new JPanel();
            this.centerAddressPanel.setLayout(new BorderLayout());
            this.centerAddressPanel.add(getCenterAddressEditorPanel(), java.awt.BorderLayout.CENTER);
        }
        return this.centerAddressPanel;
    }


    /**
     * This method initializes centerAdditionalInfoPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCenterAdditionalInfoPanel() {
        if (this.centerAdditionalInfoPanel == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.gridy = 2;
            gridBagConstraints13.weightx = 1.0;
            gridBagConstraints13.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints13.gridx = 1;
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints12.gridy = 2;
            this.centerImageLabel = new JLabel();
            this.centerImageLabel.setText("Image URL");
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridy = 3;
            gridBagConstraints10.weightx = 1.0;
            gridBagConstraints10.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints10.gridx = 1;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints9.gridy = 3;
            this.centerRSSLabel = new JLabel();
            this.centerRSSLabel.setText("RSS URL");
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.gridy = 1;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints8.gridx = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints7.gridy = 1;
            this.centerURLLabel = new JLabel();
            this.centerURLLabel.setText("Homepage URL");
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.gridy = 4;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.weighty = 0.0D;
            gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints6.gridx = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints5.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints5.gridy = 4;
            this.centerDescLabel = new JLabel();
            this.centerDescLabel.setText("Description");
            this.centerAdditionalInfoPanel = new JPanel();
            this.centerAdditionalInfoPanel.setLayout(new GridBagLayout());
            this.centerAdditionalInfoPanel.add(this.centerDescLabel, gridBagConstraints5);
            this.centerAdditionalInfoPanel.add(getCenterDescTextField(), gridBagConstraints6);
            this.centerAdditionalInfoPanel.add(this.centerURLLabel, gridBagConstraints7);
            this.centerAdditionalInfoPanel.add(getCenterHomepageTextField(), gridBagConstraints8);
            this.centerAdditionalInfoPanel.add(this.centerRSSLabel, gridBagConstraints9);
            this.centerAdditionalInfoPanel.add(getCenterRSSTextField(), gridBagConstraints10);
            this.centerAdditionalInfoPanel.add(this.centerImageLabel, gridBagConstraints12);
            this.centerAdditionalInfoPanel.add(getCenterImageTextField(), gridBagConstraints13);
        }
        return this.centerAdditionalInfoPanel;
    }


    /**
     * This method initializes serviceInfoPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServiceInfoPanel() {
        if (this.serviceInfoPanel == null) {
            GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
            gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints19.gridx = 1;
            gridBagConstraints19.gridy = 1;
            gridBagConstraints19.weightx = 1;
            gridBagConstraints19.weighty = 1;
            gridBagConstraints19.insets = new Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints18.gridy = 1;
            gridBagConstraints18.anchor = GridBagConstraints.EAST;
            gridBagConstraints18.gridx = 0;
            gridBagConstraints18.weightx = 1;
            gridBagConstraints18.weighty = 1;
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints17.gridx = 1;
            gridBagConstraints17.gridy = 0;
            gridBagConstraints17.weightx = 1;
            gridBagConstraints17.weighty = 1;
            gridBagConstraints17.insets = new Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints16.gridy = 0;
            gridBagConstraints16.anchor = GridBagConstraints.EAST;
            gridBagConstraints16.gridx = 0;
            gridBagConstraints16.weightx = 1;
            gridBagConstraints16.weighty = 1;
            serviceVersionLabel = new JLabel();
            serviceVersionLabel.setText("Version");
            serviceDescriptionLabel = new JLabel();
            serviceDescriptionLabel.setText("Description");
            this.serviceInfoPanel = new JPanel();
            serviceInfoPanel.setLayout(new GridBagLayout());
            serviceInfoPanel.add(serviceDescriptionLabel, gridBagConstraints16);
            serviceInfoPanel.add(getServiceDescriptionTextField(), gridBagConstraints17);
            serviceInfoPanel.add(serviceVersionLabel, gridBagConstraints18);
            serviceInfoPanel.add(getServiceVersionTextField(), gridBagConstraints19);
        }
        return this.serviceInfoPanel;
    }


    /**
     * This method initializes serviceTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getServiceTabbedPane() {
        if (this.serviceTabbedPane == null) {
            this.serviceTabbedPane = new JTabbedPane();
            this.serviceTabbedPane.addTab("Points of Contact", null, getServicePOCPanel(), null);
        }
        return this.serviceTabbedPane;
    }


    /**
     * This method initializes servicePOCPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServicePOCPanel() {
        if (this.servicePOCPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0;
            gridBagConstraints4.gridx = 0;
            this.servicePOCPanel = new JPanel();
            this.servicePOCPanel.setLayout(new GridBagLayout());
            this.servicePOCPanel.add(getServicePointsOfContactEditorPanel(), gridBagConstraints4);
        }
        return this.servicePOCPanel;
    }


    /**
     * This method initializes displayNameTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCenterDisplayNameTextField() {
        if (this.centerDisplayNameTextField == null) {
            this.centerDisplayNameTextField = new JTextField();
            this.centerDisplayNameTextField.setColumns(10);
            this.centerDisplayNameTextField.getDocument().addDocumentListener(new TextBoxListener());
        }
        return this.centerDisplayNameTextField;
    }


    /**
     * This method initializes jTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCenterShortNameTextField() {
        if (this.centerShortNameTextField == null) {
            this.centerShortNameTextField = new JTextField();
            this.centerShortNameTextField.setColumns(10);
            this.centerShortNameTextField.getDocument().addDocumentListener(new TextBoxListener());
        }
        return this.centerShortNameTextField;
    }


    /**
     * This method initializes centerAddressEditorPanel
     * 
     * @return 
     *         gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata
     *         .AddressEditorPanel
     */
    private AddressEditorPanel getCenterAddressEditorPanel() {
        if (this.centerAddressEditorPanel == null) {
            this.centerAddressEditorPanel = new AddressEditorPanel();
        }
        return this.centerAddressEditorPanel;
    }


    /**
     * This method initializes centerPointsOfContactEditorPanel
     * 
     * @return 
     *         gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata
     *         .PointsOfContactEditorPanel
     */
    private PointsOfContactEditorPanel getCenterPointsOfContactEditorPanel() {
        if (this.centerPointsOfContactEditorPanel == null) {
            this.centerPointsOfContactEditorPanel = new PointsOfContactEditorPanel();
        }
        return this.centerPointsOfContactEditorPanel;
    }


    /**
     * This method initializes servicePointsOfContactEditorPanel
     * 
     * @return 
     *         gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata
     *         .PointsOfContactEditorPanel
     */
    private PointsOfContactEditorPanel getServicePointsOfContactEditorPanel() {
        if (this.servicePointsOfContactEditorPanel == null) {
            this.servicePointsOfContactEditorPanel = new PointsOfContactEditorPanel();
        }
        return this.servicePointsOfContactEditorPanel;
    }


    /**
     * This method initializes centerDescjTextArea
     * 
     * @return javax.swing.JTextArea
     */
    private JTextField getCenterDescTextField() {
        if (this.centerDescTextArea == null) {
            this.centerDescTextArea = new JTextField();
            this.centerDescTextArea.setColumns(10);
        }
        return this.centerDescTextArea;
    }


    /**
     * This method initializes centerHomepageTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCenterHomepageTextField() {
        if (this.centerHomepageTextField == null) {
            this.centerHomepageTextField = new JTextField();
            this.centerHomepageTextField.setColumns(10);
        }
        return this.centerHomepageTextField;
    }


    /**
     * This method initializes centerRSSURLTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCenterRSSTextField() {
        if (this.centerRSSTextField == null) {
            this.centerRSSTextField = new JTextField();
            this.centerRSSTextField.setColumns(10);
        }
        return this.centerRSSTextField;
    }


    /**
     * This method initializes centerImageTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getCenterImageTextField() {
        if (this.centerImageTextField == null) {
            this.centerImageTextField = new JTextField();
            this.centerImageTextField.setColumns(10);
        }
        return this.centerImageTextField;
    }


    /**
     * This method initializes serviceDescriptionTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getServiceDescriptionTextField() {
        if (serviceDescriptionTextField == null) {
            serviceDescriptionTextField = new JTextField();
            serviceDescriptionTextField.setColumns(25);
            serviceDescriptionTextField.getDocument().addDocumentListener(new TextBoxListener());
        }
        return serviceDescriptionTextField;
    }


    /**
     * This method initializes serviceVersionTextField
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getServiceVersionTextField() {
        if (serviceVersionTextField == null) {
            serviceVersionTextField = new JTextField();
            serviceVersionTextField.getDocument().addDocumentListener(new TextBoxListener());
        }
        return serviceVersionTextField;
    }


    public static void main(String[] args) {
        JFrame f = new JFrame();
        JPanel p = new JPanel();
        try {
            JFileChooser fc = new JFileChooser(".");
            if (fc.showOpenDialog(f) != JFileChooser.APPROVE_OPTION) {
                System.out.println("No file selected exiting");
                System.exit(0);
            }

            final ServiceMetadataEditor viewer = new ServiceMetadataEditor(null, null, new File(
                "../metadata/schema/cagrid/types/caGridMetadata.xsd"), null);
            ServiceMetadata model = MetadataUtils.deserializeServiceMetadata(new FileReader(fc.getSelectedFile()));
            viewer.setServiceMetadata(model);

            p.add(viewer);
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        viewer.validateResourceProperty();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    System.out.println(viewer.getResultRPString());

                }
            });

            p.add(saveButton);
            f.getContentPane().add(p);
            f.pack();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return the serviceMetadata
     */
    public ServiceMetadata getServiceMetadata() {
        return this.serviceMetadata;
    }


    /**
     * @param serviceMetadata
     *            the serviceMetadata to set
     */
    public void setServiceMetadata(ServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
        updateView();
    }


    protected void updateView() {
        ResearchCenter hostingResearchCenter = null;
        Service service = null;

        if (this.serviceMetadata != null) {
            if (this.serviceMetadata.getHostingResearchCenter() != null) {
                hostingResearchCenter = this.serviceMetadata.getHostingResearchCenter().getResearchCenter();
            }
            if (this.serviceMetadata.getServiceDescription() != null) {
                service = this.serviceMetadata.getServiceDescription().getService();
            }
        }

        updateCenterView(hostingResearchCenter);
        updateServiceView(service);
    }


    private void updateServiceView(Service service) {

        String desc = "";
        String version = "";

        if (service != null) {
            desc = service.getDescription();
            version = service.getVersion();
        }
        getServiceDescriptionTextField().setText(desc);
        getServiceVersionTextField().setText(version);

        PointOfContact[] pointsOfContact = null;
        if (service != null && service.getPointOfContactCollection() != null) {
            pointsOfContact = service.getPointOfContactCollection().getPointOfContact();
        }

        List<PointOfContact> list = new ArrayList<PointOfContact>();
        if (pointsOfContact != null) {
            list = new ArrayList<PointOfContact>(pointsOfContact.length);
            Collections.addAll(list, pointsOfContact);
        }

        getServicePointsOfContactEditorPanel().setPointsOfContact(list);

        // dis/en-able the service tab based on whether service is null
        int tabCount = getMetadataTabbedPane().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            if (getMetadataTabbedPane().getTitleAt(i).equals(SERVICE_INFORMATION_TAB_NAME)) {
                getMetadataTabbedPane().setEnabledAt(i, service != null);
                break;
            }
        }

    }


    private void updateCenterView(ResearchCenter hostingResearchCenter) {
        String displayName = null;
        String shortName = null;
        Address address = null;
        ResearchCenterDescription researchCenterDescription = null;
        PointOfContact[] pointsOfContact = null;

        if (hostingResearchCenter != null) {
            displayName = hostingResearchCenter.getDisplayName();
            shortName = hostingResearchCenter.getShortName();
            address = hostingResearchCenter.getAddress();
            researchCenterDescription = hostingResearchCenter.getResearchCenterDescription();
            if (hostingResearchCenter.getPointOfContactCollection() != null) {
                pointsOfContact = hostingResearchCenter.getPointOfContactCollection().getPointOfContact();
            }
        }

        List<PointOfContact> list = new ArrayList<PointOfContact>();
        if (pointsOfContact != null) {
            list = new ArrayList<PointOfContact>(pointsOfContact.length);
            Collections.addAll(list, pointsOfContact);
        }

        getCenterDisplayNameTextField().setText(displayName);
        getCenterShortNameTextField().setText(shortName);
        getCenterAddressEditorPanel().setAddress(address);
        updateCenterInfoView(researchCenterDescription);
        getCenterPointsOfContactEditorPanel().setPointsOfContact(list);
    }


    private void updateCenterInfoView(ResearchCenterDescription researchCenterDescription) {
        String description = null;
        String homepageURL = null;
        String imageURL = null;
        String rssNewsURL = null;

        if (researchCenterDescription != null) {
            description = researchCenterDescription.getDescription();
            homepageURL = researchCenterDescription.getHomepageURL();
            imageURL = researchCenterDescription.getImageURL();
            rssNewsURL = researchCenterDescription.getRssNewsURL();
        }

        getCenterDescTextField().setText(description);
        getCenterHomepageTextField().setText(homepageURL);
        getCenterImageTextField().setText(imageURL);
        getCenterRSSTextField().setText(rssNewsURL);
    }


    @Override
    public void validateResourceProperty() throws Exception {
        this.result = null;
        if (this.serviceMetadata == null) {
            throw new Exception("Cannot save a null ServiceMetadata instance!");
        }

        // make sure we have the proper containers
        if (this.serviceMetadata.getHostingResearchCenter() == null) {
            this.serviceMetadata.setHostingResearchCenter(new ServiceMetadataHostingResearchCenter());
        }
        if (this.serviceMetadata.getHostingResearchCenter().getResearchCenter() == null) {
            this.serviceMetadata.getHostingResearchCenter().setResearchCenter(new ResearchCenter());
        }
        if (this.serviceMetadata.getServiceDescription() == null) {
            this.serviceMetadata.setServiceDescription(new ServiceMetadataServiceDescription());
        }

        // save the center (we create if not there yet)
        if (!saveCenter(this.serviceMetadata.getHostingResearchCenter().getResearchCenter())) {
            throw new Exception();
        }

        // save the service (we DON'T create if not there yet)
        Service service = this.serviceMetadata.getServiceDescription().getService();
        if (service != null) {
            if (!saveService(service)) {
                throw new Exception("Unknown error saving service");
            }
        }

        StringWriter writer = new StringWriter();
        try {
            MetadataUtils.serializeServiceMetadata(this.serviceMetadata, writer);
            this.result = writer.toString();
        } catch (Exception e) {
            throw new Exception("Problem saving ServiceMetadata instance: " + e.getMessage(), e);
        }

        // should we validate?
        if (getSchemaFile() != null) {
            try {
                SchemaValidator validator = new SchemaValidator(getSchemaFile().getAbsolutePath());
                validator.validate(this.result);
            } catch (SchemaValidationException e) {
                throw new Exception("Problem validating result:" + e.getMessage()
                    + " Correct the error and save again.", e);
            }
        }

        if (!validatePanel()) {
            throw new Exception("caBIG Service Metadata is not properly populated.  See validation errors.");
        }
    }


    @Override
    public String getResultRPString() {
        return this.result;
    }


    /**
     * @param serviceDescription
     */
    private boolean saveService(Service serviceDescription) {
        serviceDescription.setDescription(getServiceDescriptionTextField().getText());
        serviceDescription.setVersion(getServiceVersionTextField().getText());

        // save pocs
        List<PointOfContact> pointsOfContact = getServicePointsOfContactEditorPanel().getPointsOfContact();
        PointOfContact[] poc = null;
        if (pointsOfContact != null) {
            poc = new PointOfContact[pointsOfContact.size()];
            poc = pointsOfContact.toArray(poc);
        }
        serviceDescription.setPointOfContactCollection(new ServicePointOfContactCollection(poc));

        return true;
    }


    private boolean saveCenter(ResearchCenter researchCenter) {
        // save basic info
        researchCenter.setDisplayName(getCenterDisplayNameTextField().getText());
        researchCenter.setAddress(getCenterAddressEditorPanel().getAddress());
        researchCenter.setShortName(getCenterShortNameTextField().getText());

        // save pocs
        List<PointOfContact> pointsOfContact = getCenterPointsOfContactEditorPanel().getPointsOfContact();
        PointOfContact[] poc = null;
        if (pointsOfContact != null) {
            poc = new PointOfContact[pointsOfContact.size()];
            poc = pointsOfContact.toArray(poc);
        }
        researchCenter.setPointOfContactCollection(new ResearchCenterPointOfContactCollection(poc));

        // save center desc
        if (researchCenter.getResearchCenterDescription() == null) {
            researchCenter.setResearchCenterDescription(new ResearchCenterDescription());
        }

        return saveCenterDescription(researchCenter.getResearchCenterDescription());
    }


    /**
     * @param researchCenterDescription
     */
    private boolean saveCenterDescription(ResearchCenterDescription researchCenterDescription) {
        researchCenterDescription.setDescription(getCenterDescTextField().getText());
        researchCenterDescription.setHomepageURL(getCenterHomepageTextField().getText());
        researchCenterDescription.setImageURL(getCenterImageTextField().getText());
        researchCenterDescription.setRssNewsURL(getCenterRSSTextField().getText());

        return true;

    }

} // @jve:decl-index=0:visual-constraint="10,10"

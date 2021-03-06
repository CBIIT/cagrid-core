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
package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.UpgradeDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionRemovalException;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.ServiceExtensionRemover;
import gov.nih.nci.cagrid.introduce.upgrade.common.ExtensionUpgraderI;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ExtensionsUpgradeManager {
	private ServiceInformation serviceInformation;
	private String pathToService;

	public ExtensionsUpgradeManager(ServiceInformation serviceInformation,
			String pathToService) {
		this.serviceInformation = serviceInformation;
		this.pathToService = pathToService;
	}

	
	public boolean needsUpgrading() {
		if (needsRemoving()) {
			return true;
		}
		ExtensionType[] extensions = serviceInformation.getServiceDescriptor()
				.getExtensions().getExtension();
		if (extensions != null) {
			for (int extensionI = 0; extensionI < extensions.length; extensionI++) {
				ExtensionType extension = extensions[extensionI];
				String serviceExtensionVersion = extension.getVersion();
				ExtensionDescription extDescription = 
				    ExtensionsLoader.getInstance().getExtension(extension.getName());
				if ((extDescription != null)
						&& (extDescription.getVersion() != null)) {
					if ((serviceExtensionVersion == null)
							|| !extDescription.getVersion().equals(
									serviceExtensionVersion)) {
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}
	

	private boolean needsRemoving() {
		ExtensionType[] extensions = serviceInformation.getServiceDescriptor()
				.getExtensions().getExtension();
		if (extensions != null) {
			for (int extensionI = 0; extensionI < extensions.length; extensionI++) {
				ExtensionType extension = extensions[extensionI];
				ExtensionDescription extDescription = ExtensionsLoader
						.getInstance().getExtension(extension.getName());
				if (extDescription != null
						&& extDescription.getServiceExtensionDescription() != null) {
					if (extDescription.getServiceExtensionDescription().getShouldBeRemoved() != null
							&& extDescription.getServiceExtensionDescription()
							    .getShouldBeRemoved().booleanValue()) {
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}

	
	private void remove(IntroduceUpgradeStatus status) {
		List<String> toBeRemoved = new ArrayList<String>();

		List<ExtensionType> newExtensions = new ArrayList<ExtensionType>();
		String extensionsPropertyString = "";
		ExtensionType[] extensions = serviceInformation.getServiceDescriptor()
				.getExtensions().getExtension();
		if (extensions != null) {
			for (int extensionI = 0; extensionI < extensions.length; extensionI++) {
				ExtensionType extension = extensions[extensionI];
				ExtensionDescription extDescription = ExtensionsLoader
						.getInstance().getExtension(extension.getName());
				if (extDescription != null
						&& extDescription.getServiceExtensionDescription() != null
						&& extDescription.getServiceExtensionDescription().getShouldBeRemoved() != null
						&& extDescription.getServiceExtensionDescription().getShouldBeRemoved().booleanValue()) {
					toBeRemoved.add(extension.getName());
					if (extDescription.getServiceExtensionDescription().getServiceExtensionRemover() != null) {
						try {
							ServiceExtensionRemover remover =
							    ExtensionTools.getServiceExtensionRemover(
							        extension.getName());
							if (remover != null) {
								remover.remove(ExtensionsLoader.getInstance()
										.getServiceExtension(extension.getName()),
										serviceInformation);
								status.addDescriptionLine("Removed extension " + extension.getName());
							}
						} catch (ExtensionRemovalException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					newExtensions.add(extension);
					if (extensionsPropertyString.length() > 0) {
						extensionsPropertyString += ",";
					}
					extensionsPropertyString += extension.getName();
				}
			}
			ExtensionType[] newExtensionsArr = 
			    new ExtensionType[newExtensions.size()];
			System.arraycopy(newExtensions.toArray(), 0, newExtensionsArr, 0, newExtensionsArr.length);
			serviceInformation.getServiceDescriptor().getExtensions().setExtension(newExtensionsArr);
			serviceInformation.getIntroduceServiceProperties().setProperty(
				IntroduceConstants.INTRODUCE_SKELETON_EXTENSIONS,
				extensionsPropertyString);
		}
	}

	
	public void upgrade(IntroduceUpgradeStatus status) throws Exception {
		remove(status);

		List<String> error = new ArrayList<String>();

		ExtensionType[] extensions = serviceInformation.getServiceDescriptor()
				.getExtensions().getExtension();
		for (int extensionI = 0; extensionI < extensions.length; extensionI++) {
			ExtensionType extension = extensions[extensionI];
			String serviceExtensionVersion = extension.getVersion();
			ExtensionDescription extDescription = 
			    ExtensionsLoader.getInstance().getExtension(extension.getName());
			if (extDescription != null) {
				if ((extDescription.getVersion() != null)) {
					List<UpgradeDescriptionType> upgrades = new ArrayList<UpgradeDescriptionType>();
					if (((serviceExtensionVersion == null) && 
					    (extDescription.getVersion() != null)) || 
					    !extDescription.getVersion().equals(serviceExtensionVersion)) {
						// service needs to be upgraded
						// put together a list of upgrades to run
						UpgradeDescriptionType[] extensionUpgrades = null;
						if ((extDescription.getUpgradesDescription() != null)
								&& (extDescription.getUpgradesDescription()
										.getUpgradeDescription() != null)) {
							extensionUpgrades = extDescription.getUpgradesDescription().getUpgradeDescription();

							String currentVersion = serviceExtensionVersion;
							while (((currentVersion == null) || 
							    !currentVersion.equals(extDescription.getVersion()))) {
								boolean found = false;
								int i = 0;
								for (i = 0; i < extensionUpgrades.length; i++) {
									if ((extensionUpgrades[i].getFromVersion() == null)
											&& (currentVersion == null)) {
										found = true;
										break;
									} else if (extensionUpgrades[i].getFromVersion() != null
											&& extensionUpgrades[i].getFromVersion().equals(currentVersion)) {
										found = true;
										break;
									}
								}
								if (found) {
									upgrades.add(extensionUpgrades[i]);
									currentVersion = extensionUpgrades[i].getToVersion();
								} else {
									error.add(extension.getName()
										+ " extension used on service is older than currently installed "
										+ "and does not appear to have correct upgrade.");
									break;
								}
							}
						} else {
							error.add(extension.getName()
								+ " extension used on service is older than currently "
								+ "installed and does not appear to have any upgrades.");
						}
					}

					// run the upgraders that we put together in order
					for (int i = 0; i < upgrades.size(); i++) {
						UpgradeDescriptionType upgrade = upgrades.get(i);
						Class<?> clazz = ExtensionTools.loadExtensionClass(upgrade.getUpgradeClass());
						Constructor<?> con = clazz.getConstructor(new Class[] {
								ExtensionType.class, ServiceInformation.class,
								String.class, String.class, String.class });
						ExtensionUpgraderI upgrader = (ExtensionUpgraderI) con.newInstance(
						        new Object[] { extension,
										serviceInformation, pathToService,
										upgrade.getFromVersion(),
										upgrade.getToVersion() });
						upgrader.execute();
						status.addExtensionUpgradeStatus(upgrader.getStatus());
					}
				}
			} else {
				error.add(extension.getName()
					+ " extension used on service is not available in this instance of Introduce.");
			}
		}

		if (error.size() > 0) {
		    // at least one error happened during the upgrade process.
		    // turn that into a string and throw an exception
			String errorString = "";
			for (int errorI = 0; errorI < error.size(); errorI++) {
				errorString += error.get(errorI) + "\n";
			}
			throw new Exception(errorString);
		}
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alfresco.support.expert.jmxdumpanalyzerfx;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.*;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.math.RoundingMode;
import java.util.Scanner;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

/**
 *
 * @author astrachan
 * @version 2.x
 * 
 *          This is a complete JavaFX re-write of the original SWT
 *          JMXDumpAnalyzer utility. Basically this utility takes an incoming
 *          JMXDump file (generated from Alfresco) and not only displays
 *          relevant information in a logical sense but also performs some
 *          analysis of the data to aid support activities
 * 
 *          2.0.0 -- astrachan, re-vamped for 5.1 plus implemented the ability
 *          to drag and drop the dump file into the application as well as open
 *          Alfresco generated ZIP files
 * 
 *          2.0.1 -- astrachan, added pressing ENTER on search box functionality
 *          (feature request ;) )
 * 
 *          2.0.2 -- astrachan, added parsing and displaying of appliedVersion
 *          information so that it's easier to see the upgrade path
 *          
 *          2.0.3 -- astrachan, added repo management info and some other bits n' pieces
 *          
 *          2.0.4 -- astrachan
 *          Corrected smart folders (from virtual folders)
 *          Implemented functionality for increasing/decreasing font sizes
 *          Implemented non-modal selection (experimental) where a user can select not to have modal
 *          windows. i.e. as many windows can be created as you like. You are not restricted to one at a time
 *          Tidied things up a little more
 *          
 *          2.1.0 -- astrachan
 *          General cleaning up of the UI
 *          Tidied amps, populated cron properly
 *          Added serverInfo and readonly? to basic information
 *          Added new 'Java' tab for java-specific system info
 *          Separated AMP information in the AMP tab - much easier on the eyes
 *          Made the index information specific to the current index.subsystem, instead of listing EVERYTHING that pattern matches. This will make getting the correct information out for indexing easier
 *          
 *          2.1.1 -- astrachan
 *          Extract start-time and up-time from JVM runtime variables, then calculate them into human readable form (Basics tab)
 *          
 *          2.1.2 -- astrachan
 *          Cleaned up UI
 *          
 *          2.1.3 -- astrachan, XP-66 | fixed rubbish number of document in index. This time we isolate the indexing subsystem and count accordingly. Before we were also adding the total of docs in the SOLR searcher objects...
 *          
 * 
 */

public class JmxMainController implements Initializable {

	public static String version = "2.1.3";
	public static String filePath = null;
	public static File file;
	public static File openedZipfile;

	private static Set<String> basics = new HashSet<String>();
	private static Set<String> usergroups = new HashSet<String>();
	private static Set<String> basicMem = new HashSet<String>();

	public static Set<String> setGlobalProps = new HashSet<String>();
	private static Set<String> setRepo = new HashSet<String>();
	public static Set<String> setJVMProps = new HashSet<String>();
	public static Set<String> setOSProps = new HashSet<String>();
	public static Set<String> setSysProps = new HashSet<String>();
	public static String crons = "";
	public static String mods = "";

	static SortedSet setAppliedVersions = new TreeSet();

	public static String maxMem = "";
	private static Scanner scanner;
	private static long docCount;
	private static String customer = "";
	private static String indexing = "";

	private static ZipFile zipfile;

	@FXML
	private Label lblFileName;
	@FXML
	private Label lblFileSize;
	@FXML
	private Label lblCapture;
	@FXML
	private Label lblCustomer;
	
	@FXML
	public Button btnIncreaseFont;
	
	@FXML
	public Button btnDecreaseFont;
	
	@FXML
	public Label lblFontSize;
	
	@FXML
	public CheckBox chkModal;

	// TextAreas in the UI
	@FXML
	public TextArea txtBasic;
	@FXML
	public TextArea txtJVM;
	@FXML
	public TextArea txtOS;
	@FXML
	public TextArea txtDirs;
	@FXML
	public TextArea txtLic;
	@FXML
	public TextArea txtCluster;
	@FXML
	public TextArea txtDB;
	@FXML
	public TextArea txtAuth;
	@FXML
	public TextArea txtFS;
	@FXML
	public TextArea txtIndex;
	@FXML
	public TextArea txtAudit;
	@FXML
	public TextArea txtMail;
	@FXML
	public TextArea txtModule;
	@FXML
	public TextArea txt3rdParty;
	@FXML
	public TextArea txtBPM;
	@FXML
	public TextArea txtTransform;
	@FXML
	public TextArea txtJMS;
	@FXML
	public TextArea txtCMIS;
	@FXML
	public TextArea txtAct;
	@FXML
	public TextArea txtCron;
	@FXML
	public TextArea txtCache;
	@FXML
	public TextArea txtVF;
	@FXML
	public TextArea txtSys;
	@FXML
	public TextArea txtJava;
	@FXML
	public TextArea txtOther;
	@FXML
	public TextArea txtSearchResults;

	@FXML
	public MenuItem menuOpenFile;
	@FXML
	public MenuItem menuOpenZip;
	@FXML
	public MenuItem menuReset;
	@FXML
	public MenuItem menuRec;
	@FXML
	public MenuItem menuVersion;
	@FXML
	public MenuItem menuCon;
	@FXML
	public MenuItem menuMem;
	@FXML
	public MenuItem menuEditor;
	@FXML
	public MenuItem menuCacheStats;
	@FXML
	public MenuItem menuSOLR;

	@FXML
	public TextField txtSearch;
	@FXML
	public Button btnSearch;
	@FXML
	public TabPane myTabPane;
	@FXML
	public Tab myTab;
	@FXML
	public CheckBox chkBox;
	@FXML
	public AnchorPane mainAnchorPane;
	
	@FXML
	private void handleIncreaseFont(ActionEvent event) throws IOException {
		Double fnt = txtBasic.getFont().getSize();
		fnt ++;
		
		if (fnt <= 17){
		txtBasic.setStyle("-fx-font-size:" + fnt);;
		txtJVM.setStyle("-fx-font-size:" + fnt);;
		txtOS.setStyle("-fx-font-size:" + fnt);;
		txtDirs.setStyle("-fx-font-size:" + fnt);;
		txtLic.setStyle("-fx-font-size:" + fnt);;
		txtCluster.setStyle("-fx-font-size:" + fnt);;
		txtDB.setStyle("-fx-font-size:" + fnt);;
		txtAuth.setStyle("-fx-font-size:" + fnt);;
		txtFS.setStyle("-fx-font-size:" + fnt);;
		txtIndex.setStyle("-fx-font-size:" + fnt);;
		txtAudit.setStyle("-fx-font-size:" + fnt);;
		txtMail.setStyle("-fx-font-size:" + fnt);;
		txtModule.setStyle("-fx-font-size:" + fnt);;
		txt3rdParty.setStyle("-fx-font-size:" + fnt);;
		txtBPM.setStyle("-fx-font-size:" + fnt);;
		txtTransform.setStyle("-fx-font-size:" + fnt);;
		txtJMS.setStyle("-fx-font-size:" + fnt);;
		txtCMIS.setStyle("-fx-font-size:" + fnt);;
		txtAct.setStyle("-fx-font-size:" + fnt);;
		txtCron.setStyle("-fx-font-size:" + fnt);;
		txtCache.setStyle("-fx-font-size:" + fnt);;
		txtVF.setStyle("-fx-font-size:" + fnt);;
		txtSys.setStyle("-fx-font-size:" + fnt);;
		txtJava.setStyle("-fx-font-size:" + fnt);;
		txtOther.setStyle("-fx-font-size:" + fnt);;
		txtSearchResults.setStyle("-fx-font-size:" + fnt);;
		lblFontSize.setText(fnt.toString());
		}
	}
	
	
	@FXML
	private void handleDecreaseFont(ActionEvent event) throws IOException {
		Double fnt = txtBasic.getFont().getSize();
		fnt --; 
		
		if (fnt >= 8){
		txtBasic.setStyle("-fx-font-size:" + fnt);
		txtJVM.setStyle("-fx-font-size:" + fnt);;
		txtOS.setStyle("-fx-font-size:" + fnt);;
		txtDirs.setStyle("-fx-font-size:" + fnt);;
		txtLic.setStyle("-fx-font-size:" + fnt);;
		txtCluster.setStyle("-fx-font-size:" + fnt);;
		txtDB.setStyle("-fx-font-size:" + fnt);;
		txtAuth.setStyle("-fx-font-size:" + fnt);;
		txtFS.setStyle("-fx-font-size:" + fnt);;
		txtIndex.setStyle("-fx-font-size:" + fnt);;
		txtAudit.setStyle("-fx-font-size:" + fnt);;
		txtMail.setStyle("-fx-font-size:" + fnt);;
		txtModule.setStyle("-fx-font-size:" + fnt);;
		txt3rdParty.setStyle("-fx-font-size:" + fnt);;
		txtBPM.setStyle("-fx-font-size:" + fnt);;
		txtTransform.setStyle("-fx-font-size:" + fnt);;
		txtJMS.setStyle("-fx-font-size:" + fnt);;
		txtCMIS.setStyle("-fx-font-size:" + fnt);;
		txtAct.setStyle("-fx-font-size:" + fnt);;
		txtCron.setStyle("-fx-font-size:" + fnt);;
		txtCache.setStyle("-fx-font-size:" + fnt);;
		txtVF.setStyle("-fx-font-size:" + fnt);;
		txtSys.setStyle("-fx-font-size:" + fnt);;
		txtJava.setStyle("-fx-font-size:" + fnt);;
		txtOther.setStyle("-fx-font-size:" + fnt);;
		txtSearchResults.setStyle("-fx-font-size:" + fnt);;
		lblFontSize.setText(fnt.toString());
		}
	}

	@FXML
	private void handleFileEditor(ActionEvent event) throws IOException {
		if (file != null) {
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				String cmd = "rundll32 url.dll,FileProtocolHandler " + file.getCanonicalPath();
				Runtime.getRuntime().exec(cmd);
			} else {
				Desktop.getDesktop().edit(file);
			}
		}
	}

	@FXML
	private void handleSearch(ActionEvent event) throws IOException 
	{
		doSearch();
	}
	
	// Implemented pressing ENTER on search box as feature request - astrachan
	@FXML
	public void searchEnterPressed(KeyEvent ke) throws IOException {
		if (ke.getCode().equals(KeyCode.ENTER)) 
		{
			doSearch();
		}
	}
	
	private void doSearch() throws IOException {

		txtSearchResults.setText("");

		if ((txtSearch.getText().length() > 1) && !chkBox.isSelected()) {
			txtSearchResults.appendText("### Searched tabs ###\r\n" + "\r\n");

			scanner = new Scanner(txtBasic.getText());
			scanner.useDelimiter("\\n");

			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Basic Tab)\r\n");
				}
			}

			scanner.close();
			scanner = new Scanner(txtJVM.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in JVM Tab)\r\n");
				}
			}

			scanner.close();
			scanner = new Scanner(txtOS.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in OS Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtDirs.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Directories Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtLic.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Licenses Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtCluster.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Cluster Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtDB.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in RDBMS Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtAuth.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Authentication (Auth) Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtFS.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in File-systems (FS) Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtIndex.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Indexing Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtAudit.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Auditing Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtMail.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Mail/IMAP Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtModule.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Modules () Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txt3rdParty.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in 3rd Party Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtBPM.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Workflow (BPM) Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtTransform.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Transformers Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtJMS.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in JMS/Messaging Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtCMIS.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in CMIS Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtAct.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Activities Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtCron.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Cron Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtCache.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Cache Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtVF.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Virtual/Smart folders Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtSys.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in System Tab)\r\n");
				}
			}
			scanner = new Scanner(txtJava.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Java Tab)\r\n");
				}
			}
			scanner.close();
			scanner = new Scanner(txtOther.getText());
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (next.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
					txtSearchResults.appendText(next + "   (in Other (un-categorised) Tab)\r\n");
				}
			}
			myTabPane.getSelectionModel().select(myTab);
		} else {
			// do nothing
		}
		if ((txtSearch.getText().length() > 1) && chkBox.isSelected()) {
			txtSearchResults.appendText("### Searched source JMXDump file ###\r\n" + "\r\n");
			String line5;
			BufferedReader br5 = new BufferedReader(new FileReader(filePath));

			try {
				while ((line5 = br5.readLine()) != null) {
					if (line5.toLowerCase().contains(txtSearch.getText().toLowerCase())) {
						txtSearchResults.appendText(line5.trim() + "\r\n");
					}
				}
				myTabPane.getSelectionModel().select(myTab);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@FXML
	// handler for menu item -> Close event
	private void handleClose(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	// handler for menu item -> Open JMXDump file event
	private void handleFileOpenAction(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open JMXDump text file");
		file = fileChooser.showOpenDialog(null);
		if (file != null && file.isFile()) {
			resetAll();
			filePath = file.getCanonicalPath();
			lblFileName.setText("File: " + filePath);
			lblFileSize.setText("Size: " + file.length());
			menuReset.setDisable(false);
			menuRec.setDisable(false);
			menuVersion.setDisable(false);
			menuCon.setDisable(false);
			menuMem.setDisable(false);
			menuEditor.setDisable(false);
			menuCacheStats.setDisable(false);
			menuSOLR.setDisable(false);
			txtSearch.setDisable(false);
			btnSearch.setDisable(false);
			chkModal.setSelected(false);
			chkModal.setDisable(false);
			btnIncreaseFont.setDisable(false);
			btnDecreaseFont.setDisable(false);
			menuOpenFile.setDisable(true);
			menuOpenZip.setDisable(true);
			chkBox.setDisable(false);
			localParse(filePath);
		}
	}
	
	

	@FXML
	// handler for menu item -> Open ZIP file event
	private void handleFileOpenZipAction(ActionEvent event) throws IOException {
		Boolean fileGood = true;
		File targetFile = new File("unzippedJMXDumpfile.txt");
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Open JMXDump ZIP file");
		openedZipfile = fileChooser.showOpenDialog(null);

		try {
			if (openedZipfile != null) {
				zipfile = new ZipFile(openedZipfile);
			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			System.out.println("Unrecognised zip file....");
			e.printStackTrace();
		} catch (NullPointerException e) {
			// swallow this one
		}
		try {
		ZipEntry zipentry;
		int fileNumber = 0;
		for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements(); fileNumber++) {
			zipentry = e.nextElement();

			if (!zipentry.isDirectory()) {
				InputStream inputStream = zipfile.getInputStream(zipentry);

				// if there is a file already on the FS, delete it.
				if (targetFile.exists()) {
					targetFile.delete();
				}
				FileUtils.copyInputStreamToFile(inputStream, targetFile);
			} else {
				fileGood = false;
				System.out.println(
						"This zip file doesn't look like it came directly from Alfresco. Try and unzip the file yourself first");
				if (targetFile.exists()) {
					targetFile.delete();
				}
				resetAll();
				break;
			}
		}
		} catch (NullPointerException e){
			// don't worry about it
		}

		if (fileGood && targetFile != null && targetFile.isFile()) {
			resetAll();
			file = targetFile;
			filePath = targetFile.getCanonicalPath();
			lblFileName.setText("File: " + filePath);
			lblFileSize.setText("Size: " + file.length());
			menuReset.setDisable(false);
			menuRec.setDisable(false);
			menuVersion.setDisable(false);
			menuCon.setDisable(false);
			menuMem.setDisable(false);
			menuEditor.setDisable(false);
			menuCacheStats.setDisable(false);
			menuSOLR.setDisable(false);
			txtSearch.setDisable(false);
			btnSearch.setDisable(false);
			chkModal.setSelected(false);
			chkModal.setDisable(false);
			btnIncreaseFont.setDisable(false);
			btnDecreaseFont.setDisable(false);
			menuOpenFile.setDisable(true);
			menuOpenZip.setDisable(true);
			chkBox.setDisable(false);
			localParse(filePath);
		}

	}

	@FXML
	private void handleDragFile(DragEvent event) throws IOException {
		mouseDragDropped(event);
	}

	@FXML
	private void handleDragOver(DragEvent event) throws IOException {
		mouseDragOver(event);
	}

	private void mouseDragDropped(final DragEvent e) {

		final Dragboard db = e.getDragboard();
		boolean success = false;
		if (db.hasFiles()) {
			success = true;
			// Only get the first file from the list
			file = db.getFiles().get(0);
			Platform.runLater(new Runnable() {
				public void run() {
					if (file != null && file.isFile()) {
						resetAll();
						try {
							filePath = file.getCanonicalPath();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						lblFileName.setText("File: " + filePath);
						lblFileSize.setText("Size: " + file.length());
						menuReset.setDisable(false);
						menuRec.setDisable(false);
						menuVersion.setDisable(false);
						menuCon.setDisable(false);
						menuMem.setDisable(false);
						menuEditor.setDisable(false);
						menuCacheStats.setDisable(false);
						menuSOLR.setDisable(false);
						txtSearch.setDisable(false);
						btnSearch.setDisable(false);
						chkModal.setSelected(false);
						chkModal.setDisable(false);
						btnIncreaseFont.setDisable(false);
						btnDecreaseFont.setDisable(false);
						menuOpenFile.setDisable(true);
						menuOpenZip.setDisable(true);
						chkBox.setDisable(false);

						try {
							localParse(filePath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
		}
		e.setDropCompleted(success);
		e.consume();
	}

	private void mouseDragOver(final DragEvent e) {
		final Dragboard db = e.getDragboard();

		final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".txt");

		if (db.hasFiles()) {
			if (isAccepted) {
				e.acceptTransferModes(TransferMode.COPY);
			}
		} else {
			e.consume();
		}
	}

	@FXML
	// handler for menu Help -> About
	private void handleAboutAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("JmxAbout.fxml"));
		Stage stage = new Stage();

		stage.setScene(new Scene(root));
		stage.setTitle("jmxdump-analyzer-fx | about");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	// handler for menu Tools -> Memory
	private void handleMemoryAnalysisAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("JmxMem.fxml"));
		
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheets/textAreas.css").toExternalForm());
		
		Stage stage = new Stage();

		stage.setScene(scene);
		stage.setTitle("jmxdump-analyzer-fx | memory analysis");
		if (chkModal.isSelected()){
			stage.initModality(Modality.NONE);
		}
		else {
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		stage.show();
	}

	@FXML
	// handler for menu Tools -> SOLR
	private void handleSOLRAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("JmxSOLR.fxml"));
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheets/textAreas.css").toExternalForm());
        
		Stage stage = new Stage();

		stage.setScene(scene);
		stage.setTitle("jmxdump-analyzer-fx | SOLR");
		if (chkModal.isSelected()){
			stage.initModality(Modality.NONE);
		}
		else {
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		stage.show();
	}

	@FXML
	// handler for menu Tools -> Memory
	private void handleCacheStatisticsAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("JmxCacheStats.fxml"));
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheets/textAreas.css").toExternalForm());
		
		Stage stage = new Stage();

		stage.setScene(scene);
		stage.setTitle("jmxdump-analyzer-fx | cache statistics");
		if (chkModal.isSelected()){
			stage.initModality(Modality.NONE);
		}
		else {
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		stage.show();
	}

	@FXML
	// handler for menu Tools -> Version info
	private void handleAppliedToServerStatisticsAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("JmxAppliedToServer.fxml"));
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheets/textAreas.css").toExternalForm());
		Stage stage = new Stage();

		stage.setScene(scene);
		stage.setTitle("jmxdump-analyzer-fx | version info");
		if (chkModal.isSelected()){
			stage.initModality(Modality.NONE);
		}
		else {
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		stage.show();
	}

	@FXML
	// handler for menu Tools -> ContentStore
	private void handleCSAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("JmxCS.fxml"));
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheets/textAreas.css").toExternalForm());
        
		Stage stage = new Stage();

		stage.setScene(scene);
		stage.setTitle("jmxdump-analyzer-fx | contentstore info");
		if (chkModal.isSelected()){
			stage.initModality(Modality.NONE);
		}
		else {
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		stage.show();
	}

	@FXML
	// handler for menu Tools -> ContentStore
	private void handleRecAction(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("JmxRec.fxml"));
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add(this.getClass().getResource("/stylesheets/textAreas.css").toExternalForm());
        
		Stage stage = new Stage();

		stage.setScene(scene);
		stage.setTitle("jmxdump-analyzer-fx | recommendations");
		if (chkModal.isSelected()){
			stage.initModality(Modality.NONE);
		}
		else {
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		stage.show();
	}

	@FXML
	// handler for resetting everything
	private void handleReset(ActionEvent event) {
		resetAll();
	}

	private void localParse(String filePath) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filePath));
		BufferedReader br2 = new BufferedReader(new FileReader(filePath));
		BufferedReader br3 = new BufferedReader(new FileReader(filePath));
		BufferedReader br4 = new BufferedReader(new FileReader(filePath));
		BufferedReader br5 = new BufferedReader(new FileReader(filePath));
		BufferedReader br6 = new BufferedReader(new FileReader(filePath));
		BufferedReader br7 = new BufferedReader(new FileReader(filePath));

		int modCount = 0;

		try {
			String line;  // Global propertiessetRepo
			String line3; // General info - in fact, just about all of it
			String line4; // OS properties
			String line5; // JVM properties
			String line6; // Helper for docCount work
			String line7; // Simply exists to get server up-time and start-time

			// populate a String set with the globalProperties from the JMX dump
			// file
			while ((line = br.readLine()) != null) {
				if (line.contains("Alfresco:Name=GlobalProperties")) {
					while (line != null && !line.isEmpty()) {
						setGlobalProps.add(line);
						line = br.readLine();
					}
				}
			}
			// populate a String set with the JVM properties for the repo
			while ((line5 = br5.readLine()) != null) {
				if (line5.contains("InputArguments")) {
					while (!line5.contains("  ]") && !line5.isEmpty()) {
						setJVMProps.add(line5);
						line5 = br5.readLine();
					}
				}
			}
			
			// get runtime information starting with start-time and up-time fromm the JVM (human readable stuff is calculated afterwards)
			while ((line7 = br7.readLine()) != null) {
				if (line7.contains("** Object Name") && line7.contains("java.lang:type=Runtime")) {
					while (!line7.isEmpty()) {
						if (line7.startsWith("StartTime") || line7.startsWith("Uptime")) {
							basics.add(line7);
						}
						line7 = br7.readLine();
					}
				}
			}
			

			// populate the Basic info tab as we go along
			while ((line3 = br3.readLine()) != null) {

				if (line3.contains("Alfresco:Name=RepositoryDescriptor,Type=Current")) {
					while (line3 != null && !line3.isEmpty()) {
						if (line3.startsWith("Version ") || line3.startsWith("Name ") || line3.startsWith("Schema ")
								|| line3.startsWith("Id  ")) {
							basics.add(line3);
						}
						line3 = br3.readLine();
					}
				}
				if (line3.contains("Alfresco:Name=SystemProperties")) {
					while (line3 != null && !line3.isEmpty()) {
						
						if (line3.startsWith("java.")){
							txtJava.appendText(line3 + "\r\n");
							if (line3.startsWith("java.version ") || line3.startsWith("java.vm.name ") || line3.startsWith("java.vm.vendor")) {
								basics.add(line3);
								setSysProps.add(line3);
							}
						}
						line3 = br3.readLine();
					}
				}
				
				if (line3.contains("Alfresco:Name=RepoServerMgmt")) {
					while (line3 != null && !line3.isEmpty()) {
						if (line3.startsWith("TicketCountAll") || line3.startsWith("TicketCountNonExpired") || line3.startsWith("UserCountAll") || line3.startsWith("UserCountNonExpired")) {
							txtAuth.appendText(line3 + "\r\n");
						}
						if (line3.startsWith("ReadOnly ")) {
							basics.add(line3);
						}
						line3 = br3.readLine();
					}
				}
				
				
				
				
				
				if (line3.startsWith("JmxDumpUtil ")) {
					lblCapture.setText("Captured: " + line3.substring(line3.lastIndexOf(": ") + 2));
				}
				if (line3.startsWith("NumberOfGroups") || line3.startsWith("NumberOfUsers")) {
					usergroups.add(line3);
				}
				if (line3.startsWith("os.name") || line3.startsWith("os.version")) {
					basics.add(line3);
					setSysProps.add(line3);
				}
				if (line3.startsWith("DatabaseProductVersion")) {
					basics.add(line3);
				}
				if (line3.startsWith("DatabaseProductName")) {
					basics.add(line3);
				}
				
				if (line3.startsWith("DatabaseProductName")) {
					basics.add(line3);
				}
				
				if (line3.startsWith("serverInfo")) {
					txtBasic.appendText("App Server: " + line3.substring(line3.lastIndexOf(" ")) + "\r\n");
				}
				

				if (line3.startsWith("chain")) {
					int ind = line3.lastIndexOf(" ");
					txtBasic.appendText("Authentication chain: " + line3.substring(ind) + "\r\n");
				}
				if (line3.startsWith("index.subsystem.name")) {
					txtBasic.appendText("Index subsystem: " + line3.substring(line3.lastIndexOf(" ")) + "\r\n");
					
					// set the indexing subsystem to gather the information specific to it later on.
					indexing = line3.substring(line3.lastIndexOf(" ")).trim();
				}

				if (line3.startsWith("MaxMemory ")) {
					basicMem.add(line3);
					maxMem = line3;
				}
				if (line3.startsWith("TotalMemory ") || line3.startsWith("FreeMemory")) {
					basicMem.add(line3);
				}
				
				try {
					// LUCENE
					if (indexing.equals("lucene") || indexing.isEmpty()) {
						if (line3.startsWith("NumberOfDocuments")) {
							docCount = docCount + Long.parseLong(line3.substring(line3.lastIndexOf(" ") + 1));
						}
					}
					
					// SOLR1, SOLR4, SOLR6
					else 
						{
							if (line3.startsWith("NumDocuments")) {
								docCount = docCount + Long.parseLong(line3.substring(line3.lastIndexOf(" ") + 1));
							}
						}
					} catch (java.lang.NumberFormatException name) {
				}

				// -------------------------------------------------------------------------------------------------------
				// DIRs tab (checked against Global Properties
				if (line3.startsWith("dir.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtDirs.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// License tab (no check needed here)
				if (line3.startsWith("Holder")) {
					customer = line3;
					lblCustomer.setText("Customer: " + customer.substring((customer.indexOf("=") + 1)));
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("Issued")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("LicenseMode")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("Days")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("RemainingDays")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("ValidUntil")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("HeartBeatDisabled")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("location.license.")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("CloudSyncKeyAvailable") || line3.startsWith("ClusterEnabled")
						|| line3.startsWith("CurrentDocs") || line3.startsWith("CurrentUsers")
						|| line3.startsWith("CryptodocEnabled") || line3.startsWith("MaxUsers")) {
					txtLic.appendText(line3 + "\r\n");
				}
				if (line3.contains("Alfresco:Name=ContentTransformer,Type=TransformationServer")) {
					while (line3 != null && !line3.isEmpty()) {
						if (line3.startsWith("LicensedUntil")) {
							txtLic.appendText("Transformation Server: " + line3 + "\r\n");
						}
						if (line3.startsWith("ServerUrl")) {
							txtTransform.appendText("Transformation Server: " + line3 + "\r\n");
						}
						if (line3.startsWith("Available")) {
							txtTransform.appendText("Transformation Server: " + line3 + "\r\n");
						}

						line3 = br3.readLine();
					}
				}

				// -------------------------------------------------------------------------------------------------------

				// clustering
				if (line3.startsWith("alfresco.ehcache.") || line3.startsWith("alfresco.tcp.initial_hosts")
						|| line3.startsWith("alfresco.cluster.") || line3.startsWith("alfresco.jgroups.")
						|| line3.startsWith("alfresco.fping") || line3.startsWith("alfresco.rmi.")
						|| line3.startsWith("alfresco.hazelcast") || line3.startsWith("ClusterName")
						|| line3.startsWith("ClusteringEnabled") || line3.startsWith("HostName")
						|| line3.startsWith("IPAddress") || line3.startsWith("NumClusterMembers")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtCluster.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// DB tab (some checks needed)
				if (line3.startsWith("DefaultTransactionIsolation")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("Url")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("DriverClassName")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("InitialSize")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("MaxActive")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("MaxIdle")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("MaxWait")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("MinEvictableIdleTimeMillis")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("MinIdle")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("NumActive")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("NumIdle")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("RemoveAbandoned")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("RemoveAbandonedTimeout")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("TestOnBorrow")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("TestOnReturn")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("TestWhileIdle")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("DatabaseProductName")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("DatabaseProductVersion")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("DriverName")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("DriverVersion")) {
					txtDB.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("DatabaseMajorVersion")) {
					txtDB.appendText(line3 + "\r\n");
				}
				// -------------------------------------------------------------------------------------------------------

				// Authentication tab
				if (line3.startsWith("ntlm.") || line3.startsWith("authentication.") || line3.startsWith("passthru.")
						|| line3.startsWith("kerberos.") || line3.startsWith("alfresco.authentication.")
						|| line3.startsWith("domain.") || line3.startsWith("synchronization.")
						|| line3.startsWith("create.missing") || line3.startsWith("ldap.synchronization")
						|| line3.startsWith("ldap.authentication") || line3.startsWith("user.name.")) {
					if (setGlobalProps.contains(line3)) {
						// do nothing
					} else {
						txtAuth.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// Filesystem (FS)
				if (line3.startsWith("cifs.") || line3.startsWith("filesystem.") || line3.startsWith("ftp.")
						|| line3.startsWith("nfs.") || line3.startsWith("system.webdav.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtFS.appendText(line3 + "\r\n");
					}
				}
				if (line3.startsWith("CIFSServerAddress")) {
					txtFS.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("CIFSServerEnabled")) {
					txtFS.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("CIFSServerName")) {
					txtFS.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("FTPServerEnabled")) {
					txtFS.appendText(line3 + "\r\n");
				}
				if (line3.startsWith("NFSServerEnabled")) {
					txtFS.appendText(line3 + "\r\n");
				}
				// -------------------------------------------------------------------------------------------------------

				// Indexing Tab
				if (line3.contains("Search$managed$" + indexing)) {
					
					if (line3.substring(line3.lastIndexOf(" ")).trim().equals("Search$managed$" + indexing)) {
					
					while (!line3.isEmpty()){
						
						if (line3.startsWith("search.") 
								|| line3.startsWith("solr.") 
								|| line3.startsWith("solr6.") 
								|| line3.startsWith("tracker.") 
								|| line3.startsWith("lucene.")
								|| line3.startsWith("index.")
								|| line3.startsWith("solr_facets")
								|| line3.startsWith("people.search")
								|| line3.startsWith("solr4.")) {
										
							txtIndex.appendText(line3 + "\r\n");
						}
						line3 = br3.readLine();
					}
				  }
			    }
				// -------------------------------------------------------------------------------------------------------

				// Auditing Tab
				if (line3.startsWith("audit.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtAudit.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// Mail/IMAP Tab
				if (line3.startsWith("mail.") || line3.startsWith("imap.") || line3.startsWith("email.")
						|| line3.startsWith("notification.email") || line3.startsWith("dev.email")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtMail.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// Module info Tab (having to trim() the strings with this one)
				if (line3.contains("module.description")) {
					txtModule.appendText(line3.trim() + "\r\n");
				}
				if (line3.contains("module.id")) {
					modCount++;
					txtModule.appendText(line3.trim() + "\r\n");
				}
				if (line3.contains("module.installDate")) {
					txtModule.appendText(line3.trim() + "\r\n");
				}
				if (line3.contains("module.installState")) {
					txtModule.appendText(line3.trim() + "\r\n");
				}
				if (line3.contains("module.title")) {
					txtModule.appendText(line3.trim() + "\r\n");
				}
				if (line3.contains("module.version")) {
					txtModule.appendText(line3.trim() + "\r\n\r\n");
				}
				
				
				// -------------------------------------------------------------------------------------------------------

				// 3rd Party Tab
				if (line3.startsWith("ooo.") || line3.startsWith("jodconverter.") || line3.startsWith("img.")
						|| line3.startsWith("swf.") || line3.contains("VersionString") || line3.contains("Features:")
						|| line3.contains("Delegates:")) {
					if (setGlobalProps.contains(line3) || line3.contains("VersionString  <null>")) { // do
																										// nothing
					} else {
						txt3rdParty.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// BPM / workflow-related TAB
				if (line3.startsWith("ActivitiEngineEnabled") || line3.startsWith("ActivitiWorkflowDefinitionsVisible")
						|| line3.startsWith("JBPMEngineEnabled") || line3.startsWith("JBPMWorkflowDefinitionsVisible")
						|| line3.startsWith("NumberOfActivitiTaskInstances")
						|| line3.startsWith("NumberOfActivitiWorkflowDefinitionsDeployed")
						|| line3.startsWith("NumberOfActivitiWorkflowInstances")
						|| line3.startsWith("NumberOfJBPMTaskInstances")
						|| line3.startsWith("NumberOfJBPMWorkflowDefinitionsDeployed")
						|| line3.startsWith("NumberOfJBPMWorkflowInstances") || line3.startsWith("system.workflow")
						|| line3.startsWith("hibernate.jdbc") || line3.startsWith("wcm.")
						|| line3.startsWith("hybridworkflow.") || line3.startsWith("avm.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtBPM.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------
				// Transformer information tab
				if (line3.startsWith("content.transformer.") || line3.startsWith("system.thumbnail.")
						|| line3.startsWith("transformserver.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtTransform.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// JMS tab
				if (line3.startsWith("messaging.") || line3.startsWith("messaging.subsystem.")
						|| line3.startsWith("events.subsystem")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtJMS.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// CMIS Tab
				if (line3.startsWith("opencmis.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtCMIS.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// Activities Tab
				if (line3.startsWith("activities.") || line3.contains("activities.enabled")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtAct.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------

				// Cron Tab
				if (line3.contains("Alfresco:Name=Schedule,Group=")) {
					
					while (!line3.contains("]") && !line3.isEmpty()){
						
						if (line3.startsWith("CronExpression")) {
							crons = crons + line3 + "\n";
						}
						
						if (line3.startsWith("Name")) {
							crons = crons + line3 + "\n";
						}
						if (line3.startsWith("JobName")) {
							crons = crons + line3 + "\n";
						}
						
						if (line3.startsWith("RepeatInterval")) {
							crons = crons + line3 + "\n";
						}
						line3 = br3.readLine();
					}
					crons = crons + "\n";
					txtCron.appendText(crons);
					crons = "";
				}
				
				
				// -------------------------------------------------------------------------------------------------------

				// Cache settings tab
				if (line3.startsWith("cache.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtCache.appendText(line3 + "\r\n");
					}
				}

				// -------------------------------------------------------------------------------------------------------
				// Virtual/Smart folders tab
				if (line3.startsWith("virtual.") || line3.startsWith("smart.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtVF.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------
				// System tab
				if (line3.startsWith("system.") || line3.startsWith("ReadOnly")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtSys.appendText(line3 + "\r\n");
					}
				}
				// -------------------------------------------------------------------------------------------------------
				// Java tab
				if (line3.startsWith("sun.") || line3.startsWith("user.")) {
					if (line3.startsWith("user.name.caseSensitive")){
						// do nothing
					}
					else {
						txtJava.appendText(line3 + "\r\n");	 
					}
				}
				// -------------------------------------------------------------------------------------------------------
				// Other settings Tab
				if (line3.startsWith("bulkImport.") || line3.startsWith("avmsync.") || line3.startsWith("deployment.")
						|| line3.startsWith("default.async.") || line3.startsWith("domain.")
						|| line3.startsWith("encryption.") || line3.startsWith("fileFolderService.")
						|| line3.startsWith("home.folder.") || line3.startsWith("download.")
						|| line3.startsWith("home_folder_") || line3.startsWith("orphanReaper.")
						|| line3.startsWith("policy.content.") || line3.startsWith("publishing.")
						|| line3.startsWith("replication.") || line3.startsWith("repo.")
						|| line3.startsWith("sample.site.") || line3.startsWith("security.")
						|| line3.startsWith("server.") || line3.startsWith("share.") || line3.startsWith("shutdown.")
						|| line3.startsWith("spaces.") || line3.startsWith("transferservice.")
						|| line3.startsWith("urlshortening.")
						|| line3.startsWith("version.store") || line3.startsWith("synchronization.")
						|| line3.startsWith("action.") || line3.startsWith("default.")
						|| line3.startsWith("subsystems.") || line3.startsWith("sync.")
						|| line3.startsWith("webscripts.")) {
					if (setGlobalProps.contains(line3)) { // do nothing
					} else {
						txtOther.appendText(line3 + "\r\n");
					}
				}
				if (line3.contains("appliedToServer")) {
					if (setAppliedVersions.contains(line3)) { // do nothing
					} else if (line3.contains("UNKOWN") || line3.contains("UNKNOWN")) {
						// do nothing
					} else {
						setAppliedVersions.add(line3.substring(27));
					}
				}

			}

			for (String basicItem : basics) {
				String[] tokens = basicItem.split("\\s{2,}");
				if (tokens[0].startsWith("Version")) {
					txtBasic.appendText("Alfresco version: " + tokens[1] + "\r\n");
				}
				if (tokens[0].startsWith("ReadOnly")) {
					txtBasic.appendText("System read-only?: " + tokens[1] + "\r\n");
				}
				if (tokens[0].startsWith("StartTime")) {
					try {
					long epoch = Long.parseLong(tokens[1])/1000;
					String startDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date (epoch*1000));
					txtBasic.appendText("JVM Start Time: " + tokens[1] + " / " + startDate + "\r\n");
					} catch (Exception e) {
						txtBasic.appendText("JVM Start Time: " + tokens[1] + "\r\n");
					}
				}
				if (tokens[0].startsWith("Uptime")) {
					try {
					txtBasic.appendText("JVM Uptime: " + tokens[1] + " / approx. " + (Long.parseLong(tokens[1])/1000/60/60 + " hour(s)" + "\r\n"));
					} catch (Exception e) {
						txtBasic.appendText("JVM Uptime: " + tokens[1] + "\r\n");
					}
				}
				
				if (tokens[0].startsWith("java.version")) {
					txtBasic.appendText("Java version: " + tokens[1] + "\r\n");
				}
				if (tokens[0].startsWith("java.vm.name")) {
					txtBasic.appendText("Java details: " + tokens[1] + "\r\n");
				}
				if (tokens[0].startsWith("Id")) {
					txtBasic.appendText("Repo ID: " + tokens[1] + "\r\n");

				}
				if (tokens[0].startsWith("os.name")) {
					txtBasic.appendText("OS: " + tokens[1] + "\r\n");
				}
				if (tokens[0].startsWith("DatabaseProductName")) {
					txtBasic.appendText("Database type: " + tokens[1] + "\r\n");
				}
				if (tokens[0].startsWith("DatabaseProductVersion")) {
					txtBasic.appendText("Database version: " + tokens[1] + "\r\n");
				}

			}
			for (String basicItem : usergroups) {
				String[] tokens = basicItem.split("\\s{1,}");

				if (tokens[0].startsWith("NumberOfGroups")) {
					txtBasic.appendText("Total groups: " + tokens[1] + "\r\n");
				}
				if (tokens[0].startsWith("NumberOfUsers")) {
					txtBasic.appendText("Total users: " + tokens[1] + "\r\n");
				}

			}

			for (String basicItem : basicMem) {
				String[] tokens = basicItem.split("\\s{2,}");
				if (tokens[0].startsWith("MaxMemory")) {
					Double gbVal = (((Double.parseDouble(tokens[1]) / 1024) / 1024) / 1024);
					txtBasic.appendText("Maximum heap: " + tokens[1] + " bytes  / " + round(gbVal, 2) + "GB\r\n");
				}
				if (tokens[0].startsWith("TotalMemory")) {
					Double gbVal = (((Double.parseDouble(tokens[1]) / 1024) / 1024) / 1024);
					txtBasic.appendText("Allocated/committed heap: " + tokens[1] + " bytes  / " + round(gbVal, 2) + "GB\r\n");
				}
				if (tokens[0].startsWith("FreeMemory")) {
					Double gbVal = (((Double.parseDouble(tokens[1]) / 1024) / 1024) / 1024);
					txtBasic.appendText("Remaining free committed heap memory: " + tokens[1] + " bytes  / " + round(gbVal, 2) + "GB\r\n");
				}
			}

			txtBasic.appendText("Number of modules/amps: " + modCount + "\r\n");

			if (docCount != 0) {
				txtBasic.appendText("Approx. index document count: " + docCount + "\r\n");
			}

			// JVM Props
			for (String jvmProp : setJVMProps) {
				if (jvmProp.contains(",") || jvmProp.contains("InputArguments") || jvmProp.contains("[")) {
					// do nothing
				} else {
					txtJVM.appendText(jvmProp.trim() + "\r\n");
				}
			}

			// populate a String set with the OS properties for the repo
			while ((line4 = br4.readLine()) != null) {
				if (line4.contains("type=OperatingSystem")) {
					while (!line4.contains("]") && !line4.isEmpty()) {
						if (line4.contains("Object")) {
							// do nothing
						} else {
							setOSProps.add(line4);
							txtOS.appendText(line4 + "\r\n");
						}
						line4 = br4.readLine();
					}
				}
			}
			br.readLine();

		} catch (IOException e) {
			// Handle exception
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// work through the Global Properties for each set of properties and populate accordingly.
		for (String prop : setGlobalProps) {
			if (prop.startsWith("dir.")) {
				txtDirs.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("alfresco.ehcache.") || prop.startsWith("alfresco.tcp.initial_hosts")
					|| prop.startsWith("alfresco.cluster") || prop.startsWith("alfresco.jgroups.")
					|| prop.startsWith("alfresco.fping") || prop.startsWith("alfresco.rmi.")
					|| prop.startsWith("alfresco.hazelcast") || prop.startsWith("ClusterName")
					|| prop.startsWith("ClusteringEnabled") || prop.startsWith("HostName")
					|| prop.startsWith("IPAddress") || prop.startsWith("NumClusterMembers")) {
				txtCluster.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("db.") || prop.startsWith("mybatis.") || prop.startsWith("hibernate.")) {
				txtDB.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("ntlm.") || prop.startsWith("authentication.") || prop.startsWith("passthru.")
					|| prop.startsWith("kerberos.") || prop.startsWith("alfresco.authentication.")
					|| prop.startsWith("domain.") || prop.startsWith("synchronization.")
					|| prop.startsWith("create.missing") || prop.startsWith("ldap.synchronization")
					|| prop.startsWith("ldap.authentication") || prop.startsWith("user.name.")) {
				txtAuth.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("cifs.") || prop.startsWith("filesystem.") || prop.startsWith("ftp.")
					|| prop.startsWith("nfs.") || prop.startsWith("system.webdav.")) {
				txtFS.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("index.") || prop.startsWith("lucene.") || prop.startsWith("solr.") || prop.startsWith("solr6.")
					|| prop.startsWith("solr4.") || prop.startsWith("search.") || prop.startsWith("fts.")
					|| prop.startsWith("tracker.") || prop.startsWith("solr_facets")
					|| prop.startsWith("people.search")) {
				txtIndex.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("audit.")) {
				txtAudit.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("mail.") || prop.startsWith("imap.") || prop.startsWith("email.")
					|| prop.startsWith("notification.email") || prop.startsWith("dev.email")) {
				txtMail.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("ooo.") || prop.startsWith("jodconverter.") || prop.startsWith("img.")
					|| prop.startsWith(".swf")) {
				txt3rdParty.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("system.workflow.") || prop.startsWith("hibernate.jdbc") || prop.startsWith("wcm.")
					|| prop.startsWith("wcm.") || prop.startsWith("hybridworkflow.") || prop.startsWith("avm.")) {

				txtBPM.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("content.transformer.") || prop.startsWith("system.thumbnail.")
					|| prop.startsWith("transformserver.")) {
				txtTransform.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("messaging.") || prop.startsWith("messaging.subsystem.")
					|| prop.startsWith("events.subsystem")) {
				txtJMS.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("opencmis.")) {
				txtCMIS.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("activities.") || prop.contains("activities.enabled")) {
				txtAct.appendText("** " + prop + "\r\n");
			}
			if (prop.contains(".cronExpression")) {
				txtCron.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("cache.")) {
				txtCache.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("virtual.") || prop.startsWith("smart.")) {
				txtVF.appendText("** " + prop + "\r\n");
			}
			if (prop.startsWith("system.")) {
				txtSys.appendText("** " + prop + "\r\n");
			}

			if (prop.startsWith("bulkImport.") || prop.startsWith("avmsync.") || prop.startsWith("deployment.")
					|| prop.startsWith("default.async.") || prop.startsWith("domain.") || prop.startsWith("encryption.")
					|| prop.startsWith("fileFolderService.") || prop.startsWith("home.folder.")
					|| prop.startsWith("home_folder_") || prop.startsWith("download.")
					|| prop.startsWith("orphanReaper.") || prop.startsWith("policy.content.")
					|| prop.startsWith("publishing.") || prop.startsWith("replication.") || prop.startsWith("repo.")
					|| prop.startsWith("sample.site.") || prop.startsWith("security.") || prop.startsWith("server.")
					|| prop.startsWith("share.") || prop.startsWith("shutdown.") || prop.startsWith("spaces.")
					|| prop.startsWith("transferservice.") || prop.startsWith("urlshortening.")
					|| prop.startsWith("version.store") || prop.startsWith("action.")
					|| prop.startsWith("default.") || prop.startsWith("subsystems.") || prop.startsWith("sync.")
					|| prop.startsWith("webscripts."))

			{
				txtOther.appendText("** " + prop + "\r\n");
			}
		}
		
		if (indexing.equals("")) {
			txtBasic.appendText("Unknown indexing subsystem, assuming Lucene \r\n");
		}
	}

	public void resetAll() {
		lblFileName.setText("");
		lblFileSize.setText("");
		lblCapture.setText("");
		lblCustomer.setText("");
		
		maxMem = "";
		filePath = null;
		docCount = 0;
		crons = "";
		indexing = "";

		txtBasic.setText("");
		txtJVM.setText("");
		txtOS.setText("");
		txtDirs.setText("");
		txtLic.setText("");
		txtCluster.setText("");
		txtDB.setText("");
		txtAuth.setText("");
		txtFS.setText("");
		txtIndex.setText("");
		txtAudit.setText("");
		txtMail.setText("");
		txtModule.setText("");
		txt3rdParty.setText("");
		txtBPM.setText("");
		txtTransform.setText("");
		txtCMIS.setText("");
		txtAct.setText("");
		txtCron.setText("");
		txtCache.setText("");
		txtOther.setText("");
		txtJMS.setText("");
		txtVF.setText("");
		txtSys.setText("");
		txtJava.setText("");
		txtSearchResults.setText("");
		resetFonts();
		lblFontSize.setText("");
		
		basics = new HashSet<String>();
		usergroups = new HashSet<String>();
		basicMem = new HashSet<String>();

		setGlobalProps.clear();
		setRepo.clear();
		setJVMProps.clear();
		setOSProps.clear();
		setSysProps.clear();
		setAppliedVersions.clear();

		menuReset.setDisable(true);
		menuRec.setDisable(true);
		menuVersion.setDisable(true);
		menuCon.setDisable(true);
		menuMem.setDisable(true);
		menuEditor.setDisable(true);
		menuCacheStats.setDisable(true);
		menuSOLR.setDisable(true);
		menuOpenFile.setDisable(false);
		menuOpenZip.setDisable(false);
		txtSearch.setText("");
		txtSearch.setDisable(true);
		btnSearch.setDisable(true);
		chkModal.setSelected(false);
		chkModal.setDisable(true);
		btnIncreaseFont.setDisable(true);
		btnDecreaseFont.setDisable(true);
		chkBox.setSelected(false);
		chkBox.setDisable(true);
	}
	
	public void resetFonts() {
		txtBasic.setStyle("-fx-font-size:" + 12);
		txtJVM.setStyle("-fx-font-size:" + 12);
		txtOS.setStyle("-fx-font-size:" + 12);
		txtDirs.setStyle("-fx-font-size:" + 12);
		txtLic.setStyle("-fx-font-size:" + 12);
		txtCluster.setStyle("-fx-font-size:" + 12);
		txtDB.setStyle("-fx-font-size:" + 12);
		txtAuth.setStyle("-fx-font-size:" + 12);
		txtFS.setStyle("-fx-font-size:" + 12);
		txtIndex.setStyle("-fx-font-size:" + 12);
		txtAudit.setStyle("-fx-font-size:" + 12);
		txtMail.setStyle("-fx-font-size:" + 12);
		txtModule.setStyle("-fx-font-size:" + 12);
		txt3rdParty.setStyle("-fx-font-size:" + 12);
		txtBPM.setStyle("-fx-font-size:" + 12);
		txtTransform.setStyle("-fx-font-size:" + 12);
		txtJMS.setStyle("-fx-font-size:" + 12);
		txtCMIS.setStyle("-fx-font-size:" + 12);
		txtAct.setStyle("-fx-font-size:" + 12);
		txtCron.setStyle("-fx-font-size:" + 12);
		txtCache.setStyle("-fx-font-size:" + 12);
		txtVF.setStyle("-fx-font-size:" + 12);
		txtSys.setStyle("-fx-font-size:" + 12);
		txtJava.setStyle("-fx-font-size:" + 12);
		txtOther.setStyle("-fx-font-size:" + 12);
		txtSearchResults.setStyle("-fx-font-size:" + 12);
		lblFontSize.setText("12.0");
	}

	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		menuReset.setDisable(true);
		menuRec.setDisable(true);
		menuVersion.setDisable(true);
		menuCon.setDisable(true);
		menuMem.setDisable(true);
		menuEditor.setDisable(true);
		menuCacheStats.setDisable(true);
		menuSOLR.setDisable(true);
		menuOpenFile.setDisable(false);
		menuOpenZip.setDisable(false);
		txtSearch.setDisable(true);
		btnSearch.setDisable(true);
		chkModal.setSelected(false);
		chkModal.setDisable(true);
		btnIncreaseFont.setDisable(true);
		btnDecreaseFont.setDisable(true);
		chkBox.setSelected(false);
		chkBox.setDisable(true);

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}

/**
 * "nakoso.fxml"コントローラ・クラスのサンプル・スケルトン
 */

package application;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class Controller {

	static String strFileConfig = null;
	static String strFilePDF = null;


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private Button buttonConfigOpen;

    @FXML
    private Button buttonPDFOpen;

    @FXML
    private Button buttonRename;

    @FXML
    private Label labelPdfFile;

    @FXML
    private TextField textConfig;

    @FXML
    private TextField textMsg;

    @FXML
    private TextField textNewFile;

    @FXML
    private TextArea textPDF;

    @FXML
    private TextField textPDFFile;

    @FXML // fx:id="x1"
    private Font x1; // Value injected by FXMLLoader

    @FXML // fx:id="x2"
    private Color x2; // Value injected by FXMLLoader

    @FXML // fx:id="x3"
    private Font x3; // Value injected by FXMLLoader

    @FXML // fx:id="x4"
    private Color x4; // Value injected by FXMLLoader

    @FXML
    void onPDFOpenAction(ActionEvent event) {
    	openPDF();
    }

    @FXML
    void onConfigOpenAction(ActionEvent event) {
    	openConfig();
    }

    @FXML
    void onRenameFileAction(ActionEvent event) {
    	renameFile();
    }


    void openPDF() {
    	D.dprint_method_start();
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("PDFファイルの選択");
	    if (strFilePDF != null) {
	    	File file1 = new File(strFilePDF);
	    	File fileDir = new File(file1.getParent());
	    	fileChooser.setInitialDirectory(fileDir);
	    }
	    File file = null;
		try {
			file = fileChooser.showOpenDialog(Main.stage);
		} catch (Exception e1) {
			printMsg("ファイルの選択ができませんでした。");
			D.dprint_method_end();
			return;
		}
	    if (file == null) {
			printMsg("ファイルが選択されませんでした。");
			D.dprint_method_end();
			return;
		}
	    String strFile = file.getAbsolutePath();
		FileProc fileProc = new FileProc(strFile);
		String strRet = fileProc.readFile();
		if (strRet != null) {
			printMsg(strRet);
			D.dprint_method_end();
			return;
		}
		textPDFFile.setEditable(true);
		textPDFFile.setText(strFile);
		textPDFFile.end();
		textPDFFile.setEditable(false);

        Controller.strFilePDF = strFile;
		String strText = fileProc.getText();

//		System.out.println("PDFファイル内のテキスト");
//		System.out.println("==============================");
//		System.out.println(strText);
//		System.out.println("==============================");

//		strSrcDisp = "<font color=\"green\">" +
//				aSrc[i].substring(
//				o.getDeletedStart(),
//				o.getDeletedEnd()+1) +
//				"</font>";

		
		textPDF.setEditable(true);
        textPDF.setText(strText);
        textPDF.end();
        textPDF.setEditable(false);
        Main.fileProc = fileProc;
        analyzeText();
		D.dprint_method_end();
		return;
    }


    void openConfig() {
    	D.dprint_method_start();
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("設定ファイルの選択");
	    if (Controller.strFileConfig != null) {
	    	File file1 = new File(
	    			Controller.strFileConfig);
	    	File fileDir = new File(file1.getParent());
	    	fileChooser.setInitialDirectory(fileDir);
	    }
	    File file = null;
		try {
			file = fileChooser.showOpenDialog(Main.stage);
		} catch (Exception e1) {
			printMsg("ファイルの選択ができませんでした。");
			Main.configProc = null;
			textConfig.setEditable(true);
			textConfig.setText("");
			textConfig.end();
			textConfig.setEditable(false);
			D.dprint_method_end();
			return;
		}
	    if (file == null) {
			printMsg("ファイルが選択されませんでした。");
			Main.configProc = null;
			textConfig.setEditable(true);
			textConfig.setText("");
			textConfig.end();
			textConfig.setEditable(false);
			D.dprint_method_end();
			return;
		}
	    Controller.strFileConfig = file.getAbsolutePath();
		Analysis analysis = new Analysis();
		ConfigProc configProc = new ConfigProc(
				Controller.strFileConfig, analysis);
		String strRet = configProc.readConfig();
		if (strRet != null) {
			printMsg(strRet);
			Main.configProc = null;
			textConfig.setEditable(true);
			textConfig.setText("");
			textConfig.end();
			textConfig.setEditable(false);
			D.dprint_method_end();
			return;
		}
		Float configVersion = configProc.getVersion();
		if (configVersion == 0.0f) {
			printMsg(String.format(
					"設定ファイルのバージョン%.2fが低い",
					configVersion));
//			System.out.println(
//					"設定ファイルの内容を確認してください");
			Main.configProc = null;
			textConfig.setEditable(true);
			textConfig.setText("");
			textConfig.end();
			textConfig.setEditable(false);
			D.dprint_method_end();
			return;
		}
		Main.configProc = configProc;
		textConfig.setEditable(true);
		textConfig.setText(Controller.strFileConfig);
		textConfig.end();
		textConfig.setEditable(false);
		printMsg("設定ファイルを読込みました");
		analyzeText();
		D.dprint_method_end();
    	return;
    }


    private void analyzeText() {
    	D.dprint_method_start();
    	if (Main.configProc == null) {
    		D.dprint_method_end();
    		return;
    	}
    	if (Main.fileProc == null) {
    		D.dprint_method_end();
    		return;
    	}
    	String strText = Main.fileProc.getText();
		String aStr[] = Main.configProc.getMatchString(
				1, strText, Main.fileProc);
		if (aStr[0] != null) {
			printMsg(aStr[0]);
			D.dprint_method_end();
			return;
		}
		String strFileFormat = Main.configProc.getFileFormat();
		String strFileName = String.format(strFileFormat,
				aStr[1], aStr[2], aStr[3], aStr[4],
				aStr[5], aStr[6], aStr[7], aStr[8],
				aStr[9]);
		strFileName = Main.fileProc.modifyFileName(strFileName);
		textNewFile.setEditable(true);
		textNewFile.setText(strFileName);
		textNewFile.end();
		textNewFile.setEditable(false);
		
		
//		strSrcDisp = "<font color=\"green\">" +
//		aSrc[i].substring(
//		o.getDeletedStart(),
//		o.getDeletedEnd()+1) +
//		"</font>";


//textPDF.setEditable(true);
//textPDF.setText(strText);
//textPDF.end();
//textPDF.setEditable(false);
		D.dprint_method_end();
		return;
	}

    private void renameFile() {
    	D.dprint_method_start();
		boolean flagRename = Main.configProc.getFlagRename();
		String strFileName = textNewFile.getText();
		if (flagRename) {
			boolean flag = Main.fileProc.renameFile(
					strFileName);
			if (flag) {
				textPDFFile.setEditable(true);
				textPDFFile.setText(strFileName);
				textPDFFile.end();
				textPDFFile.setEditable(false);
			} else {
				printMsg("ファイル名変更に失敗しました");
			}
		} else {
			boolean flag = Main.fileProc.copyFile(
					strFileName);
			if (! flag) {
				printMsg("ファイルのコピーに失敗しました");
			}
		}
		D.dprint_method_end();
		return;
	}


    void printMsg( String strMsg ) {
		textMsg.setEditable(true);
		textMsg.setText(strMsg);
		textMsg.end();
		textMsg.setEditable(false);
		return;
    }

    @FXML
    void initialize() {
        assert buttonConfigOpen != null : "fx:id=\"buttonConfigOpen\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert buttonPDFOpen != null : "fx:id=\"buttonPDFOpen\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert buttonRename != null : "fx:id=\"buttonRename\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert labelPdfFile != null : "fx:id=\"labelPdfFile\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textConfig != null : "fx:id=\"textConfig\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textMsg != null : "fx:id=\"textMsg\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textNewFile != null : "fx:id=\"textNewFile\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textPDF != null : "fx:id=\"textPDF\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textPDFFile != null : "fx:id=\"textPDFFile\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'nakoso.fxml'.";

    }

}

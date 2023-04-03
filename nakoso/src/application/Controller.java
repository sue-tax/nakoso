/**
 * "nakoso.fxml"コントローラ・クラスのサンプル・スケルトン
 */

// fxmlファイルを更新したら、「クリーン」することが必要

package application;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
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

    @FXML // fx:id="cbAuto"
    private CheckBox cbAuto; // Value injected by FXMLLoader

    @FXML // fx:id="choiceOption"
    private ChoiceBox<String> choiceOption; // Value injected by FXMLLoader

    @FXML
    private TextField textConfig;

    @FXML
    private TextField textMsg;

    @FXML
    private TextField textNewFile;

    @FXML
    private TextField textFileFormat;

    @FXML // fx:id="textVersion"
    private TextField textVersion; // Value injected by FXMLLoader

//    @FXML
//    private TextArea textPDF;
    @FXML
    private WebView webviewText;


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

    @FXML
    void onDragDropped(DragEvent event) {
        Dragboard board = event.getDragboard();
        if (board.hasFiles()) {
            board.getFiles().forEach(file -> {
//                root.getChildren().add(new Label(file.getAbsolutePath()));
                System.out.println(file.getAbsolutePath());
            });
            File file = board.getFiles().get(0);
            String strFile = file.getAbsolutePath();
            openPDFmain(strFile);
            event.setDropCompleted(true);
        } else {
            event.setDropCompleted(false);
        }
    }

    @FXML
    void onDragOver(DragEvent event) {
        Dragboard board = event.getDragboard();
        if (board.hasFiles()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
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
	    openPDFmain(strFile);
		D.dprint_method_end();
		return;
    }


    void openPDFmain( String strFileName ) {
    	D.dprint_method_start();
    	D.dprint(strFileName);
		FileProc fileProc = new FileProc(strFileName);
		String strRet = fileProc.readFile();
		if (strRet != null) {
			printMsg(strRet);
			D.dprint_method_end();
			return;
		}
		textPDFFile.setEditable(true);
		textPDFFile.setText(strFileName);
		textPDFFile.end();
		textPDFFile.setEditable(false);

        Controller.strFilePDF = strFileName;
        Main.fileProc = fileProc;
		printMsg("PDFファイルを開きました。");
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
			textVersion.setEditable(true);
			textVersion.setText("");
			textVersion.end();
			textVersion.setEditable(false);
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
			textVersion.setEditable(true);
			textVersion.setText("");
			textVersion.end();
			textVersion.setEditable(false);
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
			textVersion.setEditable(true);
			textVersion.setText("");
			textVersion.end();
			textVersion.setEditable(false);
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
			Main.configProc = null;
			textVersion.setEditable(true);
			textVersion.setText("");
			textVersion.end();
			textVersion.setEditable(false);
			textConfig.setEditable(true);
			textConfig.setText("");
			textConfig.end();
			textConfig.setEditable(false);
			D.dprint_method_end();
			return;
		}
		textVersion.setEditable(true);
		String str = String.format("%.2f", configVersion);
		textVersion.setText(str);
		textVersion.end();
		textVersion.setEditable(false);

		boolean flagRename = configProc.getFlagRename();
//		choiceOption.getItems().setEditable(true);
		if (flagRename) {
			choiceOption.getSelectionModel().select(0);
		} else {
			choiceOption.getSelectionModel().select(1);
		}

		Main.configProc = configProc;
		textConfig.setEditable(true);
		textConfig.setText(Controller.strFileConfig);
		textConfig.end();
		textConfig.setEditable(false);
		String strFileFormat = configProc.getFileFormat();
		textFileFormat.setEditable(true);
		textFileFormat.setText(strFileFormat);
		textFileFormat.end();
		textFileFormat.setEditable(false);

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
//    	D.dprint(strText);
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

//		String strHtml = strText.replaceAll("\\n", "(\\\\n)<br>");
//		String strHtml = strText.replaceAll("\\n", "<br>");
//		D.dprint(strHtml);
		String strHtml = Main.configProc.getColoredString(
				strText);
		String strColored = strHtml.replaceAll("\\n", "(\\\\n)<br>");
    	webviewText.getEngine().loadContent(strColored);
//		textPDF.setEditable(true);
//		textPDF.setText(strColored);
//		textPDF.end();
//		textPDF.setEditable(false);
    	printMsg("変更ファイル名を生成しました。");
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
				printMsg("ファイル名を変更しました");
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
        assert cbAuto != null : "fx:id=\"cbAuto\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert choiceOption != null : "fx:id=\"choiceOption\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textConfig != null : "fx:id=\"textConfig\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textFileFormat != null : "fx:id=\"textFileFormat\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textMsg != null : "fx:id=\"textMsg\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textNewFile != null : "fx:id=\"textNewFile\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textPDFFile != null : "fx:id=\"textPDFFile\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert textVersion != null : "fx:id=\"textVersion\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert webviewText != null : "fx:id=\"webviewText\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert x3 != null : "fx:id=\"x3\" was not injected: check your FXML file 'nakoso.fxml'.";
        assert x4 != null : "fx:id=\"x4\" was not injected: check your FXML file 'nakoso.fxml'.";

        choiceOption.getItems().addAll("変更","ｺﾋﾟｰ");

    }

}

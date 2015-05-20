package view;

import org.apache.poi.ss.formula.functions.FinanceLib;

import main.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RootLayoutController {
    
    @SuppressWarnings("unused")
	private MainApp mainApp;
    
    @FXML private TextField grossIncome;
    @FXML private TextField monthlyDebt;
    @FXML private TextField mInterestRate;
    @FXML private TextField downPayment;
    @FXML private ChoiceBox<Integer> termDropdown;
    @FXML private Button calcButton;
    @FXML private Button clearButton;
    @FXML private Label hpLabel;
    @FXML private Label hp2Label;
    @FXML private Label mpLabel;
    @FXML private Label fmLabel;
    @FXML private Label calcErrorLabel;
    
    private double gross,
    	debt,
    	rate,
    	down,
    	housingPayment,
    	housingPaymentOb,
    	maximumPayment,
    	mortgage;
    private int term;
    private boolean autoCalc = false;
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
	@FXML
    public void handleClear() {
    	grossIncome.clear();
    	monthlyDebt.clear();
    	mInterestRate.clear();
    	downPayment.setText("0");
    	calcErrorLabel.setVisible(false);
    	hpLabel.setText("...");
    	hp2Label.setText("...");
    	mpLabel.setText("...");
    	fmLabel.setText("...");
    	termDropdown.setValue(10);
    }
	
	@FXML
	public void autoCalculate() {
		if (autoCalc)
			handleCalculate();
	}
    
    @FXML
    public void handleCalculate() {
    	autoCalc = false;
    	try {
        	calcErrorLabel.setVisible(false);
	    	gross = Double.parseDouble(grossIncome.getText());
	    	debt = Double.parseDouble(monthlyDebt.getText());
	    	rate = Double.parseDouble(mInterestRate.getText()) / 100;
	    	down = Double.parseDouble(downPayment.getText());
	    	term = termDropdown.getValue();
	    	term *= 12;
	    	
	    	housingPayment = (gross / 12) * .18;
	    	housingPaymentOb = (housingPayment * 2) - debt;
	    	maximumPayment = housingPayment < housingPaymentOb ? housingPayment : housingPaymentOb;
	    	mortgage = FinanceLib.pv(rate / 12, term, maximumPayment, 0, false) * -1;
	    	mortgage += down;
	    	
	    	hpLabel.setText("$" + Double.toString(housingPayment));
	    	hp2Label.setText("$" + Double.toString(housingPaymentOb));
	    	mpLabel.setText("$" + Double.toString(maximumPayment));
	    	fmLabel.setText("$" + Double.toString(mortgage));
	    	autoCalc = true;
    	} catch (NumberFormatException e) {
        	calcErrorLabel.setVisible(true);
    		e.printStackTrace();
    	} catch (NullPointerException e) {
        	calcErrorLabel.setVisible(true);
    		e.printStackTrace();
    	}
    }
    
    private void setTerms() {
    	termDropdown.getItems().removeAll();
    	termDropdown.getItems().addAll(
        		10,15,30
        	);
    	termDropdown.setValue(10);
    }
    
    @FXML
    void initialize() {
    	downPayment.setText("0");
    	setTerms();
    }
}
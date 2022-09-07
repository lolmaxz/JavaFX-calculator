package com.calculator;

import java.text.DecimalFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class calculatriceControlleur {

    @FXML
    private Button btn_calcul_reset;
    @FXML
    private Button btn_nbr_7;
    @FXML
    private Button btn_nbr_4;
    @FXML
    private Button btn_nbr_1;
    @FXML
    private Button btn_nbr_0;
    @FXML
    private Button btn_nbr_8;
    @FXML
    private Button btn_nbr_5;
    @FXML
    private Button btn_nbr_2;
    @FXML
    private Button btn_nbr_9;
    @FXML
    private Button btn_nbr_6;
    @FXML
    private Button btn_nbr_3;
    @FXML
    private Button btn_operation_plus;
    @FXML
    private Button btn_operation_moins;
    @FXML
    private Button btn_operation_multiplier;
    @FXML
    private Button btn_operation_diviser;
    @FXML
    private Button btn_calcul_point;
    @FXML
    private Button btn_calcul_egale;
    @FXML
    private TextField textResultat;

    // Declaration of all variable we will need here
    private double lastValue = 0;
    private boolean finOperation = false;
    private boolean AttenteOperation = false;
    private boolean premierSymboleChoisi = false;
    private boolean enTrainDentreeUnChiffre = false;
    private String symboleAction = "";
    private double deuxiemeNombre = 0;
    private double premierNombre = 0;
    private double derniereValeurEgale = 0;
    private boolean pointActiver = false;
    // ------------

    @FXML
    private void press_btn(ActionEvent event) {
        String button_name = ((Button) event.getTarget()).getId();
        String nombreAppuyer = "";
        double valeurActuelle = 0;
        String symboleActionButton = "";

        // We check if it says 'ERR' and remove it before we continue.
        if (textResultat.getText().equals("ERR")) {
            textResultat.setText("0");
            deuxiemeNombre = 0;
            premierNombre = 0;
            symboleAction = "";
            lastValue = 0;
            finOperation = false;
            AttenteOperation = false;
            enTrainDentreeUnChiffre = false;
            premierSymboleChoisi = false;
        }

        // We first try to grab the text in the calculator
        try {
            valeurActuelle = Double.valueOf(textResultat.getText());
        }catch (Exception e) {
            System.out.println("erreur: "+e);
            textResultat.setText("ERR");
            return;
        }

        if (valeurActuelle == 0) {
            // working
        }

        if (button_name.contains("btn_nbr_")) {
            nombreAppuyer = button_name.substring(button_name.length()-1);

            if (Double.valueOf(nombreAppuyer) >= 0 && Double.valueOf(nombreAppuyer) <= 9) {
                //System.out.println(nombreAppuyer);
                button_name = "NUMBER_BUTTON";
            }
        }

        if (button_name.contains("btn_operation_")) {
            switch (button_name) {
                case "btn_operation_plus":
                    button_name = "OPERATION_BUTTON";
                    symboleActionButton = "+";
                    break;
                case "btn_operation_moins":
                    button_name = "OPERATION_BUTTON";
                    symboleActionButton = "-";
                    break;
                case "btn_operation_multiplier":
                    button_name = "OPERATION_BUTTON";
                    symboleActionButton = "*";
                    break;
                case "btn_operation_diviser":
                    button_name = "OPERATION_BUTTON";
                    symboleActionButton = "/";
                    break;
            }

        }


        DecimalFormat format = new DecimalFormat("0.######");


        switch (button_name) {

            case "btn_calcul_reset" :
                textResultat.setText("0");
                deuxiemeNombre = 0;
                premierNombre = 0;
                symboleAction = "";
                lastValue = 0;
                finOperation = false;
                AttenteOperation = false;
                enTrainDentreeUnChiffre = false;
                premierSymboleChoisi = false;
                break;

            case "NUMBER_BUTTON" :
                if (finOperation) {
                    textResultat.setText(nombreAppuyer);
                    AttenteOperation = false;
                    enTrainDentreeUnChiffre = true;
                    finOperation = false;
                } else if (AttenteOperation) {
                    if (enTrainDentreeUnChiffre) {
                        if (textResultat.getLength() <= 10) {
                            textResultat.setText(textResultat.getText() + nombreAppuyer);
                            premierNombre =  Double.valueOf(textResultat.getText());
                            lastValue = computeSymbole(premierNombre,symboleAction, deuxiemeNombre);
                        }
                    } else {
                        textResultat.setText(String.valueOf(format.format(Double.valueOf(nombreAppuyer))));
                        premierNombre = Double.valueOf(textResultat.getText());
                        lastValue = computeSymbole(premierNombre,symboleAction, deuxiemeNombre);
                        premierSymboleChoisi = true;
                        enTrainDentreeUnChiffre = true;
                    }
                } else if (enTrainDentreeUnChiffre) {
                    // If an operation is on progress and we are waiting for a symbol
                    if (textResultat.getLength() <= 10) {
                        textResultat.setText(textResultat.getText() + nombreAppuyer);
                        premierNombre =  Double.valueOf(textResultat.getText());
                    }

                } else {
                    textResultat.setText(format.format(Double.valueOf(nombreAppuyer)));
                    premierNombre = Double.valueOf(nombreAppuyer);
                    enTrainDentreeUnChiffre = true;
                }
                break;

            case "OPERATION_BUTTON" :
                if (finOperation) {

                    AttenteOperation = true;
                    symboleAction = symboleActionButton;                
                    enTrainDentreeUnChiffre = false;
                    deuxiemeNombre = derniereValeurEgale != 0 ? derniereValeurEgale : 0;
                    derniereValeurEgale = 0;
                    finOperation = false;

                } else if (AttenteOperation) {
                    // If an operation is on progress and we are waiting for a symbol
                    // We change the symbole for the latest pressed
                    symboleAction = symboleActionButton;
                } else if (enTrainDentreeUnChiffre) {
                    if (premierSymboleChoisi) {
                        // not the beginning so we stack numbers
                        textResultat.setText(String.valueOf(format.format(Double.valueOf(lastValue))));
                    }
                    deuxiemeNombre = premierNombre;
                    AttenteOperation = true;
                    symboleAction = symboleActionButton;
                    enTrainDentreeUnChiffre = false;

                } else {
                    AttenteOperation = true;
                    symboleAction = symboleActionButton;
                }
                pointActiver = false;
                break;

            case "btn_calcul_point" :
                if (pointActiver) return;
                if (finOperation) {
                    textResultat.setText("0.");
                    AttenteOperation = false;
                    enTrainDentreeUnChiffre = true;
                    finOperation = false;

                } else if (AttenteOperation) {
                    if (enTrainDentreeUnChiffre) {
                        if (textResultat.getLength() <= 10) {
                            textResultat.setText(textResultat.getText() + ".");
                            premierNombre =  Double.valueOf(textResultat.getText());
                            lastValue = computeSymbole(premierNombre,symboleAction, deuxiemeNombre);
                        }

                    } else {
                        textResultat.setText(String.valueOf(format.format(Double.valueOf("0")))+".");
                        premierNombre = Double.valueOf(textResultat.getText());
                        lastValue = computeSymbole(premierNombre,symboleAction, deuxiemeNombre);
                        premierSymboleChoisi = true;
                        enTrainDentreeUnChiffre = true;
                    }
                    pointActiver = true;

                } else if (enTrainDentreeUnChiffre) {
                    // If an operation is on progress and we are waiting for a symbol
                    if (textResultat.getLength() <= 10) {
                        textResultat.setText(textResultat.getText() + ".");
                        premierNombre =  Double.valueOf(format.format(Double.valueOf(textResultat.getText())));
                        pointActiver = true;
                    }

                } else {
                    textResultat.setText("0.");
                    premierNombre = Double.valueOf(0);
                    enTrainDentreeUnChiffre = true;
                    pointActiver = true;
                }
                break;
            case "btn_calcul_egale" :
                if (finOperation) {
                    derniereValeurEgale = computeSymbole(derniereValeurEgale,symboleAction, premierNombre);
                    textResultat.setText(String.valueOf(format.format(Double.valueOf(derniereValeurEgale))));
                    enTrainDentreeUnChiffre = false;

                } else if (AttenteOperation) {
                    derniereValeurEgale = computeSymbole(deuxiemeNombre,symboleAction, premierNombre);
                    textResultat.setText(String.valueOf(format.format(Double.valueOf(derniereValeurEgale))));
                    AttenteOperation = false;
                    finOperation = true;
                    enTrainDentreeUnChiffre = false;
                    premierSymboleChoisi = false;
                } else {
                    finOperation = true;
                    premierSymboleChoisi = false;
                    enTrainDentreeUnChiffre = false;
                    AttenteOperation = false;
                    deuxiemeNombre = 0;
                    symboleAction = "";
                    pointActiver = false;
                }
                break;
            default:
                // nothing here
                break;
        }
        // DEBUG SECTION //
        /*System.out.println("-----------------------");
        System.out.println("Last Value: "+lastValue);
        System.out.println("Symbole: "+symboleAction);
        System.out.println("DernierNombre: "+ deuxiemeNombre);
        System.out.println("Nombre appuyé: "+nombreAppuyer);
        System.out.println("Premier Nombre: "+premierNombre);
        System.out.println("Idle? : "+ enTrainDentreeUnChiffre);
        System.out.println("AttenteOperation? : "+AttenteOperation);*/
    }

    // Function to return a calculation
    public double computeSymbole(double value1,String symbol, double value2) {
        // ↓ DEBUG FUNCTION
        //System.out.println("value 1: "+value1+", symbol: "+symbol+", value 2: "+value2);

        // If the symbole isn't a valid symbol, we cancel and simply return 0
        if (!(symbol.equals("+") || symbol.equals("-") || symbol.equals("/") || symbol.equals("*"))) { return 0; }

        double result = 0.0;

        switch (symbol) {
            case "+":
                result = value1 + value2;
                break;
            case "-":
                result = value1 - value2;
                break;
            case "/":
                result = value1 / value2;
                break;
            case "*":
                result = value1 * value2;
                break;
            default:
                // nothing here
            break;
        }

        //System.out.println("Result: " + result);
        return result;
    }
}

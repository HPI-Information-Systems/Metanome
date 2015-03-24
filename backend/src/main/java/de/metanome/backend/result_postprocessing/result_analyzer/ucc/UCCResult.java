package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationPrinter;

import java.io.Serializable;

/**
 * Created by Alexander Spivak on 11.03.2015.
 */
public class UCCResult implements Serializable {

  // Used for serialization
  private static final long serialVersionUID = 314639549933945919L;

  //<editor-fold desc="Private attributes">

  private ColumnCombination ucc;

  private String uccAsString;

  private String uccWithColumnNames;

  private UCCRank rank;

  //</editor-fold>

  //<editor-fold desc="Constructors">

  public UCCResult() {
  }

  public UCCResult(ColumnCombination ucc) {
    this.ucc = ucc;
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public ColumnCombination getUcc() {
    return ucc;
  }

  public void setUcc(ColumnCombination ucc) {
    this.ucc = ucc;
  }

  public String getUccAsString() {
    return uccAsString;
  }

  public void setUccAsString(String uccAsString) {
    this.uccAsString = uccAsString;
  }

  public String getUccWithColumnNames() {
    return uccWithColumnNames;
  }

  public void setUccWithColumnNames(String uccWithColumnNames) {
    this.uccWithColumnNames = uccWithColumnNames;
  }

  public UCCRank getRank() {
    return rank;
  }

  public void setRank(UCCRank rank) {
    this.rank = rank;
  }

  //</editor-fold>

  public void update(TableInformation tableInformation){
    this.setUccAsString(this.getUcc().toString());
    this.setUccWithColumnNames(ColumnCombinationPrinter.prettyPrint(this.getUcc(),
                                                                    ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                                    ",", true, tableInformation));
  }

}

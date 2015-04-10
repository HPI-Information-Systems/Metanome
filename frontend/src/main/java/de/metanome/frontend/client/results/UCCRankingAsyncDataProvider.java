package de.metanome.frontend.client.results;

import com.google.gwt.core.client.GWT;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtUCCResult;
import de.metanome.frontend.client.services.GwtUCCResultRestService;

import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

/**
 * UCC ranking data provider, which allows simple pagination implementation by overriding the template methods
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
public class UCCRankingAsyncDataProvider extends AbstractRankingAsyncDataProvider<GwtUCCResult>  {

  // Service to retrieve data from database
  protected final GwtUCCResultRestService restService = GWT.create(GwtUCCResultRestService.class);

  /**
   * Retrieves the total results count from the backend and triggers the provided callback
   *
   * @param resultsCountCallback Callback which should be called after retrieving the results count
   */
  @Override
  protected void retrieveDataCountFromBackend(MethodCallback<GwtLong> resultsCountCallback) {
    restService.count(executionID, resultsCountCallback);
  }

  /**
   * Retrieves the results from the backend and triggers the provided callback
   *
   * @param resultsRetrieveCallback Callback which should be called after retrieving the results
   */
  @Override
  protected void retrieveDataFromBackend(
      MethodCallback<List<GwtUCCResult>> resultsRetrieveCallback) {
    restService.listGwtUCCResults(executionID, sortPropertyName, isAscending, start, end, resultsRetrieveCallback);
  }
}

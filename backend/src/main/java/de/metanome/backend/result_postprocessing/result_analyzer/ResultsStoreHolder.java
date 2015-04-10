package de.metanome.backend.result_postprocessing.result_analyzer;

import java.util.HashMap;
import java.util.Map;

/**
 * "Static" results holder provides the option to register a result store and to clear all registered storage
 *
 * Created by Alexander Spivak on 10.03.2015.
 */
public final class ResultsStoreHolder {

  // Results store map
  private static Map<String, ResultsStore<?>> resultsStoreMap = new HashMap<>();

  /**
   * Constructor to prevent client code instantiation
   */
  private ResultsStoreHolder(){
    // Prevents instantiation by client code as it makes no sense to instantiate a static class
  }

  /**
   * Registers the given result store under given name
   *
   * @param storeName Store name
   * @param resultsStore Results store to be registered
   */
  public static void register(String storeName, ResultsStore<?> resultsStore){
    resultsStoreMap.put(storeName, resultsStore);
  }

  /**
   * Returns the registered store for the given name
   *
   * @param storeName Name of requested store
   * @return Returns the registered store for the given name
   */
  public static ResultsStore<?> getStore(String storeName){
    return resultsStoreMap.get(storeName);
  }

  /**
   * Clears all registered stores
   */
  public static void clearStores(){
    for(ResultsStore<?> resultsStore : resultsStoreMap.values()){
      resultsStore.clear();
    }
  }

}

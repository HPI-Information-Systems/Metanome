/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.common.base.Joiner;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.TestHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.input_fields.SqlIteratorInput;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

import org.junit.Test;

public class GwtTestSqlIteratorParameter extends GWTTestCase {

  private String aUrl = "url";
  private String aPassword = "password";
  private String aUser = "user";
  private DbSystem aSystem = DbSystem.DB2;

  /**
   * Tests the selection of a specific item corresponding to the given ConfigurationSetting.
   */
  @Test
  public void testSelectDataSourceOnFilledDropdown()
      throws AlgorithmConfigurationException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    final TabWrapper tabWrapper = new TabWrapper();

    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl(aUrl);
    databaseConnection.setPassword(aPassword);
    databaseConnection.setUsername(aUser);
    final long[] databaseConnectionId = new long[1];
    TestHelper.storeDatabaseConnection(databaseConnection, new AsyncCallback<Long>() {
      @Override
      public void onFailure(Throwable caught) {
        System.out.println(caught.getMessage());
        fail();
      }

      @Override
      public void onSuccess(Long id) {
        databaseConnectionId[0] = id;
      }
    });

    final SqlIteratorInput[] widget = new SqlIteratorInput[1];
    final ConfigurationSettingSqlIterator setting = new ConfigurationSettingSqlIterator(aUrl, aUser, aPassword, aSystem);

    Timer setUpTimer = new Timer() {
      @Override
      public void run() {
        widget[0] = new SqlIteratorInput(false, tabWrapper);
      }
    };

    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        //Execute
        try {
          widget[0].selectDataSource(setting);
        } catch (AlgorithmConfigurationException e) {
          System.out.println(e.getMessage());
          fail();
        }

        //Check
        assertEquals(databaseConnectionId[0] + ": " + aUrl, widget[0].listbox.getSelectedValue());
        assertEquals(aUrl, widget[0].getValues().getDbUrl());
        assertEquals(aPassword, widget[0].getValues().getPassword());
        assertEquals(aUser, widget[0].getValues().getUsername());

        // Cleanup
        TestHelper.resetDatabaseSync();

        finishTest();
      }
    };

    delayTestFinish(10000);

    // Waiting for asynchronous calls to finish.
    setUpTimer.schedule(4000);
    executeTimer.schedule(8000);

  }

  /**
   * When setting a data source on the parent widget (InputParameter), it should be set in the first
   * child widget.
   */
  @Test
  public void testSetDataSource() throws AlgorithmConfigurationException, InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    final TabWrapper tabWrapper = new TabWrapper();

    DatabaseConnection databaseConnection = new DatabaseConnection();
    databaseConnection.setUrl(aUrl);
    databaseConnection.setPassword(aPassword);
    databaseConnection.setUsername(aUser);
    TestHelper.storeDatabaseConnection(databaseConnection, new AsyncCallback<Long>() {
      @Override
      public void onFailure(Throwable caught) {
        System.out.println(caught.getMessage());
        fail();
      }

      @Override
      public void onSuccess(Long id) {
      }
    });

    final ConfigurationSettingSqlIterator setting = new ConfigurationSettingSqlIterator(aUrl, aUser, aPassword, aSystem);

    final InputParameterSqlIteratorWidget[] dataSourceWidget = new InputParameterSqlIteratorWidget[1];
    Timer setUpTimer = new Timer() {
      @Override
      public void run() {
        //Setup
        ConfigurationSpecificationSqlIterator configSpec = new ConfigurationSpecificationSqlIterator("test");
        dataSourceWidget[0] = new InputParameterSqlIteratorWidget(configSpec, tabWrapper);
      }
    };

    Timer executeTimer = new Timer() {
      @Override
      public void run() {
        //Execute
        try {
          dataSourceWidget[0].setDataSource(setting);
        } catch (AlgorithmConfigurationException e) {
          e.printStackTrace();
          fail();
        }

        //Check
        assertTrue(((SqlIteratorInput) dataSourceWidget[0].getWidget(0)).listbox.getValues().size() > 1);

        ConfigurationSettingDataSource retrievedSetting = null;
          retrievedSetting = (ConfigurationSettingDataSource) dataSourceWidget[0]
              .getUpdatedSpecification()
              .getSettings()[0];

        assertEquals(Joiner.on(';').join(aUrl, aUser, aPassword, aSystem), retrievedSetting.getValueAsString());

        // Cleanup
        TestHelper.resetDatabaseSync();

        finishTest();
      }
    };

    delayTestFinish(10000);

    // Waiting for asynchronous calls to finish.
    setUpTimer.schedule(4000);
    executeTimer.schedule(8000);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.client.MetanomeTest";
  }
}

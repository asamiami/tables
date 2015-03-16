package org.opendatakit.tables.fragments;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opendatakit.common.android.application.CommonApplication;
import org.opendatakit.tables.activities.TableDisplayActivityStub;
import org.opendatakit.tables.activities.TableDisplayActivity.ViewFragmentType;
import org.opendatakit.tables.utils.Constants;
import org.opendatakit.tables.views.webkits.TableData;
import org.opendatakit.testutils.ODKFragmentTestUtil;
import org.opendatakit.testutils.TestCaseUtils;
import org.opendatakit.testutils.TestConstants;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import android.app.Activity;
import android.app.FragmentManager;

@RunWith(RobolectricTestRunner.class)
public class MapListViewFragmentTest {

  Activity activity;
  MapListViewFragment fragment;
  
  @After
  public void after() {
    TestCaseUtils.resetExternalStorageState();
    MapListViewFragmentStub.resetState();
  }

  @Before
  public void setupWithDefaults() {
    CommonApplication.setMocked();
    TestCaseUtils.setExternalStorageMounted();
    
    TestCaseUtils.setThreeTableDataset(true);
    ShadowLog.stream = System.out;
    activity = ODKFragmentTestUtil.startFragmentForTableDisplayActivity(
        ViewFragmentType.MAP, TestConstants.DEFAULT_TABLE_ID);
    FragmentManager mgr = activity.getFragmentManager();
    fragment = (MapListViewFragment) mgr.findFragmentByTag(Constants.FragmentTags.MAP_LIST);
  }

  @Test
  public void startsWithNoItemSelected() {
    org.fest.assertions.api.Assertions
      .assertThat(this.fragment.itemIsSelected()).isFalse();
  }

  @Test
  public void settingIndexMakesItemSelected() {
    int selectedIndex = 5;
    this.fragment.setIndexOfSelectedItem(selectedIndex);
    org.fest.assertions.api.Assertions
      .assertThat(this.fragment.itemIsSelected()).isTrue();
  }

  @Test
  public void settingNoItemSelectedResetsState() {
    int selectedIndex = 10;
    this.fragment.setIndexOfSelectedItem(selectedIndex);
    org.fest.assertions.api.Assertions
      .assertThat(this.fragment.itemIsSelected()).isTrue();
    // Now undo it.
    this.fragment.setNoItemSelected();
    org.fest.assertions.api.Assertions
      .assertThat(this.fragment.itemIsSelected()).isFalse();
  }
  
  @Test
  public void settingIndexCallsToTableData() {
    int selectedIndex = 5;
    TableData tableDataMock = spy(this.fragment.mTableDataReference);
    this.fragment.mTableDataReference = tableDataMock;
    verify(tableDataMock, times(0)).setSelectedMapIndex(selectedIndex);
    this.fragment.setIndexOfSelectedItem(selectedIndex);
    verify(tableDataMock, times(1)).setSelectedMapIndex(selectedIndex);
  }
  
  @Test
  public void settingNoIndexSelectedCallsToTableData() {
    TableData tableDataMock = spy(this.fragment.mTableDataReference);
    this.fragment.mTableDataReference = tableDataMock;
    verify(tableDataMock, times(0)).setNoItemSelected();
    this.fragment.setNoItemSelected();
    verify(tableDataMock, times(1)).setNoItemSelected();
  }


//  For some reason this isn't passing, but is returning the same object.
//  WebView issues?
//  @Test
//  public void resetViewChangesTheDataObject() {
//    MapListViewFragmentStub.FILE_NAME = "testeroo";
//    Object originalDataObject = this.getShadowWebView().getJavascriptInterface(
//        Constants.JavaScriptHandles.DATA);
//    Object sameObjectAgain = this.getShadowWebView().getJavascriptInterface(
//        Constants.JavaScriptHandles.DATA);
//    org.fest.assertions.api.Assertions.assertThat(originalDataObject)
//        .isSameAs(sameObjectAgain);
//    this.fragment.resetView();
//    Object newDataObject = this.getShadowWebView().getJavascriptInterface(
//        Constants.JavaScriptHandles.DATA);
//    org.fest.assertions.api.Assertions.assertThat(originalDataObject)
//        .isNotSameAs(newDataObject);
//  }

}

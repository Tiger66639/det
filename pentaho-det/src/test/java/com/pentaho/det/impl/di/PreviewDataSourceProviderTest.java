/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2015 Pentaho Corporation. All rights reserved.
 */

package com.pentaho.det.impl.di;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.debug.StepDebugMeta;
import org.pentaho.di.trans.debug.TransDebugMeta;
import org.pentaho.di.trans.debug.TransDebugMetaWrapper;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.ui.spoon.trans.TransGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PreviewDataSourceProviderTest {

  // region Tests
  /**
   * Tests that for each selected step in a preview a {@link StepPreviewDataSource} is added to the data sources when a {@link org.pentaho.di.ui.spoon.trans.TransGraph#PREVIEW_TRANS} event occurs
   */
  @Test
  public void testGetDataSources() {
    PreviewDataSourceProvider previewDataSourceProvider = this.createPreviewDataSourceProvider();
    String stepAName = "stepA";
    String stepBName = "stepB";
    Collection<String> stepNames = Arrays.asList( stepAName, stepBName );
    Collection<String> expectedDataSourceNames = stepNames;
    TransDebugMetaWrapper debugMetaWrapper = createMockTransDebugMetaWrapper( stepNames );

    assertThat( previewDataSourceProvider.getDataSources().keySet(), empty() );

    previewDataSourceProvider.uiEvent( debugMetaWrapper, TransGraph.PREVIEW_TRANS );

    Map<UUID, StepPreviewDataSource> actualDataSources = previewDataSourceProvider.getDataSources();
    Collection<String> actualDataSourceNames = new ArrayList<>(  );
    for( StepPreviewDataSource dataSource : actualDataSources.values() ) {
      actualDataSourceNames.add( dataSource.getName() );
    }
    assertThat( actualDataSourceNames, containsInAnyOrder( expectedDataSourceNames.toArray() ));

  }

  @Test
  public void testRespondsTo() {
    PreviewDataSourceProvider previewDataSourceProvider = this.createPreviewDataSourceProvider();

    Map<Class<?>, Set<String>> actualReponseMap = previewDataSourceProvider.respondsTo();

    assertThat( actualReponseMap.entrySet(), hasSize( 1 ) );
    Map.Entry<Class<?>, Set<String> > firstEntry = actualReponseMap.entrySet().iterator().next();
    assertThat( firstEntry.getKey().equals( TransDebugMetaWrapper.class ), is(true) );
    assertThat( firstEntry.getValue(), hasItem( TransGraph.PREVIEW_TRANS ) );

  }

  // endregion

  // region auxiliary methods

  /**
   * Wrapper method for {@link PreviewDataSourceProvider} creation
   */
  private PreviewDataSourceProvider createPreviewDataSourceProvider() {
    return new PreviewDataSourceProvider();
  }

  private TransDebugMetaWrapper createMockTransDebugMetaWrapper( final Iterable<String> stepNames ) {
    final Collection<StepMeta> mockStepMetas = createMockStepMetas( stepNames );
    final Collection<StepInterface> mockStepInterfaces = createMockStepInterfaces( mockStepMetas );

    TransDebugMeta mockDebugMeta = createMockTransDebugMeta( mockStepMetas );
    Trans mockTrans = createMockTrans( mockStepInterfaces );

    TransDebugMetaWrapper mockDebugMetaWrapper = mock( TransDebugMetaWrapper.class );
    when( mockDebugMetaWrapper.getTrans() ).thenReturn( mockTrans );
    when( mockDebugMetaWrapper.getTransDebugMeta() ).thenReturn( mockDebugMeta );

    return mockDebugMetaWrapper;
  }

  private Trans createMockTrans( final Iterable<StepInterface> stepInterfaces ) {
    Trans mockTrans = mock( Trans.class );
    when( mockTrans.findBaseSteps( anyString() ) )
      .then( new Answer<List<StepInterface>>() {
               private StepInterface findStep( String stepName ) {
                 for ( StepInterface stepInterface : stepInterfaces ) {
                   if ( stepInterface.getStepname().equals( stepName ) ) {
                     return stepInterface;
                   }
                 }
                 return null;
               }

               @Override public List<StepInterface> answer( InvocationOnMock invocationOnMock ) throws Throwable {
                 List<StepInterface> steps = new ArrayList<>();
                 String stepName = (String) invocationOnMock.getArguments()[0];
                 StepInterface stepInterface = findStep( stepName );
                 if( stepInterface != null ) {
                   steps.add( stepInterface );
                 }

                 return steps;
               }
             }
      );
    return mockTrans;
  }

  private TransDebugMeta createMockTransDebugMeta( Iterable<StepMeta> stepMetas ) {
    TransDebugMeta mockDebugMeta = mock( TransDebugMeta.class );

    Map<StepMeta, StepDebugMeta> stepMetaMap = new HashMap<>();
    for ( StepMeta stepMeta : stepMetas ) {
      StepDebugMeta mockStepDebugMeta = mock( StepDebugMeta.class );
      stepMetaMap.put( stepMeta, mockStepDebugMeta );
    }
    when( mockDebugMeta.getStepDebugMetaMap() ).thenReturn( stepMetaMap );

    return mockDebugMeta;
  }

  private Collection<StepMeta> createMockStepMetas( Iterable<String> stepNames ) {
    Collection<StepMeta> stepMetas = new ArrayList<>(  );
    for ( String stepName : stepNames ) {
      StepMeta mockStepMeta = mock( StepMeta.class );
      when( mockStepMeta.getName() ).thenReturn( stepName );
      stepMetas.add( mockStepMeta );
    }
    return stepMetas;
  }

  private Collection<StepInterface> createMockStepInterfaces( Iterable<StepMeta> stepMetas ) {
    Collection<StepInterface> stepInterfaces = new ArrayList<>(  );
    for( StepMeta stepMeta : stepMetas ) {
      String stepName = stepMeta.getName();
      StepInterface mockStepInterface = mock( StepInterface.class );
      when( mockStepInterface.getStepMeta() ).thenReturn( stepMeta );
      when( mockStepInterface.getStepname() ).thenReturn( stepName );
      stepInterfaces.add( mockStepInterface );
    }
    return stepInterfaces;
  }
  // endregion
}
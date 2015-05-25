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

package com.pentaho.det.impl.domain;

import com.pentaho.det.api.domain.IDataSource;
import com.pentaho.det.api.services.IDataSourceProvider;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

public class DataSourceProviderAggregatorTest {

  // region Tests

  /**
   * Single DataSourceProvider
   */
  @Test
  public void testGetDataSources() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    int numberOfDataSources = 3;
    Map<String, IDataSource> expectedDataSources = this.createMockDataSourceMap( numberOfDataSources );
    IDataSourceProvider dataSourceProvider = this.createMockDataSourceProvider( expectedDataSources );
    providerAggregator.addDataSourceProvider( dataSourceProvider );

    Map<String, IDataSource> actualDataSources = providerAggregator.getDataSources();

    assertThatMapsEqual( actualDataSources, expectedDataSources );
  }

  /**
   * Multiple DataSourceProviders
   */
  @Test
  public void testGetDataSourcesMultipleDataSourceProviders() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    int numberOfDataSources = 3;
    Map<String, IDataSource> dataSourcesA = this.createMockDataSourceMap( numberOfDataSources );
    IDataSourceProvider dataSourceProviderA = this.createMockDataSourceProvider( dataSourcesA );
    providerAggregator.addDataSourceProvider( dataSourceProviderA );
    Map<String, IDataSource> dataSourcesB = this.createMockDataSourceMap( numberOfDataSources );
    IDataSourceProvider dataSourceProviderB = this.createMockDataSourceProvider( dataSourcesB );
    providerAggregator.addDataSourceProvider( dataSourceProviderB );
    Map<String, IDataSource> expectedDataSources = this.joinMaps( dataSourcesA, dataSourcesB );

    Map<String, IDataSource> actualDataSources = providerAggregator.getDataSources();

    assertThatMapsEqual( actualDataSources, expectedDataSources );
  }

  @Test
  public void testGetDataSource() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    Map<String, IDataSource> expectedDataSources = new HashMap<>();
    String dataSourceUUID = "dataSourceUUID";
    IDataSource expectedDataSource = mock( IDataSource.class );
    expectedDataSources.put( dataSourceUUID, expectedDataSource );

    IDataSourceProvider dataSourceProvider = this.createMockDataSourceProvider( expectedDataSources );
    providerAggregator.addDataSourceProvider( dataSourceProvider );

    IDataSource actualDataSource = providerAggregator.getDataSource( dataSourceUUID );

    assertThat( actualDataSource, sameInstance( expectedDataSource ) );
  }

  @Test
  public void testAddDataSourceProvider() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    IDataSourceProvider provider = mock( IDataSourceProvider.class );

    providerAggregator.addDataSourceProvider( provider );

    assertThat( providerAggregator.getDataSourceProviders(), contains( provider ));
  }

  @Test
  public void testAddSourceProviderTwice() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    IDataSourceProvider dataSourceProvider = this.createMockDataSourceProvider( 0 );

    providerAggregator.addDataSourceProvider( dataSourceProvider );
    providerAggregator.addDataSourceProvider( dataSourceProvider );
    providerAggregator.addDataSourceProvider( dataSourceProvider );

    assertThat( providerAggregator.getDataSourceProviders(), hasSize( 1 ) );
  }

  @Test
  public void testRemoveDataSourceProvider()  {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    IDataSourceProvider provider = mock( IDataSourceProvider.class );
    providerAggregator.addDataSourceProvider( provider );

    providerAggregator.removeDataSourceProvider( provider );

    assertThat( providerAggregator.getDataSourceProviders(), empty() );
  }
  // endregion

  // region auxiliary methods
  private DataSourceProviderAggregator createDataSourceProviderAggregator() {
    return new DataSourceProviderAggregator();
  }

  private Map<String, IDataSource> createMockDataSourceMap( int numberOfDataSources ) {
    Map<String, IDataSource> dataSources = new HashMap<>( numberOfDataSources );
    for ( int iDataSource = 0; iDataSource < numberOfDataSources; iDataSource++ ) {
      IDataSource dataSource = mock( IDataSource.class );
      dataSources.put( this.generateUUID(), dataSource );
    }
    return dataSources;
  }

  private IDataSourceProvider createMockDataSourceProvider( int numberOfDataSources ) {
    IDataSourceProvider dataSourceProvider = mock( IDataSourceProvider.class );
    final Map<String, IDataSource> mockDataSources = this.createMockDataSourceMap( numberOfDataSources );
    when( dataSourceProvider.getDataSources() ).thenAnswer( new Answer<Object>() {
      @Override public Object answer( InvocationOnMock invocationOnMock ) throws Throwable {
        return mockDataSources;
      }
    } );
    return dataSourceProvider;
  }

  private IDataSourceProvider createMockDataSourceProvider( final Map<String, IDataSource> dataSources ) {
    IDataSourceProvider dataSourceProvider = mock( IDataSourceProvider.class );
    when( dataSourceProvider.getDataSources() ).thenAnswer( new Answer<Object>() {
      @Override public Object answer( InvocationOnMock invocationOnMock ) throws Throwable {
        return dataSources;
      }
    } );
    return dataSourceProvider;
  }

  private String generateUUID() {
    return UUID.randomUUID().toString();
  }

  private <K,V> void assertThatMapsEqual( Map<K, V> mapA, Map<K, V> mapB ) {
    Set<Map.Entry<K,V>> entrySetA = mapA.entrySet();
    Set<Map.Entry<K,V>> entrySetB = mapB.entrySet();

    assertThat( entrySetA, everyItem( isIn( entrySetB ) ) );
    assertThat( entrySetB, everyItem( isIn( entrySetA ) ) );
  }

  /**
   * Joins two maps. Duplicate keys receive the value from {@code mapB}
   * @param mapA
   * @param mapB
   * @param <K>
   * @param <V>
   * @return
   */
  private <K,V> Map<K, V> joinMaps( Map<K, V> mapA, Map<K, V> mapB ) {
    Map<K, V> joinedMap = new HashMap<>();
    joinedMap.putAll( mapA );
    joinedMap.putAll( mapB );

    return joinedMap;
  }
  // endregion

}
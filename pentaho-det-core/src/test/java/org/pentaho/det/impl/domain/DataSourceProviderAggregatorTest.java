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

package org.pentaho.det.impl.domain;

import org.pentaho.det.api.domain.IDataSource;
import org.pentaho.det.api.services.IDataSourceProvider;
import org.junit.Test;

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
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DataSourceProviderAggregatorTest {

  // region Tests

  /**
   * Tests that all data sources are returned by {@link DataSourceProviderAggregator#getDataSources()} when the {@link DataSourceProviderAggregator} aggregates a single {@link IDataSourceProvider}.
   */
  @Test
  public void testGetDataSourcesSingleDataSourceProvider() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    int numberOfDataSources = 3;
    Map<UUID, IDataSource> expectedDataSources = this.createMockDataSourceMap( numberOfDataSources );
    IDataSourceProvider dataSourceProvider = this.createMockDataSourceProvider( expectedDataSources );
    providerAggregator.addDataSourceProvider( dataSourceProvider );

    Map<UUID, IDataSource> actualDataSources = providerAggregator.getDataSources();

    assertThatMapsEqual( actualDataSources, expectedDataSources );
  }

  /**
   * Tests that all data sources are returned by {@link DataSourceProviderAggregator#getDataSources()} when the {@link DataSourceProviderAggregator} aggregates a multiple {@link IDataSourceProvider}.
   */
  @Test
  public void testGetDataSourcesMultipleDataSourceProviders() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    int numberOfDataSources = 3;
    Map<UUID, IDataSource> dataSourcesA = this.createMockDataSourceMap( numberOfDataSources );
    IDataSourceProvider dataSourceProviderA = this.createMockDataSourceProvider( dataSourcesA );
    providerAggregator.addDataSourceProvider( dataSourceProviderA );
    Map<UUID, IDataSource> dataSourcesB = this.createMockDataSourceMap( numberOfDataSources );
    IDataSourceProvider dataSourceProviderB = this.createMockDataSourceProvider( dataSourcesB );
    providerAggregator.addDataSourceProvider( dataSourceProviderB );
    Map<UUID, IDataSource> expectedDataSources = this.joinMaps( dataSourcesA, dataSourcesB );

    Map<UUID, IDataSource> actualDataSources = providerAggregator.getDataSources();

    assertThatMapsEqual( actualDataSources, expectedDataSources );
  }

  /**
   * Tests that the correct {@link IDataSource} is returned by {@link DataSourceProviderAggregator#getDataSource(UUID)} with the UUID of a {@link IDataSource} that is provided by a {@link IDataSourceProvider} aggregated by the {@link DataSourceProviderAggregator}.
   */
  @Test
  public void testGetExistingDataSourceFromUUID() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    Map<UUID, IDataSource> expectedDataSources = new HashMap<>();
    UUID dataSourceUUID = this.generateUUID();
    IDataSource expectedDataSource = mock( IDataSource.class );
    expectedDataSources.put( dataSourceUUID, expectedDataSource );

    IDataSourceProvider dataSourceProvider = this.createMockDataSourceProvider( expectedDataSources );
    providerAggregator.addDataSourceProvider( dataSourceProvider );

    IDataSource actualDataSource = providerAggregator.getDataSource( dataSourceUUID );

    assertThat( actualDataSource, sameInstance( expectedDataSource ) );
  }

  /**
   * Tests that null is returned by {@link DataSourceProviderAggregator#getDataSource(UUID)} with an UUID of a {@link IDataSource} that is NOT provided by a {@link IDataSourceProvider} aggregated by the {@link DataSourceProviderAggregator}.
   */
  @Test
  public void testGetNonExistingDataSourceFromUUID() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    UUID nonExistingDataSourceUUID = this.generateUUID();

    IDataSource actualDataSource = providerAggregator.getDataSource( nonExistingDataSourceUUID );

    assertThat( actualDataSource, nullValue() );
  }


  /**
   * Tests that {@link DataSourceProviderAggregator#addDataSourceProvider(IDataSourceProvider)} adds a {@link IDataSourceProvider} to the {@link DataSourceProviderAggregator}
   */
  @Test
  public void testAddDataSourceProvider() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    IDataSourceProvider provider = mock( IDataSourceProvider.class );

    providerAggregator.addDataSourceProvider( provider );

    assertThat( providerAggregator.getDataSourceProviders(), contains( provider ) );
  }

  /**
   * Tests that a {@link IDataSourceProvider} is only added once to the {@link DataSourceProviderAggregator} with multiple calls to {@link DataSourceProviderAggregator#addDataSourceProvider(IDataSourceProvider)}
   */
  @Test
  public void testAddSourceProviderTwice() {
    DataSourceProviderAggregator providerAggregator = this.createDataSourceProviderAggregator();
    IDataSourceProvider dataSourceProvider = this.createMockDataSourceProvider( 0 );

    providerAggregator.addDataSourceProvider( dataSourceProvider );
    providerAggregator.addDataSourceProvider( dataSourceProvider );

    assertThat( providerAggregator.getDataSourceProviders(), hasSize( 1 ) );
  }

  /**
   * Tests that {@link DataSourceProviderAggregator#removeDataSourceProvider(IDataSourceProvider)} removes a {@link IDataSourceProvider} from the {@link DataSourceProviderAggregator}
   */
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

  /**
   * Method wrapper for the creation of the {@link DataSourceProviderAggregator} to test
   */
  private DataSourceProviderAggregator createDataSourceProviderAggregator() {
    return new DataSourceProviderAggregator();
  }

  /**
   * Creates a Map with generated UUIDs for keys and mocked {@link IDataSource} for values
   * @param numberOfDataSources how many {@link IDataSource} to mock
   */
  private Map<UUID, IDataSource> createMockDataSourceMap( int numberOfDataSources ) {
    Map<UUID, IDataSource> dataSources = new HashMap<>( numberOfDataSources );
    for ( int iDataSource = 0; iDataSource < numberOfDataSources; iDataSource++ ) {
      IDataSource dataSource = mock( IDataSource.class );
      dataSources.put( this.generateUUID(), dataSource );
    }
    return dataSources;
  }

  /**
   * Creates a mock {@link IDataSourceProvider} with {@code numberOfDataSources} mocked {@link IDataSource}
   */
  private IDataSourceProvider createMockDataSourceProvider( int numberOfDataSources ) {
    IDataSourceProvider dataSourceProvider = mock( IDataSourceProvider.class );
    final Map<UUID, IDataSource> mockDataSources = this.createMockDataSourceMap( numberOfDataSources );
    when( dataSourceProvider.getDataSources() ).thenAnswer( new Answer<Object>() {
      @Override public Object answer( InvocationOnMock invocationOnMock ) throws Throwable {
        return mockDataSources;
      }
    } );
    return dataSourceProvider;
  }

  /**
   * Creates a mock {@link IDataSourceProvider} with the given Map of {@link IDataSource}
   */
  private IDataSourceProvider createMockDataSourceProvider( final Map<UUID, IDataSource> dataSources ) {
    IDataSourceProvider dataSourceProvider = mock( IDataSourceProvider.class );
    when( dataSourceProvider.getDataSources() ).thenAnswer( new Answer<Object>() {
      @Override public Object answer( InvocationOnMock invocationOnMock ) throws Throwable {
        return dataSources;
      }
    } );
    return dataSourceProvider;
  }

  private UUID generateUUID() {
    return UUID.randomUUID();
  }

  /**
   * Asserts that all entries of {@code mapA} are in {@code mapB} and that all entries of {@code mapB} are in {@code mapA}.
   */
  private <K,V> void assertThatMapsEqual( Map<K, V> mapA, Map<K, V> mapB ) {
    Set<Map.Entry<K,V>> entrySetA = mapA.entrySet();
    Set<Map.Entry<K,V>> entrySetB = mapB.entrySet();

    assertThat( entrySetA, everyItem( isIn( entrySetB ) ) );
    assertThat( entrySetB, everyItem( isIn( entrySetA ) ) );
  }

  /**
   * Joins two maps. Duplicate keys receive the v from {@code mapB}
   */
  private <K,V> Map<K, V> joinMaps( Map<K, V> mapA, Map<K, V> mapB ) {
    Map<K, V> joinedMap = new HashMap<>();
    joinedMap.putAll( mapA );
    joinedMap.putAll( mapB );

    return joinedMap;
  }
  // endregion

}
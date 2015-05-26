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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DataSourceProviderAggregator implements IDataSourceProvider {

  // region Properties
  /**
   * @return the {@link IDataSourceProvider} aggregated by {@link DataSourceProviderAggregator}
   */
  public Set<IDataSourceProvider> getDataSourceProviders() {
    return this.dataSourceProviders;
  }
  private Set<IDataSourceProvider> dataSourceProviders;

  /**
   * @return The aggregated Map of the provided {@link IDataSource} by the aggregated {@link IDataSourceProvider}.
   */
  @Override public Map<String, IDataSource> getDataSources() {
    Map<String, IDataSource> dataSources = new HashMap<>(  );
    for ( IDataSourceProvider provider : this.getDataSourceProviders() ) {
      dataSources.putAll( provider.getDataSources() );
    }
    return dataSources;
  }

  // endregion

  // region Constructors
  public DataSourceProviderAggregator() {
    // TODO check DI
    this.dataSourceProviders = new HashSet<>(  );
  }
  // endregion

  // region Methods
  /**
   * Searches the providers for the given data source. Returns null if no data source with the id was found.
   * @param dataSourceId
   * @return
   */
  public IDataSource getDataSource( String dataSourceId ) {
    IDataSource dataSource = null;
    for ( IDataSourceProvider provider : this.getDataSourceProviders() ) {
      dataSource = provider.getDataSources().get( dataSourceId );
      if ( dataSource != null ) {
        return dataSource;
      }
    }

    // no datasource found with the id
    return null;
  }

  public void addDataSourceProvider( IDataSourceProvider provider ) {
    this.getDataSourceProviders().add( provider );
  }

  public void removeDataSourceProvider( IDataSourceProvider provider ) {
    this.getDataSourceProviders().remove( provider );
  }
  // endregion

}

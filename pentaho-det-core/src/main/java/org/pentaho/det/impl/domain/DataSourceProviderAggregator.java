/*
 * Copyright 2002 - 2015 Webdetails, a Pentaho company. All rights reserved.
 *
 * This software was developed by Webdetails and is provided under the terms
 * of the Mozilla Public License, Version 2.0, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */

package org.pentaho.det.impl.domain;

import org.pentaho.det.api.domain.IDataSource;
import org.pentaho.det.api.services.IDataSourceProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


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
  @Override public Map<UUID, IDataSource> getDataSources() {
    Map<UUID, IDataSource> dataSources = new HashMap<>(  );
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
  public IDataSource getDataSource( UUID dataSourceId ) {
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

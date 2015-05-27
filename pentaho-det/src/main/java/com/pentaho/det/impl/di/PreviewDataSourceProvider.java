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

import com.pentaho.det.api.services.IDataSourceProvider;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.debug.StepDebugMeta;
import org.pentaho.di.trans.debug.TransDebugMeta;
import org.pentaho.di.trans.debug.TransDebugMetaWrapper;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.ui.spoon.SpoonUiExtenderPlugin;
import org.pentaho.di.ui.spoon.SpoonUiExtenderPluginInterface;
import org.pentaho.di.ui.spoon.trans.TransGraph;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpoonUiExtenderPlugin( id = "pentaho-det-preview-data-source-provider", image = "" )
public class PreviewDataSourceProvider implements IDataSourceProvider, SpoonUiExtenderPluginInterface {

  // region Inner Definitions
  private static final class RegisterDataSources extends EventHandler<TransDebugMetaWrapper> {

    private final Map<UUID, StepPreviewDataSource> dataSources;

    // region Constructors
    public RegisterDataSources( Map<UUID, StepPreviewDataSource> dataSources ) {
      super( TransDebugMetaWrapper.class );

      this.dataSources = dataSources;
    }
    // endregion

    @Override public void handle( TransDebugMetaWrapper transDebugMetaWrapper ) {
      // clear datasources as new steps are created for each
      this.dataSources.clear();

      final Trans trans = transDebugMetaWrapper.getTrans();
      TransDebugMeta transDebugMeta = transDebugMetaWrapper.getTransDebugMeta();
      Map<StepMeta, StepDebugMeta> stepDebugMetaMap = transDebugMeta.getStepDebugMetaMap();
      for ( final StepMeta stepMeta : stepDebugMetaMap.keySet() ) {
        String stepName = stepMeta.getName();
        for ( StepInterface baseStep : trans.findBaseSteps( stepName ) ) {
          StepPreviewDataSource dataSource = new StepPreviewDataSource( baseStep );
          this.dataSources.put( dataSource.getUUID(), dataSource );
        }
      }
    }

  }

  // endregion

  // region Properties
  public Map<UUID, StepPreviewDataSource> getDataSources() {
    return Collections.unmodifiableMap( this.dataSources );
  }
  public PreviewDataSourceProvider setDataSources( Map<UUID, StepPreviewDataSource> dataSources ) {
    this.dataSources = dataSources;
    return this;
  }
  private Map<UUID, StepPreviewDataSource> dataSources;

  private EventHandlerMap getEventHandlers() {
    return this.eventHandlers;
  }
  private EventHandlerMap eventHandlers;

  // endregion

  // region Constructors
  public PreviewDataSourceProvider() {
    // TODO: verify threading
    Map<UUID, StepPreviewDataSource> dataSources = new Hashtable<>(  ); //new ConcurrentHashMap<>();
    this.setDataSources( dataSources );

    this.eventHandlers = new EventHandlerMap();
    this.eventHandlers.addEventHandler( TransGraph.PREVIEW_TRANS, new RegisterDataSources( dataSources ) );
  }
  // endregion

  // region SpoonUiExtenderPluginInterface

  /**
   * @inheritDoc
   */
  @Override public Map<Class<?>, Set<String>> respondsTo() {
    return this.getEventHandlers().respondsTo();
  }

  /**
   * @inheritDoc
   */
  @Override public void uiEvent( Object o, String s ) {
    this.getEventHandlers().callHandlers( o, s );
  }
  // endregion

}

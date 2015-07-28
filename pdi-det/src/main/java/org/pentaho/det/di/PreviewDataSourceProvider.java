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

package org.pentaho.det.di;

import org.pentaho.det.api.services.IDataSourceProvider;
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

// TODO: check plugin id
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
      // for each step being debugged / previewed
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

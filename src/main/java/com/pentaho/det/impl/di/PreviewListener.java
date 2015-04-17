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

import org.pentaho.di.trans.debug.TransDebugMetaWrapper;

import org.pentaho.di.ui.spoon.SpoonUiExtenderPlugin;
import org.pentaho.di.ui.spoon.SpoonUiExtenderPluginInterface;
import org.pentaho.di.ui.spoon.trans.TransGraph;
import org.pentaho.di.ui.spoon.trans.TransPreviewDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

@SpoonUiExtenderPlugin( id = "pentaho-det-preview", image = "" )
public class PreviewListener implements SpoonUiExtenderPluginInterface {

  // region Properties
  private EventHandlerMap eventHandlers = new EventHandlerMap();
  private EventHandlerMap getEventHandlers() {
    return this.eventHandlers;
  }

  private static final Logger logger = LoggerFactory.getLogger( PreviewListener.class );
  private Logger getLogger() {
    return this.logger;
  }

  // endregion

  // region Constructors
  public PreviewListener() {
    EventHandlerMap.IEventHandler<TransDebugMetaWrapper> previewStartedHandler =
        new EventHandlerMap.EventHandler<TransDebugMetaWrapper>( TransDebugMetaWrapper.class ) {
          @Override public void handle( TransDebugMetaWrapper arg ) {
            int x = 42;
          }
        };

    EventHandlerMap.IEventHandler<TransPreviewDialog> previewDialogOpenHandler =
        new EventHandlerMap.EventHandler<TransPreviewDialog>( TransPreviewDialog.class ) {
          @Override public void handle( TransPreviewDialog arg ) {
            int x = 42;
          }
        };

    EventHandlerMap.IEventHandler<TransPreviewDialog.TransPreviewDialogSetDataWrapper> previewDialogDataSetHandler =
        new EventHandlerMap.EventHandler<TransPreviewDialog.TransPreviewDialogSetDataWrapper>( TransPreviewDialog.TransPreviewDialogSetDataWrapper.class ) {
          @Override public void handle( TransPreviewDialog.TransPreviewDialogSetDataWrapper arg ) {
            int x = 42;
          }
        };

    EventHandlerMap eventHandlers = this.getEventHandlers();
    eventHandlers.putEventHandler( TransGraph.PREVIEW_TRANS, previewStartedHandler );
    eventHandlers.putEventHandler( TransPreviewDialog.TRANS_PREVIEW_DIALOG,  previewDialogOpenHandler );
    eventHandlers.putEventHandler( TransPreviewDialog.TRANS_PREVIEW_DIALOG_SET_DATA, previewDialogDataSetHandler );

  }
  // endregion

  // region Methods
  @Override
  public Map<Class<?>, Set<String>> respondsTo() {
    return this.getEventHandlers().getEventMapping();
  }

  @Override
  public void uiEvent( Object o, String s ) {
    this.getEventHandlers().handle( o, s );
  }

  // endregion

}

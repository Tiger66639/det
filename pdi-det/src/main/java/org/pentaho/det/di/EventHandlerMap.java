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

package org.pentaho.det.di;

import org.pentaho.di.ui.spoon.SpoonUiExtenderPluginInterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *  Auxiliary class to make mapping of Spoon UI events to event handlers easier.
 */
public class EventHandlerMap implements SpoonUiExtenderPluginInterface {

  // region inner definitions

  /**
   * Auxiliary class that encodes the type of an event.
   * An EventType is characterized by its name and the type of the context object it is associated with.
   * @param <TContext> The type of the context object associated with the EventType
   */
  private static final class EventType<TContext> {

    /**
     * @return the type of the context object associated with this EventType
     */
    public Class<TContext> getContextClass() {
      return this.contextClass;
    }
    private final Class<TContext> contextClass;

    /**
     * @return the EventType name
     */
    public String getName() {
      return this.name;
    }
    private final String name;

    public EventType( Class<TContext> contextClass, String eventTypeName ) {
      if ( contextClass == null ) {
        throw new IllegalArgumentException( "EventType class constructor cannot receive null contextClass argument." );
      }
      if ( eventTypeName == null ) {
        throw new IllegalArgumentException( "EventType class constructor cannot receive null event type name argument." );
      }

      this.contextClass = contextClass;
      this.name = eventTypeName;
    }

    @Override public boolean equals( Object o ) {
      if ( this == o ) {
        return true;
      }
      if ( o == null || getClass() != o.getClass() ) {
        return false;
      }

      EventType eventType = (EventType) o;

      return name.equals( eventType.name );

    }

    @Override public int hashCode() {
      int result = name.hashCode();
      return result;
    }
  }

  /**
   * Auxiliary EventHandler wrapper that contains the unique identifier by which the handler is identified in this EventHandlerMap.
   * @param <TContext> The type of the context object that is passed to the handler when an event is handled.
   */
  private static final class EventHandlerWrapper<TContext> {

    public final String uuid;
    public final IEventHandler<TContext> eventHandler;

    public EventHandlerWrapper( IEventHandler<TContext> eventHandler ) {
      if ( eventHandler == null ) {
        throw new IllegalArgumentException( "EventHandlerWrapper class constructor cannot receive null eventHandler argument." );
      }

      this.eventHandler = eventHandler;
      this.uuid = UUID.randomUUID().toString();
    }
  }
  // endregion

  // region Properties

  /**
   * Each {@link EventType} has a collection of
   * @return a Map
   */
  private Map<EventType, Map<String, EventHandlerWrapper>> getEventHandlers() {
    return this.eventHandlers;
  }
  private Map<EventType, Map<String, EventHandlerWrapper>> eventHandlers = new Hashtable<>();
  // endregion

  // region Methods
  private <T> Map<String, EventHandlerWrapper> getEventHandlers( EventType<T> eventType ) {
    Map<EventType, Map<String, EventHandlerWrapper>> allEventHandlers = this.getEventHandlers();
    // lazy create event map for event type
    if ( allEventHandlers.get( eventType ) == null ) {
      Map<String, EventHandlerWrapper> eventHandlers = new Hashtable<>();
      allEventHandlers.put( eventType, eventHandlers );
    }

    return allEventHandlers.get( eventType );
  }

  /**
   * Adds an {@link IEventHandler} to be called when an event of the type specified by {@code eventTypeName} is triggered.
   * @param eventTypeName the name of the event type.
   * @param eventHandler the event handler to add.
   * @param <TContext> The type of the context object associated with the event type.
   * @return the unique identifier associated with the event handler registration. This identifier is used to remove the event handler.
   */
  public <TContext> String addEventHandler( String eventTypeName, IEventHandler<TContext> eventHandler ) {
    if ( isNullOrEmpty( eventTypeName ) || eventHandler == null ) {
      throw new IllegalArgumentException();
    }

    EventType<TContext> eventType = new EventType<>( eventHandler.getTypeParameter() , eventTypeName );
    EventHandlerWrapper<TContext> eventHandlerWrapper = new EventHandlerWrapper<>( eventHandler );
    this.getEventHandlers( eventType ).put( eventHandlerWrapper.uuid, eventHandlerWrapper );
    return eventHandlerWrapper.uuid;
  }

  /**
   * Removes the {@link IEventHandler} associated with the {@code uuid}.
   * @param uuid The unique identifier associated with the event handler to remove.
   * @return true if an event handler was removed, false otherwise.
   */
  public boolean removeEventHandler( String uuid ) {
    for ( Map<String, EventHandlerWrapper> eventHandlers : this.getEventHandlers().values() ) {
      EventHandlerWrapper eventHandlerWrapper = eventHandlers.remove( uuid );
      if ( eventHandlerWrapper != null ) { // if handler removed, exit
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the event type names that have associated event handlers. These event types are groupped by the context object class associated with them.
   * @return a Map where the keys are context object type and the values are Sets of event type names
   */
  @Override public Map<Class<?>, Set<String>> respondsTo() {
    Map<Class<?>, Set<String>> eventMap = new HashMap<>();
    for ( EventType eventType : this.getEventHandlers().keySet() ) {
      Class<?> clazz = eventType.getContextClass();
      Set<String> eventNames = eventMap.get( clazz );
      if ( eventNames != null ) {
        eventNames.add( eventType.getName() );
      } else {
        eventNames = new HashSet<>( Collections.singletonList( eventType.getName() ) );
        eventMap.put( clazz, eventNames );
      }
    }
    return eventMap;
  }

  /**
   * @see EventHandlerMap#callHandlers(Object, String)
   */
  @Override public void uiEvent( Object eventContextObject, String eventTypeName ) {
    this.callHandlers( eventContextObject, eventTypeName );
  }

  /**
   * Executes {@link IEventHandler#handle(TContext)} for all registered event handlers for the event type.
   * @param eventContextObject the context object to passed to the handler.
   * @param <TContext> the type of the context object.
   * @param eventTypeName the event type name.
   */
  public <TContext> void callHandlers( TContext eventContextObject, String eventTypeName ) {
    if ( eventContextObject == null || eventTypeName == null ) {
      throw new IllegalArgumentException();
    }
    EventType<TContext> eventType = new EventType( eventContextObject.getClass(), eventTypeName );
    for ( EventHandlerWrapper<TContext> eventHandlerWrapper : this.getEventHandlers( eventType ).values() ) {
      eventHandlerWrapper.eventHandler.handle( eventContextObject );
    }

  }

  // region aux
  private static boolean isNullOrEmpty( String string ) {
    return string == null || string.isEmpty();
  }
  // endregion

  // endregion

}

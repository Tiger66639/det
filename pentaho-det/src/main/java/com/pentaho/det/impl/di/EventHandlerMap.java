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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class EventHandlerMap {

  // region inner definitions
  private static final class EventId<T> {
    public Class<T> getContextClass() {
      return this.contextClass;
    }

    private final Class<T> contextClass;

    public String getEventName() {
      return this.eventName;
    }

    private final String eventName;

    public EventId( Class<T> contextClass, String eventName ) {
      if ( contextClass == null ) {
        throw new IllegalArgumentException( "EventId class constructor cannot receive null contextClass argument." );
      }
      if ( eventName == null ) {
        throw new IllegalArgumentException( "EventId class constructor cannot receive null eventName argument." );
      }

      this.contextClass = contextClass;
      this.eventName = eventName;
    }


    @Override public boolean equals( Object o ) {
      if ( this == o ) {
        return true;
      }
      if ( o == null || getClass() != o.getClass() ) {
        return false;
      }

      EventId eventId = (EventId) o;

      if ( !contextClass.equals( eventId.contextClass ) ) {
        return false;
      }
      return eventName.equals( eventId.eventName );

    }

    @Override public int hashCode() {
      int result = contextClass.hashCode();
      result = 31 * result + eventName.hashCode();
      return result;
    }
  }
  // endregion

  // region Properties
  private Map<EventId<?>, IEventHandler> eventHandlers = new Hashtable<>();
  private Map<EventId<?>, IEventHandler> getEventHandlers() {
    return this.eventHandlers;
  }

  public Map<Class<?>, Set<String>> getEventMapping() {
    Map<Class<?>, Set<String>> eventMap = new HashMap<>();
    for ( EventId eventId : this.getEventHandlers().keySet() ) {
      Class<?> clazz = eventId.getContextClass();
      Set<String> eventNames = eventMap.get( clazz );
      if ( eventNames != null ) {
        eventNames.add( eventId.getEventName() );
      } else {
        eventNames = new HashSet<>( Collections.singletonList( eventId.getEventName() ) );
        eventMap.put( clazz, eventNames );
      }
    }
    return eventMap;
  }
  // endregion

  // region Methods
  public <T> void handle( T object, String eventName ) {
    if ( object == null || eventName == null ) {
      throw new IllegalArgumentException();
    }
    EventId<T> eventId = new EventId( object.getClass(), eventName );
    IEventHandler<T> eventHandler = this.getEventHandlers().get( eventId );
    if ( eventHandler != null ) {
      eventHandler.handle( object );
    }
  }

  public <T> void putEventHandler( String eventName, IEventHandler<T> eventHandler ) {
    EventId<T> eventId = new EventId<>( eventHandler.getTypeParameter() , eventName );
    this.getEventHandlers().put( eventId, eventHandler );
  }

  public <T> void removeEventHandler( Class<T> clazz, String eventName ) {
    EventId<T> eventId = new EventId<>( clazz, eventName );
    this.getEventHandlers().remove( eventId );
  }

  // endregion

}

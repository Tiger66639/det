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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;


public class EventHandlerMapTest {


  // region auxiliary methods
  private <T> Collection<IEventHandler<T>> createMockEventHandlerCollection( Class<T> clazz, int numberOfEventHandlers ) {
    Collection<IEventHandler<T>> eventHandlers = new ArrayList<>( numberOfEventHandlers );
    for ( int iEventHandler = 0; iEventHandler < numberOfEventHandlers; iEventHandler++ ) {
      IEventHandler<T> eventHandler = createMock( clazz );
      eventHandlers.add( eventHandler );
    }
    return eventHandlers;
  }

  private <T> void addToEventHandlerMap( EventHandlerMap eventHandlerMap, String eventTypeName, Iterable<IEventHandler<T>> eventHandlers ) {
    for ( IEventHandler<T> eventHandler : eventHandlers ) {
      eventHandlerMap.addEventHandler( eventTypeName, eventHandler );
    }
  }

  private <T> IEventHandler<T> createMock( Class<T> clazz ) {
    @SuppressWarnings("unchecked")
    IEventHandler<T> eventHandler = mock( IEventHandler.class );
    when( eventHandler.getTypeParameter() ).thenReturn( clazz );
    return eventHandler;
  }

  // endregion

  // region Tests

  /**
   * Tests that after removing an event handler, it is not called when invoking {@link EventHandlerMap#callHandlers(Object, String)}
   */
  @Test
  public void testRemoveEventHandler() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();
    String eventTypeName = "myEventTypeName";
    IEventHandler<Integer> eventHandler = createMock( Integer.class );

    String eventHandlerRegistry = eventHandlerMap.addEventHandler( eventTypeName, eventHandler );
    eventHandlerMap.removeEventHandler( eventHandlerRegistry );

    Integer contextObject = 4;
    eventHandlerMap.callHandlers( contextObject, eventTypeName );

    verify( eventHandler, never() ).handle( anyInt() );
  }

  /**
   * Tests that {@link EventHandlerMap#removeEventHandler(String)} returns true when removing an existing event handler
   */
  @Test
  public void testRemoveExistingEventHandlerReturnsTrue() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();
    String eventTypeName = "myEventTypeName";
    IEventHandler<Integer> eventHandler = createMock( Integer.class );

    String eventHandlerRegistry = eventHandlerMap.addEventHandler( eventTypeName, eventHandler );
    boolean removeEventHandler = eventHandlerMap.removeEventHandler( eventHandlerRegistry );

    assertThat( removeEventHandler, is( true ) );
  }

  /**
   * Tests that {@link EventHandlerMap#removeEventHandler(String)} returns false when removing an event handler that does not exist in the {@link EventHandlerMap}
   */
  @Test
  public void testRemoveNonExistingEventHandlerReturnsFalse() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();
    String eventTypeName = "myEventTypeName";
    IEventHandler<Integer> eventHandler = createMock( Integer.class );

    String eventHandlerRegistry = eventHandlerMap.addEventHandler( eventTypeName, eventHandler );
    String nonExistingHandlerRegistry = eventHandlerRegistry + "NON_EXISTING";
    boolean removeEventHandler = eventHandlerMap.removeEventHandler( nonExistingHandlerRegistry );

    assertThat( removeEventHandler, is( false ) );
  }

  /**
   * Tests that when an event handler has no registered event handlers the {@link EventHandlerMap#respondsTo()} method returns an empty {@link Map}
   */
  @Test
  public void testEmptyResponds() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();

    Map<Class<?>, Set<String>> eventTypeResponseMap = eventHandlerMap.respondsTo();

    assertThat( eventTypeResponseMap.size(), is( 0 ) );
  }

  /**
   * Tests that {@link EventHandlerMap#respondsTo()} method returns the appropriate {@link Map} for the registered event handlers
   */
  @Test
  public void testResponds() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();

    String eventTypeNameA = "myEventTypeNameA";
    Class<Integer> contextTypeAB = Integer.class;
    IEventHandler<Integer> eventHandlerA = createMock( contextTypeAB );
    eventHandlerMap.addEventHandler( eventTypeNameA, eventHandlerA );

    String eventTypeNameB = "myEventTypeNameB";
    IEventHandler<Integer> eventHandlerB = createMock( contextTypeAB );
    eventHandlerMap.addEventHandler( eventTypeNameB, eventHandlerB );

    String eventTypeNameC = "myEventTypeNameC";
    Class<String> contextTypeC = String.class;
    IEventHandler<String> eventHandlerC = createMock( contextTypeC );
    eventHandlerMap.addEventHandler( eventTypeNameC, eventHandlerC );

    // Act
    Map<Class<?>, Set<String>> eventTypeResponseMap = eventHandlerMap.respondsTo();

    // Assert
    assertThat( eventTypeResponseMap, hasEntry( is ( (Object) contextTypeAB ),
                                                containsInAnyOrder( eventTypeNameA, eventTypeNameB ) ) );

    assertThat( eventTypeResponseMap, hasEntry( is ( (Object) contextTypeC ),
                                                contains( eventTypeNameC ) ) );
  }

  /**
   * Tests that a single registered event handler is executed with {@link EventHandlerMap#callHandlers(Object, String)} invocation
   */
  @Test
  public void testSingleEventHandlerExecuted() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();
    String eventTypeName = "myEventTypeName";
    IEventHandler<Integer> eventHandler = createMock( Integer.class );
    Integer contextObject = 4;

    eventHandlerMap.addEventHandler( eventTypeName, eventHandler );
    eventHandlerMap.callHandlers( contextObject, eventTypeName );

    verify( eventHandler, times( 1 ) ).handle( contextObject );
  }

  /**
   * Tests that a multiple registered event handlers for the same event type are executed with {@link EventHandlerMap#callHandlers(Object, String)} invocation
   */
  @Test
  public void testMultipleEventHandlerSameEventTypeExecuted() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();
    String eventTypeName = "myEventTypeName";
    int numberOfEventHandlers = 3;
    Collection<IEventHandler<Integer>> eventHandlers = this.createMockEventHandlerCollection( Integer.class, numberOfEventHandlers );
    this.addToEventHandlerMap( eventHandlerMap, eventTypeName, eventHandlers );

    Integer contextObject = 4;
    eventHandlerMap.callHandlers( contextObject, eventTypeName );

    for( IEventHandler<Integer> eventHandler : eventHandlers ) {
      verify( eventHandler, times( 1 ) ).handle( contextObject );
    }
  }

  /**
   * Tests that a multiple registered event handlers for different event type names are executed with {@link EventHandlerMap#callHandlers(Object, String)} invocation
   */
  @Test
  public void testMultipleEventHandlerDifferentEventTypeExecuted() {
    EventHandlerMap eventHandlerMap = new EventHandlerMap();
    String eventTypeNameA = "myEventTypeNameA";
    String eventTypeNameB = "myEventTypeNameB";
    Class<Integer> contextType = Integer.class;

    int numberOfEventHandlers = 3;
    Collection<IEventHandler<Integer>> eventHandlersA = createMockEventHandlerCollection( contextType, numberOfEventHandlers );
    addToEventHandlerMap( eventHandlerMap, eventTypeNameA, eventHandlersA );
    Collection<IEventHandler<Integer>> eventHandlersB = createMockEventHandlerCollection( contextType, numberOfEventHandlers );
    addToEventHandlerMap( eventHandlerMap, eventTypeNameB, eventHandlersB );

    Integer contextObject = 4;
    eventHandlerMap.callHandlers( contextObject, eventTypeNameA );

    for( IEventHandler<Integer> eventHandler : eventHandlersA ) {
      verify( eventHandler, times( 1 ) ).handle( contextObject );
    }
    for( IEventHandler<Integer> eventHandler : eventHandlersB ) {
      verify( eventHandler, never() ).handle( contextObject );
    }

  }
  // endregion

}
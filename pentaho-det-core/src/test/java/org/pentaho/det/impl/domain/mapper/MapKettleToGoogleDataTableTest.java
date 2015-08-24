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

package org.pentaho.det.impl.domain.mapper;

import org.junit.Test;
import org.pentaho.det.api.domain.IField.ColumnType;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertEquals;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.not;

public class MapKettleToGoogleDataTableTest {

    // region Tests
    @Test
    public void testDefaultConfiguration() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        Map<Integer, ColumnType> stringMap = mapper.getMapping();
        Map<Integer, ColumnType> expectedMap = this.createDefaultStringMap();

        assertEquals( expectedMap.size(), stringMap.size() );
        for( Integer key : expectedMap.keySet() ) {
            assertEquals( expectedMap.get(key), stringMap.get(key) );
        }
    }

    @Test
    public void testGetDataType() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        ColumnType expectedDataType = ColumnType.NUMBER;
        assertEquals( expectedDataType, mapper.getDataType( ValueMetaInterface.TYPE_INTEGER ) );
    }

    @Test
    public void testGetDataTypeDefault() {
        MapKettleToGoogleDataTable mapper = this.createEmptyMapper();
        ColumnType defaultType = ColumnType.STRING;
        ValueMetaInterface valueMeta = this.createValueMetaInterface( -1 );

        assertEquals( defaultType, mapper.getDataType( valueMeta ) );
    }

    @Test
    public void testAddMapValue() {
        MapKettleToGoogleDataTable mapper = this.createEmptyMapper();
        ValueMetaInterface  valueMeta = this.createValueMetaInterface( 99 );
        ColumnType type = ColumnType.NUMBER;

        mapper.addMapValue( valueMeta, type );
        assertEquals( type, mapper.getDataType( valueMeta ) );
    }

    @Test
    public void testRemoveMapValue() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        Integer metaType = ValueMetaInterface.TYPE_BIGNUMBER;
        ColumnType colType = ColumnType.NUMBER;

        assertThat(mapper.getMapping(), hasEntry(metaType, colType));
        mapper.removeMapValue( metaType );
        assertThat(mapper.getMapping(), not(hasEntry(metaType, colType)));
    }
    //endregion


    // region auxiliary methods
    private MapKettleToGoogleDataTable createDefaultMapper() {
        return new MapKettleToGoogleDataTable();
    }

    private MapKettleToGoogleDataTable createEmptyMapper() {
        return new MapKettleToGoogleDataTable( new HashMap<Integer, ColumnType>() );
    }

    private Map<Integer, ColumnType> createDefaultStringMap() {
        Map<Integer, ColumnType> map = new HashMap<Integer, ColumnType>();

        map.put( ValueMetaInterface.TYPE_INTEGER, ColumnType.NUMBER );
        map.put( ValueMetaInterface.TYPE_NUMBER, ColumnType.NUMBER );
        map.put( ValueMetaInterface.TYPE_BIGNUMBER, ColumnType.NUMBER );
        map.put( ValueMetaInterface.TYPE_BOOLEAN, ColumnType.BOOLEAN );
        map.put( ValueMetaInterface.TYPE_DATE, ColumnType.DATETIME );
        map.put( ValueMetaInterface.TYPE_TIMESTAMP, ColumnType.DATETIME );
        map.put( ValueMetaInterface.TYPE_STRING, ColumnType.STRING );

        return map;
    }

    private ValueMetaInterface createValueMetaInterface( int valueType ) {
        ValueMetaInterface vmi = mock( ValueMetaInterface.class );
        when( vmi.getType() ).thenReturn( valueType );

        return vmi;
    }
    //endregion

}

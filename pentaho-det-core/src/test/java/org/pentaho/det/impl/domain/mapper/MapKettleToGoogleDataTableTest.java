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

import com.google.common.collect.Sets;
import org.junit.Test;
import org.pentaho.det.api.domain.IField.ColumnType;
import org.pentaho.det.api.domain.mapper.IConverter;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.fail;

import static org.hamcrest.MatcherAssert.assertThat;

public class MapKettleToGoogleDataTableTest {

    // region Tests
    @Test
    public void testDefaultDataTypeMapConfiguration() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();
        Map<Integer, ColumnType> expectedMap = this.createDefaultDataTypeMap();

        this.assertThatSetsEqual(mapper.getDataTypes(), expectedMap.entrySet());
    }

    @Test
    public void testGetDataType() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        ColumnType expectedDataType = ColumnType.NUMBER;
        ColumnType returnedDataType = mapper.getDataType( ValueMetaInterface.TYPE_INTEGER );

        assertThat( returnedDataType, is( equalTo( expectedDataType ) ) );
    }

    @Test
    public void testGetDataTypeDefault() {
        MapKettleToGoogleDataTable mapper = this.createEmptyMapper();
        ValueMetaInterface valueMeta = this.createValueMetaInterface( -1 );

        ColumnType defaultType = ColumnType.STRING;
        ColumnType returnedDataType = mapper.getDataType(valueMeta.getType());

        assertThat( returnedDataType, is( equalTo( defaultType ) ) );
    }

    @Test
    public void testPutDataType() {
        MapKettleToGoogleDataTable mapper = this.createEmptyMapper();
        ValueMetaInterface  valueMeta = this.createValueMetaInterface( 99 );

        ColumnType type = ColumnType.NUMBER;
        mapper.putDataType( valueMeta.getType(), type );
        Map.Entry<Integer,ColumnType> expectedEntry = this.createDataTypeEntry( valueMeta.getType(), type );

        assertThat( mapper.getDataTypes(), hasItem(expectedEntry) );
    }

    @Test
    public void testRemoveMapValue() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        Integer metaType = ValueMetaInterface.TYPE_STRING;
        ColumnType colType = ColumnType.STRING;
        Map.Entry<Integer, ColumnType> expectedEntry = this.createDataTypeEntry( metaType, colType );

        assertThat( mapper.getDataTypes(), hasItem(expectedEntry) );
        mapper.removeDataType( metaType );
        assertThat( mapper.getDataTypes(), not( hasItem(expectedEntry) ) );
    }

    @Test
    public void testDefaultConverterMapConfiguration() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();
        Map<Integer, IConverter> expectedMap = this.createDefaultConverterMap();

        this.assertThatSetsEqual(mapper.getConverters(), expectedMap.entrySet());
    }

    @Test
    public void testGetConverter() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        IConverter expectedConverter = new DateToCalendarConverter();
        IConverter returnedConverter = mapper.getConverter( ValueMetaInterface.TYPE_DATE );

        assertThat( returnedConverter, is( equalTo( expectedConverter ) ) );
    }

    @Test
    public void testPutConverter() {
        MapKettleToGoogleDataTable mapper = this.createEmptyMapper();
        ValueMetaInterface  valueMeta = this.createValueMetaInterface( 99 );

        IConverter converter = new DateToCalendarConverter();
        mapper.putConverter( valueMeta.getType(), converter );
        Map.Entry<Integer,IConverter> expectedEntry = this.createConverterEntry( valueMeta.getType(), converter );

        assertThat( mapper.getConverters(), hasItem(expectedEntry) );
    }

    @Test
    public void testRemoveConverter() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        Integer metaType = ValueMetaInterface.TYPE_DATE;
        IConverter converter = new DateToCalendarConverter();
        Map.Entry<Integer,IConverter> expectedEntry = this.createConverterEntry(metaType, converter);

        assertThat( mapper.getConverters(), hasItem( expectedEntry ) );
        mapper.removeConverter( metaType );
        assertThat( mapper.getConverters(), not( hasItem( expectedEntry ) ) );
    }
    //endregion


    // region auxiliary methods
    private MapKettleToGoogleDataTable createDefaultMapper() {
        return new MapKettleToGoogleDataTable();
    }

    private MapKettleToGoogleDataTable createEmptyMapper() {
        return new MapKettleToGoogleDataTable( new HashMap<Integer, ColumnType>(), new HashMap<Integer, IConverter>() );
    }

    private Map<Integer, ColumnType> createDefaultDataTypeMap() {
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

    private Map<Integer, IConverter> createDefaultConverterMap() {
        Map<Integer, IConverter> defaultConfig = new HashMap<Integer, IConverter>();

        IConverter dateConvert = new DateToCalendarConverter();
        defaultConfig.put( ValueMetaInterface.TYPE_DATE,      dateConvert );
        defaultConfig.put( ValueMetaInterface.TYPE_TIMESTAMP, dateConvert );

        return defaultConfig;
    }

    private ValueMetaInterface createValueMetaInterface( int valueType ) {
        ValueMetaInterface vmi = mock( ValueMetaInterface.class );
        when( vmi.getType() ).thenReturn( valueType );

        return vmi;
    }

    /**
     * Asserts that all entries of {@code mapA} are in {@code mapB} and that all entries of {@code mapB} are in {@code mapA}.
     */
    private <K,V> void assertThatSetsEqual( Set<Map.Entry<K, V>> entrySetA, Set<Map.Entry<K, V>> entrySetB ) {

        assertThat( entrySetA, everyItem( isIn( entrySetB ) ) );
        assertThat( entrySetB, everyItem( isIn( entrySetA ) ) );
    }

    private Map.Entry<Integer, ColumnType> createDataTypeEntry( Integer key, ColumnType value ) {
        return new AbstractMap.SimpleEntry<Integer,ColumnType>( key, value );
    }

    private Map.Entry<Integer, IConverter> createConverterEntry( Integer key, IConverter value ) {
        return new AbstractMap.SimpleEntry<Integer,IConverter>( key, value );
    }
    //endregion
}

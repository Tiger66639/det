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
import org.pentaho.det.api.domain.mapper.IConverter;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.fail;

import static org.hamcrest.MatcherAssert.assertThat;

public class MapKettleToGoogleDataTableTest {

    // region Tests
    @Test
    public void testDefaultTypeMapConfiguration() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();
        Map<Integer, ColumnType> typeMapping = mapper.getTypeMapping();
        Map<Integer, ColumnType> expectedMap = this.createDefaultStringMap();

        assertThatMapsEqual( typeMapping, expectedMap );
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
    public void testAddDataType() {
        MapKettleToGoogleDataTable mapper = this.createEmptyMapper();
        ValueMetaInterface  valueMeta = this.createValueMetaInterface( 99 );

        ColumnType type = ColumnType.NUMBER;
        mapper.addDataType(valueMeta.getType(), type);

        assertThat( mapper.getTypeMapping(), hasEntry(valueMeta.getType(), type) );
    }

    @Test
    public void testRemoveMapValue() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        Integer metaType = ValueMetaInterface.TYPE_BIGNUMBER;
        ColumnType colType = ColumnType.NUMBER;

        assertThat( mapper.getTypeMapping(), hasEntry( metaType, colType ) );
        mapper.removeDataType(metaType);
        assertThat( mapper.getTypeMapping(), not( hasEntry( metaType, colType ) ) );
    }

    @Test
    public void testGetConverter() {
        try {
            MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

            IConverter expectedConverter = new DateToCalendarConverter();
            IConverter returnedConverter = mapper.getConverter( ValueMetaInterface.TYPE_DATE );

            assertThat( returnedConverter, is( equalTo( expectedConverter ) ) );
        } catch (KettleValueException e) {
            fail("Shouldn't throw an exception");
        }
    }

    @Test
    public void testAddConverter() {
        MapKettleToGoogleDataTable mapper = this.createEmptyMapper();
        ValueMetaInterface  valueMeta = this.createValueMetaInterface( 99 );

        IConverter converter = new DateToCalendarConverter();
        mapper.addConverter(valueMeta.getType(), converter);

        assertThat( mapper.getConverterMapping(), hasEntry( valueMeta.getType(), converter ) );
    }

    @Test
    public void testRemoveConverter() {
        MapKettleToGoogleDataTable mapper = this.createDefaultMapper();

        Integer metaType = ValueMetaInterface.TYPE_DATE;
        IConverter converter = new DateToCalendarConverter();

        assertThat( mapper.getConverterMapping(), hasEntry( equalTo( metaType ), equalTo( converter ) ) );
        mapper.removeConverter( metaType );
        assertThat( mapper.getConverterMapping(), not( hasEntry( equalTo( metaType ), equalTo( converter ) ) ) );
    }
    //endregion


    // region auxiliary methods
    private MapKettleToGoogleDataTable createDefaultMapper() {
        return new MapKettleToGoogleDataTable();
    }

    private MapKettleToGoogleDataTable createEmptyMapper() {
        return new MapKettleToGoogleDataTable( new HashMap<Integer, ColumnType>(), new HashMap<Integer, IConverter>() );
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

    /**
     * Asserts that all entries of {@code mapA} are in {@code mapB} and that all entries of {@code mapB} are in {@code mapA}.
     */
    private <K,V> void assertThatMapsEqual( Map<K, V> mapA, Map<K, V> mapB ) {
        Set<Map.Entry<K, V>> entrySetA = mapA.entrySet();
        Set<Map.Entry<K, V>> entrySetB = mapB.entrySet();

        assertThat(entrySetA, everyItem(isIn(entrySetB)));
        assertThat(entrySetB, everyItem(isIn(entrySetA)));
    }
    //endregion
}

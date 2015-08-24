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

import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.det.api.domain.IField.ColumnType;

import java.util.HashMap;
import java.util.Map;

/**
 * MapKettleToGoogleDataTable is responsible for the mapping between Kettle Data Types and Google DataTable Data Types
 * <p/>
 * Kettle Data Types:
 * +-------------+---------------------------------------------------------------+
 * |             |                                                               |
 * | Kettle Type |                        Description                            |
 * |             |                                                               |
 * +-------------+---------------------------------------------------------------+
 * | String      | A variable (unlimited) length text encoded in UTF-8 (Unicode) |
 * +-------------+---------------------------------------------------------------+
 * | Integer     | An signed long (64-bit) integer                               |
 * +-------------+---------------------------------------------------------------+
 * | Number      | A double precision floating point value                       |
 * +-------------+---------------------------------------------------------------+
 * | BigNumber   | An arbitrary (unlimited) precision number                     |
 * +-------------+---------------------------------------------------------------+
 * | Date        | A date-time value with millisecond precision                  |
 * +-------------+---------------------------------------------------------------+
 * | Timestamp*  | A date-time value with nanosecond precision                   |
 * +-------------+---------------------------------------------------------------+
 * | Boolean     | A boolean value (true or false)                               |
 * +-------------+---------------------------------------------------------------+
 * | Binary      | An array of bytes that contain any type of binary data.       |
 * +-------------+---------------------------------------------------------------+
 * | Internet A. | Internet address                                              |
 * +-------------+---------------------------------------------------------------+
 * <p/>
 * Google DataTable Data Types:
 * +-------------+----------------------------------------------------+
 * |             |                                                    |
 * | Google Type |                     Description                    |
 * |             |                                                    |
 * +-------------+----------------------------------------------------+
 * | string      | JavaScript string value                            |
 * +-------------+----------------------------------------------------+
 * | number      | JavaScript number value                            |
 * +-------------+----------------------------------------------------+
 * | boolean     | JavaScript boolean value                           |
 * +-------------+----------------------------------------------------+
 * | datetime    | JavaScript Date object including the time          |
 * +-------------+----------------------------------------------------+
 * | date        | JavaScript Date object (zero-based month)          |
 * +-------------+----------------------------------------------------+
 * | timeofday   | Array of three numbers and an optional fourth      |
 * |             | hour (0=midnight), minute, second, and millisecond |
 * +-------------+----------------------------------------------------+
 * <p/>
 * Mapping between Kettle and Google DataTable
 * We are following this logic(see bellow) to map data types between Kettle and Google DataTable based on each
 * data type description and what is already being done in CDA, that follows a very similar logic.
 * <p/>
 * If a valid match is not found for a data type, it will be mapped as a string by default.
 * <p/>
 * +-------------+----------------------------+
 * |             |                            |
 * | Google Type |        Kettle Type         |
 * |             |                            |
 * +-------------+----------------------------+
 * | string      | String                     |
 * +-------------+----------------------------+
 * | number      | Integer, Number, BigNumber |
 * +-------------+----------------------------+
 * | boolean     | Boolean                    |
 * +-------------+----------------------------+
 * | datetime    | Date, Timestamp            |
 * +-------------+----------------------------+
 */
public class MapKettleToGoogleDataTable {

    //region Properties
    public void setMapping( Map<Integer, ColumnType> map ) {
        this.mapping = map;
    }
    public Map<Integer, ColumnType> getMapping() {
        return this.mapping;
    }
    private Map<Integer, ColumnType> mapping;
    //endregion

    //region Constructors
    public MapKettleToGoogleDataTable() {
        this.mapping = defaultMapConfiguration();
    }

    public MapKettleToGoogleDataTable( HashMap<Integer, ColumnType> stringMap ) {
        this.mapping = stringMap;
    }
    //endregion

    //region Methods

    /**
     * Adds a new mapping entry between a kettle data type and a Google DataTable data type
     *
     * @param valueMetaInterface Kettle data type
     * @param columnType         Google DataTable data type
     */
    public void addMapValue( ValueMetaInterface valueMetaInterface, ColumnType columnType ) {
        this.addMapValue( valueMetaInterface.getType(), columnType );
    }

    /**
     * Adds a new mapping entry between a kettle data type and a Google DataTable data type
     *
     * @param valueType  Integer identifying a kettle data type
     * @param columnType Google DataTable data type
     */
    public void addMapValue( Integer valueType, ColumnType columnType ) {
        this.mapping.put( valueType, columnType );
    }

    /**
     * Gets the corresponding Google DataTable data type for the given kettle data type
     *
     * @param valueMetaInterface kettle data type
     *
     * @return Google DataTable data type
     */
    public ColumnType getDataType( ValueMetaInterface valueMetaInterface ) {
        return this.getDataType( valueMetaInterface.getType() );
    }

    /**
     * Gets the corresponding Google DataTable data type for the given kettle data type
     *
     * @param valueType Integer identifying a kettle data type
     *
     * @return Google DataTable data type
     */
    public ColumnType getDataType( Integer valueType ) {
        ColumnType colType = this.mapping.get( valueType );

        if( colType != null ) {
            return colType;
        } else {
            return ColumnType.STRING;
        }
    }

    /**
     * Returns a mapping object with the default configuration
     */
    private Map<Integer, ColumnType> defaultMapConfiguration() {
        Map<Integer, ColumnType> defaultConfig = new HashMap<Integer, ColumnType>();

        defaultConfig.put( ValueMetaInterface.TYPE_INTEGER, ColumnType.NUMBER );
        defaultConfig.put( ValueMetaInterface.TYPE_NUMBER, ColumnType.NUMBER );
        defaultConfig.put( ValueMetaInterface.TYPE_BIGNUMBER, ColumnType.NUMBER );
        defaultConfig.put( ValueMetaInterface.TYPE_BOOLEAN, ColumnType.BOOLEAN );
        defaultConfig.put( ValueMetaInterface.TYPE_DATE, ColumnType.DATETIME );
        defaultConfig.put( ValueMetaInterface.TYPE_TIMESTAMP, ColumnType.DATETIME );
        defaultConfig.put( ValueMetaInterface.TYPE_STRING, ColumnType.STRING );

        return defaultConfig;
    }
    //endregion
}
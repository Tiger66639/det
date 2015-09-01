package org.pentaho.det.api.domain.mapper;

import org.pentaho.di.core.exception.KettleValueException;

import java.util.Map;

public interface IMapper<OriginalType, TargetType> {

    void setTypeMapping( Map<OriginalType, TargetType> map );
    Map<OriginalType, TargetType> getTypeMapping( );

    void setConverterMapping( Map<OriginalType, IConverter> map );
    Map<OriginalType, IConverter> getConverterMapping();


    void addDataType( OriginalType source, TargetType target );
    void removeDataType( OriginalType source );
    TargetType getDataType( OriginalType source );

    void addConverter( OriginalType source, IConverter converter );
    void removeConverter( OriginalType source );
    IConverter getConverter( OriginalType source ) throws KettleValueException;
}

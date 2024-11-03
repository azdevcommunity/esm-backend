package com.example.medrese.Core.Util.Mapper.Abstracts;

import org.modelmapper.ModelMapper;
public interface IModelService {
    ModelMapper forResponse();
    ModelMapper forRequest();
}

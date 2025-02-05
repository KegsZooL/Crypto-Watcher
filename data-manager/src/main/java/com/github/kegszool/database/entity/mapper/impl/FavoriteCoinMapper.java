package com.github.kegszool.database.entity.mapper.impl;

import com.github.kegszool.database.entity.base.FavoriteCoin;
import com.github.kegszool.database.entity.mapper.EntityMapper;
import com.github.kegszool.messaging.dto.database_entity.FavoriteCoinDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteCoinMapper extends EntityMapper<FavoriteCoin, FavoriteCoinDto> {

    private final UserMapper userMapper;
    private final CoinMapper coinMapper;

    @Autowired
    public FavoriteCoinMapper(UserMapper userMapper, CoinMapper coinMapper) {
        this.userMapper = userMapper;
        this.coinMapper = coinMapper;
    }

    @Override
    public FavoriteCoin toEntity(FavoriteCoinDto favoriteCoinDto) {
        return new FavoriteCoin(
            userMapper.toEntity(favoriteCoinDto.getUser()),
            coinMapper.toEntity(favoriteCoinDto.getCoin())
        );
    }

    @Override
    public FavoriteCoinDto toDto(FavoriteCoin favoriteCoin) {
        return new FavoriteCoinDto(
            userMapper.toDto(favoriteCoin.getUser()),
            coinMapper.toDto(favoriteCoin.getCoin())
        );
    }
}
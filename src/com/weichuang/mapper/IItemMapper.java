package com.weichuang.mapper;

import com.weichuang.pojo.Item;

import java.util.List;

public interface IItemMapper {
    Item getItemById(String id);

    int updateItem(Item item);

    List<Item> getAllItem();

    int deleteItems(String[] ids);
}

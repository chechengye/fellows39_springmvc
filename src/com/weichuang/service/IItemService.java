package com.weichuang.service;

import com.weichuang.pojo.Item;

import java.util.List;

public interface IItemService {
    Item getItemById(String id);

    int updateItem(Item item);

    List<Item> getAllItem();

    int deleteItems(String[] ids);
}

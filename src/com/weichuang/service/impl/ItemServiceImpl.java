package com.weichuang.service.impl;

import com.weichuang.mapper.IItemMapper;
import com.weichuang.pojo.Item;
import com.weichuang.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements IItemService {

    @Autowired
    IItemMapper iItemMapper;
    /**
     * 根据id获取商品名称
     * @param id
     * @return
     */
    @Override
    public Item getItemById(String id) {
        return iItemMapper.getItemById(id);
    }

    /**
     * 根据id更新商品
     * @param item
     * @return
     */
    @Override
    public int updateItem(Item item) {
        return iItemMapper.updateItem(item);
    }

    @Override
    public List<Item> getAllItem() {
        return iItemMapper.getAllItem();
    }

    /**
     * 根据选种  的id 进行删除
     * @param ids
     * @return
     */
    @Override
    public int deleteItems(String[] ids) {
        return iItemMapper.deleteItems(ids);
    }
}

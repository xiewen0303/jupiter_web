package com.jupiter.web.manager.bus.label.service;

import com.jupiter.web.manager.common.dao.CategoryLabelDao;
import com.jupiter.web.manager.common.entity.CategoryLabelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryLabelService {

    @Autowired
    private CategoryLabelDao categoryLabelDao;



    /**
     * 新增label
     * @param entity
     */

    public Object insertCategoryLabel(CategoryLabelEntity entity) {
        int i = categoryLabelDao.insert(entity);
        return i > 0 ? entity.getId() : null;
    }

    /**
     * 修改label
     * @param entity
     */

    public boolean updateCategoryLabel(CategoryLabelEntity entity) {
        return categoryLabelDao.update(entity) > 0;
    }

    /**
     * 根据ID查询CategoryLabel
     * @param id
     */
    public CategoryLabelEntity getCategoryLabelById(String id) {
        return categoryLabelDao.getById(id);
    }
}

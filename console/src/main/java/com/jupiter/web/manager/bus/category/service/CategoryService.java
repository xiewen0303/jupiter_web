package com.jupiter.web.manager.bus.category.service;

import com.jupiter.web.manager.common.dao.CategoryDao;
import com.jupiter.web.manager.common.entity.CategoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {

    private CategoryDao categoryDao;




    public List<CategoryEntity> getAll() {
        return categoryDao.getAll();
    }

    /**
     * 新增label
     * @param entity
     */

    public String insertLabel(CategoryEntity entity) {
        int i = categoryDao.insert(entity);
        return i > 0 ? entity.getId() : null;
    }

    /**
     * 修改label
     * @param entity
     */

    public boolean updateLabel(CategoryEntity entity) {
        return categoryDao.update(entity) > 0;
    }

    /**
     * 根据ID查询label
     * @param id
     */
    public CategoryEntity getCategoryEntityById(String id) {
        return categoryDao.getById(id);
    }
}

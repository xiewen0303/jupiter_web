package com.jupiter.web.manager.bus.label.service;

import com.github.pagehelper.PageHelper;
import com.jupiter.web.manager.bus.category.dto.CategoryDto;
import com.jupiter.web.manager.common.dao.LabelDao;
import com.jupiter.web.manager.common.entity.LabelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    /**
     * 分页查询角色
     */
    public List<LabelEntity> getLabelPageList(CategoryDto roleDto) {
        if (!StringUtils.isEmpty(roleDto.getPage()) && !StringUtils.isEmpty(roleDto.getRows())) {
            PageHelper.startPage(roleDto.getPage(), roleDto.getRows());
        }
        List<LabelEntity> labels = labelDao.search(roleDto);
        return labels;
    }

    public List<LabelEntity> getAll() {
        return labelDao.getAll();
    }


    /**
     * 新增label
     * @param entity
     */

    public String insertLabel(LabelEntity entity) {
        int i = labelDao.insert(entity);
        return i > 0 ? entity.getId() : null;
    }

    /**
     * 修改label
     * @param entity
     */

    public boolean updateLabel(LabelEntity entity) {
        return labelDao.update(entity) > 0;
    }

    /**
     * 根据ID查询label
     * @param id
     */
    public LabelEntity getLabelEntityById(String id) {
        return labelDao.getById(id);
    }
}

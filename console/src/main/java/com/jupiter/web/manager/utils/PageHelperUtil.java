package com.jupiter.web.manager.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jupiter.web.manager.common.base.PageInputDto;
import com.jupiter.web.manager.common.base.PageOutputDto;
import com.tron.framework.dto.ResponseEntity;

import java.util.List;
import java.util.function.Supplier;

/**
 * 分页查询工具类
 * @author fuyuling
 * @created 2019/7/25 17:27
 */
public class PageHelperUtil {

    /**
     * 分页查询，返回PageOutputDto对象
     */
    public static <E, K extends PageInputDto> PageOutputDto<E> page(K pageInputDto, Supplier<List<E>> selectAction) {
        if(pageInputDto.getPage() == null) {
            pageInputDto.setPage(PageInputDto.DEFAULT_PAGE);
        }
        if(pageInputDto.getRows() == null) {
            pageInputDto.setRows(PageInputDto.DEFAULT_PAGE_SIZE);
        }
        Page<E> page = PageHelper.startPage(pageInputDto.getPage(), pageInputDto.getRows());
        List<E> data = selectAction.get();
        PageOutputDto<E> pageOutputDto = new PageOutputDto<>();
        pageOutputDto.setCurrentPage(page.getPageNum());
        pageOutputDto.setPageSize(page.getPageSize());
        pageOutputDto.setPageCount(page.getPages());
        pageOutputDto.setRecordsTotal((int) page.getTotal());
        pageOutputDto.setData(data);
        return pageOutputDto;
    }

    public static <E> PageOutputDto<E> page(Integer page, Integer pageSize, Supplier<List<E>> selectAction) {
        return page(new PageInputDto(page, pageSize), selectAction);
    }

    /**
     * 分页查询，返回ResponseEntity对象
     * @param pageInputDto
     * @param selectAction
     * @param <E>
     * @param <K>
     * @return
     */
    public static <E, K extends PageInputDto> ResponseEntity response(K pageInputDto, Supplier<List<E>> selectAction) {
        if(pageInputDto.getPage() == null) {
            pageInputDto.setPage(PageInputDto.DEFAULT_PAGE);
        }
        if(pageInputDto.getRows() == null) {
            pageInputDto.setRows(PageInputDto.DEFAULT_PAGE_SIZE);
        }
        Page<E> page = PageHelper.startPage(pageInputDto.getPage(), pageInputDto.getRows());
        List<E> data = selectAction.get();
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCurrentPage(page.getPageNum());
        responseEntity.setPageSize(page.getPageSize());
        responseEntity.setPageCount(page.getPages());
        responseEntity.setRecordsTotal((int) page.getTotal());
        responseEntity.setData(data);
        return responseEntity;
    }

    public static <E> ResponseEntity response(Integer page, Integer pageSize, Supplier<List<E>> selectAction) {
        return response(new PageInputDto(page, pageSize), selectAction);
    }

    /**
     * 分页查询，直接返回对象List
     */
    public static <E, K extends PageInputDto> List<E> list(K pageInputDto, Supplier<List<E>> selectAction) {
        if(pageInputDto.getPage() == null) {
            pageInputDto.setPage(PageInputDto.DEFAULT_PAGE);
        }
        if(pageInputDto.getRows() == null) {
            pageInputDto.setRows(PageInputDto.DEFAULT_PAGE_SIZE);
        }
        PageHelper.startPage(pageInputDto.getPage(), pageInputDto.getRows());
        return selectAction.get();
    }

    public static <E> List<E> list(Integer page, Integer pageSize, Supplier<List<E>> selectAction) {
        return list(new PageInputDto(page, pageSize), selectAction);
    }

}

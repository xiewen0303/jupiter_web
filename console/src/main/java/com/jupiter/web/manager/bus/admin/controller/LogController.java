package com.jupiter.web.manager.bus.admin.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jupiter.web.manager.common.dao.LogDao;
import com.jupiter.web.manager.log.LogType;
import com.jupiter.web.manager.utils.DateUtil;
import com.jupiter.web.manager.common.entity.Log;
import com.jupiter.web.manager.bus.admin.dto.CommonResponse;
import com.jupiter.web.manager.bus.admin.dto.LogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("log")
public class LogController extends BaseController {

    @Autowired
    private LogDao logDao;

    @RequestMapping("/searchPage")
    public String searchPage(Model model) throws Exception {
        List<LogType> list = new ArrayList<>();
        for (LogType logType : LogType.values()) {
            list.add(logType);
        }
        model.addAttribute("logTypes", list);
        return "log/logList";
    }

    @ResponseBody
    @RequestMapping("/logList")
    public CommonResponse<PageInfo<Log>> logList(LogDto logDto) throws Exception {
        CommonResponse<PageInfo<Log>> result = new CommonResponse<>();

        List<Log> list = searchLog(logDto);

        PageInfo<Log> pageInfo = new PageInfo<>(list);
        result.setModule(pageInfo);
        result.setCode("200");
        result.setMsg("查询成功");
        return result;
    }

    public List<Log> searchLog(LogDto logDto) {
        if (logDto.getAdminName() == null && logDto.getLogType() == null && logDto.getLogStart() == null && logDto.getLogEnd() == null) {
            return new ArrayList<>();
        }
        if (!StringUtils.isEmpty(logDto.getPage()) && !StringUtils.isEmpty(logDto.getRows())) {
            PageHelper.startPage(logDto.getPage(), logDto.getRows());
        }
        if (logDto.getLogStart() != null) {
            String format = logDto.getLogStart() + " 00:00:00";
            logDto.setLogStartTime(DateUtil.parseDate(format).getTime());
        }
        if (logDto.getLogEnd() != null) {
            String format = logDto.getLogEnd() + " 00:00:00";
            logDto.setLogEndTime(DateUtil.parseDate(format).getTime());
        }
        return logDao.search(logDto);
    }

}

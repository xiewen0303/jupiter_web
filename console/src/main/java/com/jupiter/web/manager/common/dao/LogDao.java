package com.jupiter.web.manager.common.dao;

import com.tron.common.mysql.mybatis.dao.CommonDao;
import com.jupiter.web.manager.common.entity.Log;
import com.jupiter.web.manager.common.provider.LogProvider;
import com.jupiter.web.manager.bus.admin.dto.LogDto;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogDao extends CommonDao<Log, Long> {

    @Override
    default Class<Log> getSelfClass() {
        return Log.class;
    }

    @SelectProvider(type = LogProvider.class, method = "search")
//    @Results({
//            @Result(property = "id", column = "id"),
//            @Result(property = "adminId", column = "admin_id"),
//            @Result(property = "adminName", column = "admin_id",
//                    one = @One(select = "AdminDao.getAdminNameById"))
//    })
    List<Log> search(LogDto logDto);

}

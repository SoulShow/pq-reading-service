package com.pq.reading.feign;


import com.pq.reading.dto.AgencyClassDto;
import com.pq.reading.utils.ReadingResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 机构服务
 * @author liutao
 */
@FeignClient("service-agency")
public interface AgencyFeign {

    /**
     * 获取机构学生数量
     *
     * @param agencyClassId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/agency/student/count", method = RequestMethod.GET)
    ReadingResult<Integer> getStudentCount(@RequestParam(value = "agencyClassId") Long agencyClassId);

    /**
     * 获取班级信息
     * @param agencyClassId
     * @return
     */
    @RequestMapping(value = "/agency/class/info", method = RequestMethod.GET)
    ReadingResult<AgencyClassDto> getAgencyClassInfo(@RequestParam(value = "agencyClassId") Long agencyClassId);
}


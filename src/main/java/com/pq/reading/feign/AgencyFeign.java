package com.pq.reading.feign;


import com.pq.reading.dto.AgencyClassDto;
import com.pq.reading.dto.AgencyStudentDto;
import com.pq.reading.utils.ReadingResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    /**
     * 获取学生信息
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/agency/student/info", method = RequestMethod.GET)
    ReadingResult<AgencyStudentDto> getStudentInfo(@RequestParam(value = "studentId") Long studentId);

    /**
     * 获取班级学生列表
     * @param classId
     * @return
     */
    @RequestMapping(value = "/agency/student/list", method = RequestMethod.GET)
    ReadingResult<List<Long>> getStudentInfoList(@RequestParam(value = "classId") Long classId);

    /**
     * 获取老师班级列表
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/agency/teacher/class/list", method = RequestMethod.GET)
    ReadingResult<List<AgencyClassDto>> getTeacherClassList(@RequestParam("userId")String userId);
}


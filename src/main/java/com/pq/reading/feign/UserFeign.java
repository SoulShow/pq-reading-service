package com.pq.reading.feign;


import com.pq.reading.dto.UserDto;
import com.pq.reading.utils.ReadingResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户信息服务
 * @author liutao
 */
@FeignClient("service-user")
public interface UserFeign {
    /**
     * 获取用户信息
     * @param userId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    ReadingResult<UserDto> getUserInfo(@RequestParam("userId") String userId);
}

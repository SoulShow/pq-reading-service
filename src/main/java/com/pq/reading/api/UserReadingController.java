package com.pq.reading.api;

import com.pq.common.exception.CommonErrors;
import com.pq.reading.dto.*;
import com.pq.reading.exception.ReadingException;
import com.pq.reading.service.ReadingService;
import com.pq.reading.service.UserReadingService;
import com.pq.reading.utils.ReadingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reading")
public class UserReadingController {
	@Autowired
	private UserReadingService userReadingService;

	@PostMapping(value = "/student/album/create")
	@ResponseBody
	public ReadingResult studentCreateAlbum(@RequestBody UserAlbumDto userAlbumDto) {
		ReadingResult result = new ReadingResult();

		try{
			userReadingService.createUserAlbum(userAlbumDto);
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@GetMapping(value = "/student/album/list")
	@ResponseBody
	public ReadingResult getUserAlbumList(@RequestParam("studentId") Long studentId,
										  @RequestParam("userId") String userId) {
		ReadingResult result = new ReadingResult();
		try{
			result.setData(userReadingService.getUserAlbumList(userId, studentId));
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@PostMapping(value = "/student/upload")
	@ResponseBody
	public ReadingResult uploadUserReading (@RequestBody UserReadingRecordDto readingRecordDto) {
		ReadingResult result = new ReadingResult();
		try{
			userReadingService.uploadUserReading(readingRecordDto);
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@GetMapping(value = "/student/album/update")
	@ResponseBody
	public ReadingResult getUserAlbumDetail(@RequestParam("albumId") Long albumId) {
		ReadingResult result = new ReadingResult();
		try{
			result.setData(userReadingService.getAlbumDetail(albumId));
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@PostMapping(value = "/student/album/update")
	@ResponseBody
	public ReadingResult studentUpdateAlbum(@RequestBody UserAlbumDto userAlbumDto) {
		ReadingResult result = new ReadingResult();

		try{
			userReadingService.updateUserAlbum(userAlbumDto);
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@PostMapping(value = "/student/album/delete")
	@ResponseBody
	public ReadingResult studentDeleteAlbum(@RequestBody UserAlbumDto userAlbumDto) {
		ReadingResult result = new ReadingResult();
		try{
			userReadingService.delUserAlbum(userAlbumDto.getId());
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@GetMapping(value = "/student/album/reading/list")
	@ResponseBody
	public ReadingResult getUserAlbumReadingList(@RequestParam("albumId") Long albumId,
												 @RequestParam("isPrivate") int isPrivate,
												 @RequestParam(value = "page",required = false) Integer page,
												 @RequestParam(value = "size",required = false) Integer size) {
		ReadingResult result = new ReadingResult();
		if (page == null || page < 1) {
			page = 1;
		}
		if (size == null || size < 1) {
			size = 10;
		}
		int offset = (page - 1) * size;
		try{
			result.setData(userReadingService.getUserAlbumReadingList(albumId,isPrivate,offset,size));
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@GetMapping(value = "/student/reading")
	@ResponseBody
	public ReadingResult<MyReadingDto> getUserPrivateReadingList(@RequestParam("studentId") Long studentId,
                                                                 @RequestParam("userId") String userId) {
		ReadingResult result = new ReadingResult();
		try{
			result.setData(userReadingService.getUserReading(studentId,userId));
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}

	@GetMapping(value = "/student/reading/detail")
	@ResponseBody
	public ReadingResult<MyReadingDetailDto> getUserReadingDetail(@RequestParam("studentId") Long studentId,
																  @RequestParam("readingId") Long readingId,
																  @RequestParam(value = "readingId",required = false) Long commentId) {
		ReadingResult result = new ReadingResult();
		try{
			result.setData(userReadingService.getUserReadingDetail(studentId,readingId,commentId));
		}catch (ReadingException e){
			result.setStatus(e.getErrorCode().getErrorCode());
			result.setMessage(e.getErrorCode().getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(CommonErrors.DB_EXCEPTION.getErrorCode());
			result.setMessage(CommonErrors.DB_EXCEPTION.getErrorMsg());
		}
		return result;
	}
}
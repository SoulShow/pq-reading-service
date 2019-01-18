package com.pq.reading.api;

import com.pq.common.exception.CommonErrors;
import com.pq.reading.dto.CreateReadingTaskDto;
import com.pq.reading.dto.TaskReadingPlayLogDto;
import com.pq.reading.dto.UserAlbumDto;
import com.pq.reading.dto.UserReadingRecordDto;
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
	public ReadingResult getUserAlbumReadingList(@RequestParam("albumId") Long albumId) {
		ReadingResult result = new ReadingResult();
		try{
			result.setData(userReadingService.getUserAlbumReadingList(albumId));
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

	@GetMapping(value = "/student/private/reading/list")
	@ResponseBody
	public ReadingResult getUserPrivateReadingList(@RequestParam("studentId") Long studentId,
												   @RequestParam("userId") String userId) {
		ReadingResult result = new ReadingResult();
		try{
			result.setData(userReadingService.getUserPrivateReadingList(userId,studentId));
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
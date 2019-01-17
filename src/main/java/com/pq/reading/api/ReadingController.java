package com.pq.reading.api;

import com.pq.common.exception.CommonErrors;
import com.pq.reading.dto.CreateReadingTaskDto;
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
public class ReadingController {
	@Autowired
	private ReadingService readingService;
	@Autowired
	private UserReadingService userReadingService;

	@GetMapping(value = "/album/list")
	@ResponseBody
	public ReadingResult getBookAlbumList(@RequestParam("type") int type,
										  @RequestParam(value = "page",required = false) Integer page,
										  @RequestParam(value = "size",required = false) Integer size) {
		ReadingResult result = new ReadingResult();
		if (page == null || page < 1) {
			page = 1;
		}
		if (size == null || size < 1) {
			size = 20;
		}
		int offset = (page - 1) * size;

		try{
			result.setData(readingService.getBookAlbumList(type,offset,size));
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

	@GetMapping(value = "/book/list")
	@ResponseBody
	public ReadingResult getBookList(@RequestParam("albumId") Long albumId,
									 @RequestParam(value = "page",required = false) Integer page,
									 @RequestParam(value = "size",required = false) Integer size) {
		ReadingResult result = new ReadingResult();
		if (page == null || page < 1) {
			page = 1;
		}
		if (size == null || size < 1) {
			size = 20;
		}
		int offset = (page - 1) * size;

		try{
			result.setData(readingService.getBookList(albumId,offset,size));
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
	@GetMapping(value = "/chapter/list")
	@ResponseBody
	public ReadingResult getBookChapterList(@RequestParam("bookId") Long bookId,
									 @RequestParam(value = "page",required = false) Integer page,
									 @RequestParam(value = "size",required = false) Integer size) {
		ReadingResult result = new ReadingResult();
		if (page == null || page < 1) {
			page = 1;
		}
		if (size == null || size < 1) {
			size = 20;
		}
		int offset = (page - 1) * size;

		try{
			result.setData(readingService.getBookChapterList(bookId,offset,size));
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

	@GetMapping(value = "/teacher/reading/list")
	@ResponseBody
	public ReadingResult getTeacherNewReadingList(@RequestParam("classId") Long classId,
											@RequestParam(value = "page",required = false) Integer page,
											@RequestParam(value = "size",required = false) Integer size) {
		ReadingResult result = new ReadingResult();
		if (page == null || page < 1) {
			page = 1;
		}
		if (size == null || size < 1) {
			size = 20;
		}
		int offset = (page - 1) * size;

		try{
			result.setData(readingService.getTeacherNewReadingList(classId,offset,size));
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

	@GetMapping(value = "/student/reading/list")
	@ResponseBody
	public ReadingResult getStudentNewReadingList(@RequestParam("classId") Long classId,
												  @RequestParam("studentId") Long studentId,
												  @RequestParam("userId") String userId,
												  @RequestParam(value = "page",required = false) Integer page,
												  @RequestParam(value = "size",required = false) Integer size) {
		ReadingResult result = new ReadingResult();
		if (page == null || page < 1) {
			page = 1;
		}
		if (size == null || size < 1) {
			size = 20;
		}
		int offset = (page - 1) * size;

		try{
			result.setData(readingService.getStudentNewReadingList(studentId, userId, classId,offset,size));
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

	@PostMapping(value = "/task/create")
	@ResponseBody
	public ReadingResult teacherCreateTask(@RequestBody CreateReadingTaskDto readingTaskDto) {
		ReadingResult result = new ReadingResult();

		try{
			readingService.createReadingTask(readingTaskDto);
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

	@GetMapping(value = "/student/unRead/count")
	@ResponseBody
	public ReadingResult getStudentNewReadingList(@RequestParam("classId") Long classId,
												  @RequestParam("studentId") Long studentId,
												  @RequestParam("userId") String userId) {
		ReadingResult result = new ReadingResult();

		try{
			result.setData(readingService.getUnReadCount(studentId, userId, classId));
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
	public ReadingResult getStudentReadingDetail(@RequestParam("taskId") Long taskId,
												  @RequestParam("studentId") Long studentId,
												  @RequestParam("userId") String userId) {
		ReadingResult result = new ReadingResult();

		try{
			result.setData(readingService.getReadingTaskDetail(taskId, studentId, userId));
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
}
package com.pq.reading.api;

import com.pq.common.exception.CommonErrors;
import com.pq.reading.dto.*;
import com.pq.reading.exception.ReadingException;
import com.pq.reading.service.ReadingService;
import com.pq.reading.service.UserReadingService;
import com.pq.reading.utils.ReadingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@GetMapping(value = "/student/my/reading/detail")
	@ResponseBody
	public ReadingResult<MyReadingDetailDto> getUserReadingDetail(@RequestParam("studentId") Long studentId,
																  @RequestParam("readingId") Long readingId,
																  @RequestParam(value = "commentId",required = false) Long commentId,
																  @RequestParam("praiseUserId") String praiseUserId,
																  @RequestParam(value = "praiseStudentId",
																		  defaultValue = "0",required = false) Long praiseStudentId) {
		ReadingResult result = new ReadingResult();
		try{
			result.setData(userReadingService.getUserReadingDetail(studentId,readingId,commentId,praiseUserId,praiseStudentId));
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

	@GetMapping(value = "/student/reading/comment")
	@ResponseBody
	public ReadingResult<List<StudentReadingCommentDto>> getReadingCommentList(@RequestParam("readingId") Long readingId,
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
			result.setData(userReadingService.getReadingCommentList(readingId,offset,size));
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

	@GetMapping(value = "/student/message/list")
	@ResponseBody
	public ReadingResult<List<CommentMessageDto>> getCommentMessageList(@RequestParam("studentId") Long studentId,
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
			result.setData(userReadingService.getCommentMessageList(studentId,offset,size));
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

	@PostMapping(value = "/student/praise")
	@ResponseBody
	public ReadingResult praise(@RequestBody PraiseDto praiseDto) {
		ReadingResult result = new ReadingResult();
		try{
			userReadingService.praise(praiseDto);
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

	@PostMapping(value = "/student/praise/cancel")
	@ResponseBody
	public ReadingResult cancelPraise(@RequestBody PraiseDto praiseDto){
		ReadingResult result = new ReadingResult();
		try{
			userReadingService.praiseCancel(praiseDto);
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

	@PostMapping(value = "/student/comment")
	@ResponseBody
	public ReadingResult createComment(@RequestBody CommentDto commentDto) {
		ReadingResult result = new ReadingResult();
		try{
			userReadingService.createComment(commentDto);
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

	@GetMapping(value = "/student/reading/ranking/list")
	@ResponseBody
	public ReadingResult<List<AgencyStudentDto>> getReadingRankingList(@RequestParam("chapterId") Long chapterId,
																	   @RequestParam(value = "classId",required = false) Long classId,
																	   @RequestParam("type") int type,
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
			result.setData(userReadingService.getReadingRankingList(chapterId,classId,type,offset,size));
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

	@PostMapping(value = "/student/reading/delete")
	@ResponseBody
	public ReadingResult delStudentReading(@RequestBody DelUserReadingDto delUserReadingDto) {
		ReadingResult result = new ReadingResult();

		try{
			userReadingService.delUserReading(delUserReadingDto.getReadingId(),delUserReadingDto.getStudentId());
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
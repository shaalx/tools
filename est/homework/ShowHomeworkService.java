package com.edu863.service.service012;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu863.dao.common.SelectPropertyByKeyDao;
import com.edu863.dao.dao012.ShowHomeworkDao;

@Service
@Transactional
/**
 * module 作品展示与评价
 * class ShowHomeworkService.java
 * function showHomework画面业务层
 * author 唐义凡（刘垚）
 * remark
 */
public class ShowHomeworkService {
	// 自动装配ShowHomeworkDao
	@Autowired
	private ShowHomeworkDao showHomeworkDao;
	// 自动装配SelectPropertyByKeyDao
	@Autowired
	private SelectPropertyByKeyDao selectPropertyByKeyDao;

	/**
	 * method insertComment
	 * function 添加评论信息
	 * @param userId 用户ID
	 * @param userType 用户类型
	 * @param classId 班级编号
	 * @param homeworkId 作品ID
	 * @param commentText 评论内容
	 * @param commentGrade 评价分数
	 * @param schoolId 学校编号
	 * @return null或者错误消息编号
	 * @throws 无
	 */
	public Map<String, String> insertComment(String userId, String userType, String studentNo, String classId, String homeworkId, String commentText, String commentGrade, String schoolId) {
		Object[] args;
		Map<String, String> errorMessage = new TreeMap<String, String>();		
		// 评论内容的长度超出范围
		if (commentText.length() > 280) {
			errorMessage.put("hasCommentTextW0010003", "0");
		}
		// 评论内容为空
		if (commentText.length() <= 0) {
			errorMessage.put("hasCommentTextW0010006", "0");
		}
		// 评价分数为空
		if (StringUtils.isEmpty(commentGrade)) {
			errorMessage.put("hasCommentGradeW0010006", "0");
		}
		// 输入合法
		if (errorMessage.size() <= 0) {
			// 如果用户是班级的主讲教师
			if (userType.equals("03") && selectPropertyByKeyDao.getPropertyByKey("CLASSNO", classId, "TEACHERNO", "TBL_CLASS_001", "", schoolId).equals(userId)) {
				args = new Object[] { homeworkId, "0", userType, userId, "02", commentText, commentGrade, schoolId, userId, userId };
				boolean teacherUpdated = showHomeworkDao.updateTeacherComment(new Object[]{commentGrade, userId, homeworkId, schoolId, studentNo, userId});
				if (!teacherUpdated) {
					errorMessage.put("hasW0120005", "1");
					return errorMessage;
				}
			} else {
				args = new Object[] { homeworkId, "-1", userType, userId, "03", commentText, commentGrade, schoolId, userId, userId };
			}
			boolean inserted = showHomeworkDao.insertComment(args);
			if (!inserted) {
				errorMessage.put("hasW0120005", "1");
				return errorMessage;
			}
			if (userType.equals("02")) {
				errorMessage.put("hasW0120003", "0");
			}
			if (userType.equals("03")) {
				errorMessage.put("hasW0120004", "0");
			}
		}
		return errorMessage;
	}
}
package com.edu863.service.service010;

import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.edu863.dao.dao010.ShowTestpaperDao;

@Service
@Transactional
/**
 * module 测试反馈系统
 * class ShowTestpaperService.java
 * function showTestpaper画面业务层
 * author 唐义凡（刘垚）
 * remark
 */
public class ShowTestpaperService {
	// 自动装配ShowTestpaperDao
	@Autowired
	private ShowTestpaperDao showTestpaperDao;

	/**
	 * method updateTestpaperQuestionValid
	 * function 更新试卷中题目的有效状态
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param question 题目类型和题目ID
	 * @param isValid 有效状态
	 * @return 执行是否成功
	 * @throws 无
	 */
	public boolean updateTestpaperQuestionValid(String userId, String testpaperId, String[] question, String isValid) {
		// 获取题目类型和题目ID
		String[] questionType = new String[question.length];
		String[] questionId = new String[question.length];
		for (int i = 0; i < question.length; i++) {
			questionType[i] = question[i].substring(0, 2);
			questionId[i] = question[i].substring(2);
		}
		return showTestpaperDao.updateTestpaperQuestionValid(userId, testpaperId, questionId, questionType, isValid);
	}

	/**
	 * method updateTestpaperInfo
	 * function 更新试卷信息
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param testpaperName 试卷名
	 * @param questionScore 分值
	 * @return 错误消息
	 * @throws 无
	 */
	public Map<String, String> updateTestpaperInfo(String userId, String testpaperId, String testpaperName, String[] questionScore) {
		Map<String, String> message = new TreeMap<String, String>();
		// 检查试卷名
		// 长度超出范围
		if (testpaperName.length() > 40) {
			message.put("hasTestpaperNameW0010003", "1");
		}
		// 为空
		if (testpaperName.length() <= 0) {
			message.put("hasTestpaperNameW0010006", "1");
		}
		// 检查分值
		int[] score = new int[questionScore.length];
		for (int i = 1; i <= questionScore.length; i++) {
			try {
				// 为空
				if (questionScore[i - 1].length() <= 0) {
					message.put("hasQuestionScore" + i + "W0010006", "1");
				} else {
					score[i - 1] = Integer.valueOf(questionScore[i - 1]);
					// 小于0
					if (score[i - 1] < 0) {
						message.put("hasQuestionScore" + i + "W0010017", "1");
					}
				}
			} catch (Exception e) {
				// 不是数字
				message.put("hasQuestionScore" + i + "W0010008", "1");
			}
		}
		// 输入合法
		if (message.size() <= 0) {
			if (showTestpaperDao.updateTestpaperInfo(userId, testpaperId, testpaperName, score)) {
				message.put("hasUpdateSuccessMessage", "1");
				return message;
			}
		}
		message.put("hasUpdateFailedMessage", "1");
		return message;
	}
	
	/**
	 * method updateTestpaperInfo
	 * function 更新试卷信息: 新增，更新试卷名，各个题目的分值。
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param testpaperName 试卷名
	 * @param questionScore 分值
	 * @param essayqsScore 问答题分值
	 * @param operqsScore 操作题分值
	 * @return 错误消息
	 * @throws 无
	 */
	public Map<String, String> updateTestpaperInfo(String userId, String testpaperId, String testpaperName, String[] questionScore, Map<Integer, Integer> essayqsScore, Map<Integer, Integer> operqsScore) {
		Map<String, String> message = new TreeMap<String, String>();
		// 检查试卷名
		// 长度超出范围
		if (testpaperName.length() > 40) {
			message.put("hasTestpaperNameW0010003", "1");
		}
		// 为空
		if (testpaperName.length() <= 0) {
			message.put("hasTestpaperNameW0010006", "1");
		}
		// 检查分值
		int[] score = new int[questionScore.length];
		for (int i = 1; i <= questionScore.length; i++) {
			try {
				// 为空
				if (questionScore[i - 1].length() <= 0) {
					message.put("hasQuestionScore" + i + "W0010006", "1");
				} else {
					score[i - 1] = Integer.valueOf(questionScore[i - 1]);
					// 小于0
					if (score[i - 1] < 0) {
						message.put("hasQuestionScore" + i + "W0010017", "1");
					}
				}
			} catch (Exception e) {
				// 不是数字
				message.put("hasQuestionScore" + i + "W0010008", "1");
				score[i - 1] = 0;
			}
		}
		// 输入合法
		if (message.size() <= 0) {
			if (showTestpaperDao.updateTestpaperInfo(userId, testpaperId,score, essayqsScore, operqsScore) && showTestpaperDao.updateTestpaperInfo(userId, testpaperId, testpaperName, score)) {
				message.put("hasUpdateSuccessMessage", "1");
				return message;
			}
		}
		message.put("hasUpdateFailedMessage", "1");
		return message;
	}
}
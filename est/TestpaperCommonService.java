package com.edu863.service.service010;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu863.dao.dao010.ShowTestpaperDao;
import com.edu863.dao.dao010.TestpaperCommonDao;

@Service
@Transactional
/**
 * module 测试反馈系统
 * class TestpaperCommonService.java
 * function Testpaper共用业务层
 * author 唐义凡（刘垚）
 * remark
 */
public class TestpaperCommonService {
	// 自动装配ShowTestpaperDao
	@Autowired
	private ShowTestpaperDao showTestpaperDao;
	// 自动装配TestpaperCommonDao
	@Autowired
	private TestpaperCommonDao testpaperCommonDao;

	/**
	 * method updateIndex
	 * function 设置某张试卷中所有题目的序号
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param disrupt 是否执行乱序
	 * @return 无
	 * @throws 无
	 */
	public boolean updateIndex(String userId, String testpaperId, boolean disrupt) {
		boolean result = true;
		for (int i = 1; i <= 6; i++) {
			String questionType = "0" + i;
			if (!updateIndex(userId, testpaperId, questionType, disrupt)) {
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * method modIndex
	 * function 交换题目序号
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param sqindex 题目序号（与qsindex+1 交换顺序）
	 * @param qsid 题目id
	 * @param sqtype 题目类型
	 * @return 无
	 * @throws 无
	 */
	public boolean modIndex(String userId, String testpaperId, String qsindex, String qsid, String qstype) {
	
		try {
			int index = Integer.valueOf(qsindex);			
			return showTestpaperDao.modIndex(userId, testpaperId, index, qsid, qstype);
		} catch (Exception e) {
			// 非数字
			return false;
		}
	}
	

	/**
	 * method updateIndex
	 * function 设置某张试卷中某个类型题目的序号
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param questionType 题目类型
	 * @param disrupt 是否执行乱序
	 * @return 无
	 * @throws 无
	 */
	private boolean updateIndex(String userId, String testpaperId, String questionType, boolean disrupt) {
		// 获取一张试卷中一个类型的题目信息
		List<Map<String, Object>> questionList = testpaperCommonDao.getTestpaperQuestion(testpaperId, questionType);
		// 打乱题目序号
		if (disrupt) {
			Collections.shuffle(questionList);
		}
		// 更新题目序号
		boolean result = true;
		int index = 1;
		for (Map<String, Object> question : questionList) {
			if (!showTestpaperDao.updateIndex(userId, testpaperId, question.get("QSID"), questionType, index++)) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * method insertTestpaperQuestion
	 * function 添加试卷题目信息
	 * @param userId 用户ID
	 * @param schoolId 学校编号
	 * @param testpaperId 试卷ID
	 * @param questionType 题目类型
	 * @param questionId 题目ID
	 * @return 执行是否成功
	 * @throws 无
	 */
	public boolean insertTestpaperQuestion(String userId, String schoolId, String testpaperId, String questionType, String[] questionId) {
		// 生成试卷题目信息
		List<Map<String, Object>> questionList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < questionId.length; i++) {
			Map<String, Object> question = new TreeMap<String, Object>();
			// 试卷ID
			question.put("TPID", testpaperId);
			// 题目序号
			question.put("QSINDEX", 10000 + i);
			// 学校编号
			question.put("SCHOOLNO", schoolId);
			// 题目类型
			question.put("QSTYPE", questionType);
			// 题目ID
			question.put("QSID", questionId[i]);
			// 有效状态
			question.put("ISVALID", "1");
			// 登录用户ID
			question.put("CRTUSID", userId);
			// 更新用户ID
			question.put("UPDUSID", userId);
			questionList.add(question);
		}
		// 添加试卷题目信息
		if (testpaperCommonDao.insertTestpaperQuestion(questionList)) {
			// 更新试卷中题目的序号
			updateIndex(userId, testpaperId, false);
			// 更新试卷信息
			testpaperCommonDao.updateTestpaperQuestionCount(userId, testpaperId);
			return true;
		} else {
			return false;
		}
	}
}
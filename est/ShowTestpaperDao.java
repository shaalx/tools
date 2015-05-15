package com.edu863.dao.dao010;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
/**
 * module 测试反馈系统
 * class ShowTestpaperDao.java
 * function showTestpaper画面持久层
 * author 唐义凡（刘垚）
 * remark
 */
public class ShowTestpaperDao {
	private final static char separator = '|';
	// 自动装配JdbcTemplate
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * method getSingleChoiceList
	 * function 根据试卷ID，获得试卷中的单选题信息
	 * @param testpaperId 试卷ID
	 * @return 单选题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getSingleChoiceList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_SINGLECHOICEQS_010.QSID, QSDESC, REFPIC, OPTIONA, OPTIONB, OPTIONC, OPTIOND ");
		sql.append(" from TBL_SINGLECHOICEQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_SINGLECHOICEQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_SINGLECHOICEQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "01", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * method getMultiChoiceList
	 * function 根据试卷ID，获得试卷中的多选题信息
	 * @param testpaperId 试卷ID
	 * @return 多选题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getMultiChoiceList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_MULTICHOICEQS_010.QSID, QSDESC, REFPIC ");
		sql.append(" from TBL_MULTICHOICEQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_MULTICHOICEQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_MULTICHOICEQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "02", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * method getChoiceList
	 * function 根据多选题的题目ID，获得多选题的选项
	 * @param questionId 题目编号
	 * @return 多选题选项
	 * @throws 无
	 */
	public List<Map<String, Object>> getChoiceList(Object questionId) {
		String sql = " select OPTIONID, OPTIONDESC from TBL_MULTICHOICEQSOPTION_010 where QSID = ? and ISVALID = ? order by OPTIONID asc ";
		try {
			return jdbcTemplate.queryForList(sql, new Object[] { questionId, "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * method getJudgeList
	 * function 根据试卷ID，获得试卷中的判断题信息
	 * @param testpaperId 试卷ID
	 * @return 判断题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getJudgeList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_JUDGEQS_010.QSID, QSDESC ");
		sql.append(" from TBL_JUDGEQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_JUDGEQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_JUDGEQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "03", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * method getFillinList
	 * function 根据试卷ID，获得试卷中的填空题信息
	 * @param testpaperId 试卷ID
	 * @return 填空题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getFillinList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_FILLINQS_010.QSID, QSDESC, REFPIC ");
		sql.append(" from TBL_FILLINQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_FILLINQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_FILLINQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "04", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * method getEssayList
	 * function 根据试卷ID，获得试卷中的问答题信息
	 * @param testpaperId 试卷ID
	 * @return 问答题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getEssayList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_ESSAYQS_010.QSID, QSDESC, REFPIC ");
		sql.append(" from TBL_ESSAYQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_ESSAYQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_ESSAYQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "05", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * method getOperationList
	 * function 根据试卷ID，获得试卷中的操作题信息:
	 * 新增：从TBL_TESTPAPERQS_010中获得分值
	 * @param testpaperId 试卷ID
	 * @return 操作题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getEssayInfoList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_ESSAYQS_010.QSID, QSDESC, REFPIC, SCORE ");
		sql.append(" from TBL_ESSAYQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_ESSAYQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_ESSAYQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "05", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * method getOperationList
	 * function 根据试卷ID，获得试卷中的操作题信息
	 * @param testpaperId 试卷ID
	 * @return 操作题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getOperationList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_OPERATIONQS_010.QSID, QSDESC, QSADDR ");
		sql.append(" from TBL_OPERATIONQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_OPERATIONQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_OPERATIONQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "06", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * method getOperationList
	 * function 根据试卷ID，获得试卷中的操作题信息:
	 * 新增：从TBL_TESTPAPERQS_010中获得分值
	 * @param testpaperId 试卷ID
	 * @return 操作题信息
	 * @throws 无
	 */
	public List<Map<String, Object>> getOperationInfoList(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select TBL_OPERATIONQS_010.QSID, QSDESC, QSADDR, SCORE ");
		sql.append(" from TBL_OPERATIONQS_010, TBL_TESTPAPERQS_010 ");
		// 连接条件
		sql.append(" where TBL_OPERATIONQS_010.QSID = TBL_TESTPAPERQS_010.QSID ");
		// “试卷ID”的条件
		sql.append(" and TPID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_OPERATIONQS_010.ISVALID = ? ");
		sql.append(" and TBL_TESTPAPERQS_010.ISVALID = ? ");
		// 排序
		sql.append(" order by QSINDEX asc ");
		try {
			return jdbcTemplate.queryForList(sql.toString(), new Object[] { testpaperId, "06", "1", "1" });
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * method getTestpaperSumScore
	 * function 根据试卷ID，获得试卷总分:
	 * 新增：从TBL_TESTPAPERQS_010中获得总分值
	 * @param testpaperId 试卷ID
	 * @return 操作题信息
	 * @throws 无
	 */
	public int getTestpaperSumScore(String testpaperId) {
		StringBuilder sql = new StringBuilder(" select SUM(SCORE) from TBL_TESTPAPERQS_010 where TPID = ? ");
		try {
			return jdbcTemplate.queryForInt(sql.toString(), new Object[] { testpaperId});
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * method getQuestionKnowledge
	 * function 根据题目编号和题目类型，获得题目的知识点
	 * @param questionId 题目编号
	 * @param questionType 题目类型
	 * @return 知识点名称
	 * @throws 无
	 */
	public String getQuestionKnowledge(Object questionId, Object questionType) {
		StringBuilder sql = new StringBuilder(" select KNOWLEDGENM ");
		sql.append(" from TBL_QSKNO_010, TBL_KNOWLEDGE_003 ");
		// 连接条件
		sql.append(" where TBL_QSKNO_010.KNOID = TBL_KNOWLEDGE_003.KNOWLEDGE_ID ");
		// “题目编号”的条件
		sql.append(" and QSID = ? ");
		// “题目类型”的条件
		sql.append(" and QSTYPE = ? ");
		// “有效状态”的条件
		sql.append(" and TBL_QSKNO_010.ISVALID = ? ");
		sql.append(" and TBL_KNOWLEDGE_003.ISVALID = ? ");
		// 排序
		sql.append(" order by TBL_KNOWLEDGE_003.KNOWLEDGE_ID asc ");
		try {
			List<Map<String, Object>> knowledgeList = jdbcTemplate.queryForList(sql.toString(), new Object[] { questionId, questionType, "1", "1" });
			StringBuilder builder = new StringBuilder();
			for (Map<String, Object> knowledge : knowledgeList) {
				if (builder.length() > 0) {
					builder.append(separator);
				}
				builder.append(knowledge.get("KNOWLEDGENM"));
			}
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * method updateTestpaperInfo
	 * function 更新试卷信息
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param testpaperName 试卷名
	 * @param score 分值
	 * @return 执行是否成功
	 * @throws 无
	 */
	public boolean updateTestpaperInfo(String userId, String testpaperId, String testpaperName, int[] score) {
		// update语句参数列表
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" update TBL_TESTPAPER_010 set ");
		// 更新分值
		String[] column = { "SCOREOFSCQS", "SCOREOFMCQS", "SCOREOFJUDGEQS", "SCOREOFFILLINQS"};//, "SCOREOFESSQS", "SCOREOFOPERQS" 
		for (int i = 0; i < column.length; i++) {
			sql.append(column[i] + " = ?, ");
			args.add(score[i]);
		}
		// 更新“试卷名”
		sql.append(" TPNAME = ?, ");
		args.add(testpaperName);
		// 更新“更新用户ID”
		sql.append(" UPDUSID = ?, ");
		args.add(userId);
		// 更新“更新日期”
		sql.append(" UPDDT = now() ");
		// “试卷ID”的条件
		sql.append(" where TPID = ? ");
		args.add(testpaperId);
		// 执行update语句
		try {
			return 1 == jdbcTemplate.update(sql.toString(), args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * method updateTestpaperInfo
	 * function 更新试卷信息
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param testpaperName 试卷名
	 * @param score 分值
	 * @param otherScore 其它题型分值
	 * @param essayqsScore 问答题分值
	 * @param operqsScore 操作题分值
	 * @return 执行是否成功
	 * @throws 无
	 */
	public boolean updateTestpaperInfo(String userId, String testpaperId, int[] otherScore, Map<Integer, Integer> essayqsScore, Map<Integer, Integer> operqsScore) {
		// update语句参数列表
		boolean ok = true;
		String []qstypes = {"01","02","03","04"};
		for(int i = 0; i< otherScore.length; i++){
			StringBuilder sql = new StringBuilder(" update TBL_TESTPAPERQS_010 set ");
			List<Object> args = new ArrayList<Object>();
			int score = otherScore[i];
			sql.append("SCORE = ? ");
			args.add(score);
			// 更新“更新用户ID”
			sql.append(", UPDUSID = ? ");
			args.add(userId);
			// 更新“更新日期”
			sql.append(", UPDDT = now() ");
			// “试卷ID”的条件
			sql.append(" where TPID = ? ");
			args.add(testpaperId);
			// 题目类型
			sql.append(" and QSTYPE = ? ");
			args.add(qstypes[i]);
			// 执行update语句
			try {
				jdbcTemplate.update(sql.toString(), args.toArray());
			} catch (Exception e) {
				ok=false;
			}
		}
		// 更新分值
		for(int i = 0; i< essayqsScore.size(); i++){
			StringBuilder sql = new StringBuilder(" update TBL_TESTPAPERQS_010 set ");
			List<Object> args = new ArrayList<Object>();
			int score = essayqsScore.get(i+1);
			sql.append("SCORE = ? ");
			args.add(score);
			// 更新“更新用户ID”
			sql.append(", UPDUSID = ? ");
			args.add(userId);
			// 更新“更新日期”
			sql.append(", UPDDT = now() ");
			// “试卷ID”的条件
			sql.append(" where TPID = ? ");
			args.add(testpaperId);
			// 试卷类型
			sql.append(" and QSTYPE = ? ");
			args.add("05");
			// 题目类型
			sql.append(" and QSINDEX = ?");
			args.add(i+1);
			// 执行update语句
			try {
				jdbcTemplate.update(sql.toString(), args.toArray());
			} catch (Exception e) {
				ok=false;
			}
		}

		for(int i = 0; i< operqsScore.size(); i++){
			StringBuilder sql = new StringBuilder(" update TBL_TESTPAPERQS_010 set ");
			List<Object> args = new ArrayList<Object>();
			int score = operqsScore.get(i+1);
			sql.append("SCORE = ? ");
			args.add(score);
			// 更新“更新用户ID”
			sql.append(", UPDUSID = ? ");
			args.add(userId);
			// 更新“更新日期”
			sql.append(", UPDDT = now() ");
			// “试卷ID”的条件
			sql.append(" where TPID = ? ");
			args.add(testpaperId);
			// 试卷类型
			sql.append(" and QSTYPE = ? ");
			args.add("06");
			// 题目类型
			sql.append(" and QSINDEX = ?");
			args.add(i+1);
			// 执行update语句
			try {
				jdbcTemplate.update(sql.toString(), args.toArray());
			} catch (Exception e) {
				ok=false;
			}
		}
		return ok;
	}

	/**
	 * method updateIndex
	 * function 更新题目序号
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param questionId 题目ID
	 * @param questionType 题目类型
	 * @param index 题目序号
	 * @return 执行是否成功
	 * @throws 无
	 */
	public boolean updateIndex(String userId, String testpaperId, Object questionId, String questionType, int index) {
		StringBuilder sql = new StringBuilder(" update TBL_TESTPAPERQS_010 ");
		sql.append(" set QSINDEX = ?, UPDUSID = ?, UPDDT = now() ");
		sql.append(" where TPID = ? and QSID = ? and QSTYPE = ? ");
		// 执行update语句
		try {
			return 1 == jdbcTemplate.update(sql.toString(), new Object[] { index, userId, testpaperId, questionId, questionType });
		} catch (Exception e) {
			return false;
		}
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
	public boolean modIndex(String userId, String testpaperId, int qsindex, String qsid, String qstype) {
		boolean result = true;
		StringBuilder sql_1 = new StringBuilder(" update TBL_TESTPAPERQS_010 ");
		sql_1.append(" set QSINDEX = ?, UPDUSID = ?, UPDDT = now() ");
		sql_1.append(" where TPID = ? and QSINDEX = ? and QSTYPE = ? ");
		
		StringBuilder sql_2 = new StringBuilder(" update TBL_TESTPAPERQS_010 ");
		sql_2.append(" set QSINDEX = ?, UPDUSID = ?, UPDDT = now() ");
		sql_2.append(" where TPID = ? and QSID = ? and QSTYPE = ? ");
		// 执行update语句
		// 应该使用事务
		try {
			int update_1 = jdbcTemplate.update(sql_1.toString(), new Object[] {qsindex, userId, testpaperId, qsindex+1, qstype});
			
			int update_2 = jdbcTemplate.update(sql_2.toString(), new Object[] {qsindex+1, userId, testpaperId, qsid, qstype});					
			System.out.println(update_1+update_2);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	/**
	 * method updateTestpaperQuestionValid
	 * function 更新试卷中题目的有效状态
	 * @param userId 用户ID
	 * @param testpaperId 试卷ID
	 * @param questionId 题目ID
	 * @param questionType 题目类型
	 * @param isValid 有效状态
	 * @return 执行是否成功
	 * @throws 无
	 */
	public boolean updateTestpaperQuestionValid(String userId, String testpaperId, String[] questionId, String[] questionType, String isValid) {
		// update语句参数列表
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" delete from TBL_TESTPAPERQS_010 ");
		// “试卷ID”的条件
		sql.append(" where TPID = ? and ");
		args.add(testpaperId);
		// “题目ID”和“题目类型”的条件
		sql.append(" ( ");
		for (int i = 0; i < questionId.length; i++) {
			if (i > 0) {
				sql.append(" or ");
			}
			sql.append(" (QSID = ? and QSTYPE = ?) ");
			args.add(questionId[i]);
			args.add(questionType[i]);
		}
		sql.append(" ) ");
		// 执行update语句
		try {
			return questionId.length == jdbcTemplate.update(sql.toString(), args.toArray());
		} catch (Exception e) {
			return false;
		}
	}
}
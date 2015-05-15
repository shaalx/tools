package com.edu863.controller.controller010;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu863.authorization.InvalidAccessException;
import com.edu863.authorization.UserPermission;
import com.edu863.authorization.UserPropEnum;
import com.edu863.dao.common.MessageDao;
import com.edu863.dao.dao010.ShowTestpaperDao;
import com.edu863.dao.dao010.TestpaperCommonDao;
import com.edu863.service.service010.ShowTestpaperService;
import com.edu863.service.service010.TestpaperCommonService;

@Controller
@RequestMapping(value = "/testpaper/show")
/**
 * module 测试反馈系统
 * class ShowTestpaperController.java
 * function showTestpaper画面控制器
 * author 唐义凡（刘垚）
 * remark
 */
public class ShowTestpaperController {
	private final static String number[] = { "一", "二", "三", "四", "五", "六" };
	// 自动装配ShowTestpaperService
	@Autowired
	private ShowTestpaperService showTestpaperService;
	// 自动装配TestpaperCommonService
	@Autowired
	private TestpaperCommonService commonService;
	// 自动装配ShowTestpaperDao
	@Autowired
	private ShowTestpaperDao showTestpaperDao;
	// 自动装配MessageDao
	@Autowired
	private MessageDao messageDao;
	// 自动装配TestpaperCommonDao
	@Autowired
	private TestpaperCommonDao commonDao;

	@RequestMapping(value = "")
	@UserPermission(userProp = { UserPropEnum.ADMIN, UserPropEnum.TEACHER })
	/**
	 * method show
	 * function 负责处理/testpaper/show的请求
	 * @param request HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String show(HttpServletRequest request, HttpSession session, Model model) {
		// 获取参数开始
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 判断是否从其他画面返回本画面
		String isReturn = request.getParameter("return");
		// 获取试卷ID
		String testpaperId = request.getParameter("testpaperId");
		// 获取来源画面
		String from = request.getParameter("from");
		// 获取参数结束
		// 数据处理开始
		// 检索试卷信息
		Map<String, Object> testpaperInfo = commonDao.getTestpaperInfo(testpaperId);
		// 检索单选题信息
		List<Map<String, Object>> singleChoiceList = showTestpaperDao.getSingleChoiceList(testpaperId);
		if (singleChoiceList != null) {
			for (Map<String, Object> singleChoice : singleChoiceList) {
				// 添加知识点信息
				singleChoice.put("KNOWLEDGENM", showTestpaperDao.getQuestionKnowledge(singleChoice.get("QSID"), "01"));
				// 删除无效的题目图片
				if (StringUtils.isEmpty((String) singleChoice.get("REFPIC"))) {
					singleChoice.remove("REFPIC");
				}
			}
		}
		// 检索多选题信息
		List<Map<String, Object>> multiChoiceList = showTestpaperDao.getMultiChoiceList(testpaperId);
		if (multiChoiceList != null) {
			for (Map<String, Object> multiChoice : multiChoiceList) {
				// 添加选项
				multiChoice.put("choiceList", showTestpaperDao.getChoiceList(multiChoice.get("QSID")));
				// 添加知识点信息
				multiChoice.put("KNOWLEDGENM", showTestpaperDao.getQuestionKnowledge(multiChoice.get("QSID"), "02"));
				// 删除无效的题目图片
				if (StringUtils.isEmpty((String) multiChoice.get("REFPIC"))) {
					multiChoice.remove("REFPIC");
				}
			}
		}
		// 检索判断题信息
		List<Map<String, Object>> judgeList = showTestpaperDao.getJudgeList(testpaperId);
		if (judgeList != null) {
			for (Map<String, Object> judge : judgeList) {
				// 添加知识点信息
				judge.put("KNOWLEDGENM", showTestpaperDao.getQuestionKnowledge(judge.get("QSID"), "03"));
			}
		}
		// 检索填空题信息
		List<Map<String, Object>> fillinList = showTestpaperDao.getFillinList(testpaperId);
		if (fillinList != null) {
			for (Map<String, Object> fillin : fillinList) {
				// 添加知识点信息
				fillin.put("KNOWLEDGENM", showTestpaperDao.getQuestionKnowledge(fillin.get("QSID"), "04"));
				// 删除无效的题目图片
				if (StringUtils.isEmpty((String) fillin.get("REFPIC"))) {
					fillin.remove("REFPIC");
				}
			}
		}
		// 检索问答题信息
		List<Map<String, Object>> essayList = showTestpaperDao.getEssayInfoList(testpaperId);
		int essayScore = 0;
		if (essayList != null) {
			for (Map<String, Object> essay : essayList) {
				// 添加知识点信息
				essay.put("KNOWLEDGENM", showTestpaperDao.getQuestionKnowledge(essay.get("QSID"), "05"));
				// 删除无效的题目图片
				if (StringUtils.isEmpty((String) essay.get("REFPIC"))) {
					essay.remove("REFPIC");
				}
				essayScore += ((Long)(essay.get("SCORE"))).intValue();
			}
		}
		// 检索操作题信息
		List<Map<String, Object>> operationList = showTestpaperDao.getOperationInfoList(testpaperId);
		int operationScore = 0;
		if (operationList != null) {
			int i = 0;
			for (Map<String, Object> operation : operationList) {
				i++;
				// 添加知识点信息
				operation.put("KNOWLEDGENM", showTestpaperDao.getQuestionKnowledge(operation.get("QSID"), "06"));
				operationScore += ((Long)(operation.get("SCORE"))).intValue();
			}
		}
		// 计算总分：新的计算方法，从tbl_testpaperqs_010中获取
		int sum_score = showTestpaperDao.getTestpaperSumScore(testpaperId);
		testpaperInfo.remove("TOTALSCORE");
		testpaperInfo.put("TOTALSCORE", sum_score);
		// 数据处理结束
		// 生成模型开始
		model.addAllAttributes(request.getParameterMap());
		// 设置共通部分-画面ID
		model.addAttribute("pageID", "frm_edit_test_paper_010_11");
		// 设置共通部分-画面名称
		model.addAttribute("pageName", "试卷查看及编辑");
		// 将试卷ID放入模型
		model.addAttribute("testpaperId", testpaperId);
		// 将试卷是否只读放入模型
		if (!userId.equals(testpaperInfo.get("CREATORID")) || !commonDao.isUsed(testpaperId).equals("")) {
			model.addAttribute("readOnly", "1");
		}
		// 试卷不可编辑，只读放入模型
		if(!commonDao.canEdit(testpaperId) && userId.equals(testpaperInfo.get("CREATORID"))){
			model.addAttribute("readOnly", "1");
			// 消息
			model.addAttribute("hasBeenUsingMessage", messageDao.getMessage("W0120006", null));
		}
		int index = 0;
		// 将单选题放入模型
		if (singleChoiceList != null && singleChoiceList.size() > 0) {
			model.addAttribute("singleChoiceList", singleChoiceList);
			model.addAttribute("singleChoiceNumber", number[index++]);
			// 将单选题的总分放入模型
			model.addAttribute("singleChoiceScore", ((Integer) testpaperInfo.get("SCOREOFSCQS")) * ((Integer) testpaperInfo.get("NUMOFSCQS")));
		}
		// 将多选题放入模型
		if (multiChoiceList != null && multiChoiceList.size() > 0) {
			model.addAttribute("multiChoiceList", multiChoiceList);
			model.addAttribute("multiChoiceNumber", number[index++]);
			// 将多选题的总分放入模型
			model.addAttribute("multiChoiceScore", ((Integer) testpaperInfo.get("SCOREOFMCQS")) * ((Integer) testpaperInfo.get("NUMOFMCQS")));
		}
		// 将判断题放入模型
		if (judgeList != null && judgeList.size() > 0) {
			model.addAttribute("judgeList", judgeList);
			model.addAttribute("judgeNumber", number[index++]);
			// 将判断题的总分放入模型
			model.addAttribute("judgeScore", ((Integer) testpaperInfo.get("SCOREOFJUDGEQS")) * ((Integer) testpaperInfo.get("NUMOFJUDGEQS")));
		}
		// 将填空题放入模型
		if (fillinList != null && fillinList.size() > 0) {
			model.addAttribute("fillinList", fillinList);
			model.addAttribute("fillinNumber", number[index++]);
			// 将填空题的总分放入模型
			model.addAttribute("fillinScore", ((Integer) testpaperInfo.get("SCOREOFFILLINQS")) * ((Integer) testpaperInfo.get("NUMOFFILLINQS")));
		}
		// 将问答题放入模型
		if (essayList != null && essayList.size() > 0) {
			model.addAttribute("essayList", essayList);
			model.addAttribute("essayNumber", number[index++]);
			// 将问答题的总分放入模型
			model.addAttribute("essayScore", essayScore);//((Integer) testpaperInfo.get("SCOREOFESSQS")) * ((Integer) testpaperInfo.get("NUMOFESSQS")));
		}
		// 将操作题放入模型
		if (operationList != null && operationList.size() > 0) {
			model.addAttribute("operationList", operationList);
			model.addAttribute("operationNumber", number[index++]);
			// 将操作题的总分放入模型			
			model.addAttribute("operationScore", operationScore);//((Integer) testpaperInfo.get("SCOREOFOPERQS")) * ((Integer) testpaperInfo.get("NUMOFOPERQS")));
		}
		// 读取输入开始
		if (!StringUtils.isEmpty(isReturn)) {
			// 读取试卷名字
			testpaperInfo.put("TPNAME", session.getAttribute("testpaperName_010_011"));
			// 读取来源画面
			from = (String) session.getAttribute("from_010_011");
			// 读取分值
			testpaperInfo.put("SCOREOFSCQS", session.getAttribute("SCOREOFSCQS_010_011"));
			testpaperInfo.put("SCOREOFMCQS", session.getAttribute("SCOREOFMCQS_010_011"));
			testpaperInfo.put("SCOREOFJUDGEQS", session.getAttribute("SCOREOFJUDGEQS_010_011"));
			testpaperInfo.put("SCOREOFFILLINQS", session.getAttribute("SCOREOFFILLINQS_010_011"));
			testpaperInfo.put("SCOREOFESSQS", session.getAttribute("SCOREOFESSQS_010_011"));
			testpaperInfo.put("SCOREOFOPERQS", session.getAttribute("SCOREOFOPERQS_010_011"));
		}
		// 读取输入结束
		// 将来源画面放入模型
		model.addAttribute("from", from);
		// 将试卷信息放入模型
		model.addAttribute("testpaperInfo", testpaperInfo);
		// 将“乱序”的消息放入模型
		model.addAttribute("disruptConfirmMessage", messageDao.getMessage("W0120001", new String[] { "乱序" }));
		model.addAttribute("disruptSuccessMessage", messageDao.getMessage("W0120004", new String[] { "乱序" }));
		model.addAttribute("disruptFailedMessage", messageDao.getMessage("W0120005", new String[] { "乱序" }));
		// 将“更新顺序”的消息放入模型
		model.addAttribute("modIndexSuccessMessage", messageDao.getMessage("W0120004", new String[] { "交换题序" }));
		model.addAttribute("modIndexFailedMessage", messageDao.getMessage("W0120005", new String[] { "交换题序" }));
		// 将“保存输入”的消息放入模型
		model.addAttribute("updateConfirmMessage", messageDao.getMessage("W0120001", new String[] { "保存输入" }));
		model.addAttribute("updateSuccessMessage", messageDao.getMessage("W0120004", new String[] { "保存输入" }));
		model.addAttribute("updateFailedMessage", messageDao.getMessage("W0120005", new String[] { "保存输入" }));
		// 将“删除”的消息放入模型
		String parameter[] = { "题目", "题目" };
		model.addAttribute("deleteErrorMessage", messageDao.getMessage("W0010002", parameter));
		model.addAttribute("deleteConfirmMessage", messageDao.getMessage("W0120001", new String[] { "删除" }));
		model.addAttribute("deleteSuccessMessage", messageDao.getMessage("W0120004", new String[] { "删除" }));
		model.addAttribute("deleteFailedMessage", messageDao.getMessage("W0120005", new String[] { "删除" }));
		// 将“试卷名”的消息放入模型
		model.addAttribute("testpaperNameW0010003", messageDao.getMessage("W0010003", new String[] { "试卷名" }));
		model.addAttribute("testpaperNameW0010006", messageDao.getMessage("W0010006", new String[] { "试卷名" }));
		// 将“分值”的消息放入模型
		model.addAttribute("questionScoreW0010006", messageDao.getMessage("W0010006", new String[] { "分值" }));
		model.addAttribute("questionScoreW0010008", messageDao.getMessage("W0010008", new String[] { "分值" }));
		model.addAttribute("questionScoreW0010017", messageDao.getMessage("W0010017", new String[] { "分值" }));
		// 将“返回”的消息放入模型
		model.addAttribute("backConfirmMessage", messageDao.getMessage("W0120002", null));
		// 生成模型结束
		return "010/showTestpaper";
	}

	@RequestMapping(value = "disrupt")
	@UserPermission(userProp = { UserPropEnum.TEACHER })
	/**
	 * method disrupt
	 * function 负责处理/testpaper/show/disrupt的请求
	 * @param request HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String disrupt(HttpServletRequest request, HttpSession session, Model model) {
		// 检查当前用户是否为教务员
		if(request.getSession().getAttribute("jwyDepartNO") != null){
			// 当前用户性质和权限所设的用户性质不一致,抛出非法访问的异常
			throw new InvalidAccessException();
		}
		// 保存用户输入
		saveInput(request, session);
		// 获取参数开始
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 获取试卷ID
		String testpaperId = request.getParameter("testpaperId");
		// 获取参数结束
		// 试卷可否编辑
		boolean canEdit = commonDao.canEdit(testpaperId);
		if (!canEdit) {
			// 将试卷ID放入模型
			model.addAttribute("testpaperId", testpaperId);
			model.addAttribute("return", "1");
			// 生成模型结束
			return "redirect:/testpaper/show";
		}
		// 数据处理开始
		if (commonService.updateIndex(userId, testpaperId, true)) {
			model.addAttribute("hasDisruptSuccessMessage", "1");
		} else {
			model.addAttribute("hasDisruptFailedMessage", "1");
		}
		// 数据处理结束
		// 生成模型开始
		// 将试卷ID放入模型
		model.addAttribute("testpaperId", testpaperId);
		model.addAttribute("return", "1");
		// 生成模型结束
		return "redirect:/testpaper/show";
	}
	
	@RequestMapping(value = "modIndex")
	@UserPermission(userProp = { UserPropEnum.TEACHER })
	/**
	 * method modIndex
	 * function 负责处理/testpaper/show/modIndex的请求
	 * @param request HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String modIndex(HttpServletRequest request, HttpSession session, Model model) {
		// 检查当前用户是否为教务员
		if(request.getSession().getAttribute("jwyDepartNO") != null){
			// 当前用户性质和权限所设的用户性质不一致,抛出非法访问的异常
			throw new InvalidAccessException();
		}
		// 保存用户输入
		saveInput(request, session);
		// 获取参数开始
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 获取试卷ID
		String testpaperId = request.getParameter("testpaperId");
		// 获取题目顺序
		String qsindex = request.getParameter("qsindex");
		// 获取题目编号
		String qsid = request.getParameter("qsid");
		// 获取题目类型
		String qstype = request.getParameter("qstype");
		// 获取参数结束
		// 试卷可否编辑
		boolean canEdit = commonDao.canEdit(testpaperId);
		if (!canEdit) {
			// 将试卷ID放入模型
			model.addAttribute("testpaperId", testpaperId);
			model.addAttribute("return", "1");
			// 生成模型结束
			return "redirect:/testpaper/show";
		}
		// 数据处理开始
		if (commonService.modIndex(userId, testpaperId, qsindex, qsid, qstype)) {
			model.addAttribute("hasModIndexSuccessMessage", "1");
		} else {
			model.addAttribute("hasModIndexFailedMessage", "1");
		}
		// 数据处理结束
		// 生成模型开始
		// 将试卷ID放入模型
		model.addAttribute("testpaperId", testpaperId);
		model.addAttribute("return", "1");
		// 生成模型结束
		return "redirect:/testpaper/show";
	}


	@RequestMapping(value = "insert")
	@UserPermission(userProp = { UserPropEnum.TEACHER })
	/**
	 * method insert
	 * function 负责处理/testpaper/show/insert的请求
	 * @param request HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String insert(HttpServletRequest request, HttpSession session, Model model) {
		// 检查当前用户是否为教务员
		if(request.getSession().getAttribute("jwyDepartNO") != null){
			// 当前用户性质和权限所设的用户性质不一致,抛出非法访问的异常
			throw new InvalidAccessException();
		}
		// 保存用户输入
		saveInput(request, session);
		// 获取参数开始
		// 获取试卷ID
		String testpaperId = request.getParameter("testpaperId");
		// 试卷可否编辑
		boolean canEdit = commonDao.canEdit(testpaperId);
		if (!canEdit) {
			// 将试卷ID放入模型
			model.addAttribute("testpaperId", testpaperId);
			model.addAttribute("return", "1");
			// 生成模型结束
			return "redirect:/testpaper/show";
		}
		// 获取参数结束
		// 生成模型开始
		// 将试卷ID放入模型
		model.addAttribute("testpaperId", testpaperId);
		model.addAttribute("from", "show");
		// 生成模型结束
		return "redirect:/testpaper/choose";
	}

	@RequestMapping(value = "update")
	@UserPermission(userProp = { UserPropEnum.TEACHER })
	/**
	 * method update
	 * function 负责处理/testpaper/show/update的请求
	 * @param request HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String update(HttpServletRequest request, HttpSession session, Model model) {
		// 检查当前用户是否为教务员
		if(request.getSession().getAttribute("jwyDepartNO") != null){
			// 当前用户性质和权限所设的用户性质不一致,抛出非法访问的异常
			throw new InvalidAccessException();
		}
		// 保存用户输入
		saveInput(request, session);
		// 获取参数开始
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 获取试卷ID
		String testpaperId = request.getParameter("testpaperId");
		// 获取试卷名
		String testpaperName = request.getParameter("testpaperName");
		// 获取分值
		// 试卷可否编辑
		boolean canEdit = commonDao.canEdit(testpaperId);
		if (!canEdit) {
			// 将试卷ID放入模型
			model.addAttribute("testpaperId", testpaperId);
			model.addAttribute("return", "1");
			// 生成模型结束
			return "redirect:/testpaper/show";
		}
		String[] questionScore = new String[4];
		
		Map<String, String> message = new TreeMap<String, String>();
		String num_of_essayqs_str = request.getParameter("NUMOFESSQS");
		String num_of_operqs_str = request.getParameter("NUMOFOPERQS");
		Map<Integer, Integer> essayqs_score = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> operqs_score = new TreeMap<Integer, Integer>();
		try {
//			message.put("hasOperQuestionScore" + i + "W0010006", "1");
//			message.put("hasQuestionScore" + i + "W0010017", "1");
			
			int num_of_operqs = Integer.valueOf(num_of_operqs_str);
			// 小于0
			if (num_of_operqs > 0) {
				for (int i = 0; i< num_of_operqs; i++) {
					try{
						String oper_score_str = request.getParameter("operationScore"+(i+1));
						int oper_score = Integer.valueOf(oper_score_str);
						operqs_score.put(i+1,oper_score);
					}catch(Exception e){
						operqs_score.put(i+1,0);
					}				

				}
			}
			

			int num_of_essayqs = Integer.valueOf(num_of_essayqs_str);
			// 小于0
			if (num_of_essayqs > 0) {
				for (int i = 0; i< num_of_essayqs; i++) {
					try{
						String essay_score_str = request.getParameter("essayScore"+(i+1));
						int essay_score = Integer.valueOf(essay_score_str);
						essayqs_score.put(i+1,essay_score);
					}catch(Exception e){
						essayqs_score.put(i+1,0);
					}				

				}
			}
		} catch (Exception e) {
			// 不是数字
//			message.put("hasQuestionScore" + i + "W0010008", "1");
		}

		for (int i = 1; i <= 4; i++) {
			questionScore[i - 1] = request.getParameter("questionScore" + i);
			if (questionScore[i - 1] == null) {
				questionScore[i - 1] = "0";
			}
		}
		// 获取参数结束
		// 数据处理开始
		// 更新试卷信息
		model.addAllAttributes(showTestpaperService.updateTestpaperInfo(userId, testpaperId, testpaperName, questionScore, essayqs_score, operqs_score));
		commonDao.updateTestpaperQuestionCount(userId, testpaperId);
		// 数据处理结束
		// 生成模型开始
		// 将试卷ID放入模型
		model.addAttribute("testpaperId", testpaperId);
		model.addAttribute("return", "1");
		// 生成模型结束
		return "redirect:/testpaper/show";
	}

	@RequestMapping(value = "delete")
	@UserPermission(userProp = { UserPropEnum.TEACHER })
	/**
	 * method delete
	 * function 负责处理/testpaper/show/delete的请求
	 * @param request HTTP请求
	 * @param session HTTP会话
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String delete(HttpServletRequest request, HttpSession session, Model model) {
		// 检查当前用户是否为教务员
		if(request.getSession().getAttribute("jwyDepartNO") != null){
			// 当前用户性质和权限所设的用户性质不一致,抛出非法访问的异常
			throw new InvalidAccessException();
		}
		// 保存用户输入
		saveInput(request, session);
		// 获取参数开始
		// 获取用户ID
		String userId = (String) session.getAttribute("userNO");
		// 获取试卷ID
		String testpaperId = request.getParameter("testpaperId");
		// 获取题目类型和题目ID
		String questions = request.getParameter("qdataids");
		// 获取参数结束
		// 试卷可否编辑
		boolean canEdit = commonDao.canEdit(testpaperId);
		if (!canEdit) {
			// 将试卷ID放入模型
			model.addAttribute("testpaperId", testpaperId);
			model.addAttribute("return", "1");
			// 生成模型结束
			return "redirect:/testpaper/show";
		}
		// 数据处理开始
		// 更新试卷中题目的有效状态
		String[] question = questions.split(",");
		if (showTestpaperService.updateTestpaperQuestionValid(userId, testpaperId, question, "0")) {
			model.addAttribute("hasDeleteSuccessMessage", "1");
		} else {
			model.addAttribute("hasDeleteFailedMessage", "1");
		}
		// 更新试卷中题目的序号
		commonService.updateIndex(userId, testpaperId, false);
		// 更新试卷信息
		commonDao.updateTestpaperQuestionCount(userId, testpaperId);
		// 数据处理结束
		// 生成模型开始
		// 将试卷ID放入模型
		model.addAttribute("testpaperId", testpaperId);
		model.addAttribute("return", "1");
		// 生成模型结束
		return "redirect:/testpaper/show";
	}

	@RequestMapping(value = "/back")
	@UserPermission(userProp = { UserPropEnum.ADMIN, UserPropEnum.TEACHER })
	/**
	 * method back
	 * function 负责处理/testpaper/show/back的请求
	 * @param request HTTP请求
	 * @param model 生成画面所需的模型
	 * @return 画面路径
	 * @throws 无
	 */
	public String back(HttpServletRequest request, Model model) {
		// 获取参数开始
		// 获取来源画面
		String from = request.getParameter("from");
		// 获取参数结束
		// 生成模型开始
		model.addAttribute("return", "1");
		// 生成模型结束
		if (from.equals("smart")) {
			return "redirect:/testpaper/smart";
		} else if (from.equals("choose")) {
			return "redirect:/testpaper/choose";
		} else if (from.equals("copy")) {
			return "redirect:/testpaper/copy";
		} else {
			return "redirect:/testpaper/list";
		}
	}

	/**
	 * method saveInput
	 * function 保存用户输入
	 * @param request HTTP请求
	 * @param session HTTP会话
	 * @return 无
	 * @throws 无
	 */
	private void saveInput(HttpServletRequest request, HttpSession session) {		
		// 获取并保存试卷名
		String testpaperName = request.getParameter("testpaperName");
		session.setAttribute("testpaperName_010_011", testpaperName);
		// 获取并保存来源画面
		String from = request.getParameter("from");
		session.setAttribute("from_010_011", from);
		// 获取并保存分值
		String SCOREOFSCQS = request.getParameter("questionScore1");
		session.setAttribute("SCOREOFSCQS_010_011", SCOREOFSCQS);
		String SCOREOFMCQS = request.getParameter("questionScore2");
		session.setAttribute("SCOREOFMCQS_010_011", SCOREOFMCQS);
		String SCOREOFJUDGEQS = request.getParameter("questionScore3");
		session.setAttribute("SCOREOFJUDGEQS_010_011", SCOREOFJUDGEQS);
		String SCOREOFFILLINQS = request.getParameter("questionScore4");
		session.setAttribute("SCOREOFFILLINQS_010_011", SCOREOFFILLINQS);
		String SCOREOFESSQS = request.getParameter("questionScore5");
		session.setAttribute("SCOREOFESSQS_010_011", SCOREOFESSQS);
		String SCOREOFOPERQS = request.getParameter("questionScore6");
		session.setAttribute("SCOREOFOPERQS_010_011", SCOREOFOPERQS);
	}
}